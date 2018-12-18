package com.id.yourway.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.id.yourway.providers.helpers.TableCreationHelper;

import java.util.ArrayList;
import java.util.List;

public class RouteDAO {

    private static String ROUTE_ID = "ROUTE_ID";
    private static String ROUTE_PROGRESSION = "ROUTE_PROGRESSION";
    static final String TABLE_NAME = "PROGRESSION_TABLE";

    private AppDatabase database;

    static String fetchTableSignature() {
        return TableCreationHelper.createTableWithColumns(
                TABLE_NAME,
                RouteDAO.ROUTE_ID + " VARCHAR(50) PRIMARY KEY", RouteDAO.ROUTE_PROGRESSION + " INTEGER"
        );
    }

    public RouteDAO(AppDatabase database) {
        //TODO add threading and callbacks in v2
        this.database = database;
    }

    public void storeProgression(String routeName, int poiNumber) {
        SQLiteDatabase writableDatabase = database.getWritableDatabase();
        ContentValues tableValues = new ContentValues();
        //if the record already exists update instead
        if (progressionRecordExists(writableDatabase, routeName)) {
            String selector = ROUTE_ID + " = ?";
            String[] selectorArgs = {routeName};
            tableValues.put(ROUTE_PROGRESSION, poiNumber);
            writableDatabase.update(TABLE_NAME, tableValues, selector, selectorArgs);
        } else {
            tableValues.put(ROUTE_ID, routeName);
            tableValues.put(ROUTE_PROGRESSION, poiNumber);
            writableDatabase.insertOrThrow(TABLE_NAME, null, tableValues);
        }

        database.close();
    }

    public List<String> getProgessionListByName() {
        List<String> routeProgessionList = new ArrayList<>();
        SQLiteDatabase readableDatabase = database.getReadableDatabase();
        Cursor c = readableDatabase.query(TABLE_NAME, null, null,
                null, null, null, null);
        while (c.moveToNext())
            routeProgessionList.add(c.getString(0));
        return routeProgessionList;
    }

    public int getProgession(String routeName) {
        if (!progressionRecordExists(database.getWritableDatabase(), routeName)) {
            ContentValues tableValues = new ContentValues();
            tableValues.put(ROUTE_ID, routeName);
            tableValues.put(ROUTE_PROGRESSION, 0);
            database.getWritableDatabase().insertOrThrow(TABLE_NAME, null, tableValues);
            return 0;
        }

        String selector = ROUTE_ID + " = ?";
        String[] colArgs = {ROUTE_PROGRESSION};
        String[] selectorArgs = {routeName};
        SQLiteDatabase database = this.database.getReadableDatabase();
        Cursor cursor = database.query(
                RouteDAO.TABLE_NAME,
                colArgs,
                selector,
                selectorArgs,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    private boolean progressionRecordExists(SQLiteDatabase readableDatabase, String routeName) {
        String selector = ROUTE_ID + " = ?";
        String[] selectorArgs = {routeName};
        return DatabaseUtils.queryNumEntries(readableDatabase, TABLE_NAME, selector, selectorArgs) > 0;
    }
}
