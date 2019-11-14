package com.app;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by grepixinfotech on 14/11/17.
 */

public class Route {
    ArrayList<Leg> legs = new ArrayList<>();
    Bounds bounds;
    ArrayList<LatLng> points;


    public Route(JSONObject jsonObject) throws JSONException {
        this.bounds = new Bounds(jsonObject.getJSONObject("bounds"));

        JSONArray legsArray = jsonObject.getJSONArray("legs");
        for (int i = 0; i < legsArray.length(); i++) {
            legs.add(new Leg(legsArray.getJSONObject(i)));
        }
        JSONObject overviewPolyline = jsonObject.getJSONObject("overview_polyline");
        String pointsEncoded = overviewPolyline.getString("points");
        points = decodePoly(pointsEncoded);
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

    public int getTotalDistance() {
        int distance = 0;
        for (int j = 0; j < this.legs.size(); j++) {
            distance += legs.get(j).distanceValue;
        }
        return distance;
    }

    public int getTotalDuration() {
        int duration = 0;
        for (int j = 0; j < this.legs.size(); j++) {
            duration += legs.get(j).durationValue;
        }
        return duration;
    }

    public String getTotalDurationInMin() {
        return String.format("%d mins",(int) getTotalDuration() / 60);
    }

    public String getTotalDurationInKm() {
        return String.format("%.01f km", getTotalDistance() / 1000.0);
    }
}
