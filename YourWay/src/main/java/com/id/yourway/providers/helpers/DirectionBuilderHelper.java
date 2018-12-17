package com.id.yourway.providers.helpers;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DirectionBuilderHelper {

    private static final int ALLOWEDREQUESTS = 25;

    private static String formatLatLng(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }

    private static String buildStandardRequest(String key, List<LatLng> wayPoints) {
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("?key=");
        requestBuilder.append(key);
        requestBuilder.append("&origin=");
        requestBuilder.append(formatLatLng(wayPoints.get(0)));
        if (wayPoints.size() > 2) {
            requestBuilder.append("&waypoints=");
            StringBuilder wpBuilder = new StringBuilder();
            for (int i = 1; i < wayPoints.size() - 1; i++) {
                wpBuilder.append(formatLatLng(wayPoints.get(i)));
                wpBuilder.append("|");
            }
            String wayPointString = wpBuilder.toString();
            requestBuilder.append(wayPointString.substring(0, wayPointString.length() - 2));
        }
        requestBuilder.append("&destination=");
        requestBuilder.append(formatLatLng(wayPoints.get(wayPoints.size() - 1)));
        return requestBuilder.toString();
    }

    public static List<String> buildDecoupledOptimizedRequest(String key, List<LatLng> wayPoints) {
        List<String> requestList = new ArrayList<>();
        int remainderRequest = wayPoints.size() % ALLOWEDREQUESTS;
        int numRequests = wayPoints.size() - remainderRequest;
        for (int i = 0; i < numRequests; i += ALLOWEDREQUESTS) {
            List<LatLng> decoupledWaypoints = wayPoints.subList(i, i + ALLOWEDREQUESTS);
            String requestString = buildStandardRequest(key, decoupledWaypoints);
            requestList.add(requestString);
        }
        if (remainderRequest > 0) {
            List<LatLng> remainingWaypoints = wayPoints.subList(numRequests, numRequests + remainderRequest);
            String requestString = buildStandardRequest(key, remainingWaypoints);
            requestList.add(requestString);
        }
        return requestList;
    }
}
