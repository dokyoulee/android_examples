package com.sai.test.CustomMediaPlayer;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MusicInfoAsyncTask extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        MediaPlayer mp = (MediaPlayer)params[0];
        Handler h = (Handler)params[1];

        while (mp.isPlaying()) {
            try {
                Thread.sleep(500);
                h.sendMessage(Message.obtain(h, CustomMediaPlayer.CMP_MSG_BROADCAST_PROGRESS, null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
