package com.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

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
import java.util.Timer;


/**
 * Created by devin on 2017-09-08.
 */

public class DriverLocationServices extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 1000;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;
    Controller controller;
    IBinder binder = new LocationBinder();
    private Handler handlerDistance;


    public DriverLocationServices() {

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

   public class LocationBinder extends Binder {
    public    Location getCurrentLocation() {
            return location;

        }

     public    double distanceInKm() {
            return distanceCover;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();

        controller = (Controller) getApplication();

    /*  mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation()  , 5, notify_interval);*/
        handlerDistance = new Handler();
        handlerDistance.postDelayed(handlerDistanceRunable, 1000);
        intent = new Intent(str_receiver);
        fn_getlocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        fn_update(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isNetworkEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        fn_update(location);
                    }
                }

            }


            if (isGPSEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        fn_update(location);
                    }
                }
            }


        }

    }

    Runnable handlerDistanceRunable = new Runnable() {
        @Override
        public void run() {
           // fn_update(location);
        }
    };

  /*  private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }*/

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
               // double distance = locationA.distanceTo(locationB);

                double distance =distance( lastLat,lastLong,latitude,longitude);
              //  if (distance > 100 || distanceCover == 0.0) {
                    if (controller.pref.getTRIP_STATUS2().equals("begin")) {
                        double calDistance = controller.pref.getCalCulatedDistance();
                        if (calDistance > 0) {
                            distanceCover = calDistance;
                        }
                       // double km = distance / 1000;
                        double km = distance ;
                        distanceCover = distanceCover + km;
                        controller.pref.setCalCulatedDistance(distanceCover);
                    } else {
                        distanceCover = 0;
                        controller.pref.setCalCulatedDistance(0.0);
                    }


                    lastLat = latitude;
                    lastLong = longitude;

                    updateDriverProfileApi(latitude, longitude);
               // }
            }
        }


    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;


        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
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