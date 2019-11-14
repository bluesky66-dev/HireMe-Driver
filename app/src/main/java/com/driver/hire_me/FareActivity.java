package com.driver.hire_me;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adaptor.AppController;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class FareActivity extends Activity {

    TextView amount, updatesubmerchant;
    String Amount;
    String User_id, fbuserproimg, WhoLogin, checkpassword, tripid, riderid;
    String usermail, usermobile, drivername, drivermobile, tax, wait, pickup = null, drop = null, date, distance, charges, autotripids;
    String promoamounts;
    String visiblestatus = "no";
    String rideshare;
    String fr;
    String status, submerchantids;
    Button mappage, offline, farereview;
    String rating = "0";
    RatingBar rate;
    int count = 0;
    float ratingf;
    protected static final String TAG = "fareactivity";

    public static final String PREFS_NAME = "MyPrefsFile";

    String Liveurl = "";
    String Liveurl1 = "";
    private JSONObject remove_jsonobj;
    private String remove_status;
    String text;

    String newMessage;

    private boolean whilecheck = true;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare);
        getActionBar().hide();


        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl = sharedPreferences.getString("liveurl", null);
        Liveurl1 = sharedPreferences.getString("liveurl1", null);
        System.out.println("The Live URL Is " + Liveurl);

        Intent fare = getIntent();
        User_id = fare.getStringExtra("userid");
        tripid = fare.getStringExtra("tripid");
        autotripids = fare.getStringExtra("autotripid");
        riderid = fare.getStringExtra("riderid");
        fbuserproimg = fare.getStringExtra("fbuserproimg");
        WhoLogin = fare.getStringExtra("whologin");
        checkpassword = fare.getStringExtra("password");
        Amount = fare.getStringExtra("total");

        usermail = fare.getStringExtra("useremail");
        usermobile = fare.getStringExtra("usermobile");
        drivername = fare.getStringExtra("drivername");
        drivermobile = fare.getStringExtra("driverphone");
        tax = fare.getStringExtra("tax");
        wait = fare.getStringExtra("wait");
        pickup = fare.getStringExtra("pickup");
        drop = fare.getStringExtra("drop");
        date = fare.getStringExtra("date");
        charges = fare.getStringExtra("charge");
        distance = fare.getStringExtra("distance");
        rating = fare.getStringExtra("rating");
        rideshare = fare.getStringExtra("rideshare");
        fr = fare.getStringExtra("fr");


        System.out.println("F Auto Tripid" + autotripids);
        System.out.println("F Charge is" + charges);


        System.out.println("brefore send Waiting time" + wait);
        System.out.println("Pickup and Drop Address" + pickup + "sdfhskf" + drop);
        if (pickup != null || pickup != null) {
            pickup = pickup.replaceAll("%20", " ");
        }
        if (drop != null || pickup != null) {
            drop = drop.replaceAll("%20", " ");
        }
        text = "Driver name : " + drivername + "\nPhone Number : " + drivermobile +
                "\n Pickup Location : " + pickup + "\n Drop Location : " + drop + "\n Total Tax : " + tax +
                "\n Total Distance  : " + distance + "\n Waiting Time : " + wait + "\n Total Amount : " + Amount;

        updatesubmerchant = (TextView) findViewById(R.id.updatesubmerchant);

        System.out.println("insite the fare Activity" + Amount);
        amount = (TextView) findViewById(R.id.amount);
        amount.setText("$" + Amount);

        rate = (RatingBar) findViewById(R.id.ratingBar1);
        rate.setEnabled(false);


        updatesubmerchant = (TextView) findViewById(R.id.updatesubmerchant);
        updatesubmerchant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), BrainTreeMain.class);
                i.putExtra("userid", User_id);
                i.putExtra("tripid", tripid);
                i.putExtra("autotripid", autotripids);
                i.putExtra("riderid", riderid);
                i.putExtra("fbuserproimg", fbuserproimg);
                i.putExtra("whologin", WhoLogin);
                i.putExtra("password", checkpassword);

                i.putExtra("useremail", usermail);
                i.putExtra("usermobile", usermobile);
                i.putExtra("drivername", drivername);
                i.putExtra("driverphone", drivermobile);
                i.putExtra("pickup", pickup);
                i.putExtra("drop", drop);
                i.putExtra("tax", tax);
                i.putExtra("distance", distance);
                i.putExtra("wait", wait);
                i.putExtra("date", date);
                i.putExtra("charge", charges);
                System.out.println("SlidemainActivity Waiting time" + wait);
                i.putExtra("total", Amount);
                i.putExtra("visiblestatus", visiblestatus);
                i.putExtra("rating", rating);
                i.putExtra("rideshare", rideshare);
                i.putExtra("from", "fare");
                startActivity(i);
                finish();
            }

        });


        mappage = (Button) findViewById(R.id.map);
        mappage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mHandleMessageReceiver != null) {
                    unregisterReceiver(mHandleMessageReceiver);
                }

                Intent fare = new Intent(getApplicationContext(), SlideMainActivity.class);

                fare.putExtra("userid", User_id);
                fare.putExtra("fbuserproimg", fbuserproimg);
                fare.putExtra("whologin", WhoLogin);
                fare.putExtra("password", checkpassword);
                fare.putExtra("ad", "ad");

                startActivity(fare);
                finish();
            }
        });


        farereview = (Button) findViewById(R.id.farereview);
        farereview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                Intent i = new Intent(getApplicationContext(), FareReviewActivity.class);

                i.putExtra("userid", User_id);
                i.putExtra("tripid", tripid);
                i.putExtra("autotripid", autotripids);
                i.putExtra("riderid", riderid);
                i.putExtra("fbuserproimg", fbuserproimg);
                i.putExtra("whologin", WhoLogin);
                i.putExtra("password", checkpassword);

                i.putExtra("useremail", usermail);
                i.putExtra("usermobile", usermobile);
                i.putExtra("drivername", drivername);
                i.putExtra("driverphone", drivermobile);
                i.putExtra("pickup", pickup);
                i.putExtra("drop", drop);
                i.putExtra("tax", tax);
                i.putExtra("distance", distance);
                i.putExtra("wait", wait);
                i.putExtra("date", date);
                i.putExtra("charge", charges);
                System.out.println("SlidemainActivity Waiting time" + wait);
                i.putExtra("total", Amount);
                i.putExtra("visiblestatus", visiblestatus);
                i.putExtra("rating", rating);
                i.putExtra("rideshare", rideshare);
                startActivity(i);
                finish();

            }
        });

        offline = (Button) findViewById(R.id.offline);
        offline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                System.out.println("INSITE OFFLINE BUTTON CLICK");
                android.support.v7.app.AlertDialog.Builder builder =
                        new android.support.v7.app.AlertDialog.Builder(FareActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Exit");
                builder.setMessage("Do you want Exit?");
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("INSITE OFFLINE BUTTON CLICK YES");

                        loadSavedPreferences();

                        System.out.println("Offline button clicked ");
                        Removedetails();
                        offriderdetails();
                        clearApplicationData();
                        loadSavedPreferences();
                        System.out.println("After Clear Data");
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                        finish();

                    }

                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        System.out.println("INSITE OFFLINE BUTTON CLICK");
                        dialog.dismiss();
                    }
                    //  alertdialog2.cancel();

                });

                builder.show();
            }

        });

        if (fr == null)

        {
            fr = "no";
        }

        System.out.println("FR" + fr);
        if (fr == "yes" || fr.equals("yes"))

        {
            mappage.setVisibility(View.VISIBLE);
            offline.setVisibility(View.VISIBLE);
            farereview.setVisibility(View.VISIBLE);
            visiblestatus = fr;
        } else

        {
            mappage.setVisibility(View.GONE);
            offline.setVisibility(View.GONE);
            farereview.setVisibility(View.GONE);
        }

        if (rating == null)

        {
            rate.setRating(0);
        } else

        {
            ratingf = Float.valueOf(rating);
            rate.setRating(ratingf);
        }

        user();

        GCMRegistrar.checkDevice(this);

        // Make sure the manifest permissions was properly set
        GCMRegistrar.checkManifest(this);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION)

        );


    }

    BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @SuppressWarnings("null")
        @Override
        public void onReceive(Context context, Intent intent) {

            newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
            System.out.println("Before un register New Message form Slide Main Activity" + newMessage);


            System.out.println("After un register New Message form Slide Main Activity" + newMessage);

            if (newMessage != null || newMessage == null) {
                if (newMessage.equals("paid")) {
                    if (rideshare == null) {
                        Paymentmade();
                        mappage.setVisibility(View.VISIBLE);
                        offline.setVisibility(View.VISIBLE);
                        farereview.setVisibility(View.VISIBLE);
                        visiblestatus = "yes";
                    } else {
                        count = count + 1;
                        if (count == 2) {
                            Paymentmade();
                            mappage.setVisibility(View.VISIBLE);
                            offline.setVisibility(View.VISIBLE);
                            farereview.setVisibility(View.VISIBLE);
                            visiblestatus = "yes";
                        }

                    }
                } else if (newMessage.equals("manualpay")) {
                    Paymentmade();
                    mappage.setVisibility(View.VISIBLE);
                    offline.setVisibility(View.VISIBLE);
                    farereview.setVisibility(View.VISIBLE);
                    visiblestatus = "yes";
                } else if (newMessage.equals("promopay")) {
                    Amount = "0.0";
                    amount.setText("$" + Amount);
                    Paymentmade();
                    mappage.setVisibility(View.VISIBLE);
                    offline.setVisibility(View.VISIBLE);
                    farereview.setVisibility(View.VISIBLE);
                    visiblestatus = "yes";
                }
            }
        }
    };


    public void Removedetails() {


        final String url = Liveurl + "update_location?userid=" + User_id + "&lat=" + "" + "&long=" + "" + "&status=" + status;
        System.out.println("URL is" + url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                remove_jsonobj = response.getJSONObject(i);
                                remove_status = remove_jsonobj.getString("status");
                                Log.d("OUTPUT IS", remove_status);
                                if (remove_status.matches("Success")) {


                                    System.out.println("User data will be cleared");

                                } else {

                                    System.out.println("Amount data not cleared");
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

    public void checkuserdetails() {


        final String url = Liveurl + "get_userlocation?driverid=" + User_id;
        System.out.println("URL is" + url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                remove_jsonobj = response.getJSONObject(i);
                                remove_status = remove_jsonobj.getString("status");
                                Log.d("OUTPUT IS", remove_status);
                                if (remove_status.matches("Success")) {


                                    System.out.println("User data will be cleared");
                                    mappage.setVisibility(View.GONE);
                                    offline.setVisibility(View.GONE);
                                    farereview.setVisibility(View.GONE);
                                    whilecheck = true;
                                    visiblestatus = "no";
                                    System.out.println("Visible Status is" + visiblestatus);
                                } else {

                                    System.out.println("Amount data not cleared");
                                    mappage.setVisibility(View.VISIBLE);
                                    offline.setVisibility(View.VISIBLE);
                                    farereview.setVisibility(View.VISIBLE);
                                    whilecheck = false;
                                    visiblestatus = "yes";
                                    System.out.println("Visible Status is" + visiblestatus);

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

    public void DriverStartRate() {


        final String url = Liveurl + "display_star_rate?user_id=" + User_id;
        System.out.println("URL is" + url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                remove_jsonobj = response.getJSONObject(i);
                                remove_status = remove_jsonobj.getString("status");
                                Log.d("OUTPUT IS", remove_status);
                                if (remove_status.matches("success")) {

                                    rating = remove_jsonobj.getString("rate");

                                    ratingf = Float.valueOf(rating);
                                    rate.setRating(ratingf);
                                    System.out.println("Rating" + rating);
                                    System.out.println("Rating float" + ratingf);

                                    submerchantids = remove_jsonobj.getString("submerchantid");
                                    if (submerchantids == "yes" || submerchantids.equals("yes")) {
                                        updatesubmerchant.setVisibility(View.INVISIBLE);
                                    } else {
                                        updatesubmerchant.setVisibility(View.VISIBLE);
                                    }

                                    promoamounts = remove_jsonobj.getString("promoamount");
                                    Double promoamount = Double.valueOf(promoamounts);
                                    if (promoamount != 0) {


                                        amount.setText("$" + promoamounts);
                                    }
                                } else {

                                }
                                System.out.println("After Try");


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

    public void onBackPressed() {

    }

    public void cashpayment() {


        final String url = Liveurl1 + "user_details?riderid=" + riderid;
        System.out.println("URL is" + url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                remove_jsonobj = response.getJSONObject(i);
                                remove_status = remove_jsonobj.getString("status");
                                Log.d("OUTPUT IS", remove_status);
                                if (remove_status.matches("Success")) {
                                    System.out.println("Payment Success");

                                } else {
                                    System.out.println("Payment failed");
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

    public void promopayment() {


        final String url = Liveurl1 + "promo_pay_accept?riderid=" + riderid;
        System.out.println("URL is" + url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                remove_jsonobj = response.getJSONObject(i);
                                remove_status = remove_jsonobj.getString("status");
                                Log.d("OUTPUT IS", remove_status);
                                if (remove_status.matches("Success")) {
                                    System.out.println("Payment Success");

                                } else {
                                    System.out.println("Payment failed");
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

    private void Paymentmade() {

        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(R.string.payment_completed);
        builder.setCancelable(false);
        builder.setInverseBackgroundForced(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (newMessage.equals("manualpay")) {
                    if (net_connection_check()) {
                        cashpayment();
                    }
                } else if (newMessage.equals("promopay")) {
                    if (net_connection_check()) {
                        promopayment();
                    }
                }

                dialog.cancel();
            }
        });
        builder.show();

    }

    public void user() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Insite the Thread While Check" + whilecheck);
                while (whilecheck) {
                    System.out.println("Insite the while While Check" + whilecheck);

                    try {

                        Thread.sleep(10000);

                        runOnUiThread(new Runnable() {
                            @SuppressWarnings("null")
                            @Override
                            public void run() {
                                System.out.println("INSITE THE RUN");

                                if (net_connection_check()) {
                                    DriverStartRate();
                                }

                            }
                        });
                    } catch (InterruptedException e) {

                    }
                }


            }
        })).start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                    startActivity(i, bndlanimation);
                    finish();

                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    private void loadSavedPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();

    }

    public void offriderdetails() {

        final String url = Liveurl + "availability_off?userid=" + User_id + "&status=off";
        System.out.println("URL is" + url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                remove_jsonobj = response.getJSONObject(i);
                                remove_status = remove_jsonobj.getString("status");
                                Log.d("OUTPUT IS", remove_status);

                                if (remove_status.matches("Sucess")) {

                                    System.out.println("Details Removed");
                                } else {

                                    System.out.println("Missing fields");

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

}