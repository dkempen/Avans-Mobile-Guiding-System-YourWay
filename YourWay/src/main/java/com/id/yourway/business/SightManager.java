package com.id.yourway.business;

import android.content.Context;

import com.id.yourway.entities.Sight;
import com.id.yourway.providers.BlindWallsProvider;
import com.id.yourway.providers.interfaces.SightProvider;
import com.id.yourway.providers.listeners.SightProviderListener;

import java.util.List;

public class SightManager implements SightProviderListener {

    public SightManager(Context context) {
        SightProvider sightProvider = new BlindWallsProvider(context);
        sightProvider.getSights(this);
    }

    @Override
    public void onSightsAvailable(List<Sight> sights) {

    }
}
