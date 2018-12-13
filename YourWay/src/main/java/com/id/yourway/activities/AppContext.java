package com.id.yourway.activities;

import android.content.Context;

import com.id.yourway.business.RouteManager;
import com.id.yourway.business.SightManager;

public class AppContext {

    private static AppContext instance;
    private SightManager sightManager;
    private RouteManager routeManager;


    public static AppContext getInstance(Context context) {
        if(instance == null)
            instance = new AppContext(context);
        return instance;
    }

    private AppContext(Context context) {
        sightManager = new SightManager(context);
        routeManager = new RouteManager(context);
    }

    public SightManager getSightManager() {
        return sightManager;
    }

    public RouteManager getRouteManager() {
        return routeManager;
    }
}
