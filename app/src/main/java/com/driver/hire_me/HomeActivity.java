package com.driver.hire_me;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.com.driver.webservice.SingleObject;
import com.google.firebase.iid.FirebaseInstanceId;
import com.service.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends Activity {

    protected static final String TAG = "HomeActivity";
    private String refreshedToken = null;
    String fbuserproimg;
    String WhoLogin;
    int REQUEST_CODE_SOME_FEATURES_PERMISSIONS=1;
    Controller controller;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        controller= (Controller) getApplicationContext();

        firebaseSetUp();

        new GetDeviceID().execute();

        checkAutoLogin();



     }

    private void checkAutoLogin() {
        SingleObject obj = SingleObject.getInstance();
        String response=controller.pref.getSIGN_IN_RESPONSE();
        if(!response.equals("")){


            obj.driverLoginParseApi(response);
            if (obj.isDocVerified) {
                Intent i = new Intent(getApplicationContext(), SlideMainActivity.class);
                i.putExtra("whologin",WhoLogin);
                System.out.println("Signin WhoLogin"+WhoLogin);
                i.putExtra("fbuserproimg",fbuserproimg);
                System.out.println("USer Profile Image"+fbuserproimg);
                startActivity(i);
                finish();
            }



        }
    }

    private void firebaseSetUp() {
        try {
            if (refreshedToken == null || refreshedToken.equals(null)) {

                refreshedToken = FirebaseInstanceId.getInstance().getToken();
                controller.pref.setD_DEVICE_TOKEN(refreshedToken);

                Log.d(TAG,"refreshed token1= " + refreshedToken);


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @OnClick(R.id.signin)
    public void openSignInScreen(){
        Intent signin = new Intent(getApplicationContext(), SigninActivity.class);
        signin.addFlags(signin.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(signin);



    }

    @OnClick(R.id.register)
    public void openRegisterScreen(){
        Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(register);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void onBackPressed()
    {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private class GetDeviceID extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            // progress bar
            super.onPreExecute();
        }



        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            int currentapiVersion = Build.VERSION.SDK_INT;
            System.out.println("Current API Version is " + currentapiVersion);
            System.out.println("Check API version is" + Build.VERSION_CODES.M);
            if (currentapiVersion >= Build.VERSION_CODES.M){
                int hasLocationPermission = checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION );
                int hasSMSPermission = checkSelfPermission( Manifest.permission.SEND_SMS );
                int hasAccessLocation= checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION );
                int hasGetAccounts=checkSelfPermission( Manifest.permission.GET_ACCOUNTS );
                int hasInternet=checkSelfPermission( Manifest.permission.INTERNET );
                int hasAccessNetwork=checkSelfPermission( Manifest.permission.ACCESS_NETWORK_STATE );
                int hasAccounts=checkSelfPermission( Manifest.permission.ACCOUNT_MANAGER );
                int hasCamera=checkSelfPermission( Manifest.permission.CAMERA );
                int hasVibrate=checkSelfPermission( Manifest.permission.VIBRATE );
                int hasReadContacts=checkSelfPermission( Manifest.permission.READ_CONTACTS );
                int hasWriteContacts=checkSelfPermission( Manifest.permission.WRITE_CONTACTS );
                int hasReadStorage=checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE );
                int hasWriteStorage=checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE );
                int hasWakeLock=checkSelfPermission( Manifest.permission.WAKE_LOCK );
                int hasphonestate=checkSelfPermission( Manifest.permission.READ_PHONE_STATE );
                System.out.println("HAS PHONE STATE"+hasphonestate);

                int hasChangeNetwork=checkSelfPermission( Manifest.permission.CHANGE_NETWORK_STATE );


                List<String> permissions = new ArrayList<String>();

                if( hasCamera != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.CAMERA );
                }
                if( hasphonestate != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.READ_PHONE_STATE );
                }

                if( hasVibrate != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.VIBRATE );
                }
                if( hasReadContacts != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.READ_CONTACTS );
                }
                if( hasReadStorage != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.READ_EXTERNAL_STORAGE );
                }
                if( hasLocationPermission != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.ACCESS_FINE_LOCATION );
                }

                if( hasLocationPermission != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.ACCESS_FINE_LOCATION );
                }

                if( hasSMSPermission != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.SEND_SMS );
                }

                if( hasAccessLocation != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.ACCESS_COARSE_LOCATION );
                }
                if( hasGetAccounts != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.GET_ACCOUNTS );
                }

                if( hasInternet != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.INTERNET );
                }

                if( hasAccessNetwork != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.ACCESS_NETWORK_STATE );
                }

                if( hasAccounts != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.ACCOUNT_MANAGER );
                }

                if( hasChangeNetwork != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.CHANGE_NETWORK_STATE );
                }

                if( hasWakeLock != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add( Manifest.permission.WAKE_LOCK );
                }
                if( hasWriteStorage != PackageManager.PERMISSION_GRANTED ) {
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }


                if( !permissions.isEmpty() ) {
                    requestPermissions( permissions.toArray( new String[permissions.size()] ), REQUEST_CODE_SOME_FEATURES_PERMISSIONS );

                }
                System.out.println("HAS PHONE STATE after Give Permission" + hasphonestate);

                if( hasphonestate == PackageManager.PERMISSION_GRANTED )
                {


                }else
                {
                //    new GetDeviceID().execute();
                }
            }
            else {

            }

        }

    }



    public void displayFirebaseRegId() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(com.app.Config.SHARED_PREF, 0);
        String regId2 = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id:=== " + regId2);

        System.out.println("Firebase registration id = " + regId2);

        if (!TextUtils.isEmpty(regId2)){
            Toast.makeText(HomeActivity.this,"Firebase Reg Id: " + regId2,Toast.LENGTH_LONG).show();;
            // txtRegId.setText("Firebase Reg Id: " + regId);
        }
        else{
            Toast.makeText(HomeActivity.this,"Firebase Reg Id is not received yet!",Toast.LENGTH_LONG).show();;
            // txtRegId.setText("Firebase Reg Id is not received yet!");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                //	new IntentFilter(Config.REGISTRATION_COMPLETE));
                new IntentFilter(com.app.Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(com.app.Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

    }
}

