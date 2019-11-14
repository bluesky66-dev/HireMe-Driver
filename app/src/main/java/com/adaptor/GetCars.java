package com.adaptor;

/**
 * Created by grepix on 2/23/2017.
 */
public class GetCars {
    String car_id;
    String car_name;
    String category_id;

    public GetCars(String car_id, String car_name,String category_id){
        this.car_id=car_id;
        this.car_name=car_name;
        this.category_id=category_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
