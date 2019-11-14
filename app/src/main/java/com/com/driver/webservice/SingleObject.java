package com.com.driver.webservice;

import com.adaptor.Driver1;
import com.adaptor.Trip;
import com.adaptor.TripModel;
import com.adaptor.User1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by grepix on 10/21/2016.
 */
public class SingleObject {
    private static SingleObject instance = new SingleObject();
    private ArrayList<CategoryActors> categoryList;

    private ArrayList<CategoryActors> categoryResponseList;
    private ArrayList<CarActors> carNameList;



    public String IsChangeMag;

    public String D_Lat;
    public String D_Lng;

    public boolean fareSummaryLinearLayout;
    public String d_device_token;
    public String d_degree, image_id, d_license_id, d_rc, car_id, d_rating, d_rating_count, d_created, d_modified, d_profile_image_path;
    public String d_license_image_path;
    public String d_rc_image_path;
    public String category_id;
    public String car_name;
    public String car_desc;
    public String car_reg_no;
    public String car_model;
    public String d_insurance_image_path;
    public String car_currency, car_fare_per_km, car_fare_per_min, car_created, car_modified;

    public Boolean isDocVerified;

    public boolean getfareSummaryLinearLayout() {
        return fareSummaryLinearLayout;
    }

    public void setfareSummaryLinearLayout(boolean fareSummaryLinearLayout) {
        this.fareSummaryLinearLayout = fareSummaryLinearLayout;
    }

    public String getIsChangeMag() {
        return IsChangeMag;
    }

    public void setIsChangeMag(String isChangeMag) {
        IsChangeMag = isChangeMag;
    }

    String u_device_type = "u_device_type";

    public String getU_device_type() {
        return u_device_type;
    }

    public void setU_device_type(String u_device_type) {
        this.u_device_type = u_device_type;
    }

    private SingleObject() {
    }

    public static SingleObject getInstance() {
        return instance;
    }

    public static void setInstance(SingleObject instance) {
        SingleObject.instance = instance;
    }

    private String driverId;
    private String apiKey;
    private String dFname;
    private String dLname;
    private String dEmail;
    private String dPassword;
    private String dPhone;
    private String driver_is_available;

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public void setCarRegNo(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    private String carName;
    private String carModel;
    private String carYear;
    private String carRegNo;
    private String dImageProfilePath, dImageId;


    public String getD_device_token() {
        return d_device_token;
    }

    public void setD_device_token(String d_device_token) {
        this.d_device_token = d_device_token;
    }

    public void driverLoginParseApi(String s) {
        //  loginResponseList = new ArrayList<SingleObject>();

        try {
            JSONObject rootObject = new JSONObject(s);
            String status = rootObject.getString("status");
            String message = rootObject.getString("message");

            JSONObject childObject = rootObject.getJSONObject("response");

            SingleObject singleObject = SingleObject.getInstance();

            String driver_id = childObject.getString("driver_id");
            singleObject.setDriverId(driver_id);

            String apikey = childObject.getString("api_key");
            singleObject.setApiKey(apikey);

            String dfname = childObject.getString("d_fname");
            singleObject.setdFname(dfname);

            String dlname = childObject.getString("d_lname");
            singleObject.setdLname(dlname);

            String demail = childObject.getString("d_email");
            singleObject.setdEmail(demail);

            String dpswd = childObject.getString("d_password");
            singleObject.setdPassword(dpswd);

            String dmobile = childObject.getString("d_phone");
            singleObject.setdPhone(dmobile);

            String dAvailiability = childObject.getString("d_is_available");
            singleObject.setDriver_is_available(dAvailiability);

            String dprofileImgPath = childObject.getString("d_profile_image_path");
            singleObject.setdImageProfilePath(dprofileImgPath);

            String dImgId = childObject.getString("image_id");
            singleObject.setdImageId(dImgId);

            String d_is_verified = childObject.getString("d_is_verified");
            singleObject.isDocVerified = d_is_verified.equalsIgnoreCase("1") ? true : false;
            d_license_image_path = childObject.getString("d_license_image_path");
            category_id = childObject.getString("category_id");
            d_rc_image_path = childObject.getString("d_rc_image_path");
          d_insurance_image_path = childObject.getString("d_insurance_image_path");

            String car_name=childObject.getString("car_make");
            singleObject.setCarName(car_name);
            String car_model=childObject.getString("car_name");
            singleObject.setCarModel(car_model);
            String car_reg_no=childObject.getString("car_reg_no");
            singleObject.setCarRegNo(car_reg_no);
            String year=childObject.getString("car_model");
            singleObject.setCarYear(year);
//            d_license_image_path
            // controller.pref.saveDOCTOR_ID(id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean isDocUploaded() {
        boolean isUpladed = true;
        if (d_license_image_path == null || d_license_image_path.length() == 0 || d_license_image_path.equals("null")) {
            isUpladed = false;
        }if (d_insurance_image_path == null || d_insurance_image_path.length() == 0 || d_insurance_image_path.equals("null")) {
            isUpladed = false;
        }
        if (d_rc_image_path == null || d_rc_image_path.length() == 0 || d_rc_image_path.equals("null")) {
            isUpladed = false;
        }
        if (category_id == null || category_id.length() == 0 || category_id.equals("null")) {
            isUpladed = false;
        }
        return isUpladed;
    }

    public void driverUpdateProfileParseApi(String s) {
        //  loginResponseList = new ArrayList<SingleObject>();

        try {
            JSONObject rootObject = new JSONObject(s);
//            String status = rootObject.getString("status");
//            String message = rootObject.getString("message");

            JSONObject childObject = rootObject.getJSONObject("response");

            SingleObject singleObject = SingleObject.getInstance();

            String driver_id = childObject.getString("driver_id");
            singleObject.setDriverId(driver_id);

            String apikey = childObject.getString("api_key");
            singleObject.setApiKey(apikey);

            String dfname = childObject.getString("d_fname");
            singleObject.setdFname(dfname);

            String devicetoken = childObject.getString("d_device_token");
            singleObject.setD_device_token(devicetoken);

            String dlname = childObject.getString("d_lname");
            singleObject.setdLname(dlname);

            String demail = childObject.getString("d_email");
            singleObject.setdEmail(demail);

            String dpswd = childObject.getString("d_password");
            singleObject.setdPassword(dpswd);

            String dmobile = childObject.getString("d_phone");
            singleObject.setdPhone(dmobile);

            String d_lat = childObject.getString("d_lat");
            singleObject.setD_Lat(d_lat);

            String d_lng = childObject.getString("d_lng");
            singleObject.setD_Lng(d_lng);

            String dAvailiability = childObject.getString("d_is_available");
            singleObject.setDriver_is_available(dAvailiability);

            String dprofileImgPath = childObject.getString("d_profile_image_path");
            singleObject.setdImageProfilePath(dprofileImgPath);

            String dImgId = childObject.getString("image_id");
            singleObject.setdImageId(dImgId);

            String d_degree = childObject.getString("d_degree");
            singleObject.setD_degree(d_degree);

            String image_id = childObject.getString("image_id");
            singleObject.setImage_id(image_id);

            String d_license_id = childObject.getString("d_license_id");
            singleObject.setD_license_id(d_license_id);

            String d_rc = childObject.getString("d_rc");
            singleObject.setD_rc(d_rc);

            String car_id = childObject.getString("car_id");
            singleObject.setCar_id(car_id);

            String d_rating = childObject.getString("d_rating");
            singleObject.setD_rating(d_rating);

            String d_rating_count = childObject.getString("d_rating_count");
            singleObject.setD_rating_count(d_rating_count);

            String d_profile_image_path = childObject.getString("d_profile_image_path");
            singleObject.setD_profile_image_path(d_profile_image_path);

            String d_license_image_path = childObject.getString("d_license_image_path");
            singleObject.setD_license_image_path(d_license_image_path);


              String d_insurance_image_path = childObject.getString("d_insurance_image_path");
            singleObject.setD_insurance_image_path(d_insurance_image_path);



            String d_rc_image_path = childObject.getString("d_rc_image_path");
            singleObject.setD_rc_image_path(d_rc_image_path);

            String category_id = childObject.getString("category_id");
            singleObject.setCategory_id(category_id);

            String car_name = childObject.getString("car_name");
            singleObject.setCar_name(car_name);

            String car_desc = childObject.getString("car_desc");
            singleObject.setCar_desc(car_desc);

            String car_reg_no = childObject.getString("car_reg_no");
            singleObject.setCar_reg_no(car_reg_no);

            String car_model = childObject.getString("car_model");
            singleObject.setCar_model(car_model);

            String car_currency = childObject.getString("car_currency");
            singleObject.setCar_currency(car_currency);

            String car_fare_per_km = childObject.getString("car_fare_per_km");
            singleObject.setCar_currency(car_fare_per_km);

            String car_fare_per_min = childObject.getString("car_fare_per_min");
            singleObject.setCar_fare_per_min(car_fare_per_min);

            String car_created = childObject.getString("car_created");
            singleObject.setCar_fare_per_min(car_created);

            String car_modified = childObject.getString("car_modified");
            singleObject.setCar_fare_per_min(car_modified);


            //   return true;
        } catch (JSONException e) {
            e.printStackTrace();

            // return false;
        }
    }


    public ArrayList<CategoryActors> driverCarCategoriesParseApi(String s) {
        //  loginResponseList = new ArrayList<SingleObject>();
        try {
            JSONObject rootObject = new JSONObject(s);
            String status = rootObject.getString("status");
            String message = rootObject.getString("message");
            String code = rootObject.getString("code");
            int next_offset = rootObject.getInt("next_offset");
            int last_offset = rootObject.getInt("last_offset");

            JSONArray jsonArray = rootObject.getJSONArray("response");

            categoryList = new ArrayList<CategoryActors>();
            // SingleObject singleObject = SingleObject.getInstance();
            for (int i = 0; i < jsonArray.length(); i++) {
                CategoryActors actors = new CategoryActors();
                JSONObject childObject = jsonArray.getJSONObject(i);

                String category_id = childObject.getString("category_id");
                actors.setCategory_id(category_id);
                String cat_name = childObject.getString("cat_name");
                actors.setCat_name(cat_name);
                String cat_desc = childObject.getString("cat_desc");
                actors.setCat_desc(cat_desc);
                String cat_base_price = childObject.getString("cat_base_price");
                actors.setCat_base_price(cat_base_price);
                String cat_fare_per_km = childObject.getString("cat_fare_per_km");
                actors.setCat_fare_per_km(cat_fare_per_km);
                String cat_fare_per_min = childObject.getString("cat_fare_per_min");
                actors.setCat_fare_per_min(cat_fare_per_min);
                String cat_max_size = childObject.getString("cat_max_size");
                actors.setCat_max_size(cat_max_size);
                String cat_is_fixed_price = childObject.getString("cat_is_fixed_price");
                actors.setCat_is_fixed_price(cat_is_fixed_price);
                String cat_prime_time_percentage = childObject.getString("cat_prime_time_percentage");
                actors.setCat_prime_time_percentage(cat_prime_time_percentage);
                String cat_status = childObject.getString("cat_status");
                actors.setStatus(status);
                String cat_created = childObject.getString("cat_created");
                actors.setCat_created(cat_created);
                String cat_modified = childObject.getString("cat_modified");
                actors.setCat_modified(cat_modified);


                categoryList.add(actors);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return categoryList;
    }


    public ArrayList<DriverConstants> driverConstantParseApi(String s) {
        ArrayList<DriverConstants> constantList = new ArrayList<DriverConstants>();
        //  loginResponseList = new ArrayList<SingleObject>();
        try {
            JSONObject rootObject = new JSONObject(s);
            String status = rootObject.getString("status");

            JSONArray jsonArray = rootObject.getJSONArray("response");


            // SingleObject singleObject = SingleObject.getInstance();
            for (int i = 0; i < jsonArray.length(); i++) {
                DriverConstants actors = new DriverConstants();
                JSONObject childObject = jsonArray.getJSONObject(i);

                String category_id = childObject.getString("constant_id");
                actors.setConstant_id(category_id);
                String cat_name = childObject.getString("constant_display");
                actors.setConstant_display(cat_name);
                String cat_desc = childObject.getString("constant_key");
                actors.setConstant_key(cat_desc);
                String cat_base_price = childObject.getString("constant_value");
                actors.setConstant_value(cat_base_price);
                String cat_fare_per_km = childObject.getString("active");
                actors.setActive(cat_fare_per_km);
                String cat_fare_per_min = childObject.getString("created");
                actors.setCreated(cat_fare_per_min);




                constantList.add(actors);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  constantList;
    }


    public ArrayList<User1> getNearbyUsersParseApi(String s) {
        //  loginResponseList = new ArrayList<SingleObject>();
        ArrayList<User1> nearbyUserList = new ArrayList<User1>();
        try {
            JSONObject rootObject = new JSONObject(s);
            String status = rootObject.getString("status");
            String message = rootObject.getString("message");
            String code = rootObject.getString("code");
            int next_offset = rootObject.getInt("next_offset");
            int last_offset = rootObject.getInt("last_offset");

            JSONArray jsonArray = rootObject.getJSONArray("response");


            // SingleObject singleObject = SingleObject.getInstance();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject childObject = jsonArray.getJSONObject(i);
                User1 user = User1.parseJson(childObject.toString());
                nearbyUserList.add(user);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return nearbyUserList;
    }


    public ArrayList<CarActors> carNamesParseApi(String s) {
        //  loginResponseList = new ArrayList<SingleObject>();
        try {
            JSONObject rootObject = new JSONObject(s);
            String status = rootObject.getString("status");
            String message = rootObject.getString("message");
            String code = rootObject.getString("code");
            int next_offset = rootObject.getInt("next_offset");
            int last_offset = rootObject.getInt("last_offset");

            JSONArray jsonArray = rootObject.getJSONArray("response");

            carNameList = new ArrayList<CarActors>();
            // SingleObject singleObject = SingleObject.getInstance();
            for (int i = 0; i < jsonArray.length(); i++) {
                CarActors actors = new CarActors();
                JSONObject childObject = jsonArray.getJSONObject(i);

                String car_id = childObject.getString("car_id");
                actors.setCarId(car_id);
                String category_id = childObject.getString("category_id");
                actors.setCategoryId(category_id);
               /* String image_id = childObject.getString("image_id");
                actors.setImageId(image_id)*/;
                String car_name = childObject.getString("car_name");
                actors.setCarName(car_name);
                String car_desc = childObject.getString("car_desc");
                actors.setCarDesc(car_desc);
                String car_reg_no = childObject.getString("car_reg_no");
                actors.setCarRegNo(car_reg_no);

                carNameList.add(actors);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return carNameList;
    }


    public boolean parsingSingleTripResponse(String response, TripModel tripModel) {

        try {
            JSONObject jsonRootObject = new JSONObject(response);
            JSONObject childObject = jsonRootObject.getJSONObject("response");

            tripModel.trip = Trip.parseJson(childObject.toString());
            tripModel.driver = Driver1.parseJson(childObject.getJSONObject("Driver").toString());
            tripModel.user = User1.parseJson(childObject.getJSONObject("User").toString());
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean parsingTripResponse(String response, TripModel tripModel) {

        try {
            JSONObject jsonRootObject = new JSONObject(response);
            JSONArray jsonArray = jsonRootObject.getJSONArray("response");
            JSONObject childObject = jsonArray.getJSONObject(0);
            tripModel.trip = Trip.parseJson(childObject.toString());
            tripModel.user = User1.parseJson(childObject.getJSONObject("User").toString());
            try{
                tripModel.driver = Driver1.parseJson(childObject.getJSONObject("Driver").toString());
            }catch (Exception e){

            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<CategoryActors> getCategoryResponseList() {
        return categoryResponseList;
    }

    public void setCategoryResponseList(ArrayList<CategoryActors> categoryResponseList) {
        this.categoryResponseList = categoryResponseList;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getdFname() {
        return dFname;
    }

    public void setdFname(String dFname) {
        this.dFname = dFname;
    }

    public String getdLname() {
        return dLname;
    }

    public void setdLname(String dLname) {
        this.dLname = dLname;
    }

    public String getdEmail() {
        return dEmail;
    }

    public void setdEmail(String dEmail) {
        this.dEmail = dEmail;
    }

    public String getdPassword() {
        return dPassword;
    }

    public void setdPassword(String dPassword) {
        this.dPassword = dPassword;
    }

    public String getdPhone() {
        return dPhone;
    }

    public void setdPhone(String dPhone) {
        this.dPhone = dPhone;
    }

    public String getDriver_is_available() {
        return driver_is_available;
    }

    public void setDriver_is_available(String driver_is_available) {
        this.driver_is_available = driver_is_available;
    }

    public String getdImageProfilePath() {
        return dImageProfilePath;
    }

    public void setdImageProfilePath(String dImageProfilePath) {
        this.dImageProfilePath = dImageProfilePath;
    }

    public String getdImageId() {
        return dImageId;
    }

    public void setdImageId(String dImageId) {
        this.dImageId = dImageId;
    }

    public String getD_Lat() {
        return D_Lat;
    }

    public void setD_Lat(String d_Lat) {
        D_Lat = d_Lat;
    }

    public String getD_Lng() {
        return D_Lng;
    }

    public void setD_Lng(String d_Lng) {
        D_Lng = d_Lng;
    }


    public boolean isFareSummaryLinearLayout() {
        return fareSummaryLinearLayout;
    }

    public void setFareSummaryLinearLayout(boolean fareSummaryLinearLayout) {
        this.fareSummaryLinearLayout = fareSummaryLinearLayout;
    }

    public String getD_degree() {
        return d_degree;
    }

    public void setD_degree(String d_degree) {
        this.d_degree = d_degree;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getD_license_id() {
        return d_license_id;
    }

    public void setD_license_id(String d_license_id) {
        this.d_license_id = d_license_id;
    }

    public String getD_rc() {
        return d_rc;
    }

    public void setD_rc(String d_rc) {
        this.d_rc = d_rc;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getD_rating() {
        return d_rating;
    }

    public void setD_rating(String d_rating) {
        this.d_rating = d_rating;
    }

    public String getD_rating_count() {
        return d_rating_count;
    }

    public void setD_rating_count(String d_rating_count) {
        this.d_rating_count = d_rating_count;
    }

    public String getD_created() {
        return d_created;
    }

    public void setD_created(String d_created) {
        this.d_created = d_created;
    }

    public String getD_modified() {
        return d_modified;
    }

    public void setD_modified(String d_modified) {
        this.d_modified = d_modified;
    }

    public String getD_profile_image_path() {
        return d_profile_image_path;
    }

    public void setD_profile_image_path(String d_profile_image_path) {
        this.d_profile_image_path = d_profile_image_path;
    }

    public String getD_license_image_path() {
        return d_license_image_path;
    }

    public void setD_license_image_path(String d_license_image_path) {
        this.d_license_image_path = d_license_image_path;
    }

    public String getD_rc_image_path() {
        return d_rc_image_path;
    }

    public void setD_rc_image_path(String d_rc_image_path) {
        this.d_rc_image_path = d_rc_image_path;
    }

    public String getD_insurance_image_path() {
        return d_insurance_image_path;
    }

    public void setD_insurance_image_path(String d_insurance_image_path) {
        this.d_insurance_image_path = d_insurance_image_path;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_desc() {
        return car_desc;
    }

    public void setCar_desc(String car_desc) {
        this.car_desc = car_desc;
    }

    public String getCar_reg_no() {
        return car_reg_no;
    }

    public void setCar_reg_no(String car_reg_no) {
        this.car_reg_no = car_reg_no;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getCar_currency() {
        return car_currency;
    }

    public void setCar_currency(String car_currency) {
        this.car_currency = car_currency;
    }

    public String getCar_fare_per_km() {
        return car_fare_per_km;
    }

    public void setCar_fare_per_km(String car_fare_per_km) {
        this.car_fare_per_km = car_fare_per_km;
    }

    public String getCar_fare_per_min() {
        return car_fare_per_min;
    }

    public void setCar_fare_per_min(String car_fare_per_min) {
        this.car_fare_per_min = car_fare_per_min;
    }

    public String getCar_created() {
        return car_created;
    }

    public void setCar_created(String car_created) {
        this.car_created = car_created;
    }
}
