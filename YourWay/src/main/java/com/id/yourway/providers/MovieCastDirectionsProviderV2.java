package com.id.yourway.providers;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.id.yourway.R;
import com.id.yourway.providers.helpers.DecoderHelper;
import com.id.yourway.providers.helpers.DirectionBuilderHelper;
import com.id.yourway.providers.interfaces.DirectionsProvider;
import com.id.yourway.providers.listeners.DirectionsProviderListener;
import com.id.yourway.providers.listeners.RestProviderListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieCastDirectionsProviderV2 implements DirectionsProvider {

    private static String BASE_URL = "https://maps.moviecast.io/directions";
    private RestProvider restProvider;
    private String apiKey;

    public MovieCastDirectionsProviderV2(Context context) {
        apiKey = context.getResources().getString(R.string.moviecast_directions_api_key);
        restProvider = RestProvider.getInstance(context);
    }

    private List<LatLng> getPolyLineCoordinates(JSONObject rootObject) throws JSONException {
        String encodedPolyLine = rootObject.getJSONArray("routes")
                .getJSONObject(0)
                .getJSONObject("overview_polyline")
                .getString("points");
        return DecoderHelper.decodePolyLine(encodedPolyLine);
    }

    @Override
    public void queueDirectionsRequest(List<LatLng> wayPoints, DirectionsProviderListener listener) {
        List<LatLng> directionsList = new ArrayList<>();
        List<String> requestParamList = DirectionBuilderHelper.buildDecoupledOptimizedRequest(apiKey, wayPoints);
        final int[] completedRequests = {0};
        for (int i = 0; i < requestParamList.size(); i++) {
            restProvider.getRequest(BASE_URL + requestParamList.get(i), new RestProviderListener() {
                @Override
                public void onRequestObjectAvailible(JSONObject response) {
                    //dit moet een 1 elements array zijn probeer maar eens zonder(btw object kan ook :))
                    completedRequests[0] += 1;
                    try {
                        List<LatLng> subDirectionList = getPolyLineCoordinates(response);
                        directionsList.addAll(subDirectionList);
                        if (completedRequests[0] == requestParamList.size()) {
                            listener.onReceivedDirections(directionsList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onRequestError(VolleyError error) {
                }
            }, true);
        }
    }
}
