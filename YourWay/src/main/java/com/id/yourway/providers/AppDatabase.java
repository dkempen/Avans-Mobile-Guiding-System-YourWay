package com.id.yourway.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabase extends SQLiteOpenHelper {

    private static final String NAME= "appDatabase";
    private static final int VERSION = 1;

    public AppDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO add reflection for finding tables
        db.execSQL(RouteDAO.fetchTableSignature());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       if(oldVersion < newVersion){
           db.execSQL("DROP TABLE IF EXISTS " + RouteDAO.TABLE_NAME);
           onCreate(db);
       }
    }
}
