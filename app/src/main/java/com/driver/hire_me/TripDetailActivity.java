package com.driver.hire_me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adaptor.AppController;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.com.driver.webservice.CategoryActors;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.fonts.Fonts;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by test on 2/4/16.
 */
public class TripDetailActivity extends Activity {

    Button recancel;
    TextView tripempty;
    private static final String TAG = TripDetailActivity.class.getSimpleName();
    String name, amount, time, pickup, drop, distance, date, promoAmount;
    private TextView namy, amounty, timy, picky, dropy, disty, promoAmounty;
    private TextView tvName, tvAmount, tvTime, tvPick, tvDrop, tvDistance, totalAmount;
    private Typeface typeface;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Controller controller;
    private Double amountDouble, promoAmountDouble;
    private ImageView cancel_icon;
    private TextView tax_amount;
    private String tax;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripdetail);

        controller = (Controller) getApplicationContext();

        typeface = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_CONDENCE);
        getViewId();

        getActionBar().hide();
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        recancel = (Button) findViewById(R.id.recancel);
        namy = (TextView) findViewById(R.id.name);
        TextView title = (TextView) findViewById(R.id.textView14);
        namy.setTypeface(typeface);
        title.setTypeface(typeface);
        amounty = (TextView) findViewById(R.id.amount);
        amounty.setTypeface(typeface);
        timy = (TextView) findViewById(R.id.detaildate);
        cancel_icon = (ImageView) findViewById(R.id.canceled_icon);
        timy.setTypeface(typeface);
        picky = (TextView) findViewById(R.id.detailpickup1);
        picky.setTypeface(typeface);
        dropy = (TextView) findViewById(R.id.detaildrop1);
        dropy.setTypeface(typeface);
        disty = (TextView) findViewById(R.id.detaildistance1);
        disty.setTypeface(typeface);
        tax_amount = (TextView) findViewById(R.id.tax_amount);
        tax_amount.setTypeface(typeface);
        tripempty = (TextView) findViewById(R.id.tripempty);
        tripempty.setTypeface(typeface);
        promoAmounty = (TextView) findViewById(R.id.promo_amount);
        totalAmount = (TextView) findViewById(R.id.total_amount);
        tripempty.setTypeface(typeface);
        TextView tvCarName = (TextView) findViewById(R.id.car_name);
        ImageView ivCarIcon = (ImageView) findViewById(R.id.car_icon);

        final Intent i = getIntent();

        if(i.getStringExtra("trip_status").equals("driver_cancel_at_pickup")||i.getStringExtra("trip_status").equals("driver_cancel_at_drop"))
        {
            cancel_icon.setVisibility(View.VISIBLE);
        }else {
            cancel_icon.setVisibility(View.GONE);
        }

        SingleObject obj = SingleObject.getInstance();
        ArrayList<CategoryActors> categoryResponseList = obj.driverCarCategoriesParseApi(controller.pref.getCatagoryResponce());
        String catgoryName = "";
        for(CategoryActors catagories:categoryResponseList){
            if(catagories.getCategory_id().equals(i.getStringExtra("cat_id"))){
                catgoryName=catagories.getCat_name();
            }
        };

        tvCarName.setText(catgoryName);

        switch (Integer.parseInt(i.getStringExtra("cat_id"))) {
            case 1:
               // tvCarName.setText("Hatchback");
                ivCarIcon.setImageResource(R.drawable.hatchback_icon);
                break;
            case 2:
               // tvCarName.setText("Sedan");
                ivCarIcon.setImageResource(R.drawable.sedan_icon);
                break;
            case 3:
               // tvCarName.setText("SUV");
                ivCarIcon.setImageResource(R.drawable.suv_icon);
                break;
        }


        NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.icon);

        thumbNail.setImageUrl(Constants.IMAGE_BASE_URL + i.getStringExtra("user_image"), imageLoader);

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MarkerOptions marker = new MarkerOptions();
                LatLng pick = new LatLng(Double.parseDouble(i.getStringExtra("pic_lat")), Double.parseDouble(i.getStringExtra("pic_lan")));
                LatLng drop = new LatLng(Double.parseDouble(i.getStringExtra("drop_lat")), Double.parseDouble(i.getStringExtra("drop_lan")));
                marker.position(pick).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_20));
                googleMap.addMarker(marker);
                marker.position(drop).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_20));
                googleMap.addMarker(marker);
                googleMap.getUiSettings().setScrollGesturesEnabled(false);
                googleMap.getUiSettings().setZoomGesturesEnabled(false);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                //the include method will calculate the min and max bound.
                builder.include(pick);
                builder.include(drop);
                LatLngBounds bounds = builder.build();

                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels / 2;
                int padding = (int) (width * 0.30); // offset from edges of the map 10% of screen
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                googleMap.animateCamera(cu);
            }
        });

        name = i.getStringExtra("rname");
        amount = i.getStringExtra("amount");
        time = i.getStringExtra("time");
        pickup = i.getStringExtra("pickup");
        drop = i.getStringExtra("drop");
        distance = i.getStringExtra("distance");
        promoAmount = i.getStringExtra("promo_amount");
        date = i.getStringExtra("date");
        tax= i.getStringExtra("tax_amount");

        //To evaluate Total Amount
        //Convert amount string to double
        amountDouble = Double.valueOf(amount);
        promoAmountDouble = Double.valueOf(promoAmount);
        Double grandTotal = amountDouble - promoAmountDouble;

        try {
            namy.setText(name.trim());
            picky.setText(drop.trim());
            dropy.setText(pickup.trim());
            promoAmounty.setText("-"+String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(promoAmount.trim())));
            tax_amount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(tax.trim())));
            totalAmount.setText(String.format(controller.currencyUnit()+"%.02f", grandTotal));

            try {
                amounty.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(amount.trim())));
               //disty.setText(String.format("%.02f km", (Float.parseFloat(distance.trim()) / 1600.0) * 1000));
                disty.setText(controller.checkDistanceUnit().equalsIgnoreCase("km")?String.format("%.02f km", (Float.parseFloat(distance.trim()) )):String.format("%.02f mi", (Float.parseFloat(distance.trim())*0621371)));
            } catch (Exception e) {
                e.printStackTrace();
                amounty.setText("");
                disty.setText("");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat
                        ("yyyy-MM-dd hh:mm:ss", Locale.US);
                inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

                SimpleDateFormat outputFormat =
                        new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                // Adjust locale and zone appropriately
                Date date1 = inputFormat.parse(date.trim());
                String outputText = outputFormat.format(date1);
                System.out.println(outputText);
                /*  Date date2 = dateFormat.parse(date.trim());
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");*/
                timy.setText(outputText);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        recancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });

    }


    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
//        Intent can = new Intent(getApplicationContext(), SlideMainActivity.class);
//        startActivity(can);
//        finish();
    }

    public boolean net_connection_check() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();

        if (!connection) {

            Toast toast = Toast.makeText(getApplicationContext(), R.string.there_is_no_network_please_connect, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 70);
            toast.show();

        }
        return connection;
    }

    private void getViewId() {
        tvAmount = (TextView) findViewById(R.id.detailamount);
        tvAmount.setTypeface(typeface);
        tvDistance = (TextView) findViewById(R.id.detaildistance);
        tvDistance.setTypeface(typeface);
        tvDrop = (TextView) findViewById(R.id.detaildrop);
        tvDrop.setTypeface(typeface);
        tvName = (TextView) findViewById(R.id.detailname);
        tvName.setTypeface(typeface);
        tvPick = (TextView) findViewById(R.id.detailpickup);
        tvPick.setTypeface(typeface);
        ///   tvTime = (TextView) findViewById(R.id.detaildate);
        //  tvTime.setTypeface(typeface);
    }

}