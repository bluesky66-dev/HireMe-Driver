package com.adaptor;

/**
 * Created by DELL on 12-Sep-17.
 */

public class CarDetails {
    String id, year, make, model;

   /* public CarDetails(JSONObject object) {

    }

    public CarDetails(String id, String year, String make, String model) {
        this.id = id;
        this.year = year;
        this.make = make;
        this.model = model;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
