package com.com.driver.webservice;

import android.content.Context;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by grepix on 1/4/2017.
 */
public class DriverUpdateLocation {
    Context context;
    Controller controller;
    boolean isUpdate=false;
    public  DriverUpdateLocation(Context context){
        this.context=context;

        controller= (Controller) context.getApplicationContext();
    }

    public void updateDriverProfileApi(double lat, double lng) {

        if(isUpdate){
            return;
        }
        else {
            isUpdate=true;
            // final Controller controller= (Controller) context;
            SingleObject singleObject=SingleObject.getInstance();

            final String driver_apikey=controller.pref.getAPI_KEY();
            final String driverId=controller.pref.getDRIVER_ID();
            // final String driverId=singleObject.getDriverId();

            final String d_lat=Double.toString(lat);
            final String d_lng=Double.toString(lng);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PROFILE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            isUpdate=false;

                            //  dialog.dismiss();
                            if (response != null) {
                                // driverProfileUpdateParsing(response);
                                System.out.println("Driver Update profile13= "+ response);

                          /*  Log.d("UpdateLocation Update profile = ", response);
                            System.out.println("Update profile11= "+ response);
                            SingleObject obj=SingleObject.getInstance();

                          //  controller.setSignInResponse(response);
                            // String signinResponse=controller.getSignInResponse();
                            obj.driverLoginParseApi(response);*/

                            } else {
                                //   Util.showdialog(getActivity(), "No Network !", "Internet Connection Failed");
                                //    Toast.makeText(context, "Internet Connection Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            isUpdate=false;
                            // dialog.dismiss();
                            if(error instanceof NoConnectionError){
                                //   Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show();
                            }

                            else if(error instanceof ServerError){
                                String d= new  String(error.networkResponse.data);
                                try {
                                    JSONObject jso= new JSONObject(d);
                                   // Toast.makeText(context, jso.getString("message"),Toast.LENGTH_LONG).show();
                                    // signUpFacebook();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    })
            {
                @Override
                protected Map<String,String> getParams()throws AuthFailureError {

                    Map<String,String> params = new HashMap<String, String>();
                    params.put("api_key",driver_apikey);
                    params.put("driver_id",driverId);
                    params.put("d_lat",d_lat);
                    params.put("d_lng",d_lng);

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
}
