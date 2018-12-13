package com.id.yourway.providers;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.id.yourway.providers.interfaces.DirectionsProvider;
import com.id.yourway.providers.listeners.DirectionsProviderListener;
import com.id.yourway.providers.listeners.RestProviderListener;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GoogleDirectionsProvider implements DirectionsProvider {

    private static String BASE_URL  = "https://maps.googleapis.com/maps/api/directions/json";
    private String apikey;
    private RestProvider restProvider;

    public GoogleDirectionsProvider(Context context) {
        restProvider = RestProvider.getInstance(context);
        apikey = context.getResources().getString(
                context.getResources().getIdentifier("google_maps_key", "string", context.getPackageName())
        );
        Log.e("KEYKEY", apikey);

    }

    @Override
    public void queueDirectionsRequest(List<LatLng> boi,  DirectionsProviderListener listener) {
//            restProvider.getRequest(BASE_URL + "?key="+apikey+"&origin=Toronto&destination=Montreal", new RestProviderListener() {
//                @Override
//                public void onRequestObjectAvailible(JSONObject response, RestResponseType restResponseType) {
//
//                }
//
//                @Override
//                public void onRequestError(VolleyError error, RestResponseType restResponseType) {
//
//                }
//            },);
    }
}


