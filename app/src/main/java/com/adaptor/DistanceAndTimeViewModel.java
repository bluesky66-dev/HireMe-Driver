package com.adaptor;

/**
 * Created by grepix on 3/8/2017.
 */
public class DistanceAndTimeViewModel {

    private String tripdistance;
    private String triptime;
    private String tripcurrenttime;

    public DistanceAndTimeViewModel(String tripdistance,String triptime, String tripcurrenttime){
        this.tripdistance=tripdistance;
        this.triptime=triptime;
        this.tripcurrenttime=tripcurrenttime;
    }

    public String getTripdistance() {
        return tripdistance;
    }

    public void setTripdistance(String tripdistance) {
        this.tripdistance = tripdistance;
    }

    public String getTriptime() {
        return triptime;
    }

    public void setTriptime(String triptime) {
        this.triptime = triptime;
    }

    public String getTripcurrenttime() {
        return tripcurrenttime;
    }

    public void setTripcurrenttime(String tripcurrenttime) {
        this.tripcurrenttime = tripcurrenttime;
    }
}
