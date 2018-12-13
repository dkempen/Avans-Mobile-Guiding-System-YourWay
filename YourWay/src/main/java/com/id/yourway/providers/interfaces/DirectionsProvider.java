package com.id.yourway.providers.interfaces;

import com.google.android.gms.maps.model.LatLng;
import com.id.yourway.providers.listeners.DirectionsProviderListener;

import java.util.List;

public interface DirectionsProvider {

    void queueDirectionsRequest(List<LatLng> wayPoints, DirectionsProviderListener listener);


}
