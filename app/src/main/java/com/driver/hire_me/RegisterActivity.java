package com.driver.hire_me;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.custom.CustomProgressDialog;
import com.grepix.grepixutils.CloudResponse;
import com.grepix.grepixutils.ErrorJsonParsing;
import com.grepix.grepixutils.Validations;
import com.grepix.grepixutils.WebServiceUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NewApi")
public class RegisterActivity extends Activity {

    CustomProgressDialog dialog;

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.mobile)
    EditText etmobile;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.repassword)
    EditText repassword;
    @BindView(R.id.first_name)
    EditText etfirst_name;
    @BindView(R.id.last_name)
    EditText etlast_name;

    protected static final String TAG = "RegisterActivity";
    String regId;
    int count = 1;
    Controller controller;

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        controller = (Controller) getApplicationContext();
        dialog = new CustomProgressDialog(RegisterActivity.this);
        regId = controller.pref.getD_DEVICE_TOKEN();

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        etfirst_name.requestFocus();
    }

    @OnClick(R.id.next)
    public void nextButton() {
         signUpDriver();

    }

    @OnClick(R.id.cancel)
    public void backButton() {
        finish();
    }

    private void signUpDriver() {

        final String fname_ = etfirst_name.getText().toString();
        final String lname_ = etlast_name.getText().toString();
        final String email_ = email.getText().toString();
        final String mobileNo_ = etmobile.getText().toString();
        final String password_ = password.getText().toString();
        final String repassword_ = repassword.getText().toString();

        if (fname_.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter First Name.", Toast.LENGTH_LONG).show();
            etfirst_name.requestFocus();
        } else if (lname_.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter Last Name.", Toast.LENGTH_LONG).show();
            etlast_name.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_).matches()) {
            Toast.makeText(getApplicationContext(), "Enter Valid Email Address", Toast.LENGTH_LONG).show();
            email.requestFocus();
        } else if (mobileNo_.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter Valid Mobile number.", Toast.LENGTH_LONG).show();
            etmobile.requestFocus();
        } else if (password_.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter Password.", Toast.LENGTH_LONG).show();
            password.requestFocus();
        } else if (!repassword_.equals(password_)) {
            Toast.makeText(getApplicationContext(), "Password does not match the confirm password.", Toast.LENGTH_LONG).show();
            repassword.requestFocus();
        } else {

            Map<String, String> params = new HashMap<String, String>();
            params.put("d_email", email_);
            params.put("d_phone", mobileNo_);
            params.put("d_password", password_);

            if (regId != null) {
                params.put("d_device_type", "Android");
                params.put("d_device_token", regId);
            }
            params.put("d_fname", fname_);
            params.put("d_lname", lname_);
            //params.put("d_is_verified", String.valueOf(1));
            dialog.showDialog();
            WebServiceUtil.excuteRequest(RegisterActivity.this, params, Constants.Urls.URL_DRIVER_REGISTER, new WebServiceUtil.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                    dialog.dismiss();
                    if (isUpdate) {
                        String response = data.toString();
                        ErrorJsonParsing parser = new ErrorJsonParsing();
                        CloudResponse res = parser.getCloudResponse("" + response);
                        dialog.dismiss();
                        if (res.isStatus()) {
                            controller.pref.setPASSWORD(password.getText().toString());
                            SingleObject object = SingleObject.getInstance();
                            object.driverUpdateProfileParseApi(response);
                            controller.pref.setAPI_KEY(object.getApiKey());
                            controller.pref.setDRIVER_ID(object.getDriverId());
                            controller.pref.setIsLogin(true);
                            Intent main = new Intent(getApplicationContext(), DocUploadActivity1.class);
                            main.putExtra("driver_id", object.getDriverId());
                            main.putExtra("api_key", object.getApiKey());
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.internet_error, Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }
}
