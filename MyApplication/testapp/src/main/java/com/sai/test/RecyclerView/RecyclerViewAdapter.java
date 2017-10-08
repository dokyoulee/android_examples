package com.sai.test.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<BindingViewHolder> {
    private List<T> mData;
    private int mLayoutResourceId;
    private int mBindVarId;
    private IOnItemClickListener mListener;

    public RecyclerViewAdapter(int layout, int variable, IOnItemClickListener listener) {
        mLayoutResourceId = layout;
        mBindVarId = variable;
        mListener = listener;
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public T getItem(int position) {
        if (position < mData.size()) {
            return mData.get(position);
        } else {
            return null;
        }
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mLayoutResourceId, parent, false);
        return new BindingViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        if (mData != null && position < mData.size()) {
            T item = mData.get(position);
            holder.getBinding().setVariable(mBindVarId, item);
            holder.onBind(holder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }
}
