package com.driver.hire_me;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.custom.CustomProgressDialog;
import com.fonts.Fonts;
import com.grepix.grepixutils.WebServiceUtil;

import java.util.HashMap;
import java.util.Map;

public class RatingActivity extends Activity {

    private RatingBar ratingbar1;
    private Button button;
    private Button backBtn;
    private Typeface typeface;
    private TextView tvRating,ratingCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.star_rating_layout);
        dialog=new CustomProgressDialog(RatingActivity.this);

        typeface = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_CONDENCE);

        ratingbar1 = (RatingBar) findViewById(R.id.ratingBar1);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        tvRating.setTypeface(typeface);
        button = (Button) findViewById(R.id.button1);
        backBtn = (Button) findViewById(R.id.back_btn);
        ratingCount = (TextView) findViewById(R.id.rating_count);

        ratingbar1.setClickable(false);
        ratingbar1.setFocusable(false
        );

        driverUpdateProfileRating();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();            }
        });
        //Performing action on Button Click
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String rating = String.valueOf(ratingbar1.getRating());
                Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
            }

        });
    }

    private CustomProgressDialog dialog;



    private void driverUpdateProfileRating() {
       dialog.showDialog();
        final Controller controller = (Controller) getApplicationContext();
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", controller.pref.getAPI_KEY());
        params.put("driver_id", controller.pref.getDRIVER_ID());
        WebServiceUtil.excuteRequest(this, params, Constants.UPDATE_PROFILE, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                dialog.dismiss();
                if (isUpdate) {
                    SingleObject obj = SingleObject.getInstance();
                    obj.setD_rating("");
                    obj.driverUpdateProfileParseApi(data.toString());
                    String driver_rating = obj.getD_rating();
                    if (driver_rating.length() != 0) {
                        float rating = Float.parseFloat(driver_rating);
                        ratingbar1.setRating(rating);
                        ratingCount.setText(String.format("%.01f", Float.parseFloat(obj.getD_rating_count())));
                    }

                }
            }
        });
    }
}
