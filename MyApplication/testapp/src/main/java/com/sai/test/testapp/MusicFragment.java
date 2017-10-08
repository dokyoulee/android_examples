package com.sai.test.testapp;

import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.Observable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sai.test.CustomMediaPlayer.AudioInfo;
import com.sai.test.CustomMediaPlayer.CustomMediaPlayer;
import com.sai.test.CustomMediaPlayer.IPlayerStatusCallback;
import com.sai.test.CustomMediaPlayer.MediaInfo;
import com.sai.test.RecyclerView.IOnItemClickListener;
import com.sai.test.RecyclerView.RecyclerViewAdapter;
import com.sai.test.PlayInfoBinding.PlayInfo;
import com.sai.test.testapp.databinding.FragmentMusicBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        IOnItemClickListener, IPlayerStatusCallback {
    private static final int PERMISSIONS_REQUEST = 100;
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter<MediaInfo> mRecyclerViewAdapter;
    FragmentMusicBinding mFragMusicBinding;
    CustomMediaPlayer mMediaPlayer;
    PlayInfo mPlayInfo;
    Observable.OnPropertyChangedCallback mPlayInfoCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_music, container, false);
        requestPermission();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMediaPlayer.unregisterCallback(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragMusicBinding = FragmentMusicBinding.bind(getView());
        mFragMusicBinding.setVariable(BR.fragment, this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mFragMusicBinding.recyclerViewItemList.setLayoutManager(mLayoutManager);

        mMediaPlayer = new CustomMediaPlayer(getActivity());

        mPlayInfo = new PlayInfo(0, 0);
        mPlayInfoCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (Math.abs(mMediaPlayer.getProgress()-mPlayInfo.seekProgress.get()) > 100) {
                    mMediaPlayer.seekTo(mPlayInfo.seekProgress.get());
                }
            }
        };
        mPlayInfo.seekProgress.addOnPropertyChangedCallback(mPlayInfoCallback);
        mFragMusicBinding.setVariable(BR.playinfo, mPlayInfo);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getContext().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);
        } else {
            retrieveMediaData();
        }
    }

    void retrieveMediaData() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION};

        return new CursorLoader(
                getContext(),
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Audio.Media.IS_MUSIC + "<>0",
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<MediaInfo> aryData = new ArrayList<>();
        Uri uri;
        int id;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                AudioInfo ai = new AudioInfo(
                        id,
                        uri,
                        cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                        null,
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                );

                aryData.add(ai);
            } while (cursor.moveToNext());
            cursor.close();
        }

        int n = aryData.size();
        for (int i = 0; i < n * 2; i++) {
            aryData.add(aryData.get(i));
        }

        mRecyclerViewAdapter = new RecyclerViewAdapter<>(R.layout.music_item, BR.music_item, this);
        mFragMusicBinding.recyclerViewItemList.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setData(aryData);
        mMediaPlayer.registerCallback(this);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerViewAdapter.setData(null);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void buttonHandler(View view) {
        switch (view.getId()) {
            case R.id.button_play: {
                Log.e("MusicPlayer", "Play");
                try {
                    mMediaPlayer.play();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;
            case R.id.button_pause: {
                Log.e("MusicPlayer", "Pause");
                mMediaPlayer.pause();
            }
            break;
            case R.id.button_stop: {
                Log.e("MusicPlayer", "Stop");
                mMediaPlayer.stop();
            }
            break;
        }
    }

    @Override
    public void OnItemClickListerner(int position) {
        try {
            mMediaPlayer.play(mRecyclerViewAdapter.getItem(position));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMediaChanged(int status, MediaInfo mi, int progress) {
        Log.e("MusicPlayer", "onMediaChanged");
        mFragMusicBinding.textViewMusician.setText(((AudioInfo) mi).albumTitle);
        mPlayInfo.seekMax.set(mMediaPlayer.getDuration());
        if (status == CustomMediaPlayer.CMP_STOPPED) {
            onPlaybackProgress(0);
        } else {
            onPlaybackProgress(progress);
        }
    }

    @Override
    public void onPlaybackProgress(int progress) {
        Log.e("MusicPlayer", "onPlaybackProgress");
        mPlayInfo.seekProgress.removeOnPropertyChangedCallback(mPlayInfoCallback);
        mPlayInfo.seekProgress.set(progress);
        mPlayInfo.setElapsedTime("");
        mPlayInfo.seekProgress.addOnPropertyChangedCallback(mPlayInfoCallback);
    }
}
