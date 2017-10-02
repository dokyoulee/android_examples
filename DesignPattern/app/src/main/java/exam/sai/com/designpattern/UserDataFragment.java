package exam.sai.com.designpattern;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exam.sai.com.designpattern.EmpRecyclerView.IOnItemSelectedListener;
import exam.sai.com.designpattern.EmpRecyclerView.IOnItemClickListener;
import exam.sai.com.designpattern.EmpRecyclerView.RecyclerAdapter;
import exam.sai.com.designpattern.databinding.FragmentDesignPatternBinding;
import exam.sai.com.designpattern.model.DataModel;
import exam.sai.com.designpattern.model.IDataModelAdapter;
import exam.sai.com.designpattern.model.IDataModelObserver;
import exam.sai.com.designpattern.model.ObservableUserInfo;
import exam.sai.com.designpattern.model.UserDataModelAdapter;
import exam.sai.com.designpattern.model.UserInfo;

public class UserDataFragment extends Fragment implements IDataModelObserver<UserInfo>,
        IOnItemClickListener, IOnItemSelectedListener {
    private FragmentDesignPatternBinding mBinding;

    // DataModel
    private DataModel<UserInfo> mDataModel;
    private UserDataModelAdapter mDataModelAdapter;

    // RecyclerView
    private RecyclerAdapter mRecyclerAdapter;

    // Bindable user info
    private ObservableUserInfo mInputData = new ObservableUserInfo();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_design_pattern, container, false);
        mBinding = FragmentDesignPatternBinding.bind(v);
        mBinding.setVariable(BR.fragment, this);
        mBinding.setVariable(BR.inputData, mInputData);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataModel = new DataModel();

        mDataModelAdapter = new UserDataModelAdapter(getContext().getContentResolver(), mDataModel);
        mDataModelAdapter.loadDataFromContentProvider(IDataModelAdapter.LOADTYPE_SYNC);

        mRecyclerAdapter = new RecyclerAdapter(R.layout.user_list_item, BR.userInfo, mDataModel, this, this);
        mBinding.recyclerList.setAdapter(mRecyclerAdapter);

        mRecyclerAdapter.notifyDataSetChanged();
        mDataModel.registerObserver(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataModel.unregisterObserver(this);
        mDataModelAdapter.cleanUp();
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.buttonInsert:
                mDataModel.add(mInputData.getUserInfo());
                break;
            case R.id.buttonUpdate:
                mDataModel.update(mInputData.getUserInfo());
                break;
            case R.id.buttonDelete:
                mDataModel.delete(mInputData.getUserInfo());
                break;
        }
    }

    @Override
    public void onDataModelChanged(int type, int index, UserInfo data) {
        switch (type) {
            case DM_OB_INSERT:
                mRecyclerAdapter.notifyItemInserted(index);
                break;
            case DM_OB_UPDATE:
                mRecyclerAdapter.notifyItemChanged(index);
                break;
            case DM_OB_DELETE:
                mRecyclerAdapter.notifyItemRemoved(index);
                break;
        }
    }

    @Override
    public void OnItemClickListener(int position) {
        mInputData.setUserInfo(mDataModel.getAt(position));
    }

    @Override
    public void onItemSelectedListener(int position) {
        mDataModel.delete(mDataModel.getAt(position));
    }
}
