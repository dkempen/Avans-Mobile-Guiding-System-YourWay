package com.id.yourway.providers.listeners;

import android.content.Context;

import com.id.yourway.R;
import com.id.yourway.providers.interfaces.SightProvider;

public class MovieCastDirectionsProvider implements SightProvider {

    public MovieCastDirectionsProvider(Context context) {
        context.getResources().getString(R.string.moviecast_directions_api_key);
    }

    @Override
    public void getSights(SightProviderListener listener) {

    }
}
