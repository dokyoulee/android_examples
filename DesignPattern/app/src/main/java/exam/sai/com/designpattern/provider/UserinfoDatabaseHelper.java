package exam.sai.com.designpattern.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sai on 2017-09-15.
 */

public class UserinfoDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userinfo";

    static final String TABLE_NAME = "user";
    static final String TABLE_USER_ID = "_id";
    static final String TABLE_USER_NAME = "name";
    static final String TABLE_USER_PHONE = "phone";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + TABLE_USER_ID + " integer primary key autoincrement,"
            + TABLE_USER_NAME + " varchar(20) not null,"
            + TABLE_USER_PHONE + " varchar(10) not null)";

    public UserinfoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
