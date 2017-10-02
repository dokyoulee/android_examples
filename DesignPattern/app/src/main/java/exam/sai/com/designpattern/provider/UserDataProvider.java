package exam.sai.com.designpattern.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by sai on 2017-09-15.
 */

public class UserDataProvider extends ContentProvider {
    public static final String[] ColumnName = {
            UserDatabaseHelper.TABLE_USER_ID,
            UserDatabaseHelper.TABLE_USER_NAME,
            UserDatabaseHelper.TABLE_USER_PHONE};
    private static final String USER_PROVIDER_AUTH = "exam.sai.com.userinfoprovider";
    public static final Uri USER_PROVIDER_URI = Uri.parse("content://" + USER_PROVIDER_AUTH + "/user");
    private static final int USER_PROVIDER_URI_MATCHER_USER = 1;
    private static final int USER_PROVIDER_URI_MATCHER_USER_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(USER_PROVIDER_AUTH, "user", USER_PROVIDER_URI_MATCHER_USER);
        sUriMatcher.addURI(USER_PROVIDER_AUTH, "user/#", USER_PROVIDER_URI_MATCHER_USER_ID);
    }

    UserDatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new UserDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case USER_PROVIDER_URI_MATCHER_USER:
                break;
            case USER_PROVIDER_URI_MATCHER_USER_ID:
                String id = uri.getPathSegments().get(1);
                selection = UserDatabaseHelper.TABLE_USER_ID + "=" + id
                        + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")");
                break;
            default:
                throw new UnsupportedOperationException(uri.toString());
        }

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UserDatabaseHelper.TABLE_NAME);
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case USER_PROVIDER_URI_MATCHER_USER:
                break;
            default:
                throw new UnsupportedOperationException(uri.toString());
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long id = db.insert(UserDatabaseHelper.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(USER_PROVIDER_URI + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case USER_PROVIDER_URI_MATCHER_USER:
                break;
            case USER_PROVIDER_URI_MATCHER_USER_ID:
                String id = uri.getPathSegments().get(1);
                selection = UserDatabaseHelper.TABLE_USER_ID + "=" + id
                        + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")");
                break;
            default:
                throw new UnsupportedOperationException(uri.toString());
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int count = db.delete(UserDatabaseHelper.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case USER_PROVIDER_URI_MATCHER_USER:
                break;
            case USER_PROVIDER_URI_MATCHER_USER_ID:
                String id = uri.getPathSegments().get(1);
                selection = UserDatabaseHelper.TABLE_USER_ID + "=" + id
                        + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")");
                break;
            default:
                throw new UnsupportedOperationException(uri.toString());
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int count = db.update(UserDatabaseHelper.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
