package com.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.driver.hire_me.R;


/**
 * Created by BEveryware on 4/4/14.
 */

public class CustomProgressDialog {
    private final TextView textview;
    private Dialog dialog;
    private Context context;

    public CustomProgressDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.loader_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_progress_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ProgressBar progressBar=(ProgressBar)dialog.findViewById(R.id.progressBar1);
        textview=(TextView)dialog.findViewById(R.id.text);
        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.yellow_color), android.graphics.PorterDuff.Mode.SRC_ATOP);

    }





    public void showDialogwithText(String text) {
        //dialog.setCancelable(true);
        if (!((Activity) context).isFinishing())
            textview.setText(text);
            dialog.show();
    }

    public void showDialog() {
        //dialog.setCancelable(true);
        if (!((Activity) context).isFinishing())
        dialog.show();
    }

    public void dismiss() {
        try {
            if (dialog.isShowing()) dialog.dismiss();
        }catch (Exception e){
            dialog.dismiss();
        }

    }

    public boolean isShowing() {
        if (dialog.isShowing()) return true;
        return false;
    }
}