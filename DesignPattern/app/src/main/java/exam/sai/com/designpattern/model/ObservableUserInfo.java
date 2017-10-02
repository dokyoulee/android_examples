package exam.sai.com.designpattern.model;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

/**
 * Created by sai on 2017-10-01.
 */

public class ObservableUserInfo extends BaseObservable {
    public final ObservableField<Integer> id = new ObservableField<>();
    public final ObservableField<String> userName = new ObservableField<>();
    public final ObservableField<String> userPhone = new ObservableField<>();

    public UserInfo getUserInfo() {
        return new UserInfo(id.get(), userName.get(), userPhone.get());
    }

    public void setUserInfo(UserInfo ui) {
        id.set(ui.id);
        userName.set(ui.userName);
        userPhone.set(ui.userPhone);
    }
}
