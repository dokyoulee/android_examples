package com.sai.test.CustomMediaPlayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomMediaPlayer {
    public final static int CMP_PLAYING = 1;
    public final static int CMP_STOPPED = 2;
    public final static int CMP_PAUSED = 3;
    public final static int CMP_UNKNOWN= 4;

    public final static int CMP_MSG_BROADCAST_PROGRESS = 100;

    private MediaPlayer mMediaPlayer = null;
    private MediaInfo mMediaInfo = null;
    private List<IPlayerStatusCallback> mCallback = null;
    private int mStatus = CMP_UNKNOWN;
    private Context mContext = null;
    private MusicInfoAsyncTask mPlayStatusThread = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case CMP_MSG_BROADCAST_PROGRESS:
                    Log.e("MusicPlayer", "CustomMediaPlayer::CMP_MSG_BROADCAST_PROGRESS");
                    notifyProgressChanged();
                    break;
            }
        }
    };

    public CustomMediaPlayer(Context context) {
        mContext = context;
        mMediaInfo = null;
        mMediaPlayer = new MediaPlayer();
        mCallback = new ArrayList<IPlayerStatusCallback>();
        mStatus = CMP_STOPPED;
    }

    public void registerCallback(IPlayerStatusCallback callback) {
        mCallback.add(callback);
    }

    public void unregisterCallback(IPlayerStatusCallback callback) {
        mCallback.remove(callback);
    }

    public void play() throws IOException {
        if (mMediaInfo != null) {
            play(mMediaInfo);
        }
    }

    public void play(MediaInfo mi) throws IOException {
        Log.e("MusicPlayer", "CustomMediaPlayer::play");
        new Throwable().printStackTrace();

        if (mStatus == CMP_PLAYING) {
            mMediaPlayer.reset();
        }

        if (mStatus != CMP_PAUSED) {
            mMediaInfo = mi;
            mMediaPlayer.setDataSource(mContext, mMediaInfo.uri);
            mMediaPlayer.prepare();
        }

        mMediaPlayer.start();
        mStatus = CMP_PLAYING;
        new MusicInfoAsyncTask().execute(mMediaPlayer, mHandler);
        notifyMediaChanged();
    }

    public void pause() {
        Log.e("MusicPlayer", "CustomMediaPlayer::pause");
        if (mStatus == CMP_PLAYING) {
            mMediaPlayer.pause();
        }

        notifyMediaChanged();
        mStatus = CMP_PAUSED;
    }

    public void stop() {
        Log.e("MusicPlayer", "CustomMediaPlayer::stop");
        if (mStatus == CMP_PLAYING || mStatus == CMP_PAUSED) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }

        notifyMediaChanged();
        mStatus = CMP_STOPPED;
    }

    public void seekTo(int duration) {
        Log.e("MusicPlayer", "CustomMediaPlayer::seekTo");
        if (duration < ((AudioInfo)mMediaInfo).duration && duration >= 0) {
            mMediaPlayer.seekTo(duration);
            notifyProgressChanged();
        }
    }

    public void notifyMediaChanged() {
        Log.e("MusicPlayer", "CustomMediaPlayer::notifyMediaChanged");
        for (IPlayerStatusCallback psc : mCallback) {
            psc.onMediaChanged(mStatus, mMediaInfo, mMediaPlayer.getCurrentPosition());
        }
    }

    public void notifyProgressChanged() {
        Log.e("MusicPlayer", "CustomMediaPlayer::notifyProgressChanged");
        for (IPlayerStatusCallback psc : mCallback) {
            psc.onPlaybackProgress(mMediaPlayer.getCurrentPosition());
        }
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public void clear() {
        if (mMediaPlayer != null) {
            if (mStatus == CMP_PLAYING) {
                stop();
            }
            mMediaPlayer.release();
        }
        mStatus = CMP_UNKNOWN;
    }
}
