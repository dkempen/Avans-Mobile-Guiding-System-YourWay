package com.id.yourway.business.listeners;

import com.id.yourway.entities.Route;

import java.util.List;

public interface RoutesListener {
    void onReceivedRoutes(List<Route> routes);
}
