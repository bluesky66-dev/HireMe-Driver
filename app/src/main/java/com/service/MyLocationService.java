package com.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
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
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.driver.hire_me.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by roberto on 9/29/16.
 */

public class MyLocationService extends Service {
    private static final String TAG = "MyLocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private Controller controller;
    private Intent intent;
    public static String str_receiver2 = "servicetutorial.service.receiver";

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            fn_update(location);
            double longitude=location.getLongitude();
            double latitude=location.getLatitude();
            Toast.makeText(MyLocationService.this,longitude + "yes" + latitude + " ",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    /*
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    */

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {

        Log.e(TAG, "onCreate");

        super.onCreate();

        controller = (Controller) getApplication();

    /*  mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation()  , 5, notify_interval);*/
        intent = new Intent(str_receiver2);


        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        /*try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[1]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }*/
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: "+ LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void fn_update(Location location) {

        intent.putExtra("latutide", location.getLatitude() + "");
        intent.putExtra("longitude", location.getLongitude() + "");
        calculateDistance(location.getLatitude(), location.getLongitude());
        intent.putExtra("distance_cover", distanceCover + "");
        sendBroadcast(intent);
    }


    double lastLat = 0.0;
    double lastLong = 0.0;
    double distanceCover = 0.0;

    private void calculateDistance(double latitude, double longitude) {


        if (lastLat == 0.0 || lastLong == 0.0) {
            lastLat = latitude;
            lastLong = longitude;
            return;
        } else {
            if (lastLat == latitude && lastLong == longitude) {
                return;
            } else {
                Location locationA = new Location("point A");
                locationA.setLatitude(lastLat);
                locationA.setLongitude(lastLong);
                Location locationB = new Location("point B");
                locationB.setLatitude(latitude);
                locationB.setLongitude(longitude);
                double distance = locationA.distanceTo(locationB);

                if (distance > 50 || distanceCover == 0.0) {
                    if (controller.pref.getTRIP_STATUS2().equals("begin")) {
                        double calDistance = controller.pref.getCalCulatedDistance();
                        if (calDistance > 0) {
                            distanceCover = calDistance;
                        }
                        double km = distance / 1000;
                        distanceCover = distanceCover + km;
                        controller.pref.setCalCulatedDistance(distanceCover);
                    } else {
                        distanceCover = 0;
                        controller.pref.setCalCulatedDistance(0.0);
                    }


                    lastLat = latitude;
                    lastLong = longitude;

                    updateDriverProfileApi(latitude, longitude);
                }
            }
        }


    }


    boolean isUpdate = false;

    public void updateDriverProfileApi(double lat, double lng) {
        try {
            if (isUpdate) {
                return;
            } else {
                isUpdate = true;
                // final Controller controller= (Controller) context;
                SingleObject singleObject = SingleObject.getInstance();

                final String driver_apikey = controller.pref.getAPI_KEY();
                final String driverId = controller.pref.getDRIVER_ID();
                // final String driverId=singleObject.getDriverId();

                final String d_lat = Double.toString(lat);
                final String d_lng = Double.toString(lng);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PROFILE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                isUpdate = false;

                                //  dialog.dismiss();
                                if (response != null) {
                                    // driverProfileUpdateParsing(response);
                                    System.out.println("Driver Update profile13= " + response);
                                    //      Toast.makeText(getApplicationContext(), "Driver Update Location", Toast.LENGTH_LONG).show();

                          /*  Log.d("UpdateLocation Update profile = ", response);
                            System.out.println("Update profile11= "+ response);
                            SingleObject obj=SingleObject.getInstance();

                          //  controller.setSignInResponse(response);
                            // String signinResponse=controller.getSignInResponse();
                            obj.driverLoginParseApi(response);*/

                                } else {
                                    //   Util.showdialog(getActivity(), "No Network !", "Internet Connection Failed");

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                isUpdate = false;
                                // dialog.dismiss();
                                if (error instanceof NoConnectionError) {
                                    //   Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show();
                                } else if (error instanceof ServerError) {
                                    String d = new String(error.networkResponse.data);
                                    try {
                                        JSONObject jso = new JSONObject(d);
                                        // Toast.makeText(SlideMainActivity.this, jso.getString("message"),Toast.LENGTH_LONG).show();
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
                        params.put("d_lat", d_lat);
                        params.put("d_lng", d_lng);

                        return params;

                    }

                };


                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }


        } catch (Exception e) {

        }
    }
}