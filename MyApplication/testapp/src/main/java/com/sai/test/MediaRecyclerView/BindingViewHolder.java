package com.sai.test.MediaRecyclerView;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

public class BindingViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding mBinding;
    private IOnItemClickListener mListener;

    public BindingViewHolder(View itemView, IOnItemClickListener listener) {
        super(itemView);
        mBinding = DataBindingUtil.bind(itemView);
        mListener = listener;
    }

    public void onBind(View v, final int position) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onBind", "Test");
                mListener.OnItemClickListerner(position);
            }
        });
    }

    public ViewDataBinding getBinding() {
        return mBinding;
    }
}
