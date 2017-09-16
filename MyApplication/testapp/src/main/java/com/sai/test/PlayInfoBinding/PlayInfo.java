package com.sai.test.PlayInfoBinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.util.Log;

import com.sai.test.testapp.BR;

public class PlayInfo extends BaseObservable {
    public final ObservableField<Integer> seekProgress = new ObservableField<>();
    public final ObservableField<Integer> seekMax = new ObservableField<>();

    public PlayInfo(int max, int progress) {
        seekProgress.set(progress);
        seekMax.set(max);
    }

    @Bindable
    public String getElapsedTime() {
        Log.e("MusicPlayer", "getElapsedTime");
        String str = getTimeFromDuration(seekProgress.get())
                + " / " + getTimeFromDuration(seekMax.get());

        return str;
    }

    public void setElapsedTime(String time) {
        Log.e("MusicPlayer", "setElapsedTime");
        notifyPropertyChanged(BR.elapsedTime);
    }

    private String getTimeFromDuration(int duration) {
        duration /= 1000;
        int min = duration / 60;
        duration -= min * 60;
        return String.valueOf(min) + "'" + String.valueOf(duration) + "\"";
    }
}
