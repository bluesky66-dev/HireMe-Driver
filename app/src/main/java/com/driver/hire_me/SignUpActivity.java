package com.driver.hire_me;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
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

import java.net.URL;

public class SignUpActivity extends Activity {

    Button cancelsu, nextsu;
    EditText fname, lname, eadd, payeadd, mno;
    String faname, laname, eaadd, payeaadd, mano;

    protected static final String TAG = null;

    String fuid;
    String fufname;
    String fulname;
    String fuemail;
    String fbuserproimg;

    private JSONArray login_jsonarray;
    private JSONObject login_jsonobj;
    private String login_status;
    private String userid;
    private String login_inputline;

    String Liveurl = "";
    URL fbsignup_Url;
    String WhoLogin;
    String regId;
    String imei = "";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getActionBar().hide();

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl = sharedPreferences.getString("liveurl", null);
        regId = sharedPreferences.getString("RegId", null);
        imei = sharedPreferences.getString("IMEI", null);
        System.out.println("The Live URL Is " + Liveurl);

        fname = (EditText) findViewById(R.id.firstname);
        lname = (EditText) findViewById(R.id.lastname);
        eadd = (EditText) findViewById(R.id.emailadd);
        mno = (EditText) findViewById(R.id.mobileno);
        payeadd = (EditText) findViewById(R.id.payemailadd);

        cancelsu = (Button) findViewById(R.id.cancelsu);
        nextsu = (Button) findViewById(R.id.nextsu);

        Intent i = getIntent();

        fuid = i.getStringExtra("fbuserid");
        fufname = i.getStringExtra("fbuserfname");
        fulname = i.getStringExtra("fbuserlname");
        fuemail = i.getStringExtra("fbuseremail");
        fbuserproimg = i.getStringExtra("fbuserproimg");

        WhoLogin = i.getStringExtra("whologin");

        fname.setText(fufname);
        lname.setText(fulname);
        eadd.setText(fuemail);


        cancelsu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent cancelsu = new Intent(getApplicationContext(), RegisterActivity.class);
                System.out.println("Slide Main Activity Fb Profile imag" + fbuserproimg);
                cancelsu.putExtra("userid", userid);
                cancelsu.putExtra("whologin", WhoLogin);
                cancelsu.putExtra("fbuserproimg", fbuserproimg);
                System.out.println("Signup Activity Fb Profile imag" + fbuserproimg);

                startActivity(cancelsu);
                finish();
            }
        });
        nextsu.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @SuppressLint("NewApi")
            public void onClick(View v) {
                if (net_connection_check()) {

                    faname = fname.getText().toString();
                    laname = lname.getText().toString();
                    eaadd = eadd.getText().toString();
                    payeaadd = payeadd.getText().toString();
                    eaadd = eaadd.toLowerCase();
                    payeaadd = payeaadd.toLowerCase();
                    mano = mno.getText().toString();


                    if (fname.getText().toString().equals("")) {
                        fname.setError(getText(R.string.required));
                        eadd.setError(null);
                        payeadd.setError(null);
                        fname.addTextChangedListener(new TextWatcher() {
                            public void afterTextChanged(Editable edt) {

                            }


                            @Override
                            public void beforeTextChanged(CharSequence s,
                                                          int start, int count, int after) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onTextChanged(CharSequence s,
                                                      int start, int before, int count) {
                                // TODO Auto-generated method stub
                                if (count > 0) {
                                    fname.setError(null);
                                }

                            }

                        });
                    } else if (eadd.getText().toString().equals("")) {
                        eadd.setError(null);
                        payeadd.setError(null);
                        eadd.setError(getText(R.string.required));

                    } else if (eaadd.substring(0, 1).matches("[0-9]")) {
                        eadd.setError(null);
                        payeadd.setError(null);
                        eadd.setError(getText(R.string.invalid_email));
                    } else if (!eaadd.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") || eaadd.length() < 0) {
                        eadd.setError(null);
                        payeadd.setError(null);
                        eadd.setError(getText(R.string.invalid_email));
                    } else if (payeadd.getText().toString().equals("")) {
                        eadd.setError(null);
                        payeadd.setError(null);
                        payeadd.setError(getText(R.string.required));

                    } else if (payeaadd.substring(0, 1).matches("[0-9]")) {
                        eadd.setError(null);
                        payeadd.setError(null);
                        payeadd.setError(getText(R.string.invalid_email));
                    } else if (!payeaadd.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") || payeaadd.length() < 0) {
                        eadd.setError(null);
                        payeadd.setError(null);
                        payeadd.setError(getText(R.string.invalid_email));
                    } else if (mno.getText().toString().equals("")) {
                        eadd.setError(null);
                        payeadd.setError(null);
                        mno.setError(getText(R.string.required));
                    } else if (mano.length() < 6 || mano.charAt(0) == '0' && mano.charAt(1) == '0' && mano.charAt(2) == '0') {
                        eadd.setError(null);
                        payeadd.setError(null);
                        mno.setError(getText(R.string.invalid_mobile_number));
                    } else if (eaadd.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && eaadd.length() > 0) {
                        nextsu.setEnabled(false);

                        eadd.setError(null);
                        payeadd.setError(null);
                        if (payeadd.getText().toString().equals("")) {
                            //payeadd.setError("Required");
                            payeaadd = "Paypalid";
                        }
                        faname = faname.replaceAll(" ", "");
                        laname = laname.replaceAll(" ", "");

                        final String url;
                        if (WhoLogin.equals("FaceBook")) {
                            url = Liveurl + "fb_signup?firstname=" + faname + "&lastname=" + laname + "&mobile_no=" + mano + "&email=" + eaadd + "&paypalemail=" + payeaadd + "&fb_id=" + fuid + "&regid=" + regId + "&imei=" + imei;
                        } else {
                            url = Liveurl + "gp_signup?firstname=" + faname + "&lastname=" + laname + "&mobile_no=" + mano + "&email=" + eaadd + "&paypalemail=" + payeaadd + "&gp_id=" + fuid + "&regid=" + regId + "&imei=" + imei;

                        }

                        System.out.println("URL is" + url);
                        // Creating volley request obj
                        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {


                                        // Parsing json
                                        for (int i = 0; i < response.length(); i++) {
                                            try {

                                                login_jsonobj = response.getJSONObject(i);
                                                login_status = login_jsonobj.getString("status");
                                                Log.d("OUTPUT IS", login_status);
                                                if (login_status.matches("Success")) {
                                                    userid = login_jsonobj.getString("id");
                                                    System.out.println("Insite the success");

                                                    loadSavedPreferences();
                                                    Intent fbsignup = new Intent(getApplicationContext(), DocUploadActivity.class);
                                                    fbsignup.putExtra("userid", userid);
                                                    fbsignup.putExtra("whologin", WhoLogin);
                                                    System.out.println("Insite the signup activity" + WhoLogin);
                                                    fbsignup.putExtra("fbuserproimg", fbuserproimg);
                                                    System.out.println("Signup Activity Fb Profile imag" + fbuserproimg);
                                                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                                                    startActivity(fbsignup, bndlanimation);

                                                    finish();
                                                } else if (login_status.matches("Email already exists")) {

                                                    System.out.println("Insite the already exit");
                                                    nextsu.setEnabled(true);
                                                    final Toast toast = Toast.makeText(getApplicationContext(), "Email already taken enter new email", Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            toast.cancel();
                                                        }
                                                    }, 500);
                                                } else if (login_status.matches("Mobile number already exists")) {
                                                    nextsu.setEnabled(true);
                                                    System.out.println("Insite the already exit");
                                                    final Toast toast = Toast.makeText(getApplicationContext(), "Mobile number already taken enter new mobile number", Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            toast.cancel();
                                                        }
                                                    }, 500);
                                                } else if (login_status.matches("Already exists")) {
                                                    nextsu.setEnabled(true);
                                                    System.out.println("Insite the already exit");
                                                    final Toast toast = Toast.makeText(getApplicationContext(), "Your account already taken", Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            toast.cancel();
                                                        }
                                                    }, 500);
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


                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadSavedPreferences() {
        //User has successfully logged in, save this information
        // We need an Editor object to make preference changes.

       // SharedPreferences settings = getSharedPreferences(SigninActivity.PREFS_NAME, 0);
       // SharedPreferences.Editor editor = settings.edit();

        //Set "hasLoggedIn" to true
    /*    editor.putString("sinuser", userid);
        editor.putString("whologin", WhoLogin);
        editor.putString("fbuserproimg", fbuserproimg);
        // Commit the edits!
        editor.commit();*/

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed() {
        Intent can = new Intent(getApplicationContext(), HomeActivity.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        startActivity(can, bndlanimation);
        finish();
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
}
