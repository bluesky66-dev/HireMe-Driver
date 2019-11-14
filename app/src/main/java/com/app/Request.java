package com.app;

import com.com.driver.webservice.Constants;

import java.util.HashMap;

/**
 * Created by grepixinfotech on 25/10/17.
 */

public class Request {

    /**
     *
     *
     *
     *
     */
   public static class Login {
        private HashMap<String, String> hashMap;
        public String url = Constants.Urls.URL_DRIVER_LOGIN;

        public Login() {
            hashMap = new HashMap<>();
        }

        public Login addEmail(String email) {
            hashMap.put("d_email", email);
            return this;
        }

        public Login addPassword(String password) {
            hashMap.put("d_password", password);
            return this;
        }

        public Login addIsAvailable(String isAvailable) {
            hashMap.put("d_is_available", isAvailable);
            return this;
        }

        public HashMap<String, String> build() {
            return hashMap;
        }
    }

    /**
     *
     *
     *
     */
    public static class UpdateProfile {
        private HashMap<String, String> hashMap;
        public String url = Constants.UPDATE_PROFILE;

        public UpdateProfile() {
            hashMap = new HashMap<>();
        }

        public UpdateProfile addEmail(String email) {
            hashMap.put("d_email", email);
            return this;
        }

        public UpdateProfile addPassword(String password) {
            hashMap.put("d_password", password);
            return this;
        }

        public UpdateProfile addIsAvailable(String isAvailable) {
            hashMap.put("d_is_available", isAvailable);
            return this;
        }

        public UpdateProfile addDviceToken(String token) {
            hashMap.put("d_device_token", token);
            return this;
        }


        public UpdateProfile addPhone(String d_phone) {
            hashMap.put("d_phone", d_phone);
            return this;
        }

        public UpdateProfile addFirstName(String fName) {
            hashMap.put("d_fname", fName);
            return this;
        }

        public UpdateProfile addLastName(String lName) {
            hashMap.put("d_lname", lName);
            return this;
        }


        public UpdateProfile addDviceType(String deviceType) {
            hashMap.put("d_device_type", deviceType);
            return this;
        }

        public UpdateProfile addDviceType() {
            hashMap.put("d_device_type", "Android");
            return this;
        }


        public UpdateProfile addImage(String image) {
            hashMap.put("driver_image", image);
            return this;
        }
        public UpdateProfile addImageType(String image_type) {
            hashMap.put("image_type", image_type);
            return this;
        }


        public UpdateProfile addDegree(String degree) {
            hashMap.put("d_degree", degree);
            return this;
        }

        public UpdateProfile addActive(String active) {
            hashMap.put("active", active);
            return this;
        }

        public UpdateProfile addLat(String lat) {
            hashMap.put("d_lat", lat);
            return this;
        }

        public UpdateProfile addLng(String lng) {
            hashMap.put("d_lng", lng);
            return this;
        }

        public UpdateProfile addIsSendEmail(boolean is_send_email) {
            hashMap.put("is_send_email", is_send_email ? "1" : "0");
            return this;
        }


        public HashMap<String, String> build() {
            return hashMap;
        }
    }

    /**
     *
     *
     *
     */
    static class ForgotPassword {
        private HashMap<String, String> hashMap;
        public String url = Constants.FORGET_PASSWORD;

        public ForgotPassword() {
            hashMap = new HashMap<>();
        }

        public ForgotPassword addEmail(String email) {
            hashMap.put("d_email", email);
            return this;
        }

        public HashMap<String, String> build() {
            return hashMap;
        }
    }
}
