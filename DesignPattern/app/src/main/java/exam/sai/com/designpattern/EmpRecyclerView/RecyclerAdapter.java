package exam.sai.com.designpattern.EmpRecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exam.sai.com.designpattern.model.DataModel;

/**
 * Created by sai on 2017-09-15.
 */

public class RecyclerAdapter<T> extends RecyclerView.Adapter<BindingViewHolder> {
    private int mItemLayoutId = 0;
    private DataModel<T> mDataModel;
    private int mBindingId = 0;
    private IOnItemClickListener mClickListener;
    private IOnItemSelectedListener mItemSelectedListener;

    public RecyclerAdapter(int itemLayoutId, int bindingId, @NonNull DataModel<T> dataModel,
                           IOnItemClickListener clickListener,
                           IOnItemSelectedListener menuItemSelectedListener) {
        super();
        mItemLayoutId = itemLayoutId;
        mDataModel = dataModel;
        mBindingId = bindingId;
        mClickListener = clickListener;
        mItemSelectedListener = menuItemSelectedListener;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        return new BindingViewHolder(v, mClickListener, mItemSelectedListener);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        if (mDataModel != null && position < mDataModel.getCount()) {
            T item = mDataModel.getAt(position);
            holder.getBinding().setVariable(mBindingId, item);
            holder.onBind(holder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        return mDataModel.getCount();
    }
}
