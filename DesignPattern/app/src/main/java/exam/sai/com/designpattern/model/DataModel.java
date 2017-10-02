package exam.sai.com.designpattern.model;

import java.util.ArrayList;
import java.util.HashSet;

public class DataModel<T> implements IDataModel<T> {
    ArrayList<T> mData;
    HashSet<IDataModelObserver<T>> mObservers;

    public DataModel() {
        mData = new ArrayList<>();
        mObservers = new HashSet<>();
    }

    @Override
    public void set(ArrayList<T> dataList) {
        mData = dataList;
    }

    @Override
    public void add(T data) {
        mData.add(data);
        notifyChanged(IDataModelObserver.DM_OB_INSERT, mData.size() - 1, data);
    }

    @Override
    public void delete(T data) {
        int index = getIndexWithId(data);
        if (index != -1) {
            mData.remove(index);
            notifyChanged(IDataModelObserver.DM_OB_DELETE, index, data);
        }
    }

    @Override
    public void update(T data) {
        int index = getIndexWithId(data);
        if (index != -1) {
            mData.set(index, data);
            notifyChanged(IDataModelObserver.DM_OB_UPDATE, index, data);
        }
    }

    private int getIndexWithId(T data) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).equals(data)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public T getAt(int index) {
        return mData.get(index);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public void registerObserver(IDataModelObserver<T> observer) {
        mObservers.add(observer);
    }

    @Override
    public void unregisterObserver(IDataModelObserver<T> observer) {
        mObservers.remove(observer);
    }

    public void notifyChanged(int type, int index, T data) {
        for (IDataModelObserver ob : mObservers) {
            ob.onDataModelChanged(type, index, data);
        }
    }
}
