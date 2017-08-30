package com.sai.test.myremoteservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.sai.test.myremoteservice.IMyCallback;
import com.sai.test.myremoteservice.IMyRemoteService;

import java.util.HashSet;
import java.util.Set;

public class MyRemoteService extends Service {
    Thread mThread;
    boolean isStop = false;
    int mCount = 0;
    Set<IMyCallback> mCallbackList = new HashSet<IMyCallback>();

    IMyRemoteService.Stub mService = new IMyRemoteService.Stub() {
        @Override
        public void registerCallback(IMyCallback iMyCallback) throws RemoteException {
            Log.e("MyRemoteService", "callback registered " + iMyCallback);
            mCallbackList.add(iMyCallback);
        }

        @Override
        public void unregisterCallback(IMyCallback iMyCallback) throws RemoteException {
            Log.e("MyRemoteService", "callback unregistered " + iMyCallback);
            mCallbackList.remove(iMyCallback);
        }

        @Override
        public int getCount() throws RemoteException {
            return mCount;
        }
    };

    public MyRemoteService() {
        Log.e("MyRemoteService", "Instantiate MyRemoteService");

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

                    for (IMyCallback cb : mCallbackList) {
                        try {
                            cb.callback(mCount);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }

    @Override
    public void onCreate() {
        Log.e("MyRemoteService", "onCreate");
        super.onCreate();
        mThread.start();
    }

    @Override
    public void onDestroy() {
        Log.e("MyRemoteService", "onDestroy");
        isStop = true;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyRemoteService", "onBind");
        return mService;
    }
}
