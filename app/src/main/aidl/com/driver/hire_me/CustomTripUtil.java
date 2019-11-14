package com.driver.hire_me;

import android.content.Context;

import com.adaptor.TripModel;
import com.android.volley.VolleyError;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.grepix.grepixutils.WebServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by devin on 2017-11-30.
 */
public class CustomTripUtil {

    public interface CustomTripCallback {
        public void onCustomTripResponce(boolean isTripComplte, TripModel tripModel, String error);
    }

    public static void startCustomTrip(Context context, TripModel tripModelTemp, double latitude, double longitude, Controller controller, CustomTripCallback customTripCallback) {
        fatchAddress(context,tripModelTemp,latitude,  longitude,controller,customTripCallback);
    }


    private static void fatchAddress(final Context context, final TripModel tripModelTemp, final double lat, final double lng, final Controller controller, final CustomTripCallback customTripCallback) {
        String url = "https://maps.google.com/maps/api/geocode/json?latlng=" +lat+","+lng;
        url = url.replaceAll(" ","%20");
        Map<String,String> params = new HashMap<String, String>();
        WebServiceUtil.excuteRequest(context, params,url, new  WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    if (data!= null) {
                        try {
                            JSONObject result=new JSONObject(data.toString());
                            if( result.has("results") ){

                                JSONArray array = result.getJSONArray("results");
                                if( array.length() > 0 ){
                                    JSONObject place1 = array.getJSONObject(0);
                                    String completeAddress=place1.optString("formatted_address");

                                    if(completeAddress.equals(null)||completeAddress.equals("")) {
                                        if( array.length() > 0 ){
                                            JSONObject place = array.getJSONObject(0);
                                            JSONArray components = place.getJSONArray("address_components");
                                            for( int i = 0 ; i < components.length() ; i++ ){
                                                JSONObject component = components.getJSONObject(i);
                                                JSONArray types = component.getJSONArray("types");
                                                for( int j = 0 ; j < types.length() ; j ++ ){
                                                    if( types.getString(j).equals("locality") ){
                                                       String locationAddress = component.getString("long_name");
                                                        StartTrip(context, tripModelTemp, lat,lng, controller, customTripCallback,locationAddress);
                                                    }
                                                }
                                            }
                                        }
                                    }else{

                                        String  locationAddress =completeAddress;
                                        StartTrip(context, tripModelTemp, lat,lng, controller, customTripCallback,locationAddress);
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            fatchAddress(context, tripModelTemp, lat,lng, controller, customTripCallback);
                        }

                    }else{
                        fatchAddress(context, tripModelTemp, lat,lng, controller, customTripCallback);
                    }
                } else {
                    if (error != null) {
                        fatchAddress(context, tripModelTemp, lat,lng, controller, customTripCallback);
                    }
                }
            }
        });

    }


    private static void StartTrip(final Context context, final TripModel tripModelTemp, final double lat, final double lng, final Controller controller, final CustomTripCallback customTripCallback, String locationAddress) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String tripdate = dateFormat.format(date);
        Map<String,String> params = new HashMap<String, String>();
        params.put("trip_scheduled_pick_lat",String.valueOf(lat));
        params.put("trip_scheduled_pick_lng",String.valueOf(lng));
        params.put("trip_scheduled_drop_lng",String.valueOf(lat));
        params.put("trip_scheduled_drop_lat",String.valueOf(lng));
        params.put("trip_status","Request");
        params.put("api_key",controller.pref.getAPI_KEY());
        params.put("driver_id",controller.pref.getDRIVER_ID());
        params.put("user_id","157");
        params.put("trip_from_loc",locationAddress);
        params.put("trip_date",tripdate);
        params.put("trip_searched_addr","No Destination Address for Manual Trip");
        params.put("trip_search_result_addr","No Destination Address for Manual Trip");
        params.put("trip_to_loc","No Destination Address for Manual Trip");
        params.put("trip_distance","0.0");
        params.put("trip_pay_amount","0.0");
        WebServiceUtil.excuteRequest(context, params, Constants.Urls.URL_CREATE_TRIP, new  WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    if (data!= null) {
                        SingleObject obj = SingleObject.getInstance();
                        controller.pref.setTIME_DISTANCE_VIEW(false);
                        boolean isParseRe = obj.parsingTripResponse(data.toString(),tripModelTemp);
                        if (isParseRe) {
                            customTripCallback.onCustomTripResponce(true,tripModelTemp,null);
                        }else{
                            customTripCallback.onCustomTripResponce(false,null,"Unkonown Error");
                        }
                    }else {
                        customTripCallback.onCustomTripResponce(false,null,"Unkonown Error");
                    }
                } else {
                    if (error != null) {
                        customTripCallback.onCustomTripResponce(false,null,error.getMessage());
                }
                }
            }
        });

    }

}
