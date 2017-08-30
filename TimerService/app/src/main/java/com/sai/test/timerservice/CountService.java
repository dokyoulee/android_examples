package com.sai.test.timerservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class CountService extends Service {
    Thread mThread;
    boolean isStop = false;
    int mCount = 0;

    ICountServiceAIDL.Stub mBinder = new ICountServiceAIDL.Stub() {
        @Override
        public int getCount() throws RemoteException {
            return mCount;
        }
    };

    @Override
    public void onCreate() {
        Log.e("MyLocalService", "onCreate");
        super.onCreate();
        mThread.start();
    }

    public CountService() {
        Log.e("MyLocalService", "Instantiate CountService");

        mThread = new Thread() {
            Handler handler = new Handler();

            @Override
            public void run() {
                while (!isStop) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mCount++;
                    if (mCount > 100) {
                        mCount = 0;
                    }
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        Log.e("MyLocalService", "onDestroy");
        super.onDestroy();
        isStop = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyLocalService", "onBind");
        return mBinder;
    }
}
