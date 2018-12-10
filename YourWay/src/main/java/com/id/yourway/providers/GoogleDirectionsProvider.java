package com.id.yourway.providers;

import android.content.Context;

import com.id.yourway.providers.interfaces.DirectionsProvider;
import com.id.yourway.providers.listeners.DirectionsProviderListener;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class GoogleDirectionsProvider implements DirectionsProvider {

    private static String BASE_URL  = "";
    private String apikey;
    private RestProvider restProvider;

    public GoogleDirectionsProvider(Context context) {
        restProvider = RestProvider.getInstance(context);

    }

    @Override
    public void queueDirectionsRequest(DirectionsProviderListener listener) {
            restProvider.getRequest(BASE_URL + "", new RestProviderListener() {
                @Override
                public void onRequestObjectAvailible(JSONObject response, RestResponseType restResponseType) {

                }

                @Override
                public void onRequestError(VolleyError error, RestResponseType restResponseType) {

                }
            },);
    }
}
