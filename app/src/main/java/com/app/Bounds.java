package com.app;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by grepixinfotech on 14/11/17.
 */

public class Bounds {
    LatLng northeast;
    LatLng southwest;

    public Bounds(JSONObject jsonObject) throws JSONException {
        JSONObject northeast = jsonObject.getJSONObject("northeast");
        JSONObject southwest = jsonObject.getJSONObject("southwest");
        this.northeast = new LatLng(northeast.getDouble("lat"), northeast.getDouble("lng"));
        this.southwest = new LatLng(southwest.getDouble("lat"), southwest.getDouble("lng"));
    }
}
