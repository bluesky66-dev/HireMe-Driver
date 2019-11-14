package com.adaptor;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by grepix on 2/1/2017.
 */
public class Driver1 implements Serializable {

    public static Driver1 parseJson(String driverJson) {
        return new Gson().fromJson(driverJson, Driver1.class);
    }

    private String driver_id, api_key, d_name, d_fname, d_lname, d_email, d_license_id, d_rc, car_id, d_device_type, d_device_token, d_rating;
    private String d_rating_count, d_is_available, d_created, d_modified, d_profile_image_path, d_license_image_path, d_rc_image_path;
    private String category_id, car_name, car_desc, car_reg_no, car_model, car_currency, car_created, car_modified;
    private String d_lat, d_lng, d_degree;

    public String getDriverId() {
        return driver_id;
    }

    public String getApiKey() {
        return api_key;
    }


    public String getD_name() {
        return d_name;
    }


    public String getD_fname() {
        return d_fname;
    }

    public String getD_lname() {
        return d_lname;
    }


    public String getD_email() {
        return d_email;
    }


    public String getD_license_id() {
        return d_license_id;
    }

    public String getD_rc() {
        return d_rc;
    }


    public String getCar_id() {
        return car_id;
    }


    public String getD_device_type() {
        return d_device_type;
    }


    public String getD_device_token() {
        return d_device_token;
    }

    public String getD_rating() {
        return d_rating;
    }

    public String getD_rating_count() {
        return d_rating_count;
    }


    public String getD_is_available() {
        return d_is_available;
    }


    public String getD_created() {
        return d_created;
    }

    public String getD_modified() {
        return d_modified;
    }


    public String getD_profile_image_path() {
        return d_profile_image_path;
    }


    public String getD_license_image_path() {
        return d_license_image_path;
    }


    public String getD_rc_image_path() {
        return d_rc_image_path;
    }


    public String getCategory_id() {
        return category_id;
    }


    public String getCar_name() {
        return car_name;
    }


    public String getCar_desc() {
        return car_desc;
    }


    public String getCar_reg_no() {
        return car_reg_no;
    }


    public String getCar_model() {
        return car_model;
    }


    public String getCar_currency() {
        return car_currency;
    }


    public String getCar_created() {
        return car_created;
    }


    public String getCar_modified() {
        return car_modified;
    }


    public String getD_lat() {
        return d_lat;
    }


    public String getD_lng() {
        return d_lng;
    }

    public String getD_degree() {
        return d_degree;
    }

}
