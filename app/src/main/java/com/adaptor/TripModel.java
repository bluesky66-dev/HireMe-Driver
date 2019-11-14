package com.adaptor;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by grepix on 2/1/2017.
 */
public class TripModel implements Serializable {
    /*  Controller controller;
      public  TripModel(Context context){
          controller= (Controller) context.getApplicationContext();
      }
  */
    private String TAG = "TripModel";
    public String driver_apikey;
    public String tripId;
    public String trip_reason = "";
    public String tripStatus;
    public String tripDistance;
    public String taxAmount;
    public String tripAmount;
    public User1 user;
    public Driver1 driver;
    public String pick_time;
    public String drop_time;
    public Trip trip;

    public static TripModel parseJson(String tripJson) throws JSONException {
        TripModel tripModel = new TripModel();
        tripModel.trip = Trip.parseJson(tripJson.toString());
        JSONObject tripJsonObject = new JSONObject(tripJson);
        tripModel.user = User1.parseJson(tripJsonObject.getJSONObject("User").toString());
        tripModel.driver = Driver1.parseJson(tripJsonObject.getJSONObject("Driver").toString());
        return tripModel;
    }

    public Map<String, String> getParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
        params.put("trip_status", tripStatus);
        params.put("trip_id", tripId);

        if (trip_reason != null && trip_reason.length() > 0) {
            params.put("trip_reason", trip_reason);
        }
        if(tripStatus.equals("end")||tripStatus.equals("driver_cancel_at_drop")){
            params.put("trip_distance", tripDistance);
            params.put("trip_pay_amount", tripAmount);
            params.put("trip_drop_time", drop_time);
            params.put("tax_amt", taxAmount);
        }
        if(tripStatus.equals("begin")){
            params.put("trip_pickup_time", pick_time);

        }


        Log.d(TAG, " Update Trip Params : " + params);
        return params;
    }

    public Map<String, String> getUpdateParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
        params.put("trip_status", tripStatus);
        params.put("trip_id", tripId);

        String object = driver_apikey + "  " + tripStatus + " " + tripId;
        Log.d(TAG, object);
        return params;
    }

    public Map<String, String> getParamsforGetTrip() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
        params.put("trip_id", tripId);

        String object = driver_apikey + "  " + tripStatus + " " + tripId;
        Log.d(TAG, object);
        return params;
    }


    @Override
    public String toString() {
        String object = driver_apikey + "  " + tripStatus + " " + tripId;
        Log.d(TAG, object);
        return object;
    }
}
