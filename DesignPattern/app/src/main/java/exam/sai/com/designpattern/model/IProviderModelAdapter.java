package exam.sai.com.designpattern.model;

import android.content.ContentProvider;

public interface IProviderModelAdapter <T> {
    public static final int LOADTYPE_SYNC = 1;
    public static final int LOADTYPE_ASYNC = 2;

    public void setProvider(ContentProvider cp);
    public void setDataModel(IDataModel<T> dataModel);
    public void loadDataFromContentProvider(int type);
}
