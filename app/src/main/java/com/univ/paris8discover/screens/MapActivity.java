package com.univ.paris8discover.screens;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.Manifest;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FloatingActionButton btnScanQRCode;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private GoogleMap googleMap;
    private MapView mapView;
    private boolean isFirstLocationUpdate = true;
    private SearchView searchview;
    private double lat = 48.94610144542121 ;
    private double lon  = 2.3617150845452763;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private String data = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



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

        this.data =  loadJSONFromAsset("data");
        Log.d("data", "onQueryTextSubmit: " + data);

        searchview = findViewById(R.id.searchview);
        searchview.clearFocus();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArNavigation arNavigation = new ArNavigation(lat, lon);
                Log.d("Lkwa", "mylat: " + arNavigation.getMylat());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d("l7wa", "onQueryTextSubmit: " );
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
    public  String loadJSONFromAsset(String filename) {
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
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            json = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
    /*protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

            } else {
                String qrContent = result.getContents();
                Log.d("QR content", "qrContent: " + qrContent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

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