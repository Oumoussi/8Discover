package com.univ.paris8discover.utils;

import java.util.List;

public class LocationUtils {

    // Radius of the Earth in kilometers
    private static final double EARTH_RADIUS_KM = 6371.0;

    // Function to calculate the Haversine distance between two sets of coordinates
    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    // Function to find the closest point from a list of points to a given location
    public static Point findClosestPoint(double myLat, double myLon, List<Point> points) {
        if (points.isEmpty()) {
            return null; // Return null if the list of points is empty
        }

        Point closestPoint = points.get(0);
        double minDistance = haversineDistance(myLat, myLon, closestPoint.getLat(), closestPoint.getLon());

        for (Point point : points) {
            double distance = haversineDistance(myLat, myLon, point.getLat(), point.getLon());
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = point;
            }
        }

        return closestPoint;
    }

    // Class representing a point with latitude and longitude
    public static class Point {
        private double lat;
        private double lon;

        public Point(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }
}
