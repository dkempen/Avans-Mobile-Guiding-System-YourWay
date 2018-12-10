package com.id.yourway.providers;

import com.google.android.gms.maps.model.LatLng;
import com.id.yourway.providers.listeners.DirectionsProviderListener;

public interface DirectionsProvider {

    void queueDirectionsRequest(DirectionsProviderListener listener);

}
