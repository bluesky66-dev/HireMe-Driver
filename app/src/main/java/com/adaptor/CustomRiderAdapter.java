package com.adaptor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.com.driver.webservice.CategoryActors;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.driver.hire_me.Controller;
import com.driver.hire_me.R;
import com.driver.hire_me.TripDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)

public class CustomRiderAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    //   private List<Movie> movieItems;
    private ArrayList<TripModel> movieItems;
    String userid = "";
    String riderid;
    Typeface typeface;
    Controller controller;
    View convertView;
    //   TripHistoryActors m;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    //  public CustomRiderAdapter(Activity activity, List<Movie> movieItems) {
    public CustomRiderAdapter(Activity activity, ArrayList<TripModel> movieItems, Typeface typeface) {

  /*  	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
           userid = sharedPreferences.getString("userid",null);
		System.out.println("userid in shared preferences in custom rider adapter page"+userid);*/
        this.activity = activity;
        this.movieItems = movieItems;
        this.typeface = typeface;
        controller= (Controller) activity.getApplication();

    }

    public void setTripModels(ArrayList<TripModel> movieItems) {
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        System.out.println("list size... " + movieItems.size());
        return movieItems.size();
    }

    @Override
    public TripModel getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder = null;

        //   final TripHistoryActors  m = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviewlist, parent, false);
            mViewHolder = new MyViewHolder();
            //getInitView(convertView,mViewHolder);
            convertView.setTag(mViewHolder);

            mViewHolder.ivCarIcon = (ImageView) convertView.findViewById(R.id.car_icon);
            mViewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.tvName.setTypeface(typeface);
            mViewHolder.tvTime = (TextView) convertView.findViewById(R.id.timestamp);
            mViewHolder.tvTime.setTypeface(typeface);
            mViewHolder.tvAmount = (TextView) convertView.findViewById(R.id.total);
            mViewHolder.tvAmount.setTypeface(typeface);
            mViewHolder.tvCarName = (TextView) convertView.findViewById(R.id.car_name);
            mViewHolder.tvCarName.setTypeface(typeface);
            mViewHolder.tvPickUpLoc = (TextView) convertView.findViewById(R.id.pickup_location);
            mViewHolder.tvPickUpLoc.setTypeface(typeface);
            mViewHolder.tvDropLoc = (TextView) convertView.findViewById(R.id.drop_location);
            mViewHolder.tvDropLoc.setTypeface(typeface);


        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
 /*       if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.reviewlist, parent,false);*/

        if (imageLoader == null)
            // imageLoader = AppController.getInstance().getImageLoader();
            imageLoader = Controller.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.icon);
        RelativeLayout review = (RelativeLayout) convertView.findViewById(R.id.revi);

        final TripModel m = getItem(position);
        riderid = m.trip.getTrip_id();

        Log.d("position = ", String.valueOf(position));
        System.out.println("pos = " + position);
        // System.out.println("userby  after getting====" + m.getTripId());

        mViewHolder.tvName.setText(m.user.getU_fname() + " " + m.user.getU_lname());


        SingleObject obj = SingleObject.getInstance();
        ArrayList<CategoryActors> categoryResponseList = obj.driverCarCategoriesParseApi(controller.pref.getCatagoryResponce());
        String catgoryName = "";
        for(CategoryActors catagories:categoryResponseList){
            if(catagories.getCategory_id().equals(m.driver.getCategory_id())){
                catgoryName=catagories.getCat_name();
            }
        };

        mViewHolder.tvCarName.setText(catgoryName);

        switch (Integer.parseInt(m.driver.getCategory_id())){
            case 1:
             //   mViewHolder.tvCarName.setText("Hatchback");
                mViewHolder.ivCarIcon.setImageResource(R.drawable.hatchback_icon);
                break;
            case 2:
             //   mViewHolder.tvCarName.setText("Sedan");
                mViewHolder.ivCarIcon.setImageResource(R.drawable.sedan_icon);
                break;
            case 3:
              //  mViewHolder.tvCarName.setText("SUV");
                mViewHolder.ivCarIcon.setImageResource(R.drawable.suv_icon);
                break;
        }

        mViewHolder.tvPickUpLoc.setText(m.trip.getTrip_to_loc());
        mViewHolder.tvDropLoc.setText(m.trip.getTrip_from_loc());


        String tripDate =m.trip.getTrip_created();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat
                    ("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

            SimpleDateFormat outputFormat =
                    new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            // Adjust locale and zone appropriately
            Date date1 = inputFormat.parse(tripDate);
            String outputText = outputFormat.format(date1);
            System.out.println(outputText);
            /*  Date date = dateFormat.parse(tripDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");*/
            mViewHolder.tvTime.setText(outputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {

            mViewHolder.tvAmount.setText(String.format(controller.currencyUnit()+"%.02f", Float.parseFloat(m.trip.getTrip_pay_amount())));
        } catch (Exception e) {
            e.printStackTrace();
            mViewHolder.tvAmount.setText("");

        }
        thumbNail.setImageUrl(Constants.IMAGE_BASE_URL + m.user.getUProfileImagePath(), imageLoader);
        // thumbNail.setScaleType(ScaleType.CENTER_CROP);
        review.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent can = new Intent(activity, TripDetailActivity.class);
                can.putExtra("rname", m.user.getU_fname() + " " + m.user.getU_lname());
                can.putExtra("amount", m.trip.getTrip_pay_amount());
                can.putExtra("trip_status", m.trip.getTrip_status());
                can.putExtra("pickup", m.trip.getTrip_from_loc());
                can.putExtra("drop", m.trip.getTrip_to_loc());
                can.putExtra("distance", m.trip.getTrip_distance());
                can.putExtra("promo_amount", m.trip.getTrip_promo_amt());
                can.putExtra("tax_amount", m.trip.getTrip_tax_amt());
                can.putExtra("date", m.trip.getTrip_created());
                can.putExtra("pic_lat", m.trip.getTrip_scheduled_pick_lat());
                can.putExtra("pic_lan", m.trip.getTrip_scheduled_pick_lng());
                can.putExtra("drop_lat", m.trip.getTrip_scheduled_drop_lat());
                can.putExtra("drop_lan", m.trip.getTrip_scheduled_drop_lng());
                can.putExtra("car_name",m.driver.getCar_name());
                can.putExtra("cat_id",m.driver.getCategory_id());
                can.putExtra("user_image",m.user.getUProfileImagePath());
                activity.startActivity(can);
            }
        });

        return convertView;
    }

    private class MyViewHolder {

        TextView tvName, tvTime, tvAmount,tvCarName,tvPickUpLoc,tvDropLoc;
        ImageView ivCarIcon;

    }

}

