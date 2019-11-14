package com.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

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
import com.app.Config;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.driver.hire_me.Controller;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    Controller controller;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

     //   controller.pref.setD_DEVICE_TOKEN(refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Log.d(TAG,"my tokn=====1"+ refreshedToken);
        System.out.println("my device token is.....   " + refreshedToken);


        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        //  controller= (Controller) getApplicationContext();
        Log.d("my tokn=====6", token);
        //   controller.pref.setD_DEVICE_TOKEN(token);

        Log.d(TAG, "pref tokn =" + token);
        //  System.out.println("device token54 = " + controller.pref.getD_DEVICE_TOKEN());
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        Controller controller = (Controller) getApplicationContext();
        if (controller.pref.getIsLogin()) {
            driverTokenUpateProfile(getApplicationContext(), controller, token);
        }
        editor.commit();

    }

    public static void driverTokenUpateProfile(final Context context, Controller controller, String token) {

        try {
            final SingleObject singleObject = SingleObject.getInstance();
            final String driver_apikey = controller.pref.getAPI_KEY();
            final String driverId = singleObject.getDriverId();
            final String driver_device_token = token;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PROFILE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null) {

                                SharedPreferences pref = context.getSharedPreferences(Config.SHARED_PREF, 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("isToken_updated_to Server", true);
                                editor.commit();
                            } else {
                                Toast.makeText(context, "Internet Connection Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //	dialog.dismiss();
                            if (error instanceof NoConnectionError)
                                Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show();
                            else if (error instanceof ServerError) {
                                String d = new String(error.networkResponse.data);
                                try {
                                    JSONObject jso = new JSONObject(d);
                                    //  Toast.makeText(getApplicationContext(), jso.getString("message"), Toast.LENGTH_LONG).show();
                                    // signUpFacebook();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", driver_apikey);
                    params.put("driver_id", driverId);
                    params.put("d_is_available", "1");
                    params.put("d_device_token", driver_device_token);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    500000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        } catch (Exception e) {

        }


    }

}

