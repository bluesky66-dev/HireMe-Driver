package com.app;

import com.adaptor.Driver1;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by grepixinfotech on 08/07/16.
 */
public class DriverModel extends Driver1 {

    private String d_insurance_image_path;
    private String d_is_verified;
    private String d_phone;

    public String getPhone() {
        return d_phone;
    }

    public static DriverModel parseJson(String driverJson) {

        try {
            JSONObject jsonObject = new JSONObject(driverJson);
            JSONObject childObject = jsonObject.getJSONObject("response");
            return new Gson().fromJson(childObject.toString(), DriverModel.class);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isDocVerified() {
        return d_is_verified.equalsIgnoreCase("1") ? true : false;
    }

    public String getInsuranceImagePath() {
        return d_insurance_image_path;
    }


    //            "api_key": "1efdad769eb65ab8f77c4be6eefcb9d3",
//            "d_name": "teds",
//            "d_fname": "teds",
//            "d_lname": "01",
//            "d_email": "test01@gmail.com",
//            "d_password": "05a671c66aefea124cc08b76ea6d30bb",
//            "d_phone": "1236587457",
//            "d_address": "",
//            "d_city": "Mumbai",
//            "d_state": "",
//            "d_country": "IN",
//            "d_zip": "",
//            "d_lat": "19.0176147",
//            "d_lng": "72.8561644",
//            "d_degree": "0",
//            "image_id": "4",
//            "d_license_id": "80",
//            "d_rc": "77",
//            "car_id": "2",
//            "d_device_type": "",
//            "d_device_token": "ff7baf0baca8ce25645d47b1b991fb8050d7cad767fabb8a55f8464426d7f1c9",
//            "d_rating": "0",
//            "d_rating_count": "0",
//            "d_is_available": "0",
//            "d_created": "2016-10-12 12:19:34",
//            "d_modified": "2016-11-21 06:16:18",
//            "d_profile_image_path": "9/zwTIaQCRQSacIjS.jpg",
//            "d_license_image_path": "1/YhOad1U8ftpRVbN.jpg",
//            "d_rc_image_path": "3/9eD29GUmZslLTRH.jpg",
//            "category_id": "1",
//            "car_name": "Honda",
//            "car_desc": "",
//            "car_reg_no": "",
//            "car_model": "",
//            "car_currency": "rupee",
//            "car_fare_per_km": "0",
//            "car_fare_per_min": "0",
//            "car_created": "2016-10-13 14:17:18",
//            "car_modified": "2016-10-13 14:17:18"
}
