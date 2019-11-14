package com.apiservices;

import com.google.gson.Gson;

/**
 * Created by grepixinfotech on 11/09/17.
 */

public class BaseReponse {
    @AppAnotation("status")
    public String status;
    @AppAnotation("code")
    public String statusCode;
    @AppAnotation("message")
    public String message;

    public boolean isStatusOk() {
        if (status == null) {
            return false;
        } else if (status.equalsIgnoreCase("ok")) {
            return true;
        }
        return false;
    }

    public Object parse(String s, Class<?> type) {
        Gson gson = new Gson();
         return   gson.fromJson(s, type);
    }


}
