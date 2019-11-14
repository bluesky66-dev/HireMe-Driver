package com.driver.hire_me;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.adaptor.AppController;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AvailabilityActivity extends Activity {


    String availabilitystatus;
    String Liveurl;
    protected static final String TAG = "Available status";

    TextView availabilitytitle;
    Switch availability;
    Button regcancel;
    JSONObject driver_jsonobj;
    String User_id,fbuserproimg,WhoLogin,checkpassword,accept;
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);
        getActionBar().hide();
        Intent i= getIntent();
        User_id= i.getStringExtra("userid");
        fbuserproimg=i.getStringExtra("fbuserproimg");
        WhoLogin=i.getStringExtra("whologin");
        System.out.println("Insite the Slide Main Activity WhoLogin"+WhoLogin);
        checkpassword=i.getStringExtra("password");
        accept=i.getStringExtra("accept");
        availabilitystatus="OFF";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        availabilitystatus=sharedPreferences.getString("availabilitystatus",null);
        Liveurl = sharedPreferences.getString("liveurl", null);

        System.out.println("availabilitystatus0 Driver details funcation" + availabilitystatus);

        availability = (Switch) findViewById(R.id.availability);
        availabilitytitle =(TextView)findViewById(R.id.textView14);


        if(availabilitystatus==null||availabilitystatus.equals("off")) {
            //set the switch to ON
            availability.setChecked(false);
            availabilitystatus="off";
            availabilitytitle.setText(R.string.availability);
            System.out.println("availabilitystatus1 Driver details funcation" + availabilitystatus);
            Driverstatus();
        }else
        {
            availability.setChecked(true);
            availabilitystatus="on";
            availabilitytitle.setText(R.string.availabilityon);
            System.out.println("availabilitystatus2 Driver details funcation" + availabilitystatus);
            Driverstatus();

        }

        //attach a listener to check for changes in state
        availability.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    availabilitystatus="on";
                    availabilitytitle.setText(R.string.availabilityon);
                    Driverstatus();
                    System.out.println("availabilitystatus3 Driver details funcation" + availabilitystatus);

                }else{
                    availabilitystatus="off";
                    Driverstatus();
                    availabilitytitle.setText(R.string.availability);
                    System.out.println("availabilitystatus4 Driver details funcation" + availabilitystatus);

                }

            }
        });

        //check the current state before we display the screen
        if(availability.isChecked()){
            availabilitystatus="on";
            availabilitytitle.setText(R.string.availabilityon);
            System.out.println("availabilitystatus5 Driver details funcation" + availabilitystatus);

        }
        else {
            availabilitystatus="off";
            availabilitytitle.setText(R.string.availability);
            System.out.println("availabilitystatus6 Driver details funcation" + availabilitystatus);

        }


        regcancel=(Button)findViewById(R.id.regcancel);

        regcancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent search=new Intent(getApplicationContext(),SlideMainActivity.class);
                search.putExtra("userid", User_id);
                search.putExtra("fbuserproimg",fbuserproimg);
                //System.out.println("Slide Main Activity Fb Profile imag"+fbuserproimg);
                search.putExtra("whologin",WhoLogin);
                search.putExtra("password", checkpassword);
                System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
                search.putExtra("accept", accept);

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //Set "hasLoggedIn" to true
                editor.putString("availabilitystatus",availabilitystatus );

                // Commit the edits!
                editor.commit();

                startActivity(search);
                finish();
            }
        });
    }
    public boolean net_connection_check() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();


        if (!connection) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            // Setting Dialog Title
            builder.setTitle("Network Connection Error...");

            // Setting Dialog Message
           builder.setMessage("There is no network please connect");

            // Setting Icon to Dialog

            // Setting Positive "Yes" Btn
           builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            int pid = android.os.Process.myPid();
                            android.os.Process.killProcess(pid);
                            System.exit(0);
                        }
                    });

            // Showing Alert Dialog
            builder.show();
        }
        return connection;
    }
    public void onBackPressed()
    {
        Intent search=new Intent(getApplicationContext(),SlideMainActivity.class);
        search.putExtra("userid", User_id);
        search.putExtra("fbuserproimg",fbuserproimg);
        //System.out.println("Slide Main Activity Fb Profile imag"+fbuserproimg);
        search.putExtra("whologin",WhoLogin);
        search.putExtra("password", checkpassword);
        System.out.println("Slide Main Activity WhoLogin" + WhoLogin);
        search.putExtra("accept", accept);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Set "hasLoggedIn" to true
        editor.putString("availabilitystatus",availabilitystatus );

        // Commit the edits!
        editor.commit();

        startActivity(search);
        finish();
    }
    public void Driverstatus() {

        System.out.println("Driver status inside Driver details function" + availabilitystatus);
        final String url=Liveurl+"driverstatus?userid="+User_id+"&status="+availabilitystatus;
        System.out.println("Slide main Activity URL is" + url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                              driver_jsonobj = response.getJSONObject(i);
                               String driverjson_status= driver_jsonobj.getString("status");
                                Log.d("OUTPUT IS", driverjson_status);
                                if(driverjson_status.matches("Success")){
                                    System.out.println("In Site Success Update_location URL");

                                }

                                else  {
                                    System.out.println("Data is not cleared please try again");


                                }


                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
}
