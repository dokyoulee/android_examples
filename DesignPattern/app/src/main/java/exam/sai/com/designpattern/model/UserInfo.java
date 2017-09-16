package exam.sai.com.designpattern.model;

/**
 * Created by sai on 2017-09-15.
 */

public class UserInfo {
    private int mId;
    private String mUserName;
    private String mUserPhone;

    public UserInfo(int id, String name, String phone) {
        mId = id;
        mUserName = name;
        mUserPhone = phone;
    }

    public int getUserId() {
        return mId;
    }

    public void setUserId(int id) {
        mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String name) {
        mUserName = name;
    }

    public String getUserPhone() {
        return mUserPhone;
    }

    public void setUserPhone(String phone) {
        mUserPhone = phone;
    }


}
