package exam.sai.com.designpattern.EmpRecyclerView;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sai on 2017-09-15.
 */

public class EmpViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding mBinding;

    public EmpViewHolder(View itemView) {
        super(itemView);
        mBinding = DataBindingUtil.bind(itemView);
    }

    ViewDataBinding getBinding() {
        return mBinding;
    }
}
