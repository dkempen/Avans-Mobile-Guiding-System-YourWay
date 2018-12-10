package com.id.yourway.providers.listeners;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface DirectionsProviderListener {
    void onReceivedDirections(List<LatLng> directionList);
}
