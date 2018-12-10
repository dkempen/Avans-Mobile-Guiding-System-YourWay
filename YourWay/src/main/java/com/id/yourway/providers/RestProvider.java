package com.id.yourway.providers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.id.yourway.providers.listeners.RestProviderListener;

public class RestProvider {

    private static RestProvider Instance = null;
    private static Context context;
    private RequestQueue requestqueue;

    public RestProvider(Context context) {
        this.context = context;
        requestqueue = Volley.newRequestQueue(context);
    }

    public static RestProvider getInstance(Context context) {
        if (Instance == null) {
            Instance = new RestProvider(context);
        }
        return Instance;
    }

    public void getRequest(String url, final RestProviderListener listener)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url, null,
                listener::onRequestObjectAvailible,
                listener::onRequestError
        );
        requestqueue.add(jsonObjectRequest);
    }
}
