package com.id.yourway.providers;

import android.content.Context;

import com.android.volley.VolleyError;
import com.id.yourway.entities.Sight;
import com.id.yourway.providers.interfaces.SightProvider;
import com.id.yourway.providers.listeners.RestProviderListener;
import com.id.yourway.providers.listeners.SightProviderListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BlindWallsProvider implements SightProvider {

    private final RestProvider restProvider;

    public BlindWallsProvider(Context context) {
        restProvider = RestProvider.getInstance(context);
    }

    @Override
    public void getSights(SightProviderListener listener) {
        restProvider.getRequest("https://api.blindwalls.gallery/apiv2/murals",
                new RestProviderListener() {
                    @Override
                    public void onRequestObjectAvailible(JSONObject response) {
                        List<Sight> sights = new ArrayList<>();
                        listener.onSightsAvailable(sights);
                    }

                    @Override
                    public void onRequestError(VolleyError error) {

                    }
                }, false);
    }
}
