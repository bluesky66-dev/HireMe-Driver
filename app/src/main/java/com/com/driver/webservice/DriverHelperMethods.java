package com.com.driver.webservice;

import android.content.Context;
import android.widget.Toast;

import com.adaptor.AppController;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.driver.hire_me.Controller;
import com.google.android.gms.maps.model.LatLng;
import com.grepix.grepixutils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by grepixinfotech on 07/07/16.
 */
public class DriverHelperMethods {

    public interface DriverStatusChangeListener {
        void statusChanged(DriverStatus status, String error);
    }

    public interface WebServiceCallBack {
        void onComplete(Object data, String error);
//        http://52.62.41.72/users/update_location?trip_driver_status=Request&pickuplat=28.535625922785126&pickuplong=77.25953299552202&driverid=577f36dece92700776640059&destaddress=Old Delhi New Delhi Delhi%2C%2520India&message=Request&lat=28.535625922785126&long=77.25953299552202&userid=577ca544ce92701a3764005b
    }

    //  private static  String Liveurl="http://52.62.41.72/drivers/";
    private static String Liveurl = "http://35.160.185.249/Taxi/index.php/userapi/getnearbyuserlists";

    public static void changeDriverStatus(String userId, final DriverStatus status, final DriverStatusChangeListener listener) {
        final String url = "http://52.62.41.72/drivers/driverstatus?userid=" + userId + "&status=" + (status == DriverStatus.ON ? "on" : "off");
        System.out.println("ChangeDriverStatus URL : " + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject driver_jsonobj = response.getJSONObject(i);
                                String driverjson_status = driver_jsonobj.getString("status");
                                if (driverjson_status != null && driverjson_status.matches("Success")) {

                                    listener.statusChanged(status, null);

                                } else {

                                    listener.statusChanged(status, "Data is not cleared please try again");

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.statusChanged(status, "Exception :" + e);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.statusChanged(status, "Data is not cleared please try again");
            }
        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public static void cencelReqIntentionally(String url, final WebServiceCallBack listener) {

        System.out.println(" cencelReqIntentionally:" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {

                                listener.onComplete(response.getJSONObject(0), null);

                            } else {
                                listener.onComplete(null, "No response");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                listener.onComplete(null, "" + error);

            }
        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public static void getNearByUsers(String userId, final WebServiceCallBack listener) {
//        http://52.62.41.72/users/near_by_users?driver_id=57529a0fce92708b41049c9a
        final String url = "http://52.62.41.72/users/near_by_users?driver_id=" + userId;
        System.out.println("GetNearByUsers URL :" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<LatLng> userLatLongs = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jObject = response.getJSONObject(i);
                                String uLat = jObject.getString("lat");
                                String uLong = jObject.getString("long");
                                if (uLat != null && uLong != null) {
                                    try {
                                        LatLng t = new LatLng(Double.parseDouble(uLat), Double.parseDouble(uLong));
                                        userLatLongs.add(t);
                                    } catch (Exception e) {

                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onComplete(null, "Exception :" + e);
                            }
                        }
                        listener.onComplete(userLatLongs, null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onComplete(null, "Data is not cleared please try again");
            }
        });
        Controller.getInstance().addToRequestQueue(movieReq);
    }


    public static void displayPersonalDetails(String userId, final WebServiceCallBack listener) {
        final String url = Liveurl + "display_personal_details?user_id=" + userId;
        System.out.println("DisplayPersonalDetails URL :" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<JSONObject> userLatLongs = new ArrayList<JSONObject>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jObject = response.getJSONObject(i);
                                String ststus = jObject.getString("status");
                                if (ststus.equals("Success")) {
                                    userLatLongs.add(jObject);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        listener.onComplete(userLatLongs, null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onComplete(null, "" + error);
            }
        });
        Controller.getInstance().addToRequestQueue(movieReq);
    }


    public static void displayDetails(String userId, final WebServiceCallBack listener) {
        final String url = Liveurl + "display_details?user_id=" + userId;
        System.out.println("DisplayDetails URL :" + url);
        //http://52.62.41.72/drivers/display_details?user_id=577f36dece92700776640059
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                listener.onComplete(response.getJSONObject(0), null);
                            } else {
                                listener.onComplete(null, "No response");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onComplete(null, "" + error);
            }
        });
        Controller.getInstance().addToRequestQueue(movieReq);
    }

    /**
     * @param listener
     */
    public static void acceptRequest(HashMap<String, String> params, final WebServiceCallBack listener) {

        final String url = Utils.buildURI(Liveurl + "accept", params).toString();
        System.out.println("AcceptRequest URL :" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                listener.onComplete(response.getJSONObject(0), null);
                            } else {
                                listener.onComplete(null, "No response");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onComplete(null, "" + error);
            }
        });
        Controller.getInstance().addToRequestQueue(movieReq);
    }


    public static void drivercancel_request(HashMap<String, String> params, final WebServiceCallBack listener) {

        final String url = Utils.buildURI(Liveurl + "drivercancel_request", params).toString();
        System.out.println("AcceptRequest URL :" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                listener.onComplete(response.getJSONObject(0), null);
                            } else {
                                listener.onComplete(null, "No response");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onComplete(null, "" + error);
            }
        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    public static void cancelreq_intentionally(HashMap<String, String> params, final WebServiceCallBack listener) {

        final String url = Utils.buildURI(Liveurl + "cancelreq_intentionally", params).toString();
        System.out.println("AcceptRequest URL :" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                listener.onComplete(response.getJSONObject(0), null);
                            } else {
                                listener.onComplete(null, "No response");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onComplete(null, "" + error);
            }
        });
        Controller.getInstance().addToRequestQueue(movieReq);
    }

    /**
     * @param params
     * @param listener
     */
    public static void beginTrip(HashMap<String, String> params, final WebServiceCallBack listener) {
        final String url = Utils.buildURI(Liveurl + "begin_trip", params).toString();
        System.out.println("beginTrip URL :" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                listener.onComplete(response.getJSONObject(0), null);
                            } else {
                                listener.onComplete(null, "No response");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onComplete(null, "" + error);
            }
        });
        Controller.getInstance().addToRequestQueue(movieReq);
    }


    public static void update_amount(String url, final WebServiceCallBack listener) {

        System.out.println("beginTrip URL :" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                listener.onComplete(response.getJSONObject(0), null);
                            } else {
                                listener.onComplete(null, "No response");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onComplete(null, "" + error);
            }
        });
        Controller.getInstance().addToRequestQueue(movieReq);
    }


    public static void getUserLocation(String userId, final WebServiceCallBack listener) {
        final String url = Liveurl + "get_userlocation?driverid=" + userId;
        System.out.println("GetUserLocation URL :" + url);
//        http://52.62.41.72/drivers/get_userlocation?driverid=577f36dece92700776640059
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                listener.onComplete(response.getJSONObject(0), null);
                            } else {
                                listener.onComplete(null, "No response");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onComplete(null, "" + error);
            }
        });
        Controller.getInstance().addToRequestQueue(movieReq);
    }


    public static LatLng getDroploc(String userID) {
        //        [{"status":"Success","droplat":"28.6139391","droplong":"77.2090212"}]
        String response = callGetMethod(Liveurl + "get_droploc?userid=" + userID);
        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("status").matches("Success")) {
                        String droplat = jsonObject.getString("droplat");
                        String droplong = jsonObject.getString("droplong");
                        Double droplatd = Double.parseDouble(droplat);
                        Double droplongd = Double.parseDouble(droplong);
                        return new LatLng(droplatd, droplongd);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {

            }
        }
        return null;
    }

    public static String callGetMethod(String url) {
        try {
            System.out.println("GET Request : " + url);
            URL driver_Url = new URL(url);
            BufferedReader login_reader;
            String str_login = "";
            login_reader = new BufferedReader(new InputStreamReader(driver_Url.openStream()));
            String driver_inputline = "";
            while ((driver_inputline = login_reader.readLine()) != null) {
                str_login += driver_inputline;
            }
            System.out.println("GET Request : " + str_login);
            return str_login;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface DeviceTokenServiceListener {
        void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error);
    }

    public static void excuteRequest(final Context contaxt, final Map<String, String> params,final String url, final DeviceTokenServiceListener listener) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onUpdateDeviceTokenOnServer(response, response != null, null);
                        System.out.println("Url:"+url +"\nSuccess : " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("VolleyError : " + error);
                        if (error instanceof NoConnectionError)
                            Toast.makeText(contaxt, "No internet available", Toast.LENGTH_SHORT).show();
                        else if (error instanceof ServerError) {

                            String d = new String(error.networkResponse.data);
                            try {
                                JSONObject jso = new JSONObject(d);
                                String message=jso.getString("message");
//                                 if(message.equalsIgnoreCase("Driver is not found"))
//                                 {
//                                     Toast.makeText(contaxt, jso.getString("Wrong password. Try again."), Toast.LENGTH_LONG).show();
//                                 }
//                                else
//                                 {
                                Toast.makeText(contaxt, jso.getString("message"), Toast.LENGTH_LONG).show();
//                                 }
                            } catch (JSONException e) {
                                listener.onUpdateDeviceTokenOnServer(null, false, error);
                                e.printStackTrace();
                            }
                        }
                        listener.onUpdateDeviceTokenOnServer(null, false, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(contaxt);
        requestQueue.add(stringRequest);
    }


}
