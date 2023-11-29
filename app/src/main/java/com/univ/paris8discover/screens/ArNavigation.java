package com.univ.paris8discover.screens;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.univ.paris8discover.R;
import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


public class ArNavigation extends AppCompatActivity implements OnMapReadyCallback{
    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        mapView = findViewById(R.id.mapindoorView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        String jsonResponse = "{\"num_of_pois\":9,\"pois\":[{\"lat\":\"48.946754634784924\",\"lon\":\"2.361871765468493\",\"puid\":\"poi_207bc08b-8293-4e89-b9a8-79cea06c2762\",\"buid\":\"building_20b6bbf0-579a-4f09-b9cf-ac0f0cc165ec_1700156714389\",\"floor_number\":\"0\",\"pois_type\":\"Other\"}," +
                "{\"lat\":\"48.94686649421915\",\"lon\":\"2.3617349728087333\",\"puid\":\"poi_3e6eea0c-692a-4ac0-9e79-ad3aecb19559\",\"buid\":\"building_20b6bbf0-579a-4f09-b9cf-ac0f0cc165ec_1700156714389\",\"floor_number\":\"0\",\"pois_type\":\"None\"}," +
                "{\"lat\":\"48.947012743410156\",\"lon\":\"2.3617712145826353\",\"puid\":\"poi_2dd75a21-5323-4d68-b564-71be2eeabfdb\",\"buid\":\"building_20b6bbf0-579a-4f09-b9cf-ac0f0cc165ec_1700156714389\",\"floor_number\":\"0\",\"pois_type\":\"Entrance\"},{\"lat\":\"48.947047503184905\",\"lon\":\"2.3616974613357655\",\"puid\":\"poi_11cd4971-529a-49be-9021-bea4da1d594d\",\"buid\":\"building_20b6bbf0-579a-4f09-b9cf-ac0f0cc165ec_1700156714389\",\"floor_number\":\"0\",\"pois_type\":\"None\"},{\"lat\":\"48.947079018564324\",\"lon\":\"2.361715176744026\",\"puid\":\"poi_3ceaed47-1b8a-49cf-b877-1bb140766f29\",\"buid\":\"building_20b6bbf0-579a-4f09-b9cf-ac0f0cc165ec_1700156714389\",\"floor_number\":\"0\",\"pois_type\":\"Couloire\"},{\"lat\":\"48.947125807983824\",\"lon\":\"2.3617646460401254\",\"puid\":\"poi_6ba9dbac-d12b-4834-80da-c0d69adfc16e\",\"buid\":\"building_20b6bbf0-579a-4f09-b9cf-ac0f0cc165ec_1700156714389\",\"floor_number\":\"0\",\"pois_type\":\"None\"},{\"lat\":\"48.94715355452803\",\"lon\":\"2.3617857990941538\",\"puid\":\"poi_f879cd8d-2040-4e8c-96ed-41b0c7e385c3\",\"buid\":\"building_20b6bbf0-579a-4f09-b9cf-ac0f0cc165ec_1700156714389\",\"floor_number\":\"0\",\"pois_type\":\"Couloire\"},{\"lat\":\"48.94717350191506\",\"lon\":\"2.361807729022427\",\"puid\":\"poi_68c81f4d-8265-47ab-b690-33970dd61cca\",\"buid\":\"building_20b6bbf0-579a-4f09-b9cf-ac0f0cc165ec_1700156714389\",\"floor_number\":\"0\",\"pois_type\":\"None\"},{\"lat\":\"48.94718575874096\",\"lon\":\"2.3618788189353435\",\"puid\":\"poi_8a8d472f-dde2-4722-a5a4-3b5067e34234\",\"buid\":\"building_20b6bbf0-579a-4f09-b9cf-ac0f0cc165ec_1700156714389\",\"floor_number\":\"0\",\"pois_type\":\"Room\"}],\"status\":\"success\",\"message\":\"Successfully plotted navigation.\",\"status_code\":200}";
                updateRoute(jsonResponse);
    }

    private void updateRoute(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray poisArray = jsonObject.getJSONArray("pois");

            List<LatLng> newCoordinates = new ArrayList<>();
            for (int i = 0; i < poisArray.length(); i++) {
                JSONObject poi = poisArray.getJSONObject(i);
                double lat = poi.getDouble("lat");
                double lng = poi.getDouble("lon");
                newCoordinates.add(new LatLng(lat, lng));
            }

            // Update the PolylineOptions with the new coordinates
            PolylineOptions polylineOptions = new PolylineOptions()
                    .width(5)
                    .color(Color.BLUE);
            for (LatLng coord : newCoordinates) {
                polylineOptions.add(coord);
            }

            // Clear the previous route and draw the new one
            googleMap.clear();
            googleMap.addPolyline(polylineOptions);

            // Move the camera to the first point in the new route
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newCoordinates.get(0), 18));

            addMarkersForPoints(newCoordinates);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addMarkersForPoints(List<LatLng> coordinates) {
        if (!coordinates.isEmpty()) {
            googleMap.addMarker(new MarkerOptions().position(coordinates.get(0)).title("Start Point"));
            googleMap.addMarker(new MarkerOptions().position(coordinates.get(coordinates.size() - 1)).title("Destination Point"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

