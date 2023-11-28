package com.univ.paris8discover;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView closestpoint;
    private GoogleMap googleMap;
    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.map);
/*
        checkLocationPermission();

        latitudeTextView = findViewById(R.id.Latitude);
        longitudeTextView = findViewById(R.id.Longitude);
        closestpoint = findViewById(R.id.closestpoint);
        // Button to trigger location updates
        Button getLocationButton = findViewById(R.id.getLocationButton);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationUpdates();
            }
        });*/
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
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

        LatLng currentLocation = new LatLng(48.9467, 2.3618);
        googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker at Current Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
    }
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void requestLocationUpdates() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Use FusedLocationProviderClient for more recent API versions
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000,  // Minimum time interval between updates in milliseconds
                        1,     // Minimum distance between updates in meters
                        new MyLocationListener(latitudeTextView, longitudeTextView, closestpoint)
                );

            } else {
                // Handle the case where the user has not granted location permission
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            }
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
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
