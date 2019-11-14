package com.app;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by grepixinfotech on 14/11/17.
 */

public class Step {

    LatLng startLocation;
    LatLng endLocation;
    String htmlInstructions;

    ArrayList<LatLng> points = new ArrayList<>();

    public Step(JSONObject jsonObject) throws JSONException {
        this.startLocation = new LatLng(jsonObject.getJSONObject("start_location").getDouble("lat"), jsonObject.getJSONObject("start_location").getDouble("lng"));
        this.endLocation = new LatLng(jsonObject.getJSONObject("end_location").getDouble("lat"), jsonObject.getJSONObject("end_location").getDouble("lng"));
        this.htmlInstructions = jsonObject.getString("html_instructions");
        JSONObject overviewPolyline = jsonObject.getJSONObject("polyline");
        String pointsEncoded = overviewPolyline.getString("points");
        this.points = decodePoly(pointsEncoded);
    }

    private ArrayList<LatLng> decodePoly(String encoded) {

        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
//    {
//        "distance" : {
//        "text" : "0.2 km",
//                "value" : 240
//    },
//        "duration" : {
//        "text" : "1 min",
//                "value" : 48
//    },
//        "end_location" : {
//        "lat" : 28.551153,
//                "lng" : 77.2586621
//    },
//        "html_instructions" : "Head \u003cb\u003enorth-west\u003c/b\u003e towards \u003cb\u003eLotus Temple Rd\u003c/b\u003e",
//            "polyline" : {
//        "points" : "scgmDk}pvMGREJCF?@A@A@C?KAC?A@C@ABITM`@M^O\\GRCFEDKLY\\g@l@a@n@W`@"
//    },
//        "start_location" : {
//        "lat" : 28.5498581,
//                "lng" : 77.26053709999999
//    },
//        "travel_mode" : "DRIVING"
//    }
}
