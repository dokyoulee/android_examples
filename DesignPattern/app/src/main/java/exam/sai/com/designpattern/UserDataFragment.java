package exam.sai.com.designpattern;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import exam.sai.com.designpattern.databinding.FragmentDesignPatternBinding;
import exam.sai.com.designpattern.event.DataModelEventMessage;
import exam.sai.com.designpattern.event.ItemClickEventMessage;
import exam.sai.com.designpattern.event.MenuSelectedEventMessage;
import exam.sai.com.designpattern.global.BaseConfig;
import exam.sai.com.designpattern.model.DataModel;
import exam.sai.com.designpattern.model.DataModelAdapter;
import exam.sai.com.designpattern.model.IDataModelObserver;
import exam.sai.com.designpattern.model.ObservableUserInfo;
import exam.sai.com.designpattern.model.UserDataModelAdapter;
import exam.sai.com.designpattern.model.UserInfo;
import exam.sai.com.designpattern.view.IItemClickListener;
import exam.sai.com.designpattern.view.IItemSelectedListener;
import exam.sai.com.designpattern.view.RecyclerAdapter;

public class UserDataFragment extends Fragment implements IDataModelObserver<UserInfo>,
        IItemClickListener, IItemSelectedListener {
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
        Log.e("UserDataFragment", "onCreateView called, obj=" + this);
        View v = inflater.inflate(R.layout.fragment_design_pattern, container, false);
        mBinding = FragmentDesignPatternBinding.bind(v);
        mBinding.setVariable(BR.fragment, this);
        mBinding.setVariable(BR.inputData, mInputData);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataModel = new DataModel<>();

        mDataModelAdapter = new UserDataModelAdapter(getContext().getContentResolver(), mDataModel);
        mDataModelAdapter.loadDataFromContentProvider(DataModelAdapter.LOADTYPE_SYNC);

        mRecyclerAdapter = new RecyclerAdapter<>(R.layout.user_list_item, BR.userInfo, mDataModel, this, this);
        mBinding.recyclerList.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();

        mDataModel.registerObserver(this);

        if (BaseConfig.useEventbusForNotify()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataModel.unregisterObserver(this);
        mDataModelAdapter.cleanUp();

        if (BaseConfig.useEventbusForNotify()) {
            EventBus.getDefault().unregister(this);
        }
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
    public void onDataModelChanged(int type, int position, UserInfo data) {
        switch (type) {
            case DM_OB_INSERT:
                mRecyclerAdapter.notifyItemInserted(position);
                break;
            case DM_OB_UPDATE:
                mRecyclerAdapter.notifyItemChanged(position);
                break;
            case DM_OB_DELETE:
                mRecyclerAdapter.notifyItemRemoved(position);
                break;
        }
    }

    @Override
    public void onItemClickListener(int position) {
        mInputData.setUserInfo(mDataModel.getAt(position));
    }

    @Override
    public void onMenuSelectedListener(int type, int position) {
        mDataModel.delete(mDataModel.getAt(position));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataModelMessageEvent(DataModelEventMessage eventMessage) {
        onDataModelChanged(eventMessage.type, eventMessage.position, (UserInfo) eventMessage.data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onItemClickMessageEvent(ItemClickEventMessage eventMessage) {
        onItemClickListener(eventMessage.position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMenuSelectedMessageEvent(MenuSelectedEventMessage eventMessage) {
        onMenuSelectedListener(eventMessage.type, eventMessage.position);
    }
}
