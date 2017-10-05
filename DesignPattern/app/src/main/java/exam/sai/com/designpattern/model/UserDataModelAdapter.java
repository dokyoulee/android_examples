package exam.sai.com.designpattern.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import exam.sai.com.designpattern.provider.UserDataProvider;

/**
 * This mediator class is used for loosening the cohesion between DataModel and Provider
 * that is tightly coupled to Android context and Provider
 * TODO: need to be refactored much clearer
 */

public class UserDataModelAdapter extends DataModelAdapter implements IDataModelObserver<UserInfo> {
    private IDataModel<UserInfo> mDataModel;
    private ContentResolver mContentResolver;

    public UserDataModelAdapter(ContentResolver cr, IDataModel<UserInfo> dataModel) {
        mContentResolver = cr;
        mDataModel = dataModel;
        dataModel.registerObserver(this);
    }

    @Override
    public void loadDataFromContentProvider(int type) {
        if (type == DataModelAdapter.LOADTYPE_SYNC) {
            Cursor cursor = mContentResolver.query(UserDataProvider.USER_PROVIDER_URI, null, null, null, null);

            // unregister an observer to avoid a recursive callback
            mDataModel.unregisterObserver(this);
            if (cursor != null) {
                onDataLoaded(cursor);
                cursor.close();
            }
            mDataModel.registerObserver(this);
        } else if (type == DataModelAdapter.LOADTYPE_ASYNC) {
            // TODO: Need to implement an async data loader
        }
    }

    @Override
    public void cleanUp() {
        mDataModel.unregisterObserver(this);
    }

    private void onDataLoaded(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            int[] colIndex = new int[UserDataProvider.ColumnName.length];
            String[] colNames = UserDataProvider.ColumnName;

            for (int i = 0; i < colIndex.length; i++) {
                colIndex[i] = cursor.getColumnIndex(colNames[i]);
            }

            cursor.moveToFirst();
            do {
                UserInfo user = new UserInfo(
                        cursor.getInt(colIndex[0]),
                        cursor.getString(colIndex[1]),
                        cursor.getString(colIndex[2]));
                mDataModel.add(user);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onDataModelChanged(int type, int index, UserInfo data) {
        String[] colNames;
        ContentValues cv;

        switch (type) {
            case IDataModelObserver.DM_OB_INSERT:
                colNames = UserDataProvider.ColumnName;
                cv = new ContentValues();
                cv.put(colNames[1], data.userName);
                cv.put(colNames[2], data.userPhone);
                mContentResolver.insert(UserDataProvider.USER_PROVIDER_URI, cv);
                break;

            case IDataModelObserver.DM_OB_UPDATE:
                colNames = UserDataProvider.ColumnName;
                cv = new ContentValues();
                cv.put(colNames[1], data.userName);
                cv.put(colNames[2], data.userPhone);
                mContentResolver.update(Uri.parse(UserDataProvider.USER_PROVIDER_URI + "/" + data.id),
                        cv, null, null);
                break;

            case IDataModelObserver.DM_OB_DELETE:
                mContentResolver.delete(Uri.parse(UserDataProvider.USER_PROVIDER_URI + "/" + data.id),
                        null, null);
                break;
        }
    }
}
