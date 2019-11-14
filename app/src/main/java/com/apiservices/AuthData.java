package com.apiservices;

/**
 * Created by grepixinfotech on 11/09/17.
 */

public class AuthData {

    private String ApiKey;
    private static AuthData authData;


    public static AuthData getInstance() {
        if (authData == null) {
            authData = new AuthData();
        }
        return authData;
    }

    public void setApiKey(String apiKey) {
        ApiKey = apiKey;
    }

    public String getApiKey() {
        return ApiKey;
    }
}

