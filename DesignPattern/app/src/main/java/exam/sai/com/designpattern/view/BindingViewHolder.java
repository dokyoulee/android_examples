package exam.sai.com.designpattern.view;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import exam.sai.com.designpattern.databinding.UserListItemBinding;
import exam.sai.com.designpattern.event.ItemClickEventMessage;
import exam.sai.com.designpattern.event.MenuSelectedEventMessage;
import exam.sai.com.designpattern.global.BaseConfig;

/**
 * Created by sai on 2017-09-15.
 */

class BindingViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
        MenuItem.OnMenuItemClickListener, View.OnClickListener {
    private static final int MENU_DELETE = 100;

    private UserListItemBinding mBinding;
    private IItemClickListener mClickListener;
    private IItemSelectedListener mItemSelectedListener;

    BindingViewHolder(View itemView, IItemClickListener clickListener,
                             IItemSelectedListener menuItemSelectedListener) {
        super(itemView);
        mBinding = UserListItemBinding.bind(itemView);
        mClickListener = clickListener;
        mItemSelectedListener = menuItemSelectedListener;
    }

    void onBind(View v) {
        v.setOnClickListener(this);
        v.setOnCreateContextMenuListener(this);
    }

    ViewDataBinding getBinding() {
        return mBinding;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Action");
        MenuItem menuItemDelete = menu.add(0, MENU_DELETE, 0, "Delete");
        menuItemDelete.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (BaseConfig.useEventbusForNotify() == false) {
            if (mItemSelectedListener != null) {
                mItemSelectedListener.onMenuSelectedListener(MENU_DELETE, getAdapterPosition());
            }
        } else {
            EventBus.getDefault().post(new MenuSelectedEventMessage(MENU_DELETE, getAdapterPosition()));
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (BaseConfig.useEventbusForNotify() == false) {
            if (mClickListener != null) {
                mClickListener.onItemClickListener(getAdapterPosition());
            }
        } else {
            EventBus.getDefault().post(new ItemClickEventMessage(getAdapterPosition()));
        }
    }
}
