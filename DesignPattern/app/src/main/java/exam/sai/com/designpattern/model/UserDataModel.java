package exam.sai.com.designpattern.model;

import java.util.ArrayList;
import java.util.HashSet;

public class UserDataModel implements IDataModel<UserInfo> {
    ArrayList<UserInfo> mUserData;
    HashSet<IDataModelObserver<UserInfo>> mObservers;

    public UserDataModel() {
        mUserData = new ArrayList<>();
        mObservers = new HashSet<>();
    }

    @Override
    public void add(UserInfo data) {
        mUserData.add(data);
        notifyChanged(IDataModelObserver.DM_OB_INSERT, 0, data);
    }

    @Override
    public void delete(int index) {
        mUserData.remove(index);
        notifyChanged(IDataModelObserver.DM_OB_DELETE, index, null);
    }

    @Override
    public void update(int index, UserInfo data) {
        mUserData.set(index, data);
        notifyChanged(IDataModelObserver.DM_OB_DELETE, index, data);
    }

    @Override
    public UserInfo getAt(int index) {
        return mUserData.get(index);
    }

    @Override
    public void registerObserver(IDataModelObserver<UserInfo> observer) {
        mObservers.add(observer);
    }

    @Override
    public void unregisterObserver(IDataModelObserver<UserInfo> observer) {
        mObservers.remove(observer);
    }

    public void notifyChanged(int type, int index, UserInfo data) {
        for (IDataModelObserver ob : mObservers) {
            ob.onDataModelChanged(type, index, data);
        }
    }
}
