package com.sai.test.timerservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class LocalService extends Service {
    Thread mThread;
    boolean isStop = false;
    int mCount = 0;

    IMyLocalService.Stub mBinder = new IMyLocalService.Stub() {
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

    public LocalService() {
        Log.e("MyLocalService", "Instantiate LocalService");

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
