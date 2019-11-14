package com.driver.hire_me;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adaptor.AppController;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by test on 2/16/16.
 */
public class BrainTreePaypal extends AppCompatActivity {

    Button update;
    private EditText inputFirstname;
    private TextInputLayout inputLayoutFirstname;



    private JSONObject edprofile_jsonobj;
    private String edprofile_status;

    String firstname;

    private JSONObject profile_jsonobj;
    private String profile_status;

    String User_id,fbuserproimg,WhoLogin,checkpassword,movefile;

    String profile_userfname;

    String timer="timer";
    String Liveurl="";
    String Liveurl1="";

    String status,userid;
    protected static final String TAG = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.braintreepaypal);
        getSupportActionBar().hide();



        Intent i = getIntent();

        fbuserproimg = i.getStringExtra("fbuserproimg");
        WhoLogin = i.getStringExtra("whologin");
        checkpassword = i.getStringExtra("password");
        movefile = i.getStringExtra("movefile");

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl = sharedPreferences.getString("liveurl", null);
        Liveurl1 = sharedPreferences.getString("liveurl1", null);
        User_id = sharedPreferences.getString("userid", null);

        inputLayoutFirstname = (TextInputLayout) findViewById(R.id.brainlayout_firstname);
        inputFirstname = (EditText) findViewById(R.id.brainfirstname);

        update = (Button) findViewById(R.id.update);



        inputFirstname.addTextChangedListener(new MyTextWatcher(inputFirstname));


        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (!validateFname()) {
                    return;
                }



               firstname = inputFirstname.getText().toString();

                done();


            }
        });


        if (net_connection_check()) {



            final String url = Liveurl + "display_details?user_id=" + User_id;
            System.out.println("URL is" + url);
            // Creating volley request obj
            JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {


                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    profile_jsonobj = response.getJSONObject(i);
                                    profile_status = profile_jsonobj.getString("status");
                                    User_id = profile_jsonobj.getString("id");
                                    profile_userfname = profile_jsonobj.getString("paypalemail");

                                    if (profile_userfname.equals("null")) {
                                        inputFirstname.setText("");
                                    } else {

                                        inputFirstname.setText(profile_userfname);
                                    }

                                    Log.d("OUTPUT IS", profile_status);
                                    if (profile_status.matches("Success")) {

                                        inputFirstname.setEnabled(true);
                                    }
                                    else if (profile_status.matches("Invalid")) {
                                        Toast.makeText(getApplicationContext(), "Invalid User ID", Toast.LENGTH_LONG).show();

                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(movieReq);
        }


    }


        private boolean validateFname() {

           if (inputFirstname.getText().toString().trim().isEmpty())
                    {
                inputLayoutFirstname.setError(getString(R.string.err_msg_paypal));
                requestFocus(inputFirstname);
                return false;
        }
 else if (!inputFirstname.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")  ) {

                inputLayoutFirstname.setError(getString(R.string.err_msg_paypal));
                requestFocus(inputFirstname);
                return false;

            }

    else {
        inputLayoutFirstname.setErrorEnabled(false);

               return true;
    }


        }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.brainfirstname:
                    validateFname();
                    break;

            }
        }
    }


    public void done()
    {
        firstname=firstname.toLowerCase();

        final String url=Liveurl+"update_paypal_details?user_id="+User_id+"&paypalemail="+firstname;
        System.out.println("URL is"+url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                edprofile_jsonobj = response.getJSONObject(i);
                                edprofile_status= edprofile_jsonobj.getString("status");
                                Log.d("OUTPUT IS", edprofile_status);
                                if(edprofile_status.matches("Success")){



                                    Intent back = new Intent(getApplicationContext(), SlideMainActivity.class);
                                    back.putExtra("userid", User_id);
                                    back.putExtra("fbuserproimg", fbuserproimg);
                                    System.out.println("EditProfile  Activity Fb Profile imag" + fbuserproimg);
                                    back.putExtra("whologin", WhoLogin);
                                    back.putExtra("password", checkpassword);
                                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                                    startActivity(back);
                                    finish();
                                }

                                else if(edprofile_status.matches("Invalid Details")) {
                                    Toast.makeText(getApplicationContext(), "Missing Fields ", Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
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


    public boolean net_connection_check() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();


        if (!connection) {

            Toast toast = Toast.makeText(getApplicationContext(), "There is no network please connect.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 70);
            toast.show();
        }
        return connection;
    }


    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent can=new Intent(getApplicationContext(),SlideMainActivity.class);
        can.putExtra("userid", User_id);
        can.putExtra("fbuserproimg",fbuserproimg);
        can.putExtra("whologin", WhoLogin);
        can.putExtra("password",checkpassword);
        can.putExtra("timer",timer);
        System.out.println("Profile Activity Fb Profile imag"+fbuserproimg);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        startActivity(can);
        finish();
    }

    }


