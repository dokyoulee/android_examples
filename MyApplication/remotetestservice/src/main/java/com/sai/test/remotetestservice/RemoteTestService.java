package com.sai.test.remotetestservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.sai.test.testservice.ITestService;
import com.sai.test.testservice.ITestServiceCallback;

import java.util.HashSet;
import java.util.Set;

public class RemoteTestService extends Service {
    private Thread mThread;
    private boolean mStop = false;
    private int mCount = 0;

    private Set<ITestServiceCallback> mCallbackList = new HashSet<ITestServiceCallback>();

    private ITestService.Stub mService = new ITestService.Stub() {
        @Override
        public void registerCallback(ITestServiceCallback cb) throws RemoteException {
            Log.e("RemoteTestService", "callback registered " + cb);
            mCallbackList.add(cb);
        }

        @Override
        public void unregisterCallback(ITestServiceCallback cb) throws RemoteException {
            Log.e("RemoteTestService", "callback unregistered " + cb);
            mCallbackList.remove(cb);
        }

        @Override
        public int getCount() throws RemoteException {
            return mCount;
        }
    };

    public RemoteTestService() {
        Log.e("RemoteTestService", "Instantiate RemoteTestService");

        mThread = new Thread() {

            @Override
            public void run() {
                while (!mStop) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mCount++;
                    if (mCount > 100) {
                        mCount = 0;
                    }

                    for (ITestServiceCallback cb : mCallbackList) {
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
    public void onDestroy() {
        mStop = true;
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mThread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mService;
    }
}
