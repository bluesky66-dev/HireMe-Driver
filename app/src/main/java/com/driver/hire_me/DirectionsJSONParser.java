package com.driver.hire_me;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class
DirectionsJSONParser {

    Controller controller;
    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        controller = Controller.getInstance();

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");

            System.out.println("json rout response=======1" + jRoutes);
            Log.i("Directions","json rout response=======2" + jRoutes);
            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    SlideMainActivity.distances=((JSONObject) ((JSONObject)jLegs.get(j)).get("distance")).get("text").toString();
                    SlideMainActivity.durations=((JSONObject) ((JSONObject)jLegs.get(j)).get("duration")).get("text").toString();

                    Log.d(j+"Distance22:   ", SlideMainActivity.distances.toString());
                    Log.d(j+"Duration22:   ", SlideMainActivity.durations.toString());

                    controller.setTripDistance(SlideMainActivity.distances.toString());
                    controller.setTripDuration(SlideMainActivity.durations.toString());

                    controller.pref.setTRIP_DISTANCE_CHANGABLE(SlideMainActivity.distances.toString());
                    controller.pref.setTRIP_DURATION_CHANGABLE(SlideMainActivity.durations.toString());

                    Log.d(j + "Distance223:   ",controller.getTripDistance());
                    Log.d(j + "Duration223:   ", controller.getTripDuration());

                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        if((boolean)(((JSONObject)jSteps.get(k)).has("html_instructions")))
                        {
                            String html=(String)(((JSONObject)jSteps.get(k)).get("html_instructions"));
                            html=html.replaceAll("\\<.*?>","");
                            SlideMainActivity.html_instructions.add(html);
                          //  Log.d(k+"html_instructions", html);
                        }
                        else
                        {
                            SlideMainActivity.html_instructions.add("no html");
                            Log.d(k+"SIDE", "NO Side");
                        }
                        if((boolean)(((JSONObject)jSteps.get(k)).has("maneuver")))
                        {
                            String side=(String)(((JSONObject)jSteps.get(k)).get("maneuver"));
                            SlideMainActivity.maneuver.add(side);
                         //   Log.d(k+"SIDE", side);
                        }
                        else
                        {
                            SlideMainActivity.maneuver.add("No side");
                          //  Log.d(k+"SIDE", "NO Side");
                        }
                        SlideMainActivity.dis.add((String)((JSONObject)((JSONObject)jSteps.get(k)).get("distance")).get("text"));
                      //  Log.d(k+"DIS", (String)((JSONObject)((JSONObject)jSteps.get(k)).get("distance")).get("text"));
                        SlideMainActivity.dur.add((String)((JSONObject)((JSONObject)jSteps.get(k)).get("duration")).get("text"));
                        SlideMainActivity.starting_lat.add((Double)((JSONObject)((JSONObject)jSteps.get(k)).get("start_location")).get("lat"));
                        SlideMainActivity.starting_long.add((Double)((JSONObject)((JSONObject)jSteps.get(k)).get("start_location")).get("lng"));
                        SlideMainActivity.ending_lat.add((Double)((JSONObject)((JSONObject)jSteps.get(k)).get("end_location")).get("lat"));
                        SlideMainActivity.ending_long.add((Double)((JSONObject)((JSONObject)jSteps.get(k)).get("end_location")).get("lng"));

                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }


        return routes;
    }



    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
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
}