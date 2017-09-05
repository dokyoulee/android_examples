package com.sai.test.testapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class SampleReceiver extends BroadcastReceiver {
    private static final String INTENT_SAMPLE = "com.sai.test.testapp.SAMPLE_INTENT";
    private static final String RECEIVER_PERMISSION = "com.sai.test.testapp.RECEIVER_PERM";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context.checkCallingPermission(RECEIVER_PERMISSION) == PackageManager.PERMISSION_DENIED) {
            Log.e("SampleReceiver", "Caller doesn't have proper permission(Calling)");
        }

        if (context.checkSelfPermission(RECEIVER_PERMISSION) == PackageManager.PERMISSION_DENIED) {
            Log.e("SampleReceiver", "Caller doesn't have proper permission(Self)");
        }

        Log.e("SampleReceiver", intent.getAction() + ", " + intent.getExtras().getCharSequence("data", ""));
    }
}
