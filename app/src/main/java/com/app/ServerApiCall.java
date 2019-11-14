package com.app;

import android.content.Context;

import com.grepix.grepixutils.WebServiceUtil;

import java.util.HashMap;
import java.util.Map;

public class ServerApiCall {
    public static void callWithApiKeyAndDriverId(Context context, String url, WebServiceUtil.DeviceTokenServiceListener deviceTokenServiceListener) {
        callWithApiKeyAndDriverId(context, null, url, deviceTokenServiceListener);
    }

    public static void callWithApiKeyAndDriverId(Context context, Map<String, String> params, String url, WebServiceUtil.DeviceTokenServiceListener deviceTokenServiceListener) {
        AuthData.Bulder bulder = new AuthData.Bulder();
        HashMap<String, String> build = bulder.addApiKey().addDriverId().build();
        if (params != null) {
            build.putAll(params);
        }
        WebServiceUtil.excuteRequest(context, build, url, deviceTokenServiceListener);


    }

    public static void callWithApiKey(Context context, String url, WebServiceUtil.DeviceTokenServiceListener deviceTokenServiceListener) {
        callWithApiKey(context, null, url, deviceTokenServiceListener);
    }

    public static void callWithApiKey(Context context, Map<String, String> params, String url, WebServiceUtil.DeviceTokenServiceListener deviceTokenServiceListener) {
        AuthData.Bulder bulder = new AuthData.Bulder();
        HashMap<String, String> build = bulder.addApiKey().build();
        if (params != null) {
            build.putAll(params);
        }
        WebServiceUtil.excuteRequest(context, build, url, deviceTokenServiceListener);
    }
}