package com.sai.test.localtestprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class LocalTestProvider extends ContentProvider {
    private static final String PROVIDER_AUTH = "com.sai.test.localtestprovider";
    private static final int PROVIDER_USER = 1;
    private static final int PROVIDER_USER_ID = 2;
    private static final Uri PROVIDER_URI = Uri.parse("content://" + PROVIDER_AUTH + "/user");
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PROVIDER_AUTH, "user", PROVIDER_USER);
        sUriMatcher.addURI(PROVIDER_AUTH, "user/#", PROVIDER_USER_ID);
    }

    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        Log.e("LocalProvider", "onCreate called");
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case PROVIDER_USER:
                break;
            case PROVIDER_USER_ID:
                String id = uri.getPathSegments().get(1);
                selection = DatabaseHelper.TAB_USER_ID + "=" + id
                        + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")");
                break;
            default:
                throw new UnsupportedOperationException(uri + " Not yet implemented");
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseHelper.TAB_USER);
        Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case PROVIDER_USER:
                break;
            case PROVIDER_USER_ID:
                String id = uri.getPathSegments().get(1);
                selection = DatabaseHelper.TAB_USER_ID + "=" + id
                        + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")");
                break;
            default:
                throw new UnsupportedOperationException(uri + " Not yet implemented");
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int deleteCount = db.delete(DatabaseHelper.TAB_USER, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case PROVIDER_USER:
                break;
            default:
                throw new UnsupportedOperationException(uri + " Not yet implemented");
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long id = db.insert(DatabaseHelper.TAB_USER, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(PROVIDER_URI + "/" + id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case PROVIDER_USER:
                break;
            case PROVIDER_USER_ID:
                String id = uri.getPathSegments().get(1);
                selection = DatabaseHelper.TAB_USER_ID + "=" + id
                        + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")");
                break;
            default:
                throw new UnsupportedOperationException(uri + " Not yet implemented");
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int updateCount = db.update(DatabaseHelper.TAB_USER, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
