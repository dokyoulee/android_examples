package exam.sai.com.designpattern.model;

/**
 * Created by sai on 2017-09-15.
 */

public class UserInfo {
    public int id;
    public String userName;
    public String userPhone;

    UserInfo(int id, String name, String phone) {
        this.id = id;
        this.userName = name;
        this.userPhone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserInfo)) {
            return false;
        }

        return this.id == ((UserInfo) obj).id;
    }
}
