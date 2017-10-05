package exam.sai.com.designpattern.event;

/**
 * Created by sai on 2017-10-02.
 */

public class DataModelEventMessage<T> {
    public int type;
    public int position;
    public T data;

    public DataModelEventMessage(int type, int position, T data) {
        this.type = type;
        this.position = position;
        this.data = data;
    }
}
