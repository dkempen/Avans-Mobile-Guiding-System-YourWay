package com.id.yourway.providers;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.id.yourway.R;
import com.id.yourway.providers.RestProvider;
import com.id.yourway.providers.helpers.DecoderHelper;
import com.id.yourway.providers.interfaces.DirectionsProvider;
import com.id.yourway.providers.interfaces.SightProvider;
import com.id.yourway.providers.listeners.DirectionsProviderListener;
import com.id.yourway.providers.listeners.RestProviderListener;
import com.id.yourway.providers.listeners.SightProviderListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieCastDirectionsProvider implements DirectionsProvider {

    private static String BASE_URL = "https://maps.moviecast.io/directions";
    private RestProvider restProvider;
    private String apiKey;

    public MovieCastDirectionsProvider(Context context) {
        apiKey = context.getResources().getString(R.string.moviecast_directions_api_key);
        restProvider = RestProvider.getInstance(context);
    }


    @Override
    public void queueDirectionsRequest(List<LatLng> wayPoints, DirectionsProviderListener listener) {
        String requestParams = DirectionBuilderHelper.buildStandardRequest(apiKey, wayPoints);
        restProvider.getRequest(BASE_URL + requestParams, new RestProviderListener() {
            @Override
            public void onRequestObjectAvailible(JSONObject response) {
                try {
                    String encodedPolyLine =  response.getJSONArray("routes")
                             .getJSONObject(0)
                             .getJSONObject("overview_polyline")
                             .getString("points");
                   listener.onReceivedDirections(DecoderHelper.decodePolyLine(encodedPolyLine));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestError(VolleyError error) {
                Log.e("error", "n***a");
            }
        }, true);


    }


}



class DirectionBuilderHelper{

    public static final int ALLOWEDREQUESTS = 25;

    private static String formatLatLng(LatLng latLng){
            return latLng.latitude+","+latLng.longitude;
        }
        public static String buildStandardRequest(String key, List<LatLng> wayPoints){
            StringBuilder requestBuilder = new StringBuilder();
            requestBuilder.append("?key=");
            requestBuilder.append(key);
            requestBuilder.append("&origin=");
            requestBuilder.append(formatLatLng(wayPoints.get(0)));
            if(wayPoints.size() > 2) {
                requestBuilder.append("&waypoints=");
                StringBuilder wpBuilder = new StringBuilder();
                for (int i = 1; i < wayPoints.size() - 1; i++) {
                    wpBuilder.append(formatLatLng(wayPoints.get(i)));
                    wpBuilder.append("|");
                }
                String wayPointString = wpBuilder.toString();
                requestBuilder.append(wayPointString.substring(0, wayPointString.length()-2));
            }
            requestBuilder.append("&destination=");
            requestBuilder.append(formatLatLng(wayPoints.get(wayPoints.size()-1)));
            return requestBuilder.toString();
        }

    public static List<String> buildDecoupledOptimizedRequest(String key, List<LatLng> wayPoints){
        List<String> requestList = new ArrayList<>();
        int remainderRequest = wayPoints.size() % ALLOWEDREQUESTS;
        int numRequests =  wayPoints.size()-remainderRequest;
        for(int i  = 0; i< numRequests; i+= ALLOWEDREQUESTS){
           List<LatLng> decoupledWaypoints =  wayPoints.subList(i, i+ALLOWEDREQUESTS);
           String requestString = buildStandardRequest(key, decoupledWaypoints);
           requestList.add(requestString);
        }
        if(remainderRequest > 0){
            List<LatLng> remainingWaypoints =  wayPoints.subList(numRequests, numRequests+remainderRequest);
            String requestString = buildStandardRequest(key, remainingWaypoints);
            requestList.add(requestString);
        }
        return requestList;
    }

}