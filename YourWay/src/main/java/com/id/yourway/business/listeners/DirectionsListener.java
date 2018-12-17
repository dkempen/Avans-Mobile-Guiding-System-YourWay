package com.id.yourway.business.listeners;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface DirectionsListener {
    void onReceivedDirections(List<LatLng> directionList);
    void onError(Error error);
}
