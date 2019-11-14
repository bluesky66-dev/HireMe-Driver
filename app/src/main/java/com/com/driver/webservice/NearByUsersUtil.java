package com.com.driver.webservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.adaptor.User1;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.driver.hire_me.Controller;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by grepix on 1/4/2017.
 */
public class NearByUsersUtil  {

        Context context;
        Controller controller;
     //   private ProgressDialog dialog;

        public  interface  WebServiceCallBack
        {
            void  onComplete(Object data, String error);
    //        http://52.62.41.72/users/update_location?trip_driver_status=Request&pickuplat=28.535625922785126&pickuplong=77.25953299552202&driverid=577f36dece92700776640059&destaddress=Old Delhi New Delhi Delhi%2C%2520India&message=Request&lat=28.535625922785126&long=77.25953299552202&userid=577ca544ce92701a3764005b
        }

        public  interface  DriverStatusChangeListener
        {
            void  statusChanged(DriverStatus status, String error);
        }
        public void getNearByUsers(final Context context, double d_lat, double d_lng,final WebServiceCallBack listener ) {
            this.context=context;
            controller= (Controller) context;

       /* final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(R.layout.progress_dialog);*/
            final String lat=Double.toString(d_lat);
            final String lng=Double.toString(d_lng);

            Log.d("latitude5 = ", lat);
            Log.d("longitude5",lng);
            System.out.println("lat5= " + lat + "  long5" + lng);

        final String driver_apikey=controller.pref.getAPI_KEY();
       // StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_NEAR_BY_USERS,
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.GET_NEAR_BY_USERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     //   dialog.dismiss();
                        if (response != null) {
                            SingleObject object=SingleObject.getInstance();
                            ArrayList<User1>   jObject =  object.getNearbyUsersParseApi(response);
                            ArrayList<LatLng> userLatLongs = new ArrayList<>();
                            for (int i = 0; i < jObject.size(); i++) {
                                try {
                                    String uLat=jObject.get(i).getU_lat();
                                    String uLong=jObject.get(i).getU_lng();

                                    System.out.println("User1 Lat = " + uLat);
                                    System.out.println("User1 Lng = " + uLong);

                                    Log.d("u lat2 = ", uLat);
                                    Log.d("u long2 = ", uLong);
                                 // Toast.makeText(context,"Lat = " + uLat + "  Long = " + uLong,Toast.LENGTH_SHORT).show();
                                    if(uLat!=null&&uLong!=null) {
                                        try {
                                            LatLng t = new LatLng(Double.parseDouble(uLat),Double.parseDouble(uLong));
                                            userLatLongs.add(t);
                                        }catch (Exception e)
                                        {

                                            e.printStackTrace();
                                            System.out.println("Near by users65== " + e);

                                        }
                                    }
                                }
                                catch (Exception   e) {
                                  //  e.printStackTrace();
                                  System.out.println("Near by users66== " + e);
                                    listener.onComplete(null, "Exception :" + e);
                                }
                            }
                            System.out.println("userlatlong size22 = " + userLatLongs.size());
                            Log.d("latlong size22 ", String.valueOf(userLatLongs.size()));
                            System.out.println("Near by users67== ");

                            listener.onComplete(userLatLongs,null);
                        } else {
                            System.out.println("Near by users68== ");
                         //   Toast.makeText(context, "Internet Connection Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    //    dialog.dismiss();
                        if(error instanceof NoConnectionError)
                            Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show();
                        else if(error instanceof ServerError){
                            String d= new  String(error.networkResponse.data);
                            try {
                                JSONObject jso= new JSONObject(d);
                           //     Toast.makeText(context, jso.getString("message"),Toast.LENGTH_LONG).show();

                                listener.onComplete(null, "Data is not cleared please try again");
                                // signUpFacebook();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println("Near by users69== " + e);
                            }

                        }
                    }

                })
        {
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                params.put("api_key",driver_apikey);
               // params.put("lat","28.5356459");
                params.put("lat",lat);
                params.put("lng",lng);
              //  params.put("lng","77.2595979");

                return params;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}
