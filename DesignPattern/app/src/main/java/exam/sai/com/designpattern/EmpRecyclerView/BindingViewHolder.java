package exam.sai.com.designpattern.EmpRecyclerView;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import exam.sai.com.designpattern.databinding.UserListItemBinding;

/**
 * Created by sai on 2017-09-15.
 */

public class BindingViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
        MenuItem.OnMenuItemClickListener, View.OnClickListener {
    private static final int MENU_USER_DELETE = 100;

    private UserListItemBinding mBinding;
    private IOnItemClickListener mClickListener;
    private IOnItemSelectedListener mItemSelectedListener;

    public BindingViewHolder(View itemView, IOnItemClickListener clickListener,
                             IOnItemSelectedListener menuItemSelectedListener) {
        super(itemView);
        mBinding = UserListItemBinding.bind(itemView);
        mClickListener = clickListener;
        mItemSelectedListener = menuItemSelectedListener;
    }

    public void onBind(View v) {
        v.setOnClickListener(this);
        v.setOnCreateContextMenuListener(this);
    }

    ViewDataBinding getBinding() {
        return mBinding;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Action");
        MenuItem menuItemDelete = menu.add(0, MENU_USER_DELETE, 0, "Delete");
        menuItemDelete.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_USER_DELETE:
                mItemSelectedListener.onItemSelectedListener(getAdapterPosition());
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        mClickListener.OnItemClickListener(getAdapterPosition());
    }
}
