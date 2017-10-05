package com.sai.test.RestfulRecyclerView;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sai on 2017-10-04.
 */

public class OrderbookViewHolder extends RecyclerView.ViewHolder {
    ViewDataBinding mBinding;

    public OrderbookViewHolder(View itemView) {
        super(itemView);
        mBinding = DataBindingUtil.bind(itemView);
    }

    public ViewDataBinding getBinding() {
        return mBinding;
    }
}
