package com.sai.test.CustomMediaPlayer;

public interface IPlayerStatusCallback {
    public void onMediaChanged(int status, MediaInfo mi, int progress);
    public void onPlaybackProgress(int progress);
}
