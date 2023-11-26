package com.univ.paris8discover.models;

public class Point {
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
}
