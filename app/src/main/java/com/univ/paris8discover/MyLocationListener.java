package com.univ.paris8discover;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.univ.paris8discover.models.Point;
import com.univ.paris8discover.utils.LocationUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MyLocationListener implements LocationListener {

    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView closestpoint;
    private boolean isFirstLocationUpdate = true;

    // Constructor that takes TextView parameters
    public MyLocationListener(TextView latitudeTextView, TextView longitudeTextView, TextView closestpoint) {
        this.latitudeTextView = latitudeTextView;
        this.longitudeTextView = longitudeTextView;
        this.closestpoint = closestpoint;

    }

    @Override
    public void onLocationChanged(Location location) {
        if (isFirstLocationUpdate) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();


            updateCoordinates(latitude, longitude);
            isFirstLocationUpdate = false;

            List<Point> points = new ArrayList<>();
            String buid = "building_20b6bbf0-579a-4f09-b9cf-ac0f0cc165ec_1700156714389";
            points.add(new Point("Green space","D0","poi_207bc08b-8293-4e89-b9a8-79cea06c2762", 48.9467, buid, 2.3618));
            points.add(new Point("Right green space","D0","poi_0cff82e7-1918-4268-b0e2-ecf218b8104e", 48.9465, buid, 2.3612));
            points.add(new Point("Left green space","D0","poi_ec45c0ab-a4f9-496b-96dc-f9c8efbf6d41", 48.9471, buid,  2.3621));


            Point closestPoint = LocationUtils.findClosestPoint(latitude, longitude, points);

            if (closestPoint != null) {
                /*System.out.println("Closest Point: Latitude = " + closestPoint.getCoordinatesLat() +
                        ", Longitude = " + closestPoint.getCoordinatesLon());*/

                closestpoint.setText("Closest Point: " + closestPoint.toString());
                System.out.println("Closest Point:  " + closestPoint.toString());
            } else {
                System.out.println("No points provided or the list is empty.");
            }

        }
    }
    private void updateCoordinates(double latitude, double longitude) {
        // Update the TextView with the new coordinates
        String coordinatesText = "Latitude: " + latitude + "\nLongitude: " + longitude;
        Log.d("coordinatesText", "updateCoordinates: " + latitude + " " + longitude);
        latitudeTextView.setText("Latitude: " + latitude);
        longitudeTextView.setText("Longitude: " + longitude);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle status changes if needed
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Handle provider enablement if needed
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Handle provider disablement if needed
    }
}
