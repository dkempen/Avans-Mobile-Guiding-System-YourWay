package com.id.yourway.activities;

import android.content.Context;

import com.id.yourway.business.feedback.FeedbackManager;
import com.id.yourway.business.RouteManager;
import com.id.yourway.business.SightManager;

public class AppContext {

    private static AppContext instance;
    private SightManager sightManager;
    private FeedbackManager feedbackManager;
    private RouteManager routeManager;

    public static AppContext getInstance(Context context) {
        if(instance == null)
            instance = new AppContext(context);
        return instance;
    }

    private AppContext(Context context) {
        sightManager = new SightManager(context);
        routeManager = new RouteManager(context, sightManager);
        feedbackManager = new FeedbackManager();
    }

    public SightManager getSightManager() {
        return sightManager;
    }

    public FeedbackManager getFeedbackManager() {
        return feedbackManager;
    }

    public RouteManager getRouteManager() {
        return routeManager;
    }
}
