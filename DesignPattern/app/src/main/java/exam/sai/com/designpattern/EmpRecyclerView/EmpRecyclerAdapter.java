package exam.sai.com.designpattern.EmpRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sai on 2017-09-15.
 */

public class EmpRecyclerAdapter extends RecyclerView.Adapter<EmpViewHolder> {
    private int mItemLayoutId = 0;

    public EmpRecyclerAdapter(int itemLayoutId) {
        super();
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public EmpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        EmpViewHolder viewHolder = new EmpViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmpViewHolder holder, int position) {
        //holder
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
