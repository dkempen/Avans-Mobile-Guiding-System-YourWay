package com.id.yourway.providers;

import com.android.volley.VolleyError;
import com.id.yourway.entities.RestResponseType;

import org.json.JSONObject;

public interface RestProviderListener {

    public void onRequestObjectAvailible(JSONObject response, RestResponseType restResponseType);

    public void onRequestError(VolleyError error, RestResponseType restResponseType);

}
