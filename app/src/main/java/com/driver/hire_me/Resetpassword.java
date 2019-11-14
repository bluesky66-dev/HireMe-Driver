package com.driver.hire_me;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.com.driver.webservice.Constants;
import com.custom.CustomProgressDialog;
import com.grepix.grepixutils.CloudResponse;
import com.grepix.grepixutils.ErrorJsonParsing;
import com.grepix.grepixutils.Validations;
import com.grepix.grepixutils.WebServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

@SuppressLint("NewApi")
public class Resetpassword extends Activity {

    CustomProgressDialog progressDialog;
    protected static final String TAG = "ResetPassword Activity";
    public static String User_id;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        getActionBar().hide();
        progressDialog = new CustomProgressDialog(Resetpassword.this);


        final com.custom.BEditText Email = findViewById(R.id.reemail);


        Button cancel = findViewById(R.id.recancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        com.custom.BButton button = findViewById(R.id.reset);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email_ = Email.getText().toString().toLowerCase();
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_).matches()) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Email Address", Toast.LENGTH_LONG).show();
                    Email.requestFocus();
                } else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("d_email", email_);
                    progressDialog.showDialog();
                    WebServiceUtil.excuteRequest(Resetpassword.this, params, Constants.FORGET_PASSWORD, new WebServiceUtil.DeviceTokenServiceListener() {
                        @Override
                        public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                            progressDialog.dismiss();
                            if (isUpdate) {
                                if (data != null) {
                                    try {
                                        JSONObject rootObject = new JSONObject(data.toString());
                                        String res = rootObject.optString("response");
                                        if (res != null) {
                                            Toast.makeText(getApplicationContext(), "Password will be sent in a email.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                               // Toast.makeText(Resetpassword.this, R.string.check_your_emailId, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });


    }
}
