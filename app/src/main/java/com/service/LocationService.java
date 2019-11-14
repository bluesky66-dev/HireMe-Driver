package com.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.driver.hire_me.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.helper.ILocationConstants;
import com.storage.IPreferenceConstants;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Nayanesh Gupte
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ILocationConstants, IPreferenceConstants {


    private static final String TAG = LocationService.class.getSimpleName();

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;


    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private String mLastUpdateTimeLabel;
//    private String mDistance;


    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    private Location oldLocation;

    private Location newLocation;


//    private AppPreferences appPreferences;

    /**
     * Total distance covered
     */
    private float distance;


    @Override
    public void onCreate() {
        super.onCreate();

//        appPreferences = new AppPreferences(this);

        oldLocation = new Location("Point A");
        newLocation = new Location("Point B");

       /* mLatitudeLabel = getString(R.string.latitude_label);
        mLongitudeLabel = getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getString(R.string.last_update_time_label);*/
//        mDistance = getString(R.string.distance);

        mLastUpdateTime = "";

//        distance = appPreferences.getFloat(PREF_DISTANCE, 0);

        Log.d(TAG, "onCreate Distance: " + distance);
        if (PreferencesUtils.getBoolean(this, R.string.is_recording_start, false) == true) {
            distance = (int) PreferencesUtils.getFloat(this, R.string.recorded_distance);
        } else {
            distance = 0;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        buildGoogleApiClient();

        mGoogleApiClient.connect();

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }

        return START_STICKY;

    }


    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {

        try {

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
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception ex) {


        }
    }


    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {

        if (null != mCurrentLocation) {

            StringBuilder sbLocationData = new StringBuilder();
            sbLocationData/*.append(mLatitudeLabel)
                    .append(" ")
                    .append(mCurrentLocation.getLatitude())
                    .append("\n")
                    .append(mLongitudeLabel)
                    .append(" ")
                    .append(mCurrentLocation.getLongitude())
                    .append("\n")
                    .append(mLastUpdateTimeLabel)
                    .append(" ")
                    .append(mLastUpdateTime)
                    .append("\n")
                    .append(mDistance)
                    .append(" ")*/
                    .append(getUpdatedDistance());
                    //.append(" meters");


            /*
             * update preference with latest value of distance
             */
//            appPreferences.putFloat(PREF_DISTANCE, distance);


            Log.d(TAG, "Location Data:\n" + sbLocationData.toString());

            sendLocationBroadcast(sbLocationData.toString());
        } else {

            Toast.makeText(this,"Unable to find location", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Send broadcast using LocalBroadcastManager to update UI in activity
     *
     * @param sbLocationData
     */
    private void sendLocationBroadcast(String sbLocationData) {

        Intent locationIntent = new Intent();
        locationIntent.setAction(LOACTION_ACTION);
        locationIntent.putExtra(LOCATION_MESSAGE, sbLocationData);

         if(mCurrentLocation!= null) {
             locationIntent.putExtra("c_lat", ""+mCurrentLocation.getLatitude());
             locationIntent.putExtra("c_lng", ""+mCurrentLocation.getLongitude());
         }
        LocalBroadcastManager.getInstance(this).sendBroadcast(locationIntent);

    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onDestroy() {

//        appPreferences.putFloat(PREF_DISTANCE, distance);

        stopLocationUpdates();

        mGoogleApiClient.disconnect();

        Log.d(TAG, "onDestroy Distance " + distance);


        super.onDestroy();
    }


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) throws SecurityException {
        Log.i(TAG, "Connected to GoogleApiClient");


        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }

        startLocationUpdates();

    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        PreferencesUtils.setFloat(getApplicationContext(), R.string.recorded_distance, distance);
        Log.d(TAG, "onLocationChanged :" + location + "  Cover Distance : " + distance);

    }

    @Override
    public void onConnectionSuspended(int cause) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    private float getUpdatedDistance() {
        /**
         * There is 68% chance that user is with in 100m from this location.
         * So neglect location updates with poor accuracy
         */

        if (mCurrentLocation.getAccuracy() > ACCURACY_THRESHOLD) {
            return distance;
        }

        if (oldLocation.getLatitude() == 0 && oldLocation.getLongitude() == 0) {
            oldLocation.setLatitude(mCurrentLocation.getLatitude());
            oldLocation.setLongitude(mCurrentLocation.getLongitude());
            return distance;
        } else {


//            if(isMoving)
//            {

            if (mCurrentLocation.getAccuracy() < 60) {
                float tempDistance = mCurrentLocation.distanceTo(oldLocation);
//                distance += tempDistance;
//                PreferencesUtils.setFloat(getApplicationContext(), R.string.recorded_distance, distance);
//                oldLocation.setLatitude(mCurrentLocation.getLatitude());
//                oldLocation.setLongitude(mCurrentLocation.getLongitude());
                if (tempDistance > 50) {
                    distance += tempDistance;
                    PreferencesUtils.setFloat(getApplicationContext(), R.string.recorded_distance, distance);
                    oldLocation.setLatitude(mCurrentLocation.getLatitude());
                    oldLocation.setLongitude(mCurrentLocation.getLongitude());
                }
            }
//            }
//            if (tempDistance > 50) {
//                distance += tempDistance;
//                PreferencesUtils.setFloat(getApplicationContext(), R.string.recorded_distance, distance);
//                oldLocation.setLatitude(mCurrentLocation.getLatitude());
//                oldLocation.setLongitude(mCurrentLocation.getLongitude());
//            }
        }
        return distance;
    }
//    private float getUpdatedDistance() {
//
//        /**
//         * There is 68% chance that user is with in 100m from this location.
//         * So neglect location updates with poor accuracy
//         */
//
//
//        if (mCurrentLocation.getAccuracy() > ACCURACY_THRESHOLD) {
//
//            return distance;
//        }
//
//
//        if (oldLocation.getLatitude() == 0 && oldLocation.getLongitude() == 0) {
//
//            oldLocation.setLatitude(mCurrentLocation.getLatitude());
//            oldLocation.setLongitude(mCurrentLocation.getLongitude());
//
//           // newLocation.setLatitude(mCurrentLocation.getLatitude());
//           // newLocation.setLongitude(mCurrentLocation.getLongitude());
//
//            return distance;
//        } else {
//
//            float tempDistance = mCurrentLocation.distanceTo(oldLocation);
//            if(tempDistance>50){
//                distance +=tempDistance;
//                oldLocation.setLatitude(mCurrentLocation.getLatitude());
//                oldLocation.setLongitude(mCurrentLocation.getLongitude());
//              //  newLocation.setLatitude(mCurrentLocation.getLatitude());
//              //  newLocation.setLongitude(mCurrentLocation.getLongitude());
//            }
//
//
//        }
//
//
//        /**
//         * Calculate distance between last two geo locations
//         */
//
//
//        return distance;
//    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
