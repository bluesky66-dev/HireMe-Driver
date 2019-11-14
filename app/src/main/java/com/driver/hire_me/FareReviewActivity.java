package com.driver.hire_me;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.adaptor.AppController;
import com.adaptor.TripModel;
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

public class FareReviewActivity extends Activity {

    private Button cancelfarereview;
    private Controller controller;
    private TextView pickup, drop, distance, charge, wait, tax, total, tripid, driverid;
    private TextView date,name,amount,totalAmount,promoAmount;
    private Typeface typeface;
    private Double amountDouble, promoAmountDouble;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private TextView tax_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fare_summary_activity);
        controller = (Controller) getApplicationContext();

        typeface = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_CONDENCE);

        pickup = (TextView) findViewById(R.id.pickuploc);
        pickup.setTypeface(typeface);
        drop = (TextView) findViewById(R.id.droploc);
        drop.setTypeface(typeface);
        date = (TextView) findViewById(R.id.date);
        date.setTypeface(typeface);
        distance = (TextView) findViewById(R.id.distance);
        distance.setTypeface(typeface);
        charge = (EditText) findViewById(R.id.charge);
        amount = (TextView) findViewById(R.id.amount);
        amount.setTypeface(typeface);
        promoAmount = (TextView) findViewById(R.id.promo_amount);
        promoAmount.setTypeface(typeface);
        totalAmount = (TextView) findViewById(R.id.total_amount);
        totalAmount.setTypeface(typeface);
        tax_amount = (TextView) findViewById(R.id.tax_amount);
        tax_amount.setTypeface(typeface);
        tripid = (TextView) findViewById(R.id.tripid);
        tripid.setTypeface(typeface);
        driverid = (TextView) findViewById(R.id.driverid);
        driverid.setTypeface(typeface);
        TextView tvCarName = (TextView) findViewById(R.id.car_name);
        name = (TextView) findViewById(R.id.name);
        name.setTypeface(typeface);
        ImageView ivCarIcon = (ImageView) findViewById(R.id.car_icon);


        final TripModel currentTripModel = controller.getCurrentTripModel();

        SingleObject obj = SingleObject.getInstance();
        ArrayList<CategoryActors> categoryResponseList = obj.driverCarCategoriesParseApi(controller.pref.getCatagoryResponce());
        String catgoryName = "";
        for(CategoryActors catagories:categoryResponseList){
            if(catagories.getCategory_id().equals(currentTripModel.driver.getCategory_id())){
                catgoryName=catagories.getCat_name();
            }
        };

        tvCarName.setText(catgoryName);

        if (currentTripModel != null) {

            switch (currentTripModel.driver.getCategory_id()) {
                case "1":
                    ivCarIcon.setImageResource(R.drawable.hatchback_icon);
                    break;
                case "2":
                    ivCarIcon.setImageResource(R.drawable.sedan_icon);
                    break;
                case "3":
                    ivCarIcon.setImageResource(R.drawable.suv_icon);
                    break;
            }

            NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.icon);

            thumbNail.setImageUrl(Constants.IMAGE_BASE_URL +  currentTripModel.user.getUProfileImagePath(), imageLoader);

            ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    MarkerOptions marker = new MarkerOptions();
                    LatLng pick = new LatLng(Double.parseDouble(currentTripModel.trip.getTrip_scheduled_pick_lat()), Double.parseDouble(currentTripModel.trip.getTrip_scheduled_pick_lng()));
                    LatLng drop = new LatLng(Double.parseDouble(currentTripModel.trip.getTrip_scheduled_drop_lat()), Double.parseDouble(currentTripModel.trip.getTrip_scheduled_drop_lng()));
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


            pickup.setText(currentTripModel.trip.getTrip_to_loc());
            drop.setText(currentTripModel.trip.getTrip_from_loc());

            //to make date formet
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat
                        ("yyyy-MM-dd hh:mm:ss", Locale.US);
                inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

                SimpleDateFormat outputFormat =
                        new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                // Adjust locale and zone appropriately
                Date date1 = inputFormat.parse(currentTripModel.trip.getTrip_created());
                String outputText = outputFormat.format(date1);
                System.out.println(outputText);

                date.setText(outputText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            name.setText(currentTripModel.user.getU_fname() + " " + currentTripModel.user.getU_lname());


            tripid.setText(currentTripModel.trip.getTrip_id());
            driverid.setText(currentTripModel.driver.getDriverId());

            //To evaluate Total Amount
            //Convert amount string to double
            amountDouble = Double.valueOf(currentTripModel.trip.getTrip_pay_amount());
            promoAmountDouble = Double.valueOf(currentTripModel.trip.getTrip_promo_amt());
            Double grandTotal = amountDouble - promoAmountDouble;

            try {
                distance.setText(controller.checkDistanceUnit().equalsIgnoreCase("km")?String.format("%.02f km", (Float.parseFloat(currentTripModel.trip.getTrip_distance().trim()) )):String.format("%.02f mi", (Float.parseFloat(currentTripModel.trip.getTrip_distance().trim())*0621371)));
                amount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(currentTripModel.trip.getTrip_pay_amount())));
                promoAmount.setText(String.format("-"+controller.currencyUnit()+"%.02f", Float.parseFloat(currentTripModel.trip.getTrip_promo_amt())));
                tax_amount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(currentTripModel.trip.getTrip_tax_amt())));
                totalAmount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(String.valueOf(grandTotal))));

            } catch (Exception e) {
                e.printStackTrace();
                total.setText("");
            }
        } else {

            pickup.setText(controller.pref.getTRIP_FROM_LOC());
            drop.setText(controller.pref.getTRIP_TO_LOC());
            date.setText(controller.pref.getTRIP_DROP_TIME());
            try {
                distance.setText(String.format("%.02f mi", (Float.parseFloat(controller.pref.getTRIP_DISTANCE()))));
                distance.setText(controller.checkDistanceUnit().equalsIgnoreCase("km")?String.format("%.02f km", (Float.parseFloat(controller.pref.getTRIP_DISTANCE()) )):String.format("%.02f mi", (Float.parseFloat(controller.pref.getTRIP_DISTANCE())*0621371)));
            } catch (Exception e) {
                distance.setText("");
            }
            tripid.setText(controller.pref.getTRIP_ID());
            driverid.setText(controller.pref.getDRIVER_ID());

        }

        cancelfarereview = (Button) findViewById(R.id.cancelfarereview);
        cancelfarereview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
