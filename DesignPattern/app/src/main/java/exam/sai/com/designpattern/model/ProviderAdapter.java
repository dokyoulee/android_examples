package exam.sai.com.designpattern.model;

import android.app.Activity;
import android.content.ContentProvider;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import static java.security.AccessController.getContext;

public class ProviderAdapter implements IProviderModelAdapter<UserInfo>, IDataModelObserver<UserInfo>,
        LoaderManager.LoaderCallbacks<Cursor> {
    private ContentProvider mProvider;
    private IDataModel<UserInfo> mDataModel;

    public ProviderAdapter(ContentProvider cp, IDataModel<UserInfo> dataModel) {
        mProvider = cp;
        mDataModel = dataModel;
        dataModel.registerObserver(this);
    }

    @Override
    public void setProvider(ContentProvider cp) {
        mProvider = cp;
    }

    @Override
    public void setDataModel(IDataModel<UserInfo> dataModel) {
        mDataModel = dataModel;
    }

    @Override
    public void loadDataFromContentProvider(int type, Activity activity) {
        if (type == IProviderModelAdapter.LOADTYPE_SYNC) {

        } else if (type == IProviderModelAdapter.LOADTYPE_ASYNC){
            .getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDataModelChanged(int type, int index, UserInfo data) {

    }
}
