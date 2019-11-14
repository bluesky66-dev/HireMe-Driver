package com.grepix.grepixutils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by grepixinfotech on 07/07/16.
 */
public class WebServiceUtil {



    public static String callGetMethod(String url) {
        try {
            System.out.println("GET Request : " + url);
            URL driver_Url = new URL(url);
            BufferedReader login_reader;
            String str_login = "";
            login_reader = new BufferedReader(new InputStreamReader(driver_Url.openStream()));
            String driver_inputline = "";
            while ((driver_inputline = login_reader.readLine()) != null) {
                str_login += driver_inputline;
            }
            System.out.println("GET Request : " + str_login);
            return str_login;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface DeviceTokenServiceListener {
        void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error);
    }

    public static void excuteRequest(final Context contaxt, final Map<String, String> params, final String url, final DeviceTokenServiceListener listener) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onUpdateDeviceTokenOnServer(response, response != null, null);
                        System.out.println("Url:"+url +"\nSuccess : " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("VolleyError : " + error);
                        if (error instanceof NoConnectionError)
                            Toast.makeText(contaxt, "No internet available", Toast.LENGTH_SHORT).show();
                        else if (error instanceof ServerError) {

                            String d = new String(error.networkResponse.data);
                            try {
                                JSONObject jso = new JSONObject(d);
                                String message=jso.getString("message");
//                                 if(message.equalsIgnoreCase("Driver is not found"))
//                                 {
//                                     Toast.makeText(contaxt, jso.getString("Wrong password. Try again."), Toast.LENGTH_LONG).show();
//                                 }
//                                else
//                                 {
                                     Toast.makeText(contaxt, jso.getString("message"), Toast.LENGTH_LONG).show();
//                                 }
                            } catch (JSONException e) {
                                listener.onUpdateDeviceTokenOnServer(null, false, error);
                                e.printStackTrace();
                            }
                        }
                        listener.onUpdateDeviceTokenOnServer(null, false, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(contaxt);
        requestQueue.add(stringRequest);
    }


}
