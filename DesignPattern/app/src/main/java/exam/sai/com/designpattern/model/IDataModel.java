package exam.sai.com.designpattern.model;

import java.util.ArrayList;

public interface IDataModel<T> {
    void set(ArrayList<T> dataList);

    void add(T data);

    T getAt(int index);

    void delete(T data);

    void update(T data);

    int getCount();

    void registerObserver(IDataModelObserver<T> observer);

    void unregisterObserver(IDataModelObserver<T> observer);
}
