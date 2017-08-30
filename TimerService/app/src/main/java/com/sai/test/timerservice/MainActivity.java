package com.sai.test.timerservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sai.test.myremoteservice.*;

public class MainActivity extends AppCompatActivity {
    final int UI_MSG_UPDATE_BASE = 100;
    final int UI_MSG_UPDATE_TV1 = UI_MSG_UPDATE_BASE + 0;
    final int UI_MSG_UPDATE_TV2 = UI_MSG_UPDATE_BASE + 1;
    final int UI_MSG_UPDATE_PROG = UI_MSG_UPDATE_BASE + 2;
    final int UI_MSG_UPDATE_PROG2 = UI_MSG_UPDATE_BASE + 3;

    Button button;
    Button button2;
    Button button3;
    Button button4;
    TextView textview;
    TextView textview2;
    ProgressBar progressbar;
    ProgressBar progressbar2;

    Thread mLSThread;
    Thread mRSThread;
    Handler mHandler;

    boolean mLocalServiceConnected = false;
    boolean mRemoteServiceConnected = false;

    ICountServiceAIDL mLocalService = null;
    IMyRemoteService mRemoteService = null;

    IMyCallback mRemoteCallback = new IMyCallback.Stub() {
        @Override
        public void callback(int i) throws RemoteException {
            Log.e("MyApp", "RemoteCallback called " + i);
            Message obtain = Message.obtain(mHandler, UI_MSG_UPDATE_PROG2, i, 0);
            mHandler.sendMessage(obtain);
        }
    };

    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button) {
                if (!mLocalServiceConnected) {
                    Intent ls_intent = new Intent(MainActivity.this, CountService.class);
                    bindService(ls_intent, mLocalServiceConnection, BIND_AUTO_CREATE);
                }
            } else if (v.getId() == R.id.button2) {
                if (mLocalServiceConnected) {
                    mLocalServiceConnected = false;
                    unbindService(mLocalServiceConnection);
                }
            } else if (v.getId() == R.id.button3) {
                if (!mRemoteServiceConnected) {
                    Intent rs_intent = new Intent(MainActivity.this, MyRemoteService.class);
                    bindService(rs_intent, mRemoteServiceConnection, BIND_AUTO_CREATE);
                }
            } else if (v.getId() == R.id.button4) {
                if (mRemoteServiceConnected) {
                    try {
                        mRemoteService.unregisterCallback(mRemoteCallback);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    mRemoteServiceConnected = false;
                    unbindService(mRemoteServiceConnection);
                }
            }
        }
    };

    ServiceConnection mLocalServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("MyApp", "mLocalServiceConnection:onServiceConnected");
            mLocalService = ICountServiceAIDL.Stub.asInterface(service);
            if (mLocalService != null) {
                mLSThread.start();
                mLocalServiceConnected = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("MyApp", "mLocalServiceConnection:onServiceDisconnected");
            mLocalService = null;
        }
    };

    ServiceConnection mRemoteServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("MyApp", "mRemoteServiceConnection:onServiceConnected");
            mRemoteService = IMyRemoteService.Stub.asInterface(service);
            try {
                mRemoteService.registerCallback(mRemoteCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (mRemoteService != null) {
                mRSThread.start();
                mRemoteServiceConnected = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("MyApp", "mRemoteServiceConnection:onServiceDisconnected");
            mRemoteService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);

        button.setOnClickListener(mClickListener);
        button2.setOnClickListener(mClickListener);
        button3.setOnClickListener(mClickListener);
        button4.setOnClickListener(mClickListener);

        textview = (TextView) findViewById(R.id.textView);
        textview2 = (TextView) findViewById(R.id.textView2);
        textview.setText(String.valueOf(String.valueOf(0)));
        textview2.setText(String.valueOf(String.valueOf(0)));

        progressbar = (ProgressBar)findViewById(R.id.progressBar);
        progressbar.setMax(100);
        progressbar2 = (ProgressBar)findViewById(R.id.progressBar2);
        progressbar2.setMax(100);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int v = msg.arg1;
                switch(msg.what) {
                    case UI_MSG_UPDATE_TV1:
                        textview.setText(String.valueOf(v));
                        break;
                    case UI_MSG_UPDATE_TV2:
                        textview2.setText(String.valueOf(v));
                        break;
                    case UI_MSG_UPDATE_PROG:
                        progressbar.setProgress(v);
                        break;
                    case UI_MSG_UPDATE_PROG2:
                        progressbar2.setProgress(v);
                        break;
                }
            }
        };

        mLSThread = new LocalServiceThread();
        mRSThread = new RemoteServiceThread();
    }

    class LocalServiceThread extends Thread {
        int mCount = 0;

        @Override
        public void run() {
            Thread.currentThread().setName("LocalServiceThread");

            while (mLocalServiceConnected) {
                try {
                    Thread.sleep(500);
                    mCount = mLocalService.getCount();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message obtain = Message.obtain(mHandler, UI_MSG_UPDATE_TV1, mCount, 0);
                mHandler.sendMessage(obtain);

                obtain = Message.obtain(mHandler, UI_MSG_UPDATE_PROG, mCount, 0);
                mHandler.sendMessage(obtain);
            }
        }
    }

    class RemoteServiceThread extends Thread {
        int mCount = 0;

        @Override
        public void run() {
            Thread.currentThread().setName("RemoteServiceThread");

            while (mRemoteServiceConnected) {
                try {
                    Thread.sleep(500);
                    mCount = mRemoteService.getCount();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message obtain = Message.obtain(mHandler, UI_MSG_UPDATE_TV2, mCount, 0);
                mHandler.sendMessage(obtain);
            }
        }
    }
}
