package exam.sai.com.designpattern.config;

/**
 * Created by sai on 2017-10-02.
 */

public class BaseConfig {
    private static final int USE_EVENTBUS_FOR_NOTIFY = 1;

    public static int getUseEventbusForNotify() {
        return USE_EVENTBUS_FOR_NOTIFY;
    }
}
