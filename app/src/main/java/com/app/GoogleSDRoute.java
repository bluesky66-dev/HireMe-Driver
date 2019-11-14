package com.app;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.driver.hire_me.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by grepixinfotech on 13/11/17.
 */

public class GoogleSDRoute {


    public interface GoogleSDRouteCallBack {
        void onCompleteSDRoute(RouteResponse routeResponse, Exception ex);

        void onErrorSDRoute(VolleyError routeResponse);
    }

    private Context context;
    private final LatLng srourceLoc;
    private final LatLng destinationLoc;


    public GoogleSDRoute(Context context, LatLng srourceLoc, LatLng destinationLoc) {
        this.context = context;
        this.srourceLoc = srourceLoc;
        this.destinationLoc = destinationLoc;

    }


    private void getRoute(final GoogleSDRouteCallBack call, String urlDirection) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlDirection,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleDirectionResponse(response, call);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        call.onErrorSDRoute(error);
                        System.out.println("VolleyError : " + error);
                        Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
                    }
                }) {

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void getRoute(final GoogleSDRouteCallBack call) {
        final String directionsUrl = getDirectionsUrl(srourceLoc, destinationLoc, call);
        getRoute(call, directionsUrl);
    }

    public void getRoute(final GoogleSDRouteCallBack call, ArrayList<LatLng> wayPoints) {
         LatLng src=srourceLoc;
         if(wayPoints.size()>0)
         {
              src=wayPoints.get(wayPoints.size()-1);
         }
//        final String directionsUrl = getDirectionsUrl(srourceLoc, destinationLoc, call, wayPoints);
        final String directionsUrl = getDirectionsUrl(src, destinationLoc, call);
        getRoute(call, directionsUrl);

    }

    private void handleDirectionResponse(String response, GoogleSDRouteCallBack call) {
        try {
            ParseRoute parseRoute = new ParseRoute(new JSONObject(response));
            RouteResponse routes = parseRoute.parse();
            call.onCompleteSDRoute(routes, null);
        } catch (Exception e) {
            e.printStackTrace();
            call.onCompleteSDRoute(null, e);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest, GoogleSDRouteCallBack call, ArrayList<LatLng> wayPoints) {
        String googleapikey = context.getResources().getString(R.string.googleapikey1);
        String parameters = "";
        String output = "json";
        try {
            // Origin of route
            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
            // Destination of route
            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
            String key = "key=" + googleapikey;
            // Sensor enabled
            String sensor = "sensor=true";
            ArrayList<String> point = new ArrayList<>();
            for (int i = 0; i < wayPoints.size(); i++) {
                String s = String.format("%f,%f", wayPoints.get(i).latitude, wayPoints.get(i).longitude);
                point.add(s);
            }
            String wayPoint = TextUtils.join("|", point);
            parameters = str_origin + "&" + str_dest + "&" + key + "&" + sensor + "&waypoints=optimize:true|" + wayPoint;
        } catch (Exception e) {
            call.onCompleteSDRoute(null, e);
        }
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest, GoogleSDRouteCallBack call) {
        String googleapikey = context.getResources().getString(R.string.googleapikey1);
        String parameters = "";
        String output = "json";
        try {
            // Origin of route
            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
            // Destination of route
            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
            String key = "key=" + googleapikey;
            // Sensor enabled
            String sensor = "sensor=true";
            parameters = str_origin + "&" + str_dest + "&" + key + "&" + sensor;
        } catch (Exception e) {
            call.onCompleteSDRoute(null, e);
        }
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    public class RouteResponse {
        public ArrayList<String> html_instructions = new ArrayList<String>();
        public ArrayList<String> maneuver = new ArrayList<String>();
        public ArrayList<String> dis = new ArrayList<String>();
        public ArrayList<String> dur = new ArrayList<String>();
        public ArrayList<Double> starting_lat = new ArrayList<Double>();
        public ArrayList<Double> starting_long = new ArrayList<Double>();
        public ArrayList<Double> ending_lat = new ArrayList<Double>();
        public ArrayList<Double> ending_long = new ArrayList<Double>();
        public LatLng srcLoc;
        public LatLng destLoc;
        public String distance;
        public String duration;
        ArrayList<Route> routesObject;

        public ArrayList<Polyline> addRouteOnMap(GoogleMap googleMap) {
            return addRouteOnMap(googleMap, true);
        }

        public ArrayList<Polyline> addRouteOnMap(GoogleMap googleMap, boolean isZoomMap) {

            ArrayList<PolylineOptions> polylineOptionses = makePloylineOnMap();
            ArrayList<Polyline> polylines = new ArrayList<>();
            for (PolylineOptions lineOptions :
                    polylineOptionses) {
                lineOptions.width(8);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);
                lineOptions.zIndex(4);
                polylines.add(googleMap.addPolyline(lineOptions));
            }

                if (srcLoc != null) {
                    googleMap.addMarker(new MarkerOptions().position(srcLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.carimage)));
                }
                if (destLoc != null) {
                    googleMap.addMarker(new MarkerOptions().position(destLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_25)));
                }
            if (isZoomMap) {
                if (routesObject != null && routesObject.size() > 0) {
                    Route route = routesObject.get(0);
                    LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                    bounds.include(route.bounds.northeast);
                    bounds.include(route.bounds.southwest);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100));
                }
            }
            return polylines;
        }

        public ArrayList<PolylineOptions> makePloylineOnMap() {
            ArrayList<PolylineOptions> polylineOptionses = new ArrayList<>();
            if (routesObject != null) {
                // Traversing through all the routes
                try {
                    for (int i = 0; i < routesObject.size(); i++) {
                        PolylineOptions lineOptions = new PolylineOptions();
                        // Fetching i-th route
                        Route route = routesObject.get(i);

//                        ArrayList<Leg> legs = route.legs;
//                        for (int j = 0; j < legs.size(); j++) {
//                            Leg leg = legs.get(j);
//                            for (int k = 0; k < leg.steps.size(); k++) {
//                                Step step = leg.steps.get(k);
//
////                                 for (step.points)
//                            }
//                        }
                        lineOptions.addAll(route.points);
                        polylineOptionses.add(lineOptions);
                        srcLoc = route.points.get(0);
                        destLoc = route.points.get(route.points.size() - 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return polylineOptionses;
        }


        public ArrayList<LatLng> getLastNearstLoc(Location location) {
            ArrayList<LatLng> latLngs = new ArrayList<>();
            if (routesObject != null) for (int i = 0; i < routesObject.size(); i++) {
                ArrayList<Leg> legs = routesObject.get(i).legs;

                for (int j = 0; j < legs.size(); j++) {
                    Leg leg = legs.get(j);
                    for (int k = 0; k < leg.steps.size(); k++) {
                        Step step = leg.steps.get(k);
                        ArrayList<LatLng> points = step.points;
                        for (int jk = 0; jk < points.size()-1; jk++) {
                            Location locationA = convertLatLngToLocation(points.get(jk));
                            Location locationB = convertLatLngToLocation(points.get(jk+1));
                            float disAB = locationA.distanceTo(locationB);
                            float disAC = locationA.distanceTo(location);
                            float disBC = locationB.distanceTo(location);
//                             if(disAC<disAB&&disBC<disAB) {
//
//                                     latLngs.add(points.get(jk));
//                                     return latLngs;
//
//                             }else
                            if (disAC < 100) {
                                latLngs.add(points.get(jk));
                                return latLngs;
                            }
                            latLngs.add(points.get(jk));
                        }

                    }
                }
            }
            return null;
        }

        private Location convertLatLngToLocation(LatLng latLng) {

            Location location = new Location("");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            return location
                    ;
        }

        public boolean isOnOffRoute(Location location) {

            if (routesObject != null) for (int i = 0; i < routesObject.size(); i++) {
                ArrayList<Leg> legs = routesObject.get(i).legs;
                for (int j = 0; j < legs.size(); j++) {
                    Leg leg = legs.get(j);
                    for (int k = 0; k < leg.steps.size(); k++) {
                        Step step = leg.steps.get(k);
                        ArrayList<LatLng> points = step.points;
                        for (int jk = 0; jk < points.size(); jk++) {
                            Location location1 = convertLatLngToLocation(points.get(jk));
                            float dis = location1.distanceTo(location);
                            if (dis < 60) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
    }


    class ParseRoute {


        private JSONObject jObject;

        public ParseRoute(JSONObject jsonObject) {
            jObject = jsonObject;
        }

        public RouteResponse parse() throws JSONException {

            RouteResponse routeResponse = new RouteResponse();
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
            ArrayList<Route> routesObject = new ArrayList<>();
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;
            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                Route route = new Route(jRoutes.getJSONObject(i));
                routeResponse.distance = route.getTotalDurationInKm();
                routeResponse.duration = route.getTotalDurationInMin();
                routesObject.add(route);


            }
            routeResponse.routesObject = routesObject;
            return routeResponse;
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

        public RouteResponse parseOverAll() throws JSONException {
            RouteResponse routeResponse = new RouteResponse();
            JSONArray jRoutes = jObject.getJSONArray("routes");
            ArrayList<Route> routes = new ArrayList<>();
            for (int i = 0; i < jRoutes.length(); i++) {
                JSONObject jsonObject = jRoutes.getJSONObject(i);
                Route route = new Route(jsonObject);
                routes.add(route);
                routeResponse.distance = route.getTotalDurationInKm();
                routeResponse.duration = route.getTotalDurationInMin();
            }
            routeResponse.routesObject = routes;
            return routeResponse;
        }
    }


}
