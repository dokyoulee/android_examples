package com.sai.test.testremoteprovder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "REMOTE_PROVIDER_DB";

    static final String TAB_USER = "USER";
    static final String TAB_USER_ID = "_ID";
    static final String TAB_USER_USERID = "USERID";
    static final String TAB_USER_NAME = "NAME";
    static final String TAB_USER_AGE = "AGE";
    static final String TAB_USER_LOCATION = "LOCATION";

    private static final String SQL_CREATE_MAIN = "CREATE TABLE " + TAB_USER + " (" +
            TAB_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TAB_USER_USERID + " VARCHAR(30) NOT NULL," +
            TAB_USER_NAME + " VARCHAR(30) NOT NULL," +
            TAB_USER_AGE + " INTEGER," +
            TAB_USER_LOCATION + " VARCHAR(30));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MAIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DatabaseHelper", "onUpgrade called.");
    }
}
