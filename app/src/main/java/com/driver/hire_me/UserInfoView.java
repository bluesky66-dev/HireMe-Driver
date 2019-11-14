package com.driver.hire_me;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adaptor.TripModel;

/**
 * Created by devin on 2017-10-10.
 */

public class UserInfoView {

    private UserInfoViewCallBack callback;
    public Context context;
    public View view;
    public ImageView accept;



    private TextView tvUserNme;
    private TextView tvUserMobile;
    TripModel tripModel;

    public interface UserInfoViewCallBack
    {
        void onCallButtonCliked();
    }
    public  void setUserInfoViewCallBack(UserInfoViewCallBack callback )
    {
        this.callback=callback;
    }
    public  void hide()
    {
        this.view. setVisibility(View.GONE);
    }
    public void show()
    {
        this.view.setVisibility(View.VISIBLE);
    }


    public UserInfoView(Context context, View view, TripModel tripModel)
    {
        this.context=context;
        this.view=view;
        this.tripModel=tripModel;
        init();
    }
    private void init()
    {

        tvUserNme=(TextView)view.findViewById(R.id.tv_user_name);
        tvUserNme.setText(tripModel.user.getU_name());
        tvUserMobile=(TextView)view.findViewById(R.id.tv_mobile_number);
        tvUserMobile.setText(tripModel.user.getU_phone());

        accept= (ImageView)view.findViewById(R.id.iv_user_call_number);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no = tvUserMobile.getText().toString();
                if (phone_no.equals(null)) {
                    Toast.makeText(context, "No number", Toast.LENGTH_SHORT).show();
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phone_no));
                    if (callIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(callIntent);
                    }
                }
            }
        });

    }

}
