package com.univ.paris8discover.models;

public class Point {
    private String name;
    private String image;
    private int floor_number;
    private boolean is_building_entrance;
    private String floor_name;
    private boolean is_door;
    private String puid;
    private String buid;
    private String pois_type;
    private double coordinates_lon;
    private double coordinates_lat;
    private boolean is_published;

    public Point(String name, String puid) {
        this.name = name;
        this.puid = puid;
    }
    // Constructor
    public Point(String name, String image, int floor_number, boolean is_building_entrance, String floor_name, boolean is_door, String puid, String buid, String pois_type, double coordinates_lon, double coordinates_lat, boolean is_published) {
        this.name = name;
        this.image = image;
        this.floor_number = floor_number;
        this.is_building_entrance = is_building_entrance;
        this.floor_name = floor_name;
        this.is_door = is_door;
        this.puid = puid;
        this.buid = buid;
        this.pois_type = pois_type;
        this.coordinates_lon = coordinates_lon;
        this.coordinates_lat = coordinates_lat;
        this.is_published = is_published;
    }





    public double getCoordinatesLon() {
        return coordinates_lon;
    }



    public double getCoordinatesLat() {
        return coordinates_lat;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getFloor_number() {
        return floor_number;
    }

    public boolean isIs_building_entrance() {
        return is_building_entrance;
    }

    public String getFloor_name() {
        return floor_name;
    }

    public boolean isIs_door() {
        return is_door;
    }

    public String getPuid() {
        return puid;
    }

    public String getBuid() {
        return buid;
    }

    public String getPois_type() {
        return pois_type;
    }

    public double getCoordinates_lon() {
        return coordinates_lon;
    }

    public double getCoordinates_lat() {
        return coordinates_lat;
    }

    public boolean isIs_published() {
        return is_published;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setFloor_number(int floor_number) {
        this.floor_number = floor_number;
    }

    public void setIs_building_entrance(boolean is_building_entrance) {
        this.is_building_entrance = is_building_entrance;
    }

    public void setFloor_name(String floor_name) {
        this.floor_name = floor_name;
    }

    public void setIs_door(boolean is_door) {
        this.is_door = is_door;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public void setBuid(String buid) {
        this.buid = buid;
    }

    public void setPois_type(String pois_type) {
        this.pois_type = pois_type;
    }

    public void setCoordinates_lon(double coordinates_lon) {
        this.coordinates_lon = coordinates_lon;
    }

    public void setCoordinates_lat(double coordinates_lat) {
        this.coordinates_lat = coordinates_lat;
    }

    public void setIs_published(boolean is_published) {
        this.is_published = is_published;
    }

    // toString method
    @Override
    public String toString() {
        return "Poi{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", floor_number=" + floor_number +
                ", is_building_entrance=" + is_building_entrance +
                ", floor_name='" + floor_name + '\'' +
                ", is_door=" + is_door +
                ", puid='" + puid + '\'' +
                ", buid='" + buid + '\'' +
                ", pois_type='" + pois_type + '\'' +
                ", coordinates_lon=" + coordinates_lon +
                ", coordinates_lat=" + coordinates_lat +
                ", is_published=" + is_published +
                '}';
    }
}
