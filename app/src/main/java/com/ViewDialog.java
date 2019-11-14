package com;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.driver.hire_me.R;

/**
 * Created by grepix on 12/2/2016.
 */
public class ViewDialog {

        public void showDialog(Context context, String msg){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.logout_layout);

            TextView text = (TextView) dialog.findViewById(R.id.tv_dialog_title);
            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.yes_btn);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            dialog.show();

        }
    }