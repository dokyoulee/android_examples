package exam.sai.com.designpattern.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {
    private static final int PERMISSIONS_REQUEST = 100;
    private Activity mActivity;
    private IPermissionCallback mCallback;

    public PermissionUtils(@NonNull Activity activity, IPermissionCallback callback) {
        mActivity = activity;
        mCallback = callback;
    }

    private void requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, new String[]{permission}, PERMISSIONS_REQUEST);
        }
    }

    public void onRequestPermissionResult(@NonNull String[] permissions, @NonNull int[] grantResults) {

    }

}
