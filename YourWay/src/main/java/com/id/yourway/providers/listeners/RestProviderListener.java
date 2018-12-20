package com.id.yourway.providers.listeners;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RestProviderListener {
    void onRequestObjectAvailible(JSONObject response);
    void onRequestError(VolleyError error);
}
