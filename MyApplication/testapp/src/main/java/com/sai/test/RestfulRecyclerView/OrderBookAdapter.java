package com.sai.test.RestfulRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by sai on 2017-10-04.
 */

public class OrderBookAdapter<T> extends RecyclerView.Adapter<OrderbookViewHolder> {
    private int mItemLayoutId = 0;
    private int mBindingId = 0;
    private List<T> mList = null;

    public OrderBookAdapter(int itemLayoutId, int bindingId, List<T> list) {
        super();
        this.mItemLayoutId = itemLayoutId;
        this.mBindingId = bindingId;
        this.mList = list;
    }

    @Override
    public OrderbookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        return new OrderbookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderbookViewHolder holder, int position) {
        if (mList != null && position < mList.size()) {
            T item = mList.get(position);
            holder.getBinding().setVariable(mBindingId, item);
        }
    }

    @Override
    public int getItemCount() {
        return (mList == null) ? 0 : mList.size();
    }
}
