package com.univ.paris8discover.screens;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.Manifest;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.univ.paris8discover.MainActivity;
import com.univ.paris8discover.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.univ.paris8discover.models.ListPoints;
import com.univ.paris8discover.models.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FloatingActionButton btnScanQRCode;

    private ArrayList<Point> points = new ArrayList<Point>();
    private Button btnstart;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private JSONArray filteredArray = new JSONArray();
    private GoogleMap googleMap;
    private MapView mapView;
    private boolean isFirstLocationUpdate = true;
    private SearchView searchview;
    private double lat = 48.94610144542121 ;
    private double lon  = 2.3617150845452763;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String data = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.data =  loadJSONFromAsset("data");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.map);


        checkLocationPermission();

        btnScanQRCode = findViewById(R.id.btnScanQRCode);
        btnScanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MapActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scannez un code QR");
                integrator.setCameraId(0);
                integrator.initiateScan();
            }
        });

        initializer(data);
        startnavigation();
        listView = findViewById(R.id.listView);

        // Create an ArrayAdapter to update the list
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        searchview = findViewById(R.id.searchview);
        searchview.clearFocus();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String puid = findPuidByName(data, query);
                if(puid == null){
                    showPopup("Alert !!!", "la salle entrée n'existe pas");
                }
                else{
                    Intent intent = new Intent(MapActivity.this, ArNavigation.class);

                    intent.putExtra("puid", puid);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    startActivity(intent);
                }
                //

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filterObjects(newText);
               // Log.d("l7wa", "onQueryTextSubmit: " + newText);
                return false;
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    updateMapWithCurrentLocation();
                    fusedLocationClient.removeLocationUpdates(this);
                }
            }
        };
        requestLocationUpdates();
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }
    private void initializer(String data) {

        try {
            JSONArray jsonArray = new JSONArray(data);


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("pois_type") && jsonObject.getString("pois_type").equals("Room")) {
                    this.filteredArray.put(jsonObject);
                }
            }

            Log.d("TAG", "initializer: " + filteredArray.toString(2));

        } catch (JSONException e) {
            Log.d("Error", "initializer: " + e.getMessage());
        }
    }

    private void filterObjects(String newText) {
        adapter.clear();

        for (int i = 0; i < this.filteredArray.length(); i++) {
            try {
                JSONObject jsonObject = this.filteredArray.getJSONObject(i);
                String name = jsonObject.optString("name", "");
                if (name.toLowerCase().contains(newText.toLowerCase())) {
                    adapter.add(name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Show or hide the ListView based on whether there are filtered items
        listView.setVisibility(adapter.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void startnavigation() {
        btnstart = (Button) findViewById(R.id.getLocationButton);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchview.getQuery().toString();
                String puid = findPuidByName(data, query);
                if(puid == null){
                    showPopup("Alert !!!", "la salle entrée n'existe pas");
                }
                else{
                    Intent intent = new Intent(MapActivity.this, ArNavigation.class);

                    intent.putExtra("puid", puid);
                    intent.putExtra("lat", 48.94568295742288);
                    intent.putExtra("lon", 2.3634695341113767);
                    startActivity(intent);
                }
            }
        });

    }

    private void updateMapWithCurrentLocation() {
        if (googleMap != null) {
            LatLng currentLocation = new LatLng(lat, lon);
            googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Moi"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MapActivity.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        updateMapWithCurrentLocation();
    }




    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            int resourceId = getResources().getIdentifier(filename, "raw", getPackageName());

            if (resourceId == 0) {
                Log.e("LoadJSON", "Resource not found: " + filename);
                return null;
            }

            InputStream is = getResources().openRawResource(resourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                json = sb.toString();

            } catch (IOException e) {
                Log.e("LoadJSON", "Error reading JSON file: " + filename, e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e("LoadJSON", "Error closing InputStream", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); // Update interval in milliseconds
        locationRequest.setFastestInterval(5000); // Fastest update interval in milliseconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public static String findPuidByName(String jsonString, String targetName) {
        try {
            JSONObject data = new JSONObject(jsonString);
            JSONArray poisArray = data.getJSONArray("pois");

            for (int i = 0; i < poisArray.length(); i++) {
                JSONObject poi = poisArray.getJSONObject(i);
                String name = poi.getString("name");

                if (name.toLowerCase().equals(targetName.toLowerCase())) {
                    return poi.getString("puid");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Return null if the targetName is not found in the JSON
        return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // Handle if the user canceled the scan
            } else {
                String qrContent = result.getContents();
                showPopup("Scanned QR Code", qrContent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showPopup(String title, String message) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_popup_layout);

        TextView titleTextView = dialog.findViewById(R.id.titleTextView);
        TextView messageTextView = dialog.findViewById(R.id.messageTextView);

        titleTextView.setText(title);
        messageTextView.setText(message);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // Handle any actions when the dialog is dismissed
            }
        });

        dialog.show();
    }
}