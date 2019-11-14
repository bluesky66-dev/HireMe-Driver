package com.adaptor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by grepixinfotech on 22/07/16.
 */
public class UserModel extends  User {
     public  String lat;
    public  String lng;
    public  Double userlat;
    public  Double userlng;
    public  UserModel( JSONObject  jobject)
    {
        try {
            this.id=jobject.getString("id");
            this.firstname = jobject.getString("firstname");
            this.lastname = jobject.getString("lastname");
            this.username = String.valueOf(firstname) + " " + String.valueOf(lastname) + " ";
            this.email = jobject.getString("email");
            this.mobile_no = jobject.getString("mobile_no");
            this.lat = jobject.getString("lat");
            this.lng = jobject.getString("long");
            userlat = Double.valueOf(lat);
            userlng = Double.valueOf(lng);
        }
        catch (JSONException e)
        {
            System.out.println(""+e);
        }
    }

    public void log()
    {
        System.out.println("USer ID"+this.id);
    }

}
