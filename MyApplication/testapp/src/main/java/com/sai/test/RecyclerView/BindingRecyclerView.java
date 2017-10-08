package com.sai.test.RecyclerView;

import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.ImageView;

public class BindingRecyclerView {
    @BindingAdapter({"bind:albumArt"})
    public static void setImageAlbumArt(ImageView iv, long resource) {
        Cursor cursor = iv.getContext().getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[] {Long.toString(resource)},
                null
        );

        cursor.moveToFirst();
        String result = (cursor.getCount()>0)?cursor.getString(0):null;
        cursor.close();
        Drawable img = Drawable.createFromPath(result);
        iv.setImageDrawable(img);
    }
}
