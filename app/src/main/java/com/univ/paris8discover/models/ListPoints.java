package com.univ.paris8discover.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ListPoints {
    @JsonProperty("pois")
    private ArrayList<Point> pois;

    public ArrayList<Point> getPois() {
        return pois;
    }

    public void setPois(ArrayList<Point> pois) {
        this.pois = pois;
    }
}
