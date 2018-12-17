package com.id.yourway.business;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.id.yourway.business.listeners.DirectionsListener;
import com.id.yourway.business.listeners.RoutesListener;
import com.id.yourway.entities.Route;
import com.id.yourway.entities.Sight;
import com.id.yourway.providers.MovieCastDirectionsProviderV2;
import com.id.yourway.providers.helpers.JsonLoaderHelper;
import com.id.yourway.providers.interfaces.DirectionsProvider;
import com.id.yourway.providers.listeners.DirectionsProviderListener;
import com.id.yourway.providers.listeners.SightProviderListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RouteManager {

    private DatabaseManager databaseManager;
    private SightManager sightManager;
    private DirectionsProvider directionsProvider;
    private JSONObject jsonRoutes;

    public RouteManager(Context context, SightManager sightManager){
        databaseManager = new DatabaseManager(context);
        this.sightManager = sightManager;
        jsonRoutes = JsonLoaderHelper.loadJsonFile(context, "json/BlindWallsRoutes.json");
        directionsProvider = new MovieCastDirectionsProviderV2(context);
    }

    public void getRoutes(RoutesListener listener){
        List<Route> routes = new ArrayList<>();

        sightManager.getSights(new SightProviderListener() {
            @Override
            public void onSightsAvailable(List<Sight> sights) {
                try {

                    List<Sight>  vvvRouteList = sights.subList(sights.size()-JsonLoaderHelper.VVV_ITEM_SIZE-1, sights.size()-1);
                    Route vvvRoute = new Route("kut vvv route", 1000, vvvRouteList);
                    routes.add(vvvRoute);

                    List<Sight> bwRouteList = sights.subList(0, sights.size() -1);

                    JSONArray routeJsonArray = jsonRoutes.getJSONArray("response");
                    for (int i = 0; i < routeJsonArray.length(); i++) {
                        List<Sight> subSights = new ArrayList<>();
                        JSONObject routeObject = routeJsonArray.getJSONObject(i);
                        JSONArray sightArray = routeObject.getJSONArray("sights");
                        for(int j = 0; j<sightArray.length(); j++){
                            subSights.add(getSightById(sights,sightArray.getInt(j)));
                        }
                        routes.add(new Route(routeObject.getString("name"), routeObject.getInt("km"),subSights));
                    }
                    listener.onReceivedRoutes(routes);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private Sight getSightById(List<Sight> sights,int id){
        for(Sight sight : sights){
            if(sight.getId() == id){
                return sight;
            }
        }
        return  null;
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
