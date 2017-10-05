package exam.sai.com.designpattern.model;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import exam.sai.com.designpattern.event.DataModelEventMessage;
import exam.sai.com.designpattern.global.BaseConfig;

public class DataModel<T> implements IDataModel<T> {
    private List<T> mData;
    private HashSet<IDataModelObserver<T>> mObservers;

    public DataModel() {
        mData = new ArrayList<>();
        mObservers = new HashSet<>();
    }

    @Override
    public void set(List<T> dataList) {
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

    private void notifyChanged(int type, int position, T data) {
        if (BaseConfig.useEventbusForNotify() == false) {
            for (IDataModelObserver ob : mObservers) {
                if (ob != null) {
                    ob.onDataModelChanged(type, position, data);
                }
            }
        } else {
            EventBus.getDefault().post(new DataModelEventMessage<>(type, position, data));
        }
    }
}
