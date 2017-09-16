package com.sai.test.testapp;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomPopupToast {
    public CustomPopupToast(Context context, View view, CharSequence text) {
        ImageView iv = (ImageView) view.findViewById(R.id.toast_image);
        iv.setImageResource(android.R.drawable.ic_dialog_alert);

        TextView tvMessage = (TextView) view.findViewById(R.id.custom_view_text);
        tvMessage.setText(text);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}
