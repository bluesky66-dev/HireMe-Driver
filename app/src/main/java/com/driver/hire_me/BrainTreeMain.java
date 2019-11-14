package com.driver.hire_me;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

public class BrainTreeMain extends TabActivity {
    Button back;
    String movefile;

    String Liveurl="";
    String Liveurl1="";

    String status,userid;

    String User_id,fbuserproimg,WhoLogin,checkpassword;
    String from=null,drivernames,driveremails,drivermobiles;
    String tripids,rating,rideshare,riderid;
    String pickups=null,drops=null,dates=null,distances=null,charges=null,waits=null,taxs=null,totals=null,autotripids;
    String fr,paydone;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.braintreemain);
        TabHost tabHost = getTabHost();

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
        movefile = f.getStringExtra("movefile");

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl = sharedPreferences.getString("liveurl", null);
        Liveurl1 = sharedPreferences.getString("liveurl1", null);

        // Tab for Photos
        TabHost.TabSpec photospec = tabHost.newTabSpec("Payout");

        photospec.setIndicator("Payout");
        Intent fare = new Intent(this, BrainTreeDetails.class);
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
        fare.putExtra("charge", charges);
        fare.putExtra("distance",distances);
        System.out.println("Fare Review Put String" + fr);
        fare.putExtra("fr",fr);
        fare.putExtra("paydone",paydone);
        fare.putExtra("rating", rating);
        fare.putExtra("rideshare", rideshare);
        fare.putExtra("from",from);
        photospec.setContent(fare);

        // Tab for Songs
        TabHost.TabSpec songspec = tabHost.newTabSpec("PayPal");
        // setting Title and Icon for the Tab
        songspec.setIndicator("PayPal");
        Intent songsIntent = new Intent(this, BrainTreePaypal.class);
        songsIntent.putExtra("userid", User_id);
        songsIntent.putExtra("tripid", tripids);
        songsIntent.putExtra("autotripid", autotripids);
        songsIntent.putExtra("riderid",riderid);
        songsIntent.putExtra("fbuserproimg",fbuserproimg);
        songsIntent.putExtra("whologin",WhoLogin);
        songsIntent.putExtra("password", checkpassword);
        songsIntent.putExtra("total", totals);
        songsIntent.putExtra("drivername",drivernames);
        songsIntent.putExtra("driveremail",driveremails);
        songsIntent.putExtra("drivermobile",drivermobiles);
        songsIntent.putExtra("tax",taxs);
        songsIntent.putExtra("wait",waits);
        songsIntent.putExtra("pickup",pickups);
        songsIntent.putExtra("drop",drops);
        songsIntent.putExtra("date",dates);
        songsIntent.putExtra("charge", charges);
        songsIntent.putExtra("distance", distances);
        System.out.println("Fare Review Put String" + fr);
        songsIntent.putExtra("fr",fr);
        songsIntent.putExtra("paydone",paydone);
        songsIntent.putExtra("rating", rating);
        songsIntent.putExtra("rideshare", rideshare);
        songsIntent.putExtra("from",from);
        songspec.setContent(songsIntent);


        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab





        back = (Button) findViewById(R.id.pback);


        back.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @SuppressLint("NewApi")
            public void onClick(View v) {
                if(from.equals("slide")||from=="slide") {
                    Intent back = new Intent(getApplicationContext(), SlideMainActivity.class);
                    back.putExtra("userid", User_id);
                    back.putExtra("fbuserproimg", fbuserproimg);
                    System.out.println("EditProfile  Activity Fb Profile imag" + fbuserproimg);
                    back.putExtra("whologin", WhoLogin);
                    back.putExtra("password", checkpassword);
                    back.putExtra("from", from);
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
        });
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
