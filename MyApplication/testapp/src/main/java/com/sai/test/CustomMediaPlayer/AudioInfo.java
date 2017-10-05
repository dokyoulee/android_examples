package com.sai.test.CustomMediaPlayer;

import android.net.Uri;

public class AudioInfo extends MediaInfo {
    public int id;
    public int albumId;
    public String albumTitle;
    public String albumArt;
    public String title;
    public String artist;
    public long duration;

    public AudioInfo(int id, Uri uri, int albumId, String albumTitle, String albumArt,
                     String title, String artist, long duration) {
        super(MediaInfo.MEDIAINFO_TYPE_AUDIO, uri);
        this.id = id;
        this.albumTitle = albumTitle;
        this.albumId = albumId;
        this.albumArt = albumArt;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }
}
