package exam.sai.com.designpattern.model;

public interface IDataModelAdapter {
    int LOADTYPE_SYNC = 1;
    int LOADTYPE_ASYNC = 2;

    void loadDataFromContentProvider(int type);

    void cleanUp();
}
