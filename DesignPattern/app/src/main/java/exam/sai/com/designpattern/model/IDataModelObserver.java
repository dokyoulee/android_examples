package exam.sai.com.designpattern.model;

public interface IDataModelObserver<T> {
    int DM_OB_INSERT = 1;
    int DM_OB_UPDATE = 2;
    int DM_OB_DELETE = 3;

    void onDataModelChanged(int type, int index, T data);
}
