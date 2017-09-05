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
    static final int UI_MSG_UPDATE_BASE = 100;
    static final int UI_MSG_UPDATE_TV1 = UI_MSG_UPDATE_BASE + 0;
    static final int UI_MSG_UPDATE_TV2 = UI_MSG_UPDATE_BASE + 1;
    static final int UI_MSG_UPDATE_PROG = UI_MSG_UPDATE_BASE + 2;
    static final int UI_MSG_UPDATE_PROG2 = UI_MSG_UPDATE_BASE + 3;

    TextView mTextViewLocal;
    TextView mTextviewRemote;
    ProgressBar mProgressbarProgress;
    ProgressBar mProgressbarSpin;

    Thread mLocalServiceThread;
    Thread mRemoteServiceThread;
    Handler mHandler;

    boolean mLocalServiceConnected = false;
    boolean mRemoteServiceConnected = false;

    IMyLocalService mLocalService = null;
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
            if (v.getId() == R.id.button_local_start) {
                if (!mLocalServiceConnected) {
                    Intent ls_intent = new Intent(MainActivity.this, LocalService.class);
                    bindService(ls_intent, mLocalServiceConnection, BIND_AUTO_CREATE);
                }
            } else if (v.getId() == R.id.button_local_stop) {
                if (mLocalServiceConnected) {
                    mLocalServiceConnected = false;
                    unbindService(mLocalServiceConnection);
                }
            } else if (v.getId() == R.id.button_remote_start) {
                if (!mRemoteServiceConnected) {
                    Intent rs_intent = new Intent(MainActivity.this, MyRemoteService.class);
                    bindService(rs_intent, mRemoteServiceConnection, BIND_AUTO_CREATE);
                }
            } else if (v.getId() == R.id.button_remote_stop) {
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
            mLocalService = IMyLocalService.Stub.asInterface(service);
            if (mLocalService != null) {
                mLocalServiceThread.start();
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
                mRemoteServiceThread.start();
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

        Button button;
        button = (Button)findViewById(R.id.button_local_start);
        button.setOnClickListener(mClickListener);

        button = (Button)findViewById(R.id.button_local_stop);
        button.setOnClickListener(mClickListener);

        button = (Button)findViewById(R.id.button_remote_start);
        button.setOnClickListener(mClickListener);

        button = (Button)findViewById(R.id.button_remote_stop);
        button.setOnClickListener(mClickListener);

        mTextViewLocal = (TextView) findViewById(R.id.textview_local);
        mTextViewLocal.setText(String.valueOf(String.valueOf(0)));

        mTextviewRemote = (TextView) findViewById(R.id.textview_remote);
        mTextviewRemote.setText(String.valueOf(String.valueOf(0)));

        mProgressbarProgress = (ProgressBar)findViewById(R.id.progressBar_progress);
        mProgressbarProgress.setMax(100);
        mProgressbarSpin = (ProgressBar)findViewById(R.id.progressBar_spin);
        mProgressbarSpin.setMax(100);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int value = msg.arg1;

                switch(msg.what) {
                    case UI_MSG_UPDATE_TV1:
                        mTextViewLocal.setText(String.valueOf(value));
                        break;
                    case UI_MSG_UPDATE_TV2:
                        mTextviewRemote.setText(String.valueOf(value));
                        break;
                    case UI_MSG_UPDATE_PROG:
                        mProgressbarProgress.setProgress(value);
                        break;
                    case UI_MSG_UPDATE_PROG2:
                        mProgressbarSpin.setProgress(value);
                        break;
                }
            }
        };

        mLocalServiceThread = new LocalServiceThread();
        mRemoteServiceThread = new RemoteServiceThread();
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
