package com.app;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by grepixinfotech on 14/11/17.
 */

public class Leg {
    ArrayList<Step> steps = new ArrayList<>();

    LatLng startLocation;
    LatLng endLocation;
    String distanceText;
    int distanceValue;
    String durationText;
    int durationValue;

//     "distance" : {
//        "text" : "13.4 km",
//                "value" : 13350
//    },
//            "duration" : {
//        "text" : "29 mins",
//                "value" : 1745
//    },

    public Leg(JSONObject jsonObject) throws JSONException {
        startLocation = new LatLng(jsonObject.getJSONObject("start_location").getDouble("lat"), jsonObject.getJSONObject("start_location").getDouble("lng"));
        endLocation = new LatLng(jsonObject.getJSONObject("end_location").getDouble("lat"), jsonObject.getJSONObject("end_location").getDouble("lng"));
        distanceText = jsonObject.getJSONObject("distance").getString("text");
        distanceValue = jsonObject.getJSONObject("distance").getInt("value");
        durationText = jsonObject.getJSONObject("duration").getString("text");
        durationValue = jsonObject.getJSONObject("duration").getInt("value");

        JSONArray stepsJson = jsonObject.getJSONArray("steps");
        for (int i = 0; i < stepsJson.length(); i++) {
            steps.add(new Step(stepsJson.getJSONObject(i)));
        }
    }


//               "distance" : {
//        "text" : "13.4 km",
//                "value" : 13350
//    },
//            "duration" : {
//        "text" : "29 mins",
//                "value" : 1745
//    },
//            "end_address" : "New Delhi, Delhi, India",
//            "end_location" : {
//        "lat" : 28.6139103,
//                "lng" : 77.2090204
//    },
//            "start_address" : "Ma Anandmayee Marg, NSIC Estate, Okhla Phase III, Kalkaji, New Delhi, Delhi 110019, India",
//            "start_location" : {
//        "lat" : 28.5498581,
//                "lng" : 77.26053709999999
//    },

}
