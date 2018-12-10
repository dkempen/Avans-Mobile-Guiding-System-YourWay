package com.id.yourway.providers;

import android.content.Context;

import com.id.yourway.providers.interfaces.DirectionsProvider;
import com.id.yourway.providers.listeners.DirectionsProviderListener;

public class GoogleDirectionsProvider implements DirectionsProvider {

    private String apikey;
    private RestProvider restProvider;

    public GoogleDirectionsProvider(Context context) {
        restProvider = new RestProvider();
        //context.getResources().getString()
    }

    @Override
    public void queueDirectionsRequest(DirectionsProviderListener listener) {

    }
}
