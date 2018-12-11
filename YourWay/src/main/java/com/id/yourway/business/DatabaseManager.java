package com.id.yourway.business;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.id.yourway.entities.Route;
import com.id.yourway.providers.AppDatabase;
import com.id.yourway.providers.RouteDAO;

import java.util.List;

public class DatabaseManager {

    private AppDatabase database;
    private RouteDAO routeDAO;
    public DatabaseManager(Context context) {
        database = new AppDatabase(context);
        routeDAO = new RouteDAO(database);
    }

    public void storeRouteProgression(String routeName, int progression){
        routeDAO.storeProgression(routeName, progression);
    }

    public int getRouteProgression(String routeName){
        return routeDAO.getProgession(routeName);
    }

}
