package com.sai.test.testapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class IntentTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST = 100;
    private static final String INTENT_SAMPLE = "com.sai.test.testapp.SAMPLE_INTENT";
    private static final String RECEIVER_PERMISSION = "com.sai.test.testapp.RECEIVER_PERM";

    BroadcastReceiver mIntentReceiver = null;
    View mCustomView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("IntentTestActivity", "(" + this + ") " + "onCreate();");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_test);

        Button button = (Button) findViewById(R.id.button_send_intent);
        button.setOnClickListener(this);
        button.setEnabled(false);

        button = (Button) findViewById(R.id.button_send_intent_permission);
        button.setOnClickListener(this);
        button.setEnabled(false);

        requestPermission();

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            CharSequence cs = intent.getExtras().getCharSequence("data");
            if (cs != null) {
                popupToast(cs);
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(RECEIVER_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{RECEIVER_PERMISSION}, PERMISSIONS_REQUEST);
        } else {
            registerDynReceiver();
            findViewById(R.id.button_send_intent).setEnabled(true);
            findViewById(R.id.button_send_intent_permission).setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerDynReceiver();
                findViewById(R.id.button_send_intent).setEnabled(true);
                findViewById(R.id.button_send_intent_permission).setEnabled(true);
            } else {
                popupToast("Permission not granted");
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e("IntentTestActivity", "(" + this + ") " + "onNewIntent()");
        super.onNewIntent(intent);
        this.setIntent(intent);

        if (intent != null && intent.getExtras() != null) {
            CharSequence cs = intent.getExtras().getCharSequence("data");
            if (cs != null) {
                popupToast(cs);
            }
        }

        requestPermission();
    }

    public void popupToast(CharSequence text) {
        if (mCustomView == null) {
            mCustomView = getLayoutInflater().inflate(R.layout.custom_popup_toast,
                    (ViewGroup) findViewById(R.id.custom_popup_toast_layout_id));
        }

        new CustomPopupToast(this, mCustomView, text);
    }

    private void registerDynReceiver() {
        if (mIntentReceiver == null) {
            IntentFilter ii = new IntentFilter();
            ii.addAction(INTENT_SAMPLE);

            mIntentReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (context.checkCallingPermission(RECEIVER_PERMISSION) == PackageManager.PERMISSION_DENIED) {
                        Log.e("DynamicReceiver", "Caller doesn't have proper permission(Calling)");
                    }

                    if (context.checkSelfPermission(RECEIVER_PERMISSION) == PackageManager.PERMISSION_DENIED) {
                        Log.e("DynamicReceiver", "Caller doesn't have proper permission(Self)");
                    }

                    popupToast("DynamicReceiver : " + intent.getExtras().getCharSequence("data", ""));
                    Log.e("DynamicReceiver", intent.getAction() + ", " + intent.getExtras().getCharSequence("data", ""));
                }
            };

            registerReceiver(mIntentReceiver, ii, RECEIVER_PERMISSION, null);
            //registerReceiver(mIntentReceiver, ii);
        }
    }

    private void unregisterDynReceiver() {
        if (mIntentReceiver != null) {
            unregisterReceiver(mIntentReceiver);
            mIntentReceiver = null;
        }
    }

    @Override
    protected void onResume() {
        Log.e("IntentTestActivity", "(" + this + ") " + "onResume()");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.e("IntentTestActivity", "(" + this + ") " + "onDestroy()");
        super.onDestroy();
        unregisterDynReceiver();
    }

    @Override
    protected void onPause() {
        Log.e("IntentTestActivity", "(" + this + ") " + "onPause()");
        super.onPause();
    }

    private void sendIntent() {
        Intent intent = new Intent(INTENT_SAMPLE);
        this.sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send_intent:
                Intent intent = new Intent(INTENT_SAMPLE);
                intent.putExtra("data", "Sample intent data");
                sendBroadcast(intent);
                break;
            case R.id.button_send_intent_permission:
                intent = new Intent(INTENT_SAMPLE);
                intent.putExtra("data", "Sample intent data with Permission");
                sendBroadcast(intent, RECEIVER_PERMISSION);
                break;
            default:
                break;
        }
    }
}
