package com.app;


import java.util.HashMap;

/**
 * Created by grepixinfotech on 25/10/17.
 */

public class AuthData {
    private static AuthData authData;
    private static DriverModel driverModel;

    public void setDriverModel(DriverModel driverModel) {
        this.driverModel = driverModel;
    }

    private AuthData() {

    }

    public static AuthData getInstance() {
        if (authData == null) {
            authData = new AuthData();
        }
        return authData;
    }

    static class Bulder {
        private HashMap<String, String> hashMap;

        public Bulder() {
            hashMap = new HashMap<>();
        }

        public Bulder create() {
            return this;
        }

        public Bulder addApiKey() {
            if (driverModel != null)
                hashMap.put("api_key", driverModel.getApiKey());
            return this;
        }

        public Bulder addDriverId() {
            if (driverModel != null)
                hashMap.put("driver_id", driverModel.getDriverId());
            return this;
        }

        public HashMap<String, String> build() {
            return hashMap;
        }
    }
}
