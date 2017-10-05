package exam.sai.com.designpattern.event;

/**
 * Created by sai on 2017-10-02.
 */

public class MenuSelectedEventMessage {
    public int type;
    public int position;

    public MenuSelectedEventMessage(int type, int position) {
        this.type = type;
        this.position = position;
    }
}
