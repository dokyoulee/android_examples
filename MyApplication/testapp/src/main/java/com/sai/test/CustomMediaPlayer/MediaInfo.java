package com.sai.test.CustomMediaPlayer;

import android.net.Uri;

public abstract class MediaInfo {
    public static final int MEDIAINFO_TYPE_AUDIO = 1;
    public static final int MEDIAINFO_TYPE_VIDEO = 2;

    public int type;
    public Uri uri;

    public MediaInfo(int type, Uri uri) {
        this.type = type; this.uri = uri;
    }
}