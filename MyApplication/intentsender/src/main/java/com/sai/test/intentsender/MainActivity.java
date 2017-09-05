package com.sai.test.intentsender;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSIONS_REQUEST = 100;
    private static final String INTENT_SAMPLE = "com.sai.test.testapp.SAMPLE_INTENT";
    private static final String RECEIVER_PERMISSION = "com.sai.test.testapp.RECEIVER_PERM";
    private static final String ACTIVITY_PERMISSION = "com.sai.test.testapp.ACTIVITY_PERM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("IntentSenderActivity", "(" + this + ") " + "onCreate();");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button_send_intent);
        button.setOnClickListener(this);

        button = (Button)findViewById(R.id.button_send_intent_with_permission);
        button.setOnClickListener(this);

        button = (Button)findViewById(R.id.button_launch_activity);
        button.setOnClickListener(this);

        button = (Button)findViewById(R.id.button_launch_matched);
        button.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(RECEIVER_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{RECEIVER_PERMISSION, ACTIVITY_PERMISSION}, PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            for (int i=0; i<grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("IntentSender", permissions[i] + " : Permission granted");
                } else {
                    Log.e("IntentSender", permissions[i] + " : Permission denied");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.button_send_intent:
                intent = new Intent(INTENT_SAMPLE);
                intent.putExtra("data", "Sample intent data");
                sendBroadcast(intent);
                break;
            case R.id.button_send_intent_with_permission:
                intent = new Intent(INTENT_SAMPLE);
                intent.putExtra("data", "Sample intent data with permission");
                sendBroadcast(intent, RECEIVER_PERMISSION);
                break;
            case R.id.button_launch_activity:
                intent = new Intent(INTENT_SAMPLE);
                intent.putExtra("data", "start activity using intent");
                startActivity(intent);
                break;
            case R.id.button_launch_matched:
                Resources res = getResources();
                Uri uri = new Uri.Builder()
                        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                        .authority(res.getResourcePackageName(R.drawable.profile))
                        .appendPath(res.getResourceTypeName(R.drawable.profile))
                        .appendPath(res.getResourceEntryName(R.drawable.profile))
                        .build();

                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(null, "image/png");
                intent.putExtra("data", "start activity using intent with mimetype");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
