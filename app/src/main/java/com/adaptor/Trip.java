package com.adaptor;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by grepix on 2/1/2017.
 */
public class Trip implements Serializable {


    public static Trip parseJson(String tripJson) {
        return new Gson().fromJson(tripJson, Trip.class);
    }

    private String trip_id, trip_date, trip_driver_id, trip_user_id, trip_from_loc, trip_to_loc, trip_distance, trip_fare, trip_wait_time;
    private String trip_pickup_time, trip_drop_time, trip_reason, trip_validity, trip_feedback, trip_status, trip_rating, trip_scheduled_pick_lat;
    private String trip_scheduled_pick_lng, trip_actual_pick_lat, trip_actual_pick_lng, trip_scheduled_drop_lat, trip_scheduled_drop_lng;
    private String trip_actual_drop_lat, trip_actual_drop_lng, trip_searched_addr, trip_search_result_addr, trip_pay_mode, trip_pay_amount;
    private String trip_pay_date, trip_pay_status, trip_driver_commision, trip_created, trip_modified;
    private String trip_promo_amt,tax_amt,promo_id,trip_promo_code;;


    public String getTrip_promo_amt() {
        return trip_promo_amt;
    }public String getTrip_promo_id() {
        return promo_id;
    }public String getTrip_promo_code() {
        return trip_promo_code;
    }


    public String getTrip_tax_amt() {
        return tax_amt;
    }


    public String getTrip_id() {
        return trip_id;
    }

    public String getTrip_date() {
        return trip_date;
    }


    public String getTrip_driver_id() {
        return trip_driver_id;
    }


    public String getTrip_user_id() {
        return trip_user_id;
    }


    public String getTrip_from_loc() {
        return trip_from_loc;
    }


    public String getTrip_to_loc() {
        return trip_to_loc;
    }


    public String getTrip_distance() {
        return trip_distance;
    }
    public void setTrip_distance(String trip_distance) {
        this.trip_distance=trip_distance;
    }


    public String getTrip_fare() {
        return trip_fare;
    }


    public String getTrip_wait_time() {
        return trip_wait_time;
    }


    public String getTrip_pickup_time() {
        return trip_pickup_time;
    }


    public String getTrip_drop_time() {
        return trip_drop_time;
    }


    public String getTrip_reason() {
        return trip_reason;
    }


    public String getTrip_validity() {
        return trip_validity;
    }


    public String getTrip_feedback() {
        return trip_feedback;
    }


    public String getTrip_status() {
        return trip_status;
    }


    public String getTrip_rating() {
        return trip_rating;
    }


    public String getTrip_scheduled_pick_lat() {
        return trip_scheduled_pick_lat;
    }


    public String getTrip_scheduled_pick_lng() {
        return trip_scheduled_pick_lng;
    }


    public String getTrip_actual_pick_lat() {
        return trip_actual_pick_lat;
    }


    public String getTrip_actual_pick_lng() {
        return trip_actual_pick_lng;
    }


    public String getTrip_scheduled_drop_lat() {
        return trip_scheduled_drop_lat;
    }


    public String getTrip_scheduled_drop_lng() {
        return trip_scheduled_drop_lng;
    }


    public String getTrip_actual_drop_lat() {
        return trip_actual_drop_lat;
    }

    public String getTrip_actual_drop_lng() {
        return trip_actual_drop_lng;
    }


    public String getTrip_searched_addr() {
        return trip_searched_addr;
    }


    public String getTrip_search_result_addr() {
        return trip_search_result_addr;
    }

    public String getTrip_pay_mode() {
        return trip_pay_mode;
    }


    public String getTrip_pay_amount() {
        return trip_pay_amount;
    }
    public void setTrip_pay_amount(String trip_pay_amount) {
        this.trip_pay_amount=trip_pay_amount;
    }


    public String getTrip_pay_date() {
        return trip_pay_date;
    }


    public String getTrip_pay_status() {
        return trip_pay_status;
    }


    public String getTrip_driver_commision() {
        return trip_driver_commision;
    }


    public String getTrip_created() {
        return trip_created;
    }

    public String getTrip_modified() {
        return trip_modified;
    }


}
