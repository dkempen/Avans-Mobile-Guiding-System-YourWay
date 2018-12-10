package com.id.yourway.entities;

import java.util.List;

public class Route {

    private List<Sight> sights;
    private int progress;

    public Route(List<Sight> sights) {
        this.sights = sights;
    }

    public List<Sight> getSights() {
        return sights;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
