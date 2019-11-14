package com.driver.hire_me;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adaptor.TripModel;
import com.android.volley.VolleyError;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.DriverConstants;
import com.com.driver.webservice.SingleObject;
import com.custom.CustomProgressDialog;
import com.fonts.Fonts;
import com.grepix.grepixutils.CloudResponse;
import com.grepix.grepixutils.ErrorJsonParsing;
import com.grepix.grepixutils.WebServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by grepix on 12/29/2016.
 */
public class FareSummaryActivity extends Activity {

    private TextView tvFareSummary, tvRiderAmount, tvYourPayment, tvFareReview;
    private LinearLayout linearLayout;
    private RelativeLayout paymentSubLayout;
    private static Button btnHome, btnOffline;
    private Button okPaymentbtn,fare_recieved_cash_button;;
    private TextView tvPaymentText;

    private static boolean notification_come = false;
    Controller controller;
    private TextView tvYourPaymentValue;
    private TextView tvRiderAmountValue;
    private TextView tvPromCodeApplied;
    private TripModel updatedTripModel;

    private Typeface drawerListTypeface;

    String fr;
    private static final String TAG = "FareSummaryActivity.class";
    private SingleObject object;
    private float driver_get_amount;
    Context context;

    CustomProgressDialog progressDialog;

    private String apicalled;

    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fare_summary_layout);

        progressDialog=new CustomProgressDialog(FareSummaryActivity.this);
        updatedTripModel=new TripModel();
        controller = (Controller) getApplicationContext();
        drawerListTypeface = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_CONDENCE);

        controller.pref.setIS_FARESUMMARY_OPEN("yes_open");

        object = SingleObject.getInstance();
        typeface = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_CONDENCE);

        getViewId();



        String trip_pay_amount1 = controller.pref.getTRIP_PAY_AMOUNT();


        try {
            int commision = 0;
            ArrayList<DriverConstants> constantList = controller.getConstantsList();
            for(DriverConstants constants:constantList){
                if(constants.getConstant_key().equals("appicial_commission")){
                  commision=Integer.parseInt(constants.getConstant_value());
                    break;
                }
            }

            float tripPayAmt = Float.parseFloat(trip_pay_amount1);
            driver_get_amount = ((tripPayAmt * (100-commision)) / 100);
            tvRiderAmountValue.setText(String.format(controller.currencyUnit()+"%.02f", driver_get_amount));
            tvYourPaymentValue.setText(String.format(controller.currencyUnit()+"%.02f",tripPayAmt ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        controller.pref.setTRIP_FARE(trip_pay_amount1);

        try {

            boolean layout = object.getfareSummaryLinearLayout();
            if (layout == false) {
                paymentSubLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                tvFareReview.setVisibility(View.GONE);
            } else {

                linearLayout.setVisibility(View.VISIBLE);
                paymentSubLayout.setVisibility(View.VISIBLE);
                tvFareReview.setVisibility(View.VISIBLE);
            }

            String farereview = controller.getFareReviewCalled();
            if (farereview.equalsIgnoreCase("called")) {
                linearLayout.setVisibility(View.VISIBLE);
                tvFareReview.setVisibility(View.VISIBLE);
            } else {
            }



        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        fare_recieved_cash_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payWithCashOnHand();
            }
        });


        tvFareReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleObject obj = SingleObject.getInstance();
                obj.setfareSummaryLinearLayout(false);
                Intent intent = new Intent(getApplicationContext(), FareReviewActivity.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHandleMessageReceiver != null) {
                    unregisterReceiver(mHandleMessageReceiver);
                }
                controller.pref.setIS_FARESUMMARY_OPEN("not_open");
                controller.pref.setTRIP_ID("0"
                );
                timer.cancel();

                Intent intent = new Intent(getApplicationContext(), SlideMainActivity.class);
                startActivity(intent);
                finish();
                linearLayout.setVisibility(View.GONE);
            }
        });

        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHandleMessageReceiver != null) {
                    unregisterReceiver(mHandleMessageReceiver);
                }
                timer.cancel();
                controller.pref.setTRIP_ID("0");
                controller.pref.setIS_FARESUMMARY_OPEN("not_open");
                logoutApi(controller);
                linearLayout.setVisibility(View.GONE);

            }
        });

        //    final ProgressDialog dialog1=showProgress(FareSummaryActivity.this);
        okPaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentSubLayout.setVisibility(View.GONE);

                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    tvPaymentText.setText(R.string.payment_successfully);
                                    paymentSubLayout.setVisibility(View.VISIBLE);
                                    okPaymentbtn.setText("Yes");
                                    okPaymentbtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            controller.setFareReviewCalled("called");
                                            btnOffline.setEnabled(true);
                                            btnOffline.setEnabled(true);
                                            sendNotificationToUser("Paid");
                                            updateTripStatusApi("end", controller.pref.getTRIP_ID());
                                            paymentSubLayout.setVisibility(View.GONE);
                                            object.setfareSummaryLinearLayout(true);

                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                linearLayout.setVisibility(View.VISIBLE);
                                tvFareReview.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }, 4000);


            }
        });


        /***
         call broadcast receiver
         to receive push notification message , and take decision a
         ****/
        registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));
        handleProfileUpdate();

    }

    private void payWithCashOnHand() {
       progressDialog.showDialog();
//        curURL = NSURL(string: "\(trip_url)\(UPDATE_TRIP)?api_key=\(self.appDelegate.apikey)&trip_id=\(self.appDelegate.tripid)&trip_pay_mode=\(pay_type)&trip_pay_status=Paid")!
        HashMap<String, String> params = new HashMap<>();
        params.put("api_key", controller.pref.getAPI_KEY());
        //if (currentTrip != null) {
        params.put(Constants.Keys.TRIP_ID, controller.pref.getTRIP_ID());
        try{
            if (updatedTripModel.trip.getTrip_promo_amt()!=null) {
                if(Float.parseFloat(updatedTripModel.trip.getTrip_promo_amt())>0){
                    params.put("promo_id", updatedTripModel.trip.getTrip_promo_id());
                    params.put("trip_promo_code", updatedTripModel.trip.getTrip_promo_code());
                    params.put("trip_promo_amt", String.format("%.02f",Float.parseFloat(updatedTripModel.trip.getTrip_promo_amt())));
                }}
        }catch (Exception e){
        }
        params.put(Constants.Keys.TRIP_PAY_MODE, "Cash");
        params.put(Constants.Keys.TRIP_PAY_STATUS, "Paid");
        params.put("trip_status", "end");
        params.put("trip_driver_commision",""+driver_get_amount);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        params.put("trip_pay_date", simpleDateFormat.format(new Date()));
        System.out.println("PayWithCashOnHand  Params : " + params);
        WebServiceUtil.excuteRequest(this, params, Constants.Urls.URL_USER_UPDATE_TRIP, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {

                if (isUpdate) {

                    controller.pref.setIS_FARESUMMARY_OPEN("not_open");
                    savePayment();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(FareSummaryActivity.this, "" + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void savePayment() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.Keys.API_KEY,  controller.pref.getAPI_KEY());
        params.put(Constants.Keys.TRIP_ID, controller.pref.getTRIP_ID());
        params.put("pay_mode", "Cash");
        params.put("pay_status", "Paid");
//        2017-01-25 07:02:31
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        params.put("pay_date", simpleDateFormat.format(new Date()));
        float fare=Float.parseFloat(controller.pref.getTRIP_PAY_AMOUNT());
        try{
            if (updatedTripModel.trip.getTrip_promo_amt()!=null) {
                if(Float.parseFloat(updatedTripModel.trip.getTrip_promo_amt())>0){
                    params.put("promo_id", updatedTripModel.trip.getTrip_promo_id());
                    params.put("pay_promo_code", updatedTripModel.trip.getTrip_promo_code());
                    params.put("pay_promo_amt", String.format("%.02f",Float.parseFloat(updatedTripModel.trip.getTrip_promo_amt())));
                }
                fare=fare-Float.parseFloat(updatedTripModel.trip.getTrip_promo_amt());
            }
        }catch (Exception e){

        }

        params.put("pay_amount", ""+fare);
        //  params.put("total_fare", ""+currentTrip.getTripPayAmount());

        System.out.println("PayWithCashOnHand  Params : " + params);
        WebServiceUtil.excuteRequest(this, params, Constants.Urls.URL_PAYMENT_SAVE, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                progressDialog.dismiss();
                if (isUpdate) {
                    controller.pref.setIS_FARESUMMARY_OPEN("not_open");
                    fare_recieved_cash_button.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    tvFareReview.setVisibility(View.VISIBLE);
                } else {

                    Toast.makeText(FareSummaryActivity.this, "" + error, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private Timer timer;

    public void handleProfileUpdate() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                FareSummaryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(controller.pref.getTRIP_ID().equals("0")||controller.pref.getTRIP_ID()=="0"){
                            timer.cancel();
                        }else{
                            final TripModel tripModel = new TripModel();
                            tripModel.driver_apikey = controller.pref.getAPI_KEY();
                            tripModel.tripId = controller.pref.getTRIP_ID();
                            getTrip(tripModel,false);
                        }

                    }
                });
            }
        }, 0, 5000);//put here time 1000 milliseconds=1 second
    }


    public void onBackPressed() {

    }

    private void getViewId() {
        TextView tvFaresummaryTitle = (TextView) findViewById(R.id.tv_title);
        tvFaresummaryTitle.setTypeface(typeface);
        tvFareReview = (TextView) findViewById(R.id.tv_fare_review_id);
        tvFareReview.setTypeface(typeface);
        tvFareReview.setVisibility(View.GONE);

        tvRiderAmount = (TextView) findViewById(R.id.tv_rider_amount);
        tvRiderAmount.setTypeface(typeface);
        tvYourPayment = (TextView) findViewById(R.id.tv_your_payment);
        tvYourPayment.setTypeface(typeface);
        paymentSubLayout = (RelativeLayout) findViewById(R.id.payment_sub_layout);

        fare_recieved_cash_button = (Button) findViewById(R.id.fare_recieved_cash_button);
        okPaymentbtn = (Button) findViewById(R.id.yes_btn1);
        okPaymentbtn.setTypeface(typeface);
        tvPaymentText = (TextView) findViewById(R.id.tv_dialog_title1);
        tvPaymentText.setTypeface(typeface);

        tvRiderAmountValue = (TextView) findViewById(R.id.tv_rider_amount_value);
        tvRiderAmountValue.setTypeface(typeface);
        tvPromCodeApplied = (TextView) findViewById(R.id.tv_promo_code_applied);
        tvPromCodeApplied.setTypeface(typeface);

        tvYourPaymentValue = (TextView) findViewById(R.id.tv_your_payment_value_id);
        tvYourPaymentValue.setTypeface(typeface);

        tvFareSummary = (TextView) findViewById(R.id.tv_fare_summary_id);
        tvFareSummary.setTypeface(typeface);
        linearLayout = (LinearLayout) findViewById(R.id.home_linear_id);
        btnHome = (Button) findViewById(R.id.btn_home_id);
        btnHome.setTypeface(typeface);
        btnOffline = (Button) findViewById(R.id.btn_offline_id);
        btnOffline.setTypeface(typeface);

    }

    private void logoutApi(final Controller controller) {
        progressDialog.showDialog();
        this.controller = controller;
        SingleObject singleObject = SingleObject.getInstance();

        final String driver_apikey = controller.pref.getAPI_KEY();
        final String driverId = singleObject.getDriverId();

        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
        params.put("driver_id", driverId);
        params.put("d_is_available", "0");

        WebServiceUtil.excuteRequest(FareSummaryActivity.this, params, Constants.UPDATE_PROFILE, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                progressDialog.dismiss();
                if (isUpdate) {
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (res.isStatus()) {
                        controller.pref.setSIGN_IN_RESPONSE(response);
                        controller.setSignInResponse(response);
                        SingleObject obj = SingleObject.getInstance();
                        obj.driverUpdateProfileParseApi(response);

                        Intent intent = new Intent(getApplicationContext(),SlideMainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.internet_error, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void showdialog() {
        final Dialog dialog = new Dialog(FareSummaryActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.payment_layout);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        TextView title2 = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        title2.setTypeface(drawerListTypeface);

        //	TextView text = (TextView) dialog.findViewById(R.id.alert_message);
        Button yes = (Button) dialog.findViewById(R.id.yes_btn);
        yes.setTypeface(drawerListTypeface);
        Button no = (Button) dialog.findViewById(R.id.no_btn);
        no.setTypeface(drawerListTypeface);
        title2.setText("Do you want to exit now?");
        //	text.setText(message);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendNotificationToUser("Paid");
                updateTripStatusApi("end", controller.pref.getTRIP_ID());
                dialog.dismiss();

                linearLayout.setVisibility(View.VISIBLE);
                tvFareReview.setVisibility(View.VISIBLE);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void sendNotificationToUser(String notification_message) {

        String u_device_type = controller.pref.getUSER_DEVICE_TYPE();
        Map<String, String> params = new HashMap<String, String>();
        params.put("message", notification_message);
        params.put("trip_id", controller.pref.getTRIP_ID());
        params.put("trip_status", notification_message);
        if (u_device_type.equalsIgnoreCase(Constants.Keys.ANDROID)) {
            params.put(Constants.Keys.ANDROID, controller.pref.getUSER_DEVICE_TOKEN());
        } else {
            params.put(Constants.Keys.IOS, controller.pref.getUSER_DEVICE_TOKEN());
        }
        notificationStatusApi(params, Constants.URL_COMMAN_NOTIFICATION);
    }


    private void notificationStatusApi(final Map<String, String> params, String noti_url) {
        WebServiceUtil.excuteRequest(FareSummaryActivity.this, params, noti_url, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                progressDialog.dismiss();
                if (isUpdate) {
                    if(data!=null){
                        controller.pref.setIS_FARESUMMARY_OPEN("not_open");
                    }else{
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.internet_error, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void updateTripStatusApi(final String trip_status, String trip_id) {
        final TripModel tripModel = new TripModel();
        tripModel.driver_apikey = controller.pref.getAPI_KEY();
        tripModel.tripId = trip_id;
        tripModel.tripStatus = trip_status;

       progressDialog.showDialog();
        WebServiceUtil.excuteRequest(this, tripModel.getParams(), Constants.UPDATE_TRIP_URL, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
              progressDialog.dismiss();
                if (isUpdate) {
                    JSONObject jsonRootObject = null;
                    try {
                        jsonRootObject = new JSONObject(data.toString());
                        int response = jsonRootObject.getInt("response");
                        if (response == 1) {
                            //     update trip successfully
                            controller.pref.setTRIP_STATUS2(trip_status);
                            if(trip_status.equals("Cash")||trip_status.equals("Paid")){
                                controller.pref.setIS_FARESUMMARY_OPEN("not_open");
                            }

                        } else {
                            // not update
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }

    private void getTrip(final TripModel tripModel, final boolean isDialog) {
        if(isDialog){
           progressDialog.showDialog();
        }
        WebServiceUtil.excuteRequest(FareSummaryActivity.this,tripModel.getParamsforGetTrip(), Constants.GET_TRIP_URL, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if(isDialog){
                   progressDialog.dismiss();
                }
                if (isUpdate) {
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (res.isStatus()) {
                        SingleObject obj = SingleObject.getInstance();
                        boolean isParseRe = obj.parsingTripResponse(response, tripModel);
                        if (isParseRe)
                        {
                            controller.pref.setTRIP_STATUS2(tripModel.trip.getTrip_status());
                            controller.setCurrentTripModel(tripModel);
                            controller.pref.setTRIP_DROP_TIME(tripModel.trip.getTrip_drop_time());
                            controller.pref.setTRIP_FARE(tripModel.trip.getTrip_fare());

                            boolean isCash = false;
                            if(tripModel.trip.getTrip_pay_status().equals("Paid")){
                                isCash=true;
                                controller.pref.setIS_FARESUMMARY_OPEN("not_open");
                                timer.cancel();
                            }

                            updatedTripModel=tripModel;

                            String trippayamount = tripModel.trip.getTrip_pay_amount();
                            String trip_promo_amt = tripModel.trip.getTrip_promo_amt();

                            try {
                                float payAmt = Float.parseFloat(trippayamount);
                                float promoAmnt = Float.parseFloat(trip_promo_amt);

                                if (promoAmnt>0) {
                                    tvPromCodeApplied.setText("Promo Code Applied "+controller.currencyUnit() +promoAmnt);
                                    tvPromCodeApplied.setVisibility(View.VISIBLE);
                                    float actual_trip_pay_amount = payAmt-promoAmnt;
                                    String payable_amount =String.format("%.02f ",actual_trip_pay_amount);
                                    tvYourPaymentValue.setText(actual_trip_pay_amount <=payAmt?controller.currencyUnit() +payable_amount:controller.currencyUnit()+payAmt);
                                }else{
                                    tvPromCodeApplied.setVisibility(View.GONE);
                                }
                                if(isCash){
                                    fare_recieved_cash_button.setVisibility(View.GONE);
                                    linearLayout.setVisibility(View.VISIBLE);
                                    tvFareReview.setVisibility(View.VISIBLE);
                                }


                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.internet_error, Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


          if (intent.getAction().equals(com.app.Config.PUSH_NOTIFICATION)) {
                // new push notification is received
                String message3 = intent.getStringExtra("message");
                String newMessage4 = intent.getExtras().getString(Config.EXTRA_MESSAGE);

            }


            Map<String, String> newMessageData = controller.getNotificationMessage();

            String newMessage = newMessageData.get("trip_status");
            if (newMessage.length() > 0 || newMessage != null || !newMessage.equals("null")) {
                if (newMessage.equals("Cash")) {

                    try {
                        final TripModel tripModel = new TripModel();
                        tripModel.driver_apikey = controller.pref.getAPI_KEY();
                        tripModel.tripId = controller.pref.getTRIP_ID();
                        getTrip(tripModel,true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }if (newMessage.equals("accept_payment_promo")){

                    try {
                        final TripModel tripModel = new TripModel();
                        tripModel.driver_apikey = controller.pref.getAPI_KEY();
                        tripModel.tripId = controller.pref.getTRIP_ID();

                        getTrip(tripModel,true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else{
                    final TripModel tripModel = new TripModel();
                    tripModel.driver_apikey = controller.pref.getAPI_KEY();
                    tripModel.tripId = controller.pref.getTRIP_ID();
                    getTrip(tripModel, true);
                    paymentSubLayout.setVisibility(View.VISIBLE);
                }
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mHandleMessageReceiver);
        } catch (Exception e) {
        }
    }

}
