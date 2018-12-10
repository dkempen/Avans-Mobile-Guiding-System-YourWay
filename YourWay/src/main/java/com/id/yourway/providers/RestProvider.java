package com.id.yourway.providers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.id.yourway.entities.RestResponseType;

import org.json.JSONObject;

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

    public void getRequest(String url, final RestProviderListener listener, final RestResponseType restResponseType)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        listener.onRequestObjectAvailible(response, restResponseType);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        listener.onRequestError(error, restResponseType);
                    }
                }
        );
        requestqueue.add(jsonObjectRequest);
    }
}
