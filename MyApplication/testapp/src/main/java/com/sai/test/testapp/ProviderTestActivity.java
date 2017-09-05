package com.sai.test.testapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProviderTestActivity extends AppCompatActivity {
    private final static String LOCAL_PROVIDER_AUTH = "com.sai.test.localtestprovider";
    private final static String REMOTE_PROVIDER_AUTH = "com.sai.test.remotetestprovider";

    private String mContentUri = "content://" + LOCAL_PROVIDER_AUTH + "/user";
    private View mCustomView;
    @BindView(R.id.editText_id) EditText mEditID;
    @BindView(R.id.editText_user_id) EditText mEditUserID;
    @BindView(R.id.editText_name) EditText mEditName;
    @BindView(R.id.editText_age) EditText mEditAge;
    @BindView(R.id.editText_location) EditText mEditLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_test);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.radio_local_provider, R.id.radio_remote_provider})
    void setProvider (View v) {
        switch (v.getId()) {
            case R.id.radio_local_provider:
                mContentUri = "content://" + LOCAL_PROVIDER_AUTH + "/user";
                break;
            case R.id.radio_remote_provider:
                mContentUri = "content://" + REMOTE_PROVIDER_AUTH + "/user";
                break;
        }
    }

    @OnClick({R.id.button_query, R.id.button_insert, R.id.button_update, R.id.button_delete})
    void handleButton (View v) {
        switch (v.getId()) {
            case R.id.button_query: {
                String strId = mEditID.getText().toString();
                int id = TextUtils.isEmpty(strId) ? -1 : Integer.valueOf(strId);
                query(id);
                break;
            }
            case R.id.button_insert: {
                ContentValues cv = new ContentValues();
                cv.put("userid", mEditUserID.getText().toString());
                cv.put("name", mEditName.getText().toString());
                cv.put("age", Integer.valueOf(mEditAge.getText().toString()));
                cv.put("location", mEditLocation.getText().toString());
                insert(cv);
                break;
            }
            case R.id.button_update: {
                String strId = mEditID.getText().toString();
                int id = TextUtils.isEmpty(strId) ? -1 : Integer.valueOf(strId);
                ContentValues cv = new ContentValues();
                cv.put("userid", mEditUserID.getText().toString());
                cv.put("name", mEditName.getText().toString());
                cv.put("age", Integer.valueOf(mEditAge.getText().toString()));
                cv.put("location", mEditLocation.getText().toString());
                update(id, cv);
                break;
            }
            case R.id.button_delete: {
                String strId = mEditID.getText().toString();
                int id = TextUtils.isEmpty(strId) ? -1 : Integer.valueOf(strId);
                delete(id);
                break;
            }
        }
    }

    void query(int id) {
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(Uri.parse(mContentUri+((id>=0)?"/"+id:"")), null, null, null, null);

        String msg = "";
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (true) {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    msg += c.getString(i) + ",";
                }

                msg += "\n";
                if (c.isLast()) {
                    break;
                } else {
                    c.moveToNext();
                }
            }
        } else {
            msg = "Nothing";
        }

        popupToast(msg);
    }

    void insert(ContentValues cv) {
        ContentResolver cr = getContentResolver();
        Uri uri = cr.insert(Uri.parse(mContentUri), cv);
        popupToast(uri.toString()+ " inserted.");
    }

    void update(int id, ContentValues cv) {
        ContentResolver cr = getContentResolver();
        int count = cr.update(Uri.parse(mContentUri+((id>=0)?"/"+id:"")), cv, null, null);
        popupToast(count + " items updated.");
    }

    void delete(int id) {
        ContentResolver cr = getContentResolver();
        int count = cr.delete(Uri.parse(mContentUri+((id>=0)?"/"+id:"")), null, null);
        popupToast(count + " items deleted.");
    }

    public void popupToast(CharSequence text) {
        if (mCustomView == null) {
            mCustomView = getLayoutInflater().inflate(R.layout.custom_popup_toast,
                    (ViewGroup) findViewById(R.id.custom_popup_toast_layout_id));
        }

        new CustomPopupToast(this, mCustomView, text);
    }
}
