package com.univ.paris8discover.screens;

import com.univ.paris8discover.utils.ApiCaller;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.univ.paris8discover.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ArNavigation extends AppCompatActivity implements OnMapReadyCallback{
    private MapView mapView;
    private GoogleMap googleMap;

    private String jsonfile = "";
    private double mylat;
    private double mylon;
    private String puid = "poi_6d5113e7-5fd3-466d-bcb9-9dd6d3312e6f";

    public ArNavigation(double mylat, double mylon, String puid ){
        this.mylat = mylat;
        this.mylon = mylon;
        this.puid = puid;
    }
    public ArNavigation() {
    }

    public double getMylat(){
        return this.mylat;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            this.mylat = intent.getDoubleExtra("lat", 0.0);
            this.mylon = intent.getDoubleExtra("lon", 0.0);
            this.puid = intent.getStringExtra("puid");
        }


        Log.d("Lkwa", "mylat: " + this.puid);
        setContentView(R.layout.navigation);





        jsonfile =  loadJSONFromAsset("stairsandexits");
        mapView = findViewById(R.id.mapindoorView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        String coordinates_lon = "" + this.mylon;
        String coordinates_lat = "" + this.mylat;
        String floor_number = "0";
        String puid = this.puid;

        ApiCaller apiCaller = new ApiCaller();
        apiCaller.execute("https://ap.cs.ucy.ac.cy:44/api/navigation/route/coordinates",
                "{\n" +
                        "  \"coordinates_lon\": \"" + coordinates_lon + "\",\n" +
                        "  \"coordinates_lat\": \"" + coordinates_lat + "\",\n" +
                        "  \"floor_number\": \"" + floor_number + "\",\n" +
                        "  \"pois_to\": \"" + puid + "\"\n" +
                        "}");
        try {
            apiCaller.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        googleMap = map;
        String jsonResponse = apiCaller.getResult();
        Log.d("TAG", "onMapReady: " + jsonResponse);
        updateRoute(jsonResponse);
    }

    private void updateRoute(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.has("pois") && !jsonObject.isNull("pois")) {
                JSONArray poisArray = jsonObject.getJSONArray("pois");
                List<LatLng> newCoordinates = new ArrayList<>();
                for (int i = 0; i < poisArray.length(); i++) {
                    JSONObject poi = poisArray.getJSONObject(i);
                    double lat = poi.getDouble("lat");
                    double lng = poi.getDouble("lon");
                    String type = getTypeForPuid(jsonfile, poi.getString("puid"));
                    if(type.equals("Stair")){
                        Log.d("lkwa", "lkwa : " + type);
                    }

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
            }

        } catch (JSONException e) {
            Log.d("Error", "updateRoute: " +  e.getClass().getName());;
        }
    }

    private static String getTypeForPuid(String json, String puid) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString(puid);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    private void addMarkersForPoints(List<LatLng> coordinates) {
        if (!coordinates.isEmpty()) {
            googleMap.addMarker(new MarkerOptions().position(coordinates.get(0)).title("Moi"));
            googleMap.addMarker(new MarkerOptions().position(coordinates.get(coordinates.size() - 1)).title("Destination Point"));
        }
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

