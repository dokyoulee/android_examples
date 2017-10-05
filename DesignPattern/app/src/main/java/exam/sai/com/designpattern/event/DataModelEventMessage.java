package exam.sai.com.designpattern.global;

import exam.sai.com.designpattern.model.UserInfo;

/**
 * Created by sai on 2017-10-02.
 */

public class DataModelEventMessage<T> {
    public int type;
    public int position;
    public T userInfo;

    public DataModelEventMessage(int type, int position, T userInfo) {
        this.type = type;
        this.position = position;
        this.userInfo = userInfo;
    }
}
