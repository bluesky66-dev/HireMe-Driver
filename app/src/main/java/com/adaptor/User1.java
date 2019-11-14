package com.adaptor;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by grepix on 2/1/2017.
 */
public class User1 implements Serializable {


    public static User1 parseJson(String userJson) {

        return new Gson().fromJson(userJson, User1.class);
    }

    private String user_id, user_api_key, group_id, username, user_password, u_name, u_fname, u_lname, u_email, u_password, u_phone, u_address;
private String u_profile_image_path;

    public String getUProfileImagePath() {
        return u_profile_image_path;
    }

    public String getU_city() {
        return u_city;
    }


    public String getU_state() {
        return u_state;
    }


    public String getU_country() {
        return u_country;
    }

    public String getU_zip() {
        return u_zip;
    }

    public String getU_lat() {
        return u_lat;
    }


    public String getU_lng() {
        return u_lng;
    }

    public String getU_degree() {
        return u_degree;
    }

    public String getImage_id() {
        return image_id;
    }


    public String getU_device_type() {
        return u_device_type;
    }

    public String getU_device_token() {
        return u_device_token;
    }

    public String getU_is_available() {
        return u_is_available;
    }


    public String u_city, u_state, u_country, u_zip, u_lat, u_lng, u_degree, image_id, u_device_type, u_device_token, u_is_available;

    public String getUserId() {
        return user_id;
    }


    public String getUser_api_key() {
        return user_api_key;
    }


    public String getGroup_id() {
        return group_id;
    }


    public String getUserName() {
        return username;
    }


    public String getUser_password() {
        return user_password;
    }


    public String getU_name() {
        return u_name;
    }


    public String getU_fname() {
        return u_fname;
    }


    public String getU_lname() {
        return u_lname;
    }


    public String getU_email() {
        return u_email;
    }


    public String getU_password() {
        return u_password;
    }


    public String getU_phone() {
        return u_phone;
    }


    public String getU_address() {
        return u_address;
    }

}
