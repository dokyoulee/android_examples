package exam.sai.com.designpattern.global;

/**
 * Created by sai on 2017-10-02.
 */

public class BaseConfig {
    private static final boolean USE_EVENTBUS_FOR_NOTIFY = true;

    public static boolean useEventbusForNotify() {
        return USE_EVENTBUS_FOR_NOTIFY;
    }
}
