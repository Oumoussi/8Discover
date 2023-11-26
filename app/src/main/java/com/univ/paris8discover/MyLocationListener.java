package com.univ.paris8discover;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.univ.paris8discover.utils.LocationUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MyLocationListener implements LocationListener {

    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private boolean isFirstLocationUpdate = true;

    // Constructor that takes TextView parameters
    public MyLocationListener(TextView latitudeTextView, TextView longitudeTextView) {
        this.latitudeTextView = latitudeTextView;
        this.longitudeTextView = longitudeTextView;

    }

    @Override
    public void onLocationChanged(Location location) {
        if (isFirstLocationUpdate) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();


            updateCoordinates(latitude, longitude);
            isFirstLocationUpdate = false;

            List<LocationUtils.Point> points = new ArrayList<>();
            points.add(new LocationUtils.Point(48.9467, 2.3618)); // Example point 1
            points.add(new LocationUtils.Point(48.9465, 2.3612)); // Example point 2
            points.add(new LocationUtils.Point(48.9471, 2.3621)); // Example point 3

            LocationUtils.Point closestPoint = LocationUtils.findClosestPoint(latitude, longitude, points);

            if (closestPoint != null) {
                System.out.println("Closest Point: Latitude = " + closestPoint.getLat() +
                        ", Longitude = " + closestPoint.getLon());
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
