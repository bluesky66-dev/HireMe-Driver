package com.service;


import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;

import com.driver.hire_me.ILocationRecord;
import com.driver.hire_me.R;
import com.driver.hire_me.SlideMainActivity;


/**
 * Created by grepixinfotech on 21/09/17.
 */

public class LocationTrackService extends Service implements LocationListener {

    private LocationManager locationManager;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    private Location location;
    private double distance;
    int count;


    ILocationRecord.Stub iLocationRecord = new ILocationRecord.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getPid() throws RemoteException {

            return count;
        }

        @Override
        public double coverDistance() throws RemoteException {
            return distance;
        }

        @Override
        public Location getCurrentLocation() throws RemoteException {
            return location;
        }
    };
//    bindService(intent, serviceConnection, BIND_NOT_FOREGROUND);

//


    @Override
    public IBinder onBind(Intent intent) {
        distance = intent.getDoubleExtra("distance", 0);
        fn_getlocation();
        return iLocationRecord;
    }


    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }


    private static final int NOTIFICATION_ID = 10;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fn_getlocation();

        double curLat=intent.getDoubleExtra("Latitute",0);
        double curLng=intent.getDoubleExtra("Longitute",0);
        location= new Location("");
        location.setLatitude(
                curLat);
        location.setLongitude(curLng);
        Intent nextIntent = new Intent(this, SlideMainActivity.class);

        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Trip Running..")
                .setTicker("Trip Running..")
                .setContentText("Hire Me Driver..")
                .setSmallIcon(R.drawable.appicon_driver)
                .setContentIntent(pnextIntent)
                .setOngoing(true)
                .build();
        startForeground(NOTIFICATION_ID,
                notification);


        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(this);
        }
    }

    public Location getCurrentLocation() {
        return location;
    }


    private void fn_getlocation() {

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {
            if (isNetworkEnable) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
             /*   if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                }*/
            }
            if (isGPSEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
             /*   if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                }*/
            }
          //  Toast.makeText(getApplicationContext(),"Register", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        calculateDistance(location);
        count++;
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        /*if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }*/
    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public double getCoveredDistance() {
        return distance;
    }


    public void calculateDistance(Location currentLocation) {
         if(  currentLocation.getAccuracy()>15) {

             if (location == null) {
                 location = currentLocation;
                 return;
             }

             if (currentLocation != null) {
                 float distnsDiff = currentLocation.distanceTo(location);
                 if(distnsDiff>50){
                     distance += distnsDiff;
                     location = currentLocation;
                 }


             }
         }
    }


}
