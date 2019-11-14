package com.driver.hire_me;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
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

import java.util.Calendar;


/**
 * Created by test on 2/13/16.
 */
public class BrainTreeDetails extends AppCompatActivity  implements View.OnClickListener {

    private EditText inputFirstname, inputLastname, inputDob,inputAddress,inputAccount,
            inputLocality,inputRegion,inputPostal,inputRouting;
    private TextInputLayout inputLayoutFirstname, inputLayoutLastname, inputLayoutDob,inputLayoutAddress,
            inputLayoutAccount,inputLayoutLocality,inputLayoutRegion,inputLayoutPostal,inputLayoutRouting;
    Button update;

    String status,userid;

    private JSONObject profile_jsonobj;
    private String profile_status;

    private Calendar cal;
    private int day;
    private int month;
    private int year;

    String profile_userfname,profile_userlname,profile_userdob,
            profile_useraccountno,profile_useraddress,profile_userlocality,profile_userregion,
            profile_userpostal,profile_userrouting;

    protected static final String TAG = null;

    private JSONObject edprofile_jsonobj;
    private String edprofile_status;
    String firstname,lastname,dob,address,account,locality,region,postal,routing;

    String Liveurl="";
    String Liveurl1="";

    String User_id,fbuserproimg,WhoLogin,checkpassword;
    String from=null,drivernames,driveremails,drivermobiles;
    String tripids,rating,rideshare,riderid;
    String pickups=null,drops=null,dates=null,distances=null,charges=null,waits=null,taxs=null,totals=null,autotripids;
    String fr,paydone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.braintreedetails);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl = sharedPreferences.getString("liveurl", null);
        Liveurl1 = sharedPreferences.getString("liveurl1", null);



        Intent f= getIntent();
        User_id= f.getStringExtra("userid");
        tripids= f.getStringExtra("tripid");
        autotripids= f.getStringExtra("autotripid");
        riderid=f.getStringExtra("riderid");
        fbuserproimg=f.getStringExtra("fbuserproimg");
        WhoLogin=f.getStringExtra("whologin");
        checkpassword=f.getStringExtra("password");

        pickups=f.getStringExtra("pickup");
        drops=f.getStringExtra("drop");
        dates=f.getStringExtra("date");
        distances=f.getStringExtra("distance");
        charges=f.getStringExtra("charge");
        waits=f.getStringExtra("wait");
        System.out.println("fare review wait"+waits);
        taxs=f.getStringExtra("tax");
        totals= f.getStringExtra("total");
        drivernames=f.getStringExtra("drivername");
        driveremails=f.getStringExtra("driveremail");
        drivermobiles=f.getStringExtra("drivermobile");
        rating=f.getStringExtra("rating");
        rideshare=f.getStringExtra("rideshare");
        from=f.getStringExtra("from");
        fr=f.getStringExtra("visiblestatus");
        paydone=f.getStringExtra("paydone");

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        inputLayoutFirstname = (TextInputLayout) findViewById(R.id.brainlayout_firstname);
        inputLayoutLastname = (TextInputLayout) findViewById(R.id.brainlayout_lastname);
        inputLayoutDob = (TextInputLayout) findViewById(R.id.brainlayout_dob);
        inputLayoutAddress = (TextInputLayout) findViewById(R.id.brainlayout_address);
        inputLayoutAccount = (TextInputLayout) findViewById(R.id.brainlayout_account);
       inputLayoutLocality = (TextInputLayout) findViewById(R.id.brainlayout_locality);
       inputLayoutRegion    = (TextInputLayout) findViewById(R.id.brainlayout_region);
        inputLayoutPostal = (TextInputLayout) findViewById(R.id.brainlayout_postal);
        inputLayoutRouting = (TextInputLayout) findViewById(R.id.brainlayout_routing);

        inputFirstname = (EditText) findViewById(R.id.brainfirstname);
        inputLastname = (EditText) findViewById(R.id.brainlastname);
        inputDob = (EditText) findViewById(R.id.braindob);
        inputAddress = (EditText) findViewById(R.id.brainaddress);
        inputAccount = (EditText) findViewById(R.id.brainaccount);
        inputLocality = (EditText) findViewById(R.id.brainlocality);
        inputRegion = (EditText) findViewById(R.id.brainregion);
        inputPostal = (EditText) findViewById(R.id.brainpostal);
        inputRouting = (EditText) findViewById(R.id.brainrouting);



        inputDob.setOnClickListener(this);


        update = (Button) findViewById(R.id.update);


        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (!validateFname()) {
                    return;
                }

                if (!validateLname()) {
                    return;
                }
                if (!validateDob()) {
                    return;
                }

                if (!validateAddress()) {
                    return;
                }
                if (!validateAccount()) {
                    return;
                }


                if (!validateLocality()) {
                    return;
                }

                if (!validateRegion()) {
                    return;
                }

                if (!validatePostal()) {
                    return;
                }

                if (!validateRouting()) {
                    return;
                }





                firstname = inputFirstname.getText().toString();
                lastname = inputLastname.getText().toString();
                dob = inputDob.getText().toString();
                address = inputAddress.getText().toString();
                account = inputAccount.getText().toString();
                locality = inputLocality.getText().toString();
                region = inputRegion.getText().toString();
                postal = inputPostal.getText().toString();
                routing = inputRouting.getText().toString();
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
                                    profile_userfname = profile_jsonobj.getString("firstname");
                                    profile_userlname = profile_jsonobj.getString("lastname");
                                    profile_userdob = profile_jsonobj.getString("dob");
                                    profile_useraddress = profile_jsonobj.getString("staddress");
                                    profile_useraccountno = profile_jsonobj.getString("accountno");
                                    profile_userlocality = profile_jsonobj.getString("locality");
                                    profile_userregion = profile_jsonobj.getString("region");
                                    profile_userpostal = profile_jsonobj.getString("postalcode");
                                    profile_userrouting = profile_jsonobj.getString("routingno");


                                    if (profile_userfname.equals("null")) {
                                        inputFirstname.setText("");
                                    } else {

                                        inputFirstname.setText(profile_userfname);
                                    }

                                    if (profile_userlname.equals("null")) {
                                        inputLastname.setText("");
                                    } else {
                                        inputLastname.setText(profile_userlname);
                                    }

                                    if (profile_userdob.equals("null")) {
                                        inputDob.setText("");
                                    } else {
                                        inputDob.setText(profile_userdob);
                                    }


                                    if (profile_useraddress.equals("null")) {
                                        inputAddress.setText("");
                                    } else {
                                        inputAddress.setText(profile_useraddress);
                                    }


                                    if (profile_useraccountno.equals("null")) {
                                        inputAccount.setText("");
                                    } else {
                                        inputAccount.setText(profile_useraccountno);
                                    }



                                    if (profile_userlocality.equals("null")) {
                                        inputLocality.setText("");
                                    } else {
                                        inputLocality.setText(profile_userlocality);
                                    }

                                    if (profile_userregion.equals("null")) {
                                        inputRegion.setText("");
                                    } else {
                                        inputRegion.setText(profile_userregion);
                                    }

                                    if (profile_userpostal.equals("null")) {
                                        inputPostal.setText("");
                                    } else {
                                        inputPostal.setText(profile_userpostal);
                                    }

                                    if (profile_userrouting.equals("null")) {
                                        inputRouting.setText("");
                                    } else {
                                        inputRouting.setText(profile_userrouting);
                                    }


                                    System.out.println("this is from"+from);
                                    Log.d("OUTPUT IS", profile_status);
                                    if (profile_status.matches("Success")) {




                                        inputFirstname.setEnabled(true);
                                       inputLastname.setEnabled(true);
                                        inputDob.setEnabled(true);
                                        inputAddress.setEnabled(true);
                                        inputAccount.setEnabled(true);
                                        inputLocality.setEnabled(true);
                                        inputRegion.setEnabled(true);
                                        inputPostal.setEnabled(true);
                                        inputRouting.setEnabled(true);


                                    } else if (profile_status.matches("Invalid")) {
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


    public void onClick(View v) {
        showDialog(0);
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            inputDob.setText(selectedDay + "/" + (selectedMonth + 1) + "/"
                    + selectedYear);
        }
    };




    private boolean validateFname() {
        if (inputFirstname.getText().toString().trim().isEmpty()) {
            inputLayoutFirstname.setError(getString(R.string.err_msg_fname));
            requestFocus(inputFirstname);
            return false;
        }
        else if (!inputFirstname.getText().toString().matches( "[a-zA-z]+([ '-][a-zA-Z]+)*" )  ) {

            inputLayoutFirstname.setError(getString(R.string.err_msg_fnamenum));
            requestFocus(inputFirstname);
            return false;

        }

        else {
            inputLayoutFirstname.setErrorEnabled(false);

            return true;
        }


    }

    private boolean validateLname() {
        if (inputLastname.getText().toString().trim().isEmpty()) {
            inputLayoutLastname.setError(getString(R.string.err_msg_lname));
            requestFocus(inputLastname);
            return false;
        }
        else if (!inputLastname.getText().toString().matches( "[a-zA-z]+([ '-][a-zA-Z]+)*" ) ) {

            inputLayoutLastname.setError(getString(R.string.err_msg_fnamenum));
            requestFocus(inputLastname);
            return false;

        }

        else {
            inputLayoutLastname.setErrorEnabled(false);

            return true;
        }


    }

    private boolean validateDob() {
        if (inputDob.getText().toString().trim().isEmpty()) {
            inputLayoutDob.setError(getString(R.string.err_msg_dob));
            requestFocus(inputDob);
            return false;
        } else {
            inputLayoutDob.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAddress() {
        if (inputAddress.getText().toString().trim().isEmpty()) {
            inputLayoutAddress.setError(getString(R.string.err_msg_address));
            requestFocus(inputAddress);
            return false;
        } else {
            inputLayoutAddress.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAccount() {
        if (inputAccount.getText().toString().trim().isEmpty()) {
            inputLayoutAccount.setError(getString(R.string.err_msg_account));
            requestFocus(inputAccount);
            return false;
        } else {
            inputLayoutAccount.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLocality() {
        if (inputLocality.getText().toString().trim().isEmpty()) {
            inputLayoutLocality.setError(getString(R.string.err_msg_locality));
            requestFocus(inputLocality);
            return false;
        } else {
            inputLayoutLocality.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateRegion() {
        if (inputRegion.getText().toString().trim().isEmpty()) {
            inputLayoutRegion.setError(getString(R.string.err_msg_region));
            requestFocus(inputRegion);
            return false;
        } else {
            inputLayoutRegion.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePostal() {
        if (inputPostal.getText().toString().trim().isEmpty()) {
            inputLayoutPostal.setError(getString(R.string.err_msg_postal));
            requestFocus(inputPostal);
            return false;
        } else {
            inputLayoutPostal.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateRouting() {
        if (inputRouting.getText().toString().trim().isEmpty()) {
            inputLayoutRouting.setError(getString(R.string.err_msg_routing));
            requestFocus(inputRouting);
            return false;
        } else {
            inputLayoutRouting.setErrorEnabled(false);
        }

        return true;
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
                case R.id.brainlastname:
                    validateLname();
                    break;
                case R.id.braindob:
                    validateDob();
                    break;
                case R.id.brainaddress:
                    validateAddress();
                    break;
                case R.id.brainaccount:
                    validateAccount();
                    break;

                case R.id.brainlocality:
                    validateLocality();
                    break;

                case R.id.brainregion:
                    validateRegion();
                    break;
                case R.id.brainpostal:
                    validatePostal();
                    break;
                case R.id.brainrouting:
                    validateRouting();
                    break;
            }
        }
    }




    public void done()
    {

        if (address!=null)
        {
            address=address.replaceAll(" ","%20");
            address = address.replace("#","%40");
        }

        if (region!=null)
        {
            region=region.replaceAll(" ","%20");
            region = region.replace("#","%40");
        }
        final String url=Liveurl+"braintreesubmerchant?user_id="+User_id+"&firstname="+firstname+"&lastname="+lastname+"&dob="+dob+"&staddress="
                +address+"&accountno="+account+"&locality="+locality
            +"&region="+region+"&postalcode="+postal+"&routingno="+routing;
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

                                   String  submerchantaccountid= edprofile_jsonobj.getString("submerchantaccountid");
                                    Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();

                                    if(from.equals("slide")||from=="slide") {
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
                                    else
                                    {
                                        Intent fare=new Intent(getApplicationContext(),FareActivity.class);

                                        fare.putExtra("userid", User_id);
                                        fare.putExtra("tripid", tripids);
                                        fare.putExtra("autotripid", autotripids);
                                        fare.putExtra("riderid",riderid);
                                        fare.putExtra("fbuserproimg",fbuserproimg);
                                        fare.putExtra("whologin",WhoLogin);
                                        fare.putExtra("password", checkpassword);
                                        fare.putExtra("total", totals);
                                        fare.putExtra("drivername",drivernames);
                                        fare.putExtra("driveremail",driveremails);
                                        fare.putExtra("drivermobile",drivermobiles);
                                        fare.putExtra("tax",taxs);
                                        fare.putExtra("wait",waits);
                                        fare.putExtra("pickup",pickups);
                                        fare.putExtra("drop",drops);
                                        fare.putExtra("date",dates);
                                        fare.putExtra("charge",charges);
                                        fare.putExtra("distance",distances);
                                        System.out.println("Fare Review Put String"+fr);
                                        fare.putExtra("fr",fr);
                                        fare.putExtra("paydone",paydone);
                                        fare.putExtra("rating",rating);
                                        fare.putExtra("rideshare",rideshare);
                                        startActivity(fare);
                                        finish();
                                    }
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



        if(from.equals("slide")||from=="slide") {


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
        else
        {
            Intent fare=new Intent(getApplicationContext(),FareActivity.class);

            fare.putExtra("userid", User_id);
            fare.putExtra("tripid", tripids);
            fare.putExtra("autotripid", autotripids);
            fare.putExtra("riderid",riderid);
            fare.putExtra("fbuserproimg",fbuserproimg);
            fare.putExtra("whologin",WhoLogin);
            fare.putExtra("password", checkpassword);
            fare.putExtra("total", totals);
            fare.putExtra("drivername",drivernames);
            fare.putExtra("driveremail",driveremails);
            fare.putExtra("drivermobile",drivermobiles);
            fare.putExtra("tax",taxs);
            fare.putExtra("wait",waits);
            fare.putExtra("pickup",pickups);
            fare.putExtra("drop",drops);
            fare.putExtra("date",dates);
            fare.putExtra("charge",charges);
            fare.putExtra("distance",distances);
            System.out.println("Fare Review Put String"+fr);
            fare.putExtra("fr",fr);
            fare.putExtra("paydone",paydone);
            fare.putExtra("rating",rating);
            fare.putExtra("rideshare",rideshare);
            startActivity(fare);
            finish();
        }
    }



}
