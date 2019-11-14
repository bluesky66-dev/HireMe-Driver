package com.driver.hire_me;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.adaptor.TripModel;
import com.apiservices.AuthData;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by grepix on 12/5/2016.
 */
public class Pref {

    private SharedPreferences sharedPreferences;

    public Pref(Context context) {
        sharedPreferences = context.getSharedPreferences("hireme", Context.MODE_PRIVATE);
        Set<String> set = new HashSet<String>();

    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPrefrenshes(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public SharedPreferences.Editor edit() {

        return sharedPreferences.edit();
    }


    public final String API_KEY = "api_key";



    public final String CAR_MODEL="car_model";
    public final String IS_PUSH = "is_push";
    public final String IS_LOGIN = "is_login";
    public final String EMAIL = "u_email";
    public String PASSWORD = "d_password";
    public String DRIVER_PROFILE_IMG_PATH = "driver_image";

    // public String SIGN_IN_RESPONSE="sign_in_response";
    public String SIGN_IN_RESPONSE = "sign_in_response";
    public String D_DEVICE_TOKEN = "d_device_token";
    public String D_IS_AVAILABLE = "d_is_available";

    public String UrlForDownloadTask = "url_for_download_task";
    public String TRIP_ID = "trip_id";
    public String TRIP_STATUS2 = "trip_status2";
    public String USER_DEVICE_TYPE = "u_device_type";
    public String USER_DEVICE_TOKEN = "u_device_token";

    public String TRIP_TO_LOC = "trip_to_loc";
    public String TRIP_FROM_LOC = "trip_from_loc";
    public String IS_FARESUMMARY_OPEN = "faresummary_open";

    public String BITMAP_STRING = "bitmap_string";

    //   public String IS_FARESUMMARY_OPEN ;

    public double UPDATE_LAT;
    public double UPDATE_LNG;

    public String Driver_Lat = "d_lat";
    public String Driver_Lng = "d_lng";

    public String TRIP_SCHEDULED_PICK_LNG = "trip_scheduled_pick_lng";
    public String TRIP_SCHEDULED_PICK_LAT = "trip_actual_pick_lat";

    public String TRIP_SCHEDULED_DROP_LNG = "trip_scheduled_drop_lng";
    public String TRIP_SCHEDULED_DROP_LAT = "trip_scheduled_drop_lat";

//    public String TRIP_PAY_MODE="trip_pay_mode";
//    public String TRIP_PAY_AMOUNT="trip_pay_amount";

    public String D_LAT = "d_lat";
    public String D_LNG = "d_lng";

    public String TRIP_DISTANCE = "trip_distance";
    public String TRIP_PICKUP_TIME = "trip_pickup_time";
    public String TRIP_DROP_TIME = "trip_drop_time";
    public String TRIP_WAIT_TIME = "trip_wait_time";
    public String TRIP_REASON = "trip_reason";
    public String TRIP_FARE = "trip_fare";

    public String DRIVER_ID = "driver_id";
    public String FULLBUTTON_CAPTION = "fullbutton_text";
    public String GOBUTTON_CAPTION = "gobutton_text";

    public String TRIP_PAY_MODE = "trip_pay_mode";
    public String TRIP_PAY_AMOUNT = "trip_pay_amount";
    public String TRIP_PAY_STATUS = "trip_pay_status";
    public String TRIP_DRIVER_COMMISSION = "trip_driver_commision";

    public String DESTINATION_VIEW = "dest_view";
    public String LICENSE_BITMAP_TO_STRING_ = "license";
    public String TIME_DISTANCE_VIEW = "time_distance_view";
    public String TRIP_MODIFIED_TIME = "trip_modified";
    public String PROFILE_IMAGE = "profile_img";

    public String TRIP_DISTANCE_CHANGABLE = "distance";
    public String TRIP_DURATION_CHANGABLE = "duration";
    public String TRIP_DISTANCE_CALCULATED = "distance_calculated";
    public String TRIP_START_TIME = "trip_start_time";
    public String TRIP_END_TIME = "trip_end_time";
     public String IS_CALCULATE_TRIP= "trip_is_calculate";
    public String LOCAL_TRIP_STATUS= "local_trip_status";

    private final String CATEGORY_RESPOCE= "catagory_responce";

    public void setCatagoryResponce(String userApikey){
        edit().putString(CATEGORY_RESPOCE, userApikey).commit();
    }
    public String getCatagoryResponce(){
        return sharedPreferences.getString(CATEGORY_RESPOCE,"");
    }


    public void setLocalTripState(String car_model) {
        edit().putString(LOCAL_TRIP_STATUS, car_model).commit();
    }

    public String getLocalTripState() {
        return sharedPreferences.getString(LOCAL_TRIP_STATUS, null);
    }

    public void setCar_Model(String car_model) {
        edit().putString(CAR_MODEL, car_model).commit();
    }

    public String getCar_Model() {
        return sharedPreferences.getString(CAR_MODEL, null);
    }

    public boolean isCalculateDistance() {
        return sharedPreferences.getBoolean(IS_CALCULATE_TRIP,false);
    }

    public void setCalculateDistance(boolean calculateDistance) {
        edit().putBoolean(IS_CALCULATE_TRIP, calculateDistance).commit();
    }

    public boolean isCalculateDistance;

    public  void setCalCulatedDistance(double calculatedDistance){
        edit().putFloat(TRIP_DISTANCE_CALCULATED, (float) calculatedDistance).commit();
    }

    public double getCalCulatedDistance(){
        return sharedPreferences.getFloat(TRIP_DISTANCE_CALCULATED,0);
    }

    public  void setTripStartTime(String trip_start_time){
        edit().putString(TRIP_START_TIME,trip_start_time).commit();
    }

    public String getTripStartTime(){
        return sharedPreferences.getString(TRIP_START_TIME,"");
    }

    public  void setTripEndTime(String trip_start_time){
        edit().putString(TRIP_END_TIME,trip_start_time).commit();
    }

    public String getTripEndTime(){
        return sharedPreferences.getString(TRIP_END_TIME,"");
    }


    public void setIsLogin(boolean isPush) {
        edit().putBoolean(IS_LOGIN,isPush).commit();
    }

    public boolean getIsLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN,false);
    }

    public void setIsPush(boolean isPush) {
        edit().putBoolean(IS_PUSH,isPush).commit();
    }

    public boolean getIsPush() {
        return sharedPreferences.getBoolean(IS_PUSH,false);
    }

    public void setTRIP_DISTANCE_CHANGABLE(String distance_changable) {
        edit().putString(TRIP_DISTANCE_CHANGABLE, distance_changable).commit();
    }

    public String getTRIP_DISTANCE_CHANGABLE() {
        return sharedPreferences.getString(TRIP_DISTANCE_CHANGABLE, null);
    }

    public void setTRIP_DURATION_CHANGABLE(String duration_changable) {
        edit().putString(TRIP_DURATION_CHANGABLE, duration_changable).commit();
    }

    public String getTRIP_DURATION_CHANGABLE() {
        return sharedPreferences.getString(TRIP_DURATION_CHANGABLE, null);
    }

    public void setPROFILE_IMAGE(boolean profile_img) {
        edit().putBoolean(PROFILE_IMAGE, profile_img).commit();
    }

    public boolean getPROFILE_IMAGE() {
        return sharedPreferences.getBoolean(PROFILE_IMAGE, false);
    }

    public void setTRIP_MODIFIED_TIME(String trip_modified_time) {
        edit().putString(TRIP_MODIFIED_TIME, trip_modified_time).commit();
    }

    public String getTRIP_MODIFIED_TIME() {
        return sharedPreferences.getString(TRIP_MODIFIED_TIME, null);
    }

    public void setTIME_DISTANCE_VIEW(boolean time_distance_view) {
        edit().putBoolean(TIME_DISTANCE_VIEW, time_distance_view).commit();
    }

    public boolean getTIME_DISTANCE_VIEW() {
        return sharedPreferences.getBoolean(TIME_DISTANCE_VIEW, false);
    }

    public void setDRIVER_PROFILE_IMG_PATH(String driver_image) {
        edit().putString(DRIVER_PROFILE_IMG_PATH, driver_image).commit();
    }

    public String getDRIVER_PROFILE_IMG_PATH() {
        return sharedPreferences.getString(DRIVER_PROFILE_IMG_PATH, null);
    }

    public void setLICENSE_BITMAP_TO_STRING_(String license_string) {
        edit().putString(LICENSE_BITMAP_TO_STRING_, license_string).commit();
    }

    public String getLICENSE_BITMAP_TO_STRING_() {
        return sharedPreferences.getString(LICENSE_BITMAP_TO_STRING_, null);
    }

    public void setBITMAP_STRING(String bitmap_string) {
        edit().putString(BITMAP_STRING, bitmap_string).commit();
    }

    public String getBITMAP_STRING() {
        return sharedPreferences.getString(BITMAP_STRING, null);
    }


    public void setDESTINATION_VIEW(String destination_view) {
        edit().putString(DESTINATION_VIEW, destination_view).commit();
    }

    public String getDESTINATION_VIEW() {
        return sharedPreferences.getString(DESTINATION_VIEW, null);
    }

    public void setIS_FARESUMMARY_OPEN(String faresummary_open) {
        edit().putString(IS_FARESUMMARY_OPEN, faresummary_open).commit();
    }

    public String getIS_FARESUMMARY_OPEN() {
        return sharedPreferences.getString(IS_FARESUMMARY_OPEN, null);
    }

    public void setTRIP_PAY_MODE(String trip_pay_mode) {
        edit().putString(TRIP_PAY_MODE, trip_pay_mode).commit();
    }

    public String getTRIP_PAY_MODE() {
        return sharedPreferences.getString(TRIP_PAY_MODE, null);
    }

    public void setTRIP_PAY_AMOUNT(String trip_pay_amount) {
        edit().putString(TRIP_PAY_AMOUNT, trip_pay_amount).commit();
    }

    public String getTRIP_PAY_AMOUNT() {
        return sharedPreferences.getString(TRIP_PAY_AMOUNT, null);
    }

    public void setTRIP_PAY_STATUS(String trip_pay_status) {
        edit().putString(TRIP_PAY_STATUS, trip_pay_status).commit();
    }

    public String getTRIP_PAY_STATUS() {
        return sharedPreferences.getString(TRIP_PAY_STATUS, null);
    }

    public void setTRIP_DRIVER_COMMISSION(String trip_driver_commision) {
        edit().putString(TRIP_DRIVER_COMMISSION, trip_driver_commision).commit();
    }

    public String getTRIP_DRIVER_COMMISSION() {
        return sharedPreferences.getString(TRIP_DRIVER_COMMISSION, null);
    }

    public void setFULLBUTTON_CAPTION(String fullbutton_text) {
        edit().putString(FULLBUTTON_CAPTION, fullbutton_text).commit();
    }

    public String getFULLBUTTON_CAPTION() {
        return sharedPreferences.getString(FULLBUTTON_CAPTION, null);
    }

    public void setGOBUTTON_CAPTION(String gobutton_text) {
        edit().putString(GOBUTTON_CAPTION, gobutton_text).commit();
    }

    public String getGOBUTTON_CAPTION() {
        return sharedPreferences.getString(GOBUTTON_CAPTION, null);
    }

    public void setDRIVER_ID(String driver_id) {
        edit().putString(DRIVER_ID, driver_id).commit();
    }

    public String getDRIVER_ID() {
        return sharedPreferences.getString(DRIVER_ID, null);
    }


    public void setTRIP_REASON(String trip_reason) {
        edit().putString(TRIP_REASON, trip_reason).commit();
    }

    public String getTRIP_REASON() {
        return sharedPreferences.getString(TRIP_REASON, null);
    }

    public void setTRIP_WAIT_TIME(String trip_wait_time) {
        edit().putString(TRIP_WAIT_TIME, trip_wait_time).commit();
    }

    public String getTRIP_WAIT_TIME() {
        return sharedPreferences.getString(TRIP_WAIT_TIME, null);
    }

    public void setTRIP_DISTANCE(String trip_distance) {
        edit().putString(TRIP_DISTANCE, trip_distance).commit();
    }

    public String getTRIP_DISTANCE() {
        return sharedPreferences.getString(TRIP_DISTANCE, null);
    }

    public void setTRIP_PICKUP_TIME(String trip_pickup_time) {
        edit().putString(TRIP_PICKUP_TIME, trip_pickup_time).commit();
    }

    public String getTRIP_PICKUP_TIME() {
        return sharedPreferences.getString(TRIP_PICKUP_TIME, null);
    }

    public void setTRIP_DROP_TIME(String trip_drop_time) {
        edit().putString(TRIP_DROP_TIME, trip_drop_time).commit();
    }

    public String getTRIP_DROP_TIME() {
        return sharedPreferences.getString(TRIP_DROP_TIME, null);
    }

    public void setTRIP_FARE(String trip_fare) {
        edit().putString(TRIP_FARE, trip_fare).commit();
    }

    public String getTRIP_FARE() {
        return sharedPreferences.getString(TRIP_FARE, null);
    }


    public void setD_LAT(String d_lat) {
        edit().putString(D_LAT, d_lat).commit();
    }

    public String getD_LAT() {
        return sharedPreferences.getString(D_LAT, null);
    }

    public void setD_LNG(String d_lng) {
        edit().putString(D_LNG, d_lng).commit();
    }

    public String getD_LNG() {
        return sharedPreferences.getString(D_LNG, null);
    }


    public void setTRIP_SCHEDULED_PICK_LNG(String trip_scheduled_pick_lng) {
        edit().putString(TRIP_SCHEDULED_PICK_LNG, trip_scheduled_pick_lng).commit();
    }

    public String getTRIP_SCHEDULED_PICK_LNG() {
        return sharedPreferences.getString(TRIP_SCHEDULED_PICK_LNG,"0.0");
    }

    public void setTRIP_SCHEDULED_PICK_LAT(String trip_scheduled_pick_lat) {
        edit().putString(TRIP_SCHEDULED_PICK_LAT, trip_scheduled_pick_lat).commit();
    }

    public String getTRIP_SCHEDULED_PICK_LAT() {
        return sharedPreferences.getString(TRIP_SCHEDULED_PICK_LAT,"0.0");
    }

    public void setTRIP_SCHEDULED_DROP_LNG(String trip_scheduled_drop_lng) {
        edit().putString(TRIP_SCHEDULED_DROP_LNG, trip_scheduled_drop_lng).commit();
    }

    public String getTRIP_SCHEDULED_DROP_LNG() {
        return sharedPreferences.getString(TRIP_SCHEDULED_DROP_LNG, null);
    }

    public void setTRIP_SCHEDULED_DROP_LAT(String trip_scheduled_drop_lat) {
        edit().putString(TRIP_SCHEDULED_DROP_LAT, trip_scheduled_drop_lat).commit();
    }

    public String getTRIP_SCHEDULED_DROP_LAT() {
        return sharedPreferences.getString(TRIP_SCHEDULED_DROP_LAT, null);
    }


  /*  public void setUPDATE_LAT(Double update_lat)
    {
        edit().putLong(update_lat,update_lat)
    }*/


    public void setDriver_Lat(String d_lat) {
        edit().putString(Driver_Lat, d_lat).commit();
    }

    public String getDriver_Lat() {
        return sharedPreferences.getString(Driver_Lat,"0");
    }

    public void setDriver_Lng(String d_lng) {
        edit().putString(Driver_Lng, d_lng).commit();
    }

    public String getDriver_Lng() {
        return sharedPreferences.getString(Driver_Lng,"0");
    }

    public void setUSER_DEVICE_TOKEN(String user_device_token) {
        edit().putString(USER_DEVICE_TOKEN, user_device_token).commit();
    }

    public String getUSER_DEVICE_TOKEN() {
        return sharedPreferences.getString(USER_DEVICE_TYPE, null);
    }

    public void setTRIP_TO_LOC(String trip_to_loc) {
        edit().putString(TRIP_TO_LOC, trip_to_loc).commit();
    }

    public void saveTripData(TripModel tripModel) {
        try{
            String trip_to_loc = tripModel.trip.getTrip_to_loc();
            setTRIP_TO_LOC(trip_to_loc);
            String loc_from = tripModel.trip.getTrip_from_loc();
            setTRIP_FROM_LOC(loc_from);
            setUSER_DEVICE_TYPE(tripModel.user.getU_device_type());
            setUSER_DEVICE_TOKEN(tripModel.user.getU_device_token());

            String destLat = tripModel.trip.getTrip_scheduled_pick_lat();
            setTRIP_SCHEDULED_PICK_LAT(destLat);
            String destLng = tripModel.trip.getTrip_scheduled_pick_lng();
            setTRIP_SCHEDULED_PICK_LNG(destLng);

            String dropLat = tripModel.trip.getTrip_scheduled_drop_lat();
            setTRIP_SCHEDULED_DROP_LAT(dropLat);
            String droptLng = tripModel.trip.getTrip_scheduled_drop_lng();
            setTRIP_SCHEDULED_DROP_LNG(droptLng);

            String trip_distance = tripModel.trip.getTrip_distance();
            setTRIP_DISTANCE(trip_distance);

            String trip_fare = tripModel.trip.getTrip_fare();
            setTRIP_FARE(trip_fare);

            String trip_wait_time = tripModel.trip.getTrip_wait_time();
            setTRIP_WAIT_TIME(trip_wait_time);

            String trip_pickup_time = tripModel.trip.getTrip_pickup_time();
            setTRIP_PICKUP_TIME(trip_pickup_time);

            String trip_drop_time = tripModel.trip.getTrip_drop_time();
            setTRIP_DROP_TIME(trip_drop_time);

            String trip_modified_time = tripModel.trip.getTrip_modified();
            setTRIP_MODIFIED_TIME(trip_modified_time);

            String trip_pay_mode = tripModel.trip.getTrip_pay_mode();
            setTRIP_PAY_MODE(trip_pay_mode);
            String trip_pay_amount = tripModel.trip.getTrip_pay_amount();
            setTRIP_PAY_AMOUNT(trip_pay_amount);
            String trip_pay_status = tripModel.trip.getTrip_pay_status();
            setTRIP_PAY_STATUS(trip_pay_status);
            String trip_driver_commision = tripModel.trip.getTrip_driver_commision();
            setTRIP_DRIVER_COMMISSION(trip_driver_commision);
            try{
                String originLat = tripModel.driver.getD_lat();
                setD_LAT(originLat);
                String originLng = tripModel.driver.getD_lng();
                setD_LNG(originLng);

                Log.d("Pref", "Loc from =  " + loc_from);
                Log.d("Pref", "u_device_type_is =  " + getUSER_DEVICE_TYPE());
                Log.d("Pref", "u device Token is =  " + getUSER_DEVICE_TOKEN());
                Log.d("Pref", "trip_sheduled_pick_lat =  " + getTRIP_SCHEDULED_PICK_LAT());
                Log.d("Pref", "trip_sheduled_pick_lng =  " + getTRIP_SCHEDULED_PICK_LNG());
                System.out.println("Loc from ::  " + loc_from);
            }catch (Exception e){

            }

        }catch (Exception e){

        }


    }

    public String getTRIP_TO_LOC() {
        return sharedPreferences.getString(TRIP_TO_LOC, null);
    }


    public void setTRIP_FROM_LOC(String trip_from_loc) {
        edit().putString(TRIP_FROM_LOC, trip_from_loc).commit();
    }

    public String getTRIP_FROM_LOC() {
        return sharedPreferences.getString(TRIP_FROM_LOC, null);
    }

    public void setUSER_DEVICE_TYPE(String u_device_type) {
        edit().putString(USER_DEVICE_TYPE, u_device_type).commit();
    }

    public String getUSER_DEVICE_TYPE() {
        return sharedPreferences.getString(USER_DEVICE_TYPE, null);
    }

    public void setTRIP_ID(String trip_id) {
        edit().putString(TRIP_ID, trip_id).commit();
    }

    public String getTRIP_ID() {
        return sharedPreferences.getString(TRIP_ID, "0");
    }

    /*  public void setTRIP_STATUS2(String trip_status){
          edit().putString(TRIP_STATUS2,trip_status).commit();
      }
      public String getTRIP_STATUS2(){
          return sharedPreferences.getString(TRIP_STATUS2,null);
      }*/
 /* public void setTRIP_STATUS2(String trip_status2){
      edit().putString(TRIP_STATUS2, trip_status2).commit();
  }
    public String getTRIP_STATUS2(){
        return sharedPreferences.getString(TRIP_STATUS2,null);
    }
*/
    public void setTRIP_STATUS2(String trip_status2) {
        edit().putString(TRIP_STATUS2, trip_status2).commit();

    }

    public String getTRIP_STATUS2() {
        return sharedPreferences.getString(TRIP_STATUS2,null);
    }

    public void setUrlForDownloadTask(String url_for_download_task) {
        edit().putString(UrlForDownloadTask, url_for_download_task).commit();
    }

    public String getUrlForDownloadTask() {
        return sharedPreferences.getString(UrlForDownloadTask, null);
    }

    public void setD_IS_AVAILABLE(String d_is_available) {
        edit().putString(D_IS_AVAILABLE, d_is_available).commit();
    }

    public String getD_IS_AVAILABLE() {
        return sharedPreferences.getString(D_IS_AVAILABLE, null);
    }

    public void setD_DEVICE_TOKEN(String token) {
        edit().putString(D_DEVICE_TOKEN, token).commit();

    }

    public String getD_DEVICE_TOKEN() {
        return sharedPreferences.getString(D_DEVICE_TOKEN, null);
    }

    public void setSIGN_IN_RESPONSE(String sign_in_response) {
        edit().putString(SIGN_IN_RESPONSE, sign_in_response).commit();
    }

    public String getSIGN_IN_RESPONSE() {
        return sharedPreferences.getString(SIGN_IN_RESPONSE, "");
    }

    public void setPASSWORD(String password) {
        edit().putString(PASSWORD, password).commit();
    }

    public String getPASSWORD() {
        return sharedPreferences.getString(PASSWORD, null);
    }

    public String getEMAIL() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public void saveEmail(String email) {
        edit().putString(EMAIL, email).commit();
    }


    public void setAPI_KEY(String apiKey) {
        AuthData.getInstance().setApiKey(apiKey);
        edit().putString(API_KEY, apiKey).commit();
    }

    public String getAPI_KEY() {
        return sharedPreferences.getString(API_KEY, null);
    }

    public void signOut() {
        setAPI_KEY(null);
        setSIGN_IN_RESPONSE(null);
        setD_DEVICE_TOKEN(null);
    }

}
