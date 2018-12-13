package com.id.yourway.business;

import android.content.Context;

import com.id.yourway.providers.BlindWallsProvider;
import com.id.yourway.providers.interfaces.SightProvider;
import com.id.yourway.providers.listeners.SightProviderListener;

public class SightManager {

    SightProvider sightProvider;

    public SightManager(Context context) {
        sightProvider = new BlindWallsProvider(context);
    }

    public void getSights(SightProviderListener listener) {
        sightProvider.getSights(listener);
    }
}
