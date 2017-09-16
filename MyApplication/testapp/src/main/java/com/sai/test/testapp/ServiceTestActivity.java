package com.sai.test.testapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sai.test.testservice.ITestService;
import com.sai.test.testservice.ITestServiceCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceTestActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 101;
    private static final String TEST_SERVICE_PERMISSION = "com.sai.test.testservice.SERVICE_PERM";
    private static final String LOCAL_SERVICE_PKG = "com.sai.test.testapp";
    private static final String LOCAL_SERVICE_CLS = "com.sai.test.localtestservice.LocalTestService";
    private static final String REMOTE_SERVICE_PKG = "com.sai.test.remotetestservice";
    private static final String REMOTE_SERVICE_CLS = "com.sai.test.remotetestservice.RemoteTestService";
    private static final int MSG_BASE = 100;
    private static final int MSG_UPDATE_LOCAL = MSG_BASE + 1;
    private static final int MSG_UPDATE_REMOTE = MSG_BASE + 2;

    View mCustomView;
    boolean mLocalServiceRunning = false;
    boolean mRemoteServiceRunning = false;
    ITestService mLocalService = null;
    ITestService mRemoteService = null;

    @BindView(R.id.button_local_start)
    Button mButtonLocalStart;
    @BindView(R.id.button_local_end)
    Button mButtonLocalEnd;
    @BindView(R.id.button_remote_start)
    Button mButtonRemoteStart;
    @BindView(R.id.button_remote_end)
    Button mButtonRemoteEnd;
    @BindView(R.id.tv_local_data)
    TextView mTextLocalData;
    @BindView(R.id.tv_remote_data)
    TextView mTextRemoteData;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int message = msg.what;

            switch (message) {
                case MSG_UPDATE_LOCAL:
                    mTextLocalData.setText(msg.arg1 + "(" + System.currentTimeMillis() + ")");
                    break;
                case MSG_UPDATE_REMOTE:
                    mTextRemoteData.setText(msg.arg1 + "(" + System.currentTimeMillis() + ")");
                    break;
            }
        }
    };

    ITestServiceCallback mLocalServiceCallback = new ITestServiceCallback.Stub() {
        @Override
        public void callback(int i) throws RemoteException {
            Message msg = mHandler.obtainMessage(MSG_UPDATE_REMOTE, i, 0);
            mHandler.sendMessage(msg);
        }
    };

    ITestServiceCallback mRemoteServiceCallback = new ITestServiceCallback.Stub() {
        @Override
        public void callback(int i) throws RemoteException {
            Message msg = mHandler.obtainMessage(MSG_UPDATE_LOCAL, i, 0);
            mHandler.sendMessage(msg);
        }
    };

    ServiceConnection mLocalServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("ServiceConnection", "onServiceConnected : " + name.getPackageName());
            if (name.getPackageName().equals(LOCAL_SERVICE_PKG)
                    && name.getClassName().equals(LOCAL_SERVICE_CLS)) {
                mLocalServiceRunning = true;
                mLocalService = ITestService.Stub.asInterface(service);
                try {
                    mLocalService.registerCallback(mLocalServiceCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                popupToast(LOCAL_SERVICE_PKG + " service started");
                mButtonLocalEnd.setEnabled(true);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("ServiceConnection", "onServiceDisconnected : " + name.getPackageName());
            if (name.getClassName().equals(LOCAL_SERVICE_PKG)
                    && name.getClassName().equals(LOCAL_SERVICE_CLS)) {
                try {
                    mLocalService.unregisterCallback(mLocalServiceCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mLocalServiceRunning = false;
                mLocalService = null;
                popupToast(LOCAL_SERVICE_PKG + " service stopped");
            }
        }
    };

    ServiceConnection mRemoteServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("ServiceConnection", "onServiceConnected : " + name.getPackageName());
            if (name.getPackageName().equals(REMOTE_SERVICE_PKG)
                    && name.getClassName().equals(REMOTE_SERVICE_CLS)) {
                mRemoteServiceRunning = true;
                mRemoteService = ITestService.Stub.asInterface(service);
                try {
                    mRemoteService.registerCallback(mRemoteServiceCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                popupToast(REMOTE_SERVICE_PKG + " service started");
                mButtonRemoteEnd.setEnabled(true);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("ServiceConnection", "onServiceDisconnected : " + name.getPackageName());
            if (name.getClassName().equals(REMOTE_SERVICE_PKG)
                    && name.getClassName().equals(REMOTE_SERVICE_CLS)) {
                try {
                    mRemoteService.unregisterCallback(mRemoteServiceCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mRemoteServiceRunning = false;
                mRemoteService = null;
                popupToast(REMOTE_SERVICE_PKG + " service stopped");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);
        ButterKnife.bind(this);

        mButtonLocalEnd.setEnabled(false);
        mButtonRemoteEnd.setEnabled(false);

        requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(TEST_SERVICE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{TEST_SERVICE_PERMISSION}, PERMISSIONS_REQUEST);
        } else {
            mButtonLocalStart.setEnabled(true);
            mButtonRemoteStart.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mButtonLocalStart.setEnabled(true);
                mButtonRemoteStart.setEnabled(true);
            } else {
                popupToast("Permission not granted");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mButtonLocalStart.setEnabled(true);
        mButtonLocalEnd.setEnabled(false);
        mButtonRemoteStart.setEnabled(true);
        mButtonRemoteEnd.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocalServiceRunning == true) {
            stopTestService(mLocalServiceConnection);
        }

        if (mRemoteServiceRunning == true) {
            stopTestService(mRemoteServiceConnection);
        }
    }

    @OnClick({R.id.button_local_start,
            R.id.button_local_end,
            R.id.button_remote_start,
            R.id.button_remote_end})
    void handleButtonClick(View v) {
        switch (v.getId()) {
            case R.id.button_local_start:
                startTestService(LOCAL_SERVICE_PKG,
                        LOCAL_SERVICE_CLS,
                        mLocalServiceConnection);
                mButtonLocalStart.setEnabled(false);
                mButtonLocalEnd.setEnabled(false);
                break;
            case R.id.button_local_end:
                stopTestService(mLocalServiceConnection);
                mLocalServiceRunning = false;
                mButtonLocalStart.setEnabled(true);
                mButtonLocalEnd.setEnabled(false);
                break;
            case R.id.button_remote_start:
                startTestService(REMOTE_SERVICE_PKG,
                        REMOTE_SERVICE_CLS,
                        mRemoteServiceConnection);
                mButtonRemoteStart.setEnabled(false);
                mButtonRemoteEnd.setEnabled(false);
                break;
            case R.id.button_remote_end:
                stopTestService(mRemoteServiceConnection);
                mRemoteServiceRunning = false;
                mButtonRemoteStart.setEnabled(true);
                mButtonRemoteEnd.setEnabled(false);
                break;
        }
    }

    void startTestService(String packageName, String className, ServiceConnection sc) {
        Intent serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName(packageName, className));
        bindService(serviceIntent, sc, BIND_AUTO_CREATE);
    }

    void stopTestService(ServiceConnection sc) {
        unbindService(sc);
    }

    void popupToast(CharSequence text) {
        if (mCustomView == null) {
            mCustomView = getLayoutInflater().inflate(R.layout.custom_popup_toast,
                    (ViewGroup) findViewById(R.id.custom_popup_toast_layout_id));
        }

        new CustomPopupToast(this, mCustomView, text);
    }
}
