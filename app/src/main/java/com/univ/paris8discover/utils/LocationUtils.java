package com.univ.paris8discover.utils;

import com.univ.paris8discover.models.Point;

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
        double minDistance = haversineDistance(myLat, myLon, closestPoint.getCoordinatesLat(), closestPoint.getCoordinatesLon());

        for (Point point : points) {
            double distance = haversineDistance(myLat, myLon, point.getCoordinatesLat(), point.getCoordinatesLon());
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = point;
            }
        }

        return closestPoint;
    }

/*
    public static class Point {
        private String name;
        private String image;
        private String floorNumber;
        private boolean isBuildingEntrance;
        private String floorName;
        private boolean isDoor;
        private String puid;
        private double coordinatesLon;
        private String buid;
        private String poisType;
        private double coordinatesLat;
        private boolean isPublished;


        public Point(String name,
                     String floorName,String puid, double coordinatesLon,
                     String buid,  double coordinatesLat) {
            this.name = name;
            this.floorName = floorName;
            this.puid = puid;
            this.coordinatesLon = coordinatesLon;
            this.buid = buid;
            this.coordinatesLat = coordinatesLat;
        }

        // Constructor
        public Point(String name, String image, String floorNumber, boolean isBuildingEntrance,
                     String floorName, boolean isDoor, String puid, double coordinatesLon,
                     String buid, String poisType, double coordinatesLat, boolean isPublished) {
            this.name = name;
            this.image = image;
            this.floorNumber = floorNumber;
            this.isBuildingEntrance = isBuildingEntrance;
            this.floorName = floorName;
            this.isDoor = isDoor;
            this.puid = puid;
            this.coordinatesLon = coordinatesLon;
            this.buid = buid;
            this.poisType = poisType;
            this.coordinatesLat = coordinatesLat;
            this.isPublished = isPublished;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public String getFloorNumber() {
            return floorNumber;
        }

        public boolean isBuildingEntrance() {
            return isBuildingEntrance;
        }

        public String getFloorName() {
            return floorName;
        }

        public boolean isDoor() {
            return isDoor;
        }

        public String getPuid() {
            return puid;
        }

        public double getCoordinatesLon() {
            return coordinatesLon;
        }

        public String getBuid() {
            return buid;
        }

        public String getPoisType() {
            return poisType;
        }

        public double getCoordinatesLat() {
            return coordinatesLat;
        }

        public boolean isPublished() {
            return isPublished;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "name='" + name + '\'' +
                    ", image='" + image + '\'' +
                    ", floorNumber='" + floorNumber + '\'' +
                    ", isBuildingEntrance=" + isBuildingEntrance +
                    ", floorName='" + floorName + '\'' +
                    ", isDoor=" + isDoor +
                    ", puid='" + puid + '\'' +
                    ", coordinatesLon=" + coordinatesLon +
                    ", buid='" + buid + '\'' +
                    ", poisType='" + poisType + '\'' +
                    ", coordinatesLat=" + coordinatesLat +
                    ", isPublished=" + isPublished +
                    '}';
        }
    }*/
}
