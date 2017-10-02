package exam.sai.com.designpattern.model;

/**
 * Created by sai on 2017-10-02.
 */

public abstract class DataModelAdapter {
    public static final int LOADTYPE_SYNC = 1;
    public static final int LOADTYPE_ASYNC = 2;

    abstract void loadDataFromContentProvider(int type);

    abstract void cleanUp();
}
