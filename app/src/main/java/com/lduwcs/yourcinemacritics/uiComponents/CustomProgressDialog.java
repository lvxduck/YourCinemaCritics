package com.lduwcs.yourcinemacritics.uiComponents;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lduwcs.yourcinemacritics.R;

public class CustomProgressDialog extends Dialog {
    public CustomProgressDialog(Context context) {
        super(context);
//        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
//        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
//        getWindow().setAttributes(wlmp);
//        setTitle(null);
//        setCancelable(false);
//        setOnCancelListener(null);
//        View view = LayoutInflater.from(context).inflate(
//                R.layout.progress_dialog, null);
//        setContentView(view);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.progress_dialog);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
