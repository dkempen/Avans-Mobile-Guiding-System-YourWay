package com.id.yourway.business;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.id.yourway.business.listeners.DirectionsListener;
import com.id.yourway.providers.MovieCastDirectionsProviderV2;
import com.id.yourway.providers.interfaces.DirectionsProvider;
import com.id.yourway.providers.listeners.DirectionsProviderListener;

import java.util.ArrayList;
import java.util.List;

public class RouteManager {

    private DatabaseManager databaseManager;
    private DirectionsProvider directionsProvider;
    private List<String> routes;

    public RouteManager(Context context){
        databaseManager = new DatabaseManager(context);
        directionsProvider = new MovieCastDirectionsProviderV2(context);
        routes = new ArrayList<>();
        routes.add("Blindwallroute 1");
        routes.add("Blindwallroute 2");
        routes.add("VVV route");
    }

    public List<String> getRoutes(){
        return routes;
    }

    public void getDirections(List<LatLng> wayPoint, DirectionsListener listener){
        directionsProvider.queueDirectionsRequest(wayPoint, new DirectionsProviderListener() {
            @Override
            public void onReceivedDirections(List<LatLng> directionList) {
                listener.onReceivedDirections(directionList);
            }

            @Override
            public void onError(Error error) {
                listener.onError(error);
            }
        });
    }

    public void storeRouteProgression(String routeName, int progression){
        databaseManager.storeRouteProgression(routeName, progression);
    }

    public int getRouteProgression(String routeName){
        return databaseManager.getRouteProgression(routeName);
    }






}
