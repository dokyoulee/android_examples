package exam.sai.com.designpattern.model;

public interface IDataModel <T> {
    public void add(T data);
    public T getAt(int index);
    public void delete(int index);
    public void update(int index, T data);

    public void registerObserver(IDataModelObserver<T> observer);
    public void unregisterObserver(IDataModelObserver<T> observer);
}
