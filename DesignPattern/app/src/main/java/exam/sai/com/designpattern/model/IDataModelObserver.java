package exam.sai.com.designpattern.model;

public interface IDataModelObserver<T> {
    public static final int DM_OB_INSERT = 1;
    public static final int DM_OB_UPDATE = 2;
    public static final int DM_OB_DELETE = 3;

    public void onDataModelChanged(int type, int index, T data);
}
