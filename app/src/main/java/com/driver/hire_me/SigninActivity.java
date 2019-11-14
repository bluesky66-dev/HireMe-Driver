package com.driver.hire_me;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.app.DriverModel;
import com.app.Request;
import com.app.ServerApiCall;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.custom.CustomProgressDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.grepix.grepixutils.CloudResponse;
import com.grepix.grepixutils.ErrorJsonParsing;
import com.grepix.grepixutils.Utils;
import com.grepix.grepixutils.Validations;
import com.grepix.grepixutils.WebServiceUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SigninActivity extends Activity  {


    @BindView(R.id.emails)
    EditText Email;
    @BindView(R.id.passwords)
    EditText  Psw;
    public static String User_id;

    private CustomProgressDialog progressDialog;
    int count = 1;
    private Controller controller;


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signin);


        progressDialog=new CustomProgressDialog(SigninActivity.this);

        controller = (Controller) getApplicationContext();

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        Email.requestFocus();

    }




    @OnClick(R.id.dones)
    public void signInNow(){


        final String email_ = Email.getText().toString().toLowerCase();
        final String password_ = Psw.getText().toString();


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_).matches()) {
            Toast.makeText(getApplicationContext(), "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            Email.requestFocus();
        } else if (password_.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter Password.", Toast.LENGTH_SHORT).show();
            Psw.requestFocus();
        }else {


            Request.Login login= new Request.Login();
            HashMap<String, String> params = login.addEmail(email_)
                    .addPassword(password_)
                    .addIsAvailable(String.valueOf(1))
                    .build();
            progressDialog.showDialog();
            WebServiceUtil.excuteRequest(SigninActivity.this, params, Constants.Urls.URL_DRIVER_LOGIN, new WebServiceUtil.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                    if (isUpdate) {
                        String response = data.toString();
                        ErrorJsonParsing parser = new ErrorJsonParsing();
                        CloudResponse res = parser.getCloudResponse("" + response);
                        if (res.isStatus()) {
                            SingleObject obj = SingleObject.getInstance();
                            obj.driverLoginParseApi(response);

                            controller.pref.setAPI_KEY(obj.getApiKey());
                            controller.pref.setDRIVER_ID(obj.getDriverId());
                            controller.pref.setSIGN_IN_RESPONSE(response);
                            controller.setSignInResponse(response);
                            controller.pref.setPASSWORD(Psw.getText().toString());
                            driverTokenUpateProfile();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }

    @OnClick(R.id.signuplink)
    public void goToSignUp(){
        Intent reset = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(reset);

    }

    @OnClick(R.id.forgotpsw)
    public void goToForgot(){
        Intent reset = new Intent(getApplicationContext(), Resetpassword.class);
        startActivity(reset);

    }

    @OnClick(R.id.cancels)
    public void backToHome(){
       finish();

    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed() {
        Intent can = new Intent(getApplicationContext(), HomeActivity.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        startActivity(can, bndlanimation);
        finish();

    }

    private void driverTokenUpateProfile() {
        Request.UpdateProfile profile=new Request.UpdateProfile();
        String tokern=FirebaseInstanceId.getInstance().getToken();
        profile.addIsAvailable(String.valueOf(1)).addDviceType();
        if(tokern!=null)
        {
            profile.addDviceToken(tokern);
        }
        ServerApiCall.callWithApiKeyAndDriverId(SigninActivity.this, profile.build(), Constants.UPDATE_PROFILE, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    progressDialog.dismiss();
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (res.isStatus()) {
                        controller.pref.setIsLogin(true);
                        controller.setSignInResponse(response);

                        Intent intent = new Intent(getApplicationContext(), SlideMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    progressDialog.dismiss();
                }
            }
        });
    }




}
