package exam.sai.com.designpattern.utils;

import android.support.annotation.NonNull;

public interface IPermissionCallback {
    public void onRequestPermissionResult(@NonNull String[] permissions, @NonNull int[] grantResults);
}
