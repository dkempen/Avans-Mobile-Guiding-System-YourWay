package com.id.yourway.entities;

import java.util.List;

public class Route {

    private List<Sight> sights;

    private String name;
    private double lengthInKm;
    private int progress;

    public Route(String name, double lengthInKm,  List<Sight> sights) {
        this.name = name;
        this.lengthInKm = lengthInKm;
        this.sights = sights;
    }

    public List<Sight> getSights() {
        return sights;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public double getLengthInKm() {
        return lengthInKm;
    }
}
