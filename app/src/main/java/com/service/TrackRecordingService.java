package com.service;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.com.driver.webservice.Constants;
import com.driver.hire_me.ITrackRecordingService;
import com.driver.hire_me.R;
import com.driver.hire_me.SlideMainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.helper.ILocationConstants;

import java.text.DateFormat;
import java.util.Date;

//import com.google.android.gms.location.ActivityRecognition;
//import com.google.android.gms.location.ActivityRecognitionApi;

public class TrackRecordingService extends Service implements ILocationConstants, GoogleApiClient.ConnectionCallbacks, SensorEventListener {
    int time = 0;

    public static final String RESUME_TRACK_EXTRA_NAME = "resume";
    private static final String TAG = TrackRecordingService.class.getSimpleName();
    private ServiceBinder binder = new ServiceBinder(this);
    private long recordingTrackId = 12;
    private boolean recordingTrackPaused;
    private Context context;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    // 1 second in milliseconds
//    private static final long ONE_SECOND = (long) UnitConversions.S_TO_MS;

    // 1 minute in milliseconds
//    private static final long ONE_MINUTE = (long) (UnitConversions.MIN_TO_S
//            * UnitConversions.S_TO_MS);
//    private MyTracksLocationManager myTracksLocationManager;


    private SensorManager sensorMan;
    private Sensor accelerometer;
    private float[] mGravity;
    private double mAccel;
    private double mAccelCurrent;
    private double mAccelLast;
    private boolean sensorRegistered = false;








    /*
     * Note that sharedPreferenceChangeListener cannot be an anonymous inner
     * class. Anonymous inner class will get garbage collected.
     */


    private final SharedPreferences.OnSharedPreferenceChangeListener
            sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
//            if (key == null
//                    || key.equals(PreferencesUtils.getKey(context, R.string.recording_track_id_key))) {
//                long trackId = PreferencesUtils.getLong(context, R.string.recording_track_id_key);
//            /*
//             * Only through the TrackRecordingService can one stop a recording
//             * and set the recordingTrackId to -1L.
//             */
//                if (trackId != PreferencesUtils.RECORDING_TRACK_ID_DEFAULT) {
//                    recordingTrackId = trackId;
//                }
//            }
//            if (key == null || key.equals(
//                    PreferencesUtils.getKey(context, R.string.recording_track_paused_key))) {
//                recordingTrackPaused = PreferencesUtils.getBoolean(context,
//                        R.string.recording_track_paused_key,
//                        PreferencesUtils.RECORDING_TRACK_PAUSED_DEFAULT);
//            }

        }
    };
    private GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;
    private float distance;
    private LocationRequest mLocationRequest;
    private Location oldLocation;
    private Location newLocation;

    /*
  * Note that this service, through the AndroidManifest.xml, is configured to
  * allow both MyTracks and third party apps to invoke it. For the onCreate
  * callback, we cannot tell whether the caller is MyTracks or a third party
  * app, thus it cannot start/stop a recording or write/update MyTracks
  * database.
  */
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        handler = new Handler();

//        myTracksLocationManager = new MyTracksLocationManager(this, handler.getLooper(), true);
//        activityRecognitionPendingIntent = PendingIntent.getService(context, 0,
//                new Intent(context, ActivityRecognitionIntentService.class),
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        activityRecognitionClient = new ActivityRecognitionClient(
//                context, activityRecognitionCallbacks, activityRecognitionFailedListener);
//        activityRecognitionClient.connect();
//        voiceExecutor = new PeriodicTaskExecutor(this, new AnnouncementPeriodicTaskFactory());
//        splitExecutor = new PeriodicTaskExecutor(this, new SplitPeriodicTaskFactory());
        sensorMan = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        sensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        sensorRegistered = true;

        sharedPreferences = getSharedPreferences(Constants.SETTINGS_NAME, Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        // onSharedPreferenceChanged might not set recordingTrackId.
        recordingTrackId = PreferencesUtils.RECORDING_TRACK_ID_DEFAULT;

        // Require voiceExecutor and splitExecutor to be created.
        sharedPreferenceChangeListener.onSharedPreferenceChanged(sharedPreferences, null);

        handler.post(registerLocationRunnable);
        Log.d("aa", "Concreate");
        oldLocation = new Location("Point A");
        newLocation = new Location("Point B");
    /*
     * Try to restart the previous recording track in case the service has been
     * restarted by the system, which can sometimes happen.
     */
//        Track track = myTracksProviderUtils.getTrack(recordingTrackId);
//        if (track != null) {
//            restartTrack(track);
//        } else {
//            if (isRecording()) {
//                Log.w(TAG, "track is null, but recordingTrackId not -1L. " + recordingTrackId);
//                updateRecordingState(PreferencesUtils.RECORDING_TRACK_ID_DEFAULT, true);
//            }


        if (PreferencesUtils.getBoolean(this, R.string.is_recording_start, false) == true) {
            distance = (int) PreferencesUtils.getFloat(this, R.string.recorded_distance);
        } else {
            distance = 0;
        }
        Log.d(TAG, "onCreate :" + distance);

        showNotification(false);
//        }
    }


    /*
   * Note that this service, through the AndroidManifest.xml, is configured to
   * allow both MyTracks and third party apps to invoke it. For the onStart
   * callback, we cannot tell whether the caller is MyTracks or a third party
   * app, thus it cannot start/stop a recording or write/update MyTracks
   * database.
   */
    @Override
    public void onStart(Intent intent, int startId) {
        handleStartCommand(intent, startId);
    }

    /*
     * Note that this service, through the AndroidManifest.xml, is configured to
     * allow both MyTracks and third party apps to invoke it. For the
     * onStartCommand callback, we cannot tell whether the caller is MyTracks or a
     * third party app, thus it cannot start/stop a recording or write/update
     * MyTracks database.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleStartCommand(intent, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder) binder;
    }


    /**
     * Stops the service as a foreground service.
     */
    protected void stopForegroundService() {
        stopForeground(true);
    }

    /**
     * Handles start command.
     *
     * @param intent  the intent
     * @param startId the start id
     */
    private void handleStartCommand(Intent intent, int startId) {
        // Check if the service is called to resume track (from phone reboot)
        if (intent != null && intent.getBooleanExtra(RESUME_TRACK_EXTRA_NAME, false)) {
            if (!shouldResumeTrack()) {
                Log.i(TAG, "Stop resume track.");
                updateRecordingState(PreferencesUtils.RECORDING_TRACK_ID_DEFAULT, true);
                stopSelfResult(startId);
                return;
            }
        }
    }


    private String mLastUpdateTime;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {

            mCurrentLocation = location;
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            distance = getUpdatedDistance();
            PreferencesUtils.setFloat(getApplicationContext(), R.string.recorded_distance, distance);
            Log.d(TAG, "onLocationChanged :" + location + "  Cover Distance : " + distance);
        }
    };


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

            if (mCurrentLocation.getAccuracy() < 40) {
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

    /**
     * Returns true if should resume.
     */
    private boolean shouldResumeTrack() {
//        Track track = myTracksProviderUtils.getTrack(recordingTrackId);
//
//        if (track == null) {
//            Log.d(TAG, "Not resuming. Track is null.");
//            return false;
//        }
//        int retries = PreferencesUtils.getInt(this, R.string.auto_resume_track_current_retry_key,
//                PreferencesUtils.AUTO_RESUME_TRACK_CURRENT_RETRY_DEFAULT);
//        if (retries >= MAX_AUTO_RESUME_TRACK_RETRY_ATTEMPTS) {
//            Log.d(TAG, "Not resuming. Exceeded maximum retry attempts.");
//            return false;
//        }
//        PreferencesUtils.setInt(this, R.string.auto_resume_track_current_retry_key, retries + 1);
//
//        if (autoResumeTrackTimeout == PreferencesUtils.AUTO_RESUME_TRACK_TIMEOUT_NEVER) {
//            Log.d(TAG, "Not resuming. Auto-resume track timeout set to never.");
//            return false;
//        } else if (autoResumeTrackTimeout == PreferencesUtils.AUTO_RESUME_TRACK_TIMEOUT_ALWAYS) {
//            Log.d(TAG, "Resuming. Auto-resume track timeout set to always.");
//            return true;
//        }
//
//        if (track.getTripStatistics() == null) {
//            Log.d(TAG, "Not resuming. No trip statistics.");
//            return false;
//        }
//        long stopTime = track.getTripStatistics().getStopTime();
//        return stopTime > 0
//                && (System.currentTimeMillis() - stopTime) <= autoResumeTrackTimeout * ONE_MINUTE;

        return true;
    }

    /**
     * Updates the recording states.
     *
     * @param trackId the recording track id
     * @param paused  true if the recording is paused
     */
    private void updateRecordingState(long trackId, boolean paused) {
        recordingTrackId = trackId;
        PreferencesUtils.setLong(this, R.string.recording_track_id_key, trackId);
        recordingTrackPaused = paused;
        PreferencesUtils.setBoolean(this, R.string.recording_track_paused_key, recordingTrackPaused);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(TrackRecordingService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }

    private int hitCount = 0;
    private double hitSum = 0;
    private double hitResult = 0;

    private boolean isMoving = false;

    private final int SAMPLE_SIZE = 50; // change this sample size as you want, higher is more precise but slow measure.
    private final double THRESHOLD = 0.2;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
            // Shake detection
            double x = mGravity[0];
            double y = mGravity[1];
            double z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = Math.sqrt(x * x + y * y + z * z);
            double delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if (hitCount <= SAMPLE_SIZE) {
                hitCount++;
                hitSum += Math.abs(mAccel);
            } else {
                hitResult = hitSum / SAMPLE_SIZE;

                Log.d(TAG, String.valueOf(hitResult));

                if (hitResult > THRESHOLD) {
                    Log.d(TAG, "Walking");
                    isMoving = true;
                } else {
                    isMoving = false;
                    Log.d(TAG, "Stop Walking");
                }

                hitCount = 0;
                hitSum = 0;
                hitResult = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private class ServiceBinder extends ITrackRecordingService.Stub {

        private TrackRecordingService trackRecordingService;

        public ServiceBinder(TrackRecordingService trackRecordingService) {

            this.trackRecordingService = trackRecordingService;
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void startGps() throws RemoteException {

        }

        @Override
        public void stopGps() throws RemoteException {

        }

        @Override
        public long startNewTrack() throws RemoteException {
            return 0;
        }

        @Override
        public void pauseCurrentTrack() throws RemoteException {

        }

        @Override
        public void resumeCurrentTrack() throws RemoteException {

        }

        @Override
        public void endCurrentTrack() throws RemoteException {

        }

        @Override
        public boolean isRecording() throws RemoteException {
            return false;
        }

        @Override
        public boolean isPaused() throws RemoteException {
            return false;
        }

        @Override
        public long getRecordingTrackId() throws RemoteException {
            return 0;
        }

        @Override
        public long getTotalTime() throws RemoteException {

            time++;
//            PreferencesUtils.setFloat(getApplicationContext(), R.string.recorded_distance, time);
            return time;
        }

        @Override
        public void insertTrackPoint(Location location) throws RemoteException {

        }

        @Override
        public byte[] getSensorData() throws RemoteException {
            return new byte[0];
        }

        @Override
        public int getSensorState() throws RemoteException {
            return 0;
        }

        @Override
        public void updateCalorie() throws RemoteException {

        }

        public float getDistance() {
            return distance;
        }
    }

    private final Runnable registerLocationRunnable = new Runnable() {
        @Override
        public void run() {
//            if (isRecording() && !isPaused()) {
            registerLocationListener();
//            }
            handler.postDelayed(this, 1000 * 60);
        }
    };

    /**
     * Registers the location listener.
     */
    private void registerLocationListener() {
//        if (myTracksLocationManager == null) {
//            Log.e(TAG, "locationManager is null.");
//            return;
//        }
//        try {
////            long interval = locationListenerPolicy.getDesiredPollingInterval();
//            myTracksLocationManager.requestLocationUpdates(
//                    10, 10, locationListener);
////            currentRecordingInterval = interval;
//        } catch (RuntimeException e) {
//            Log.e(TAG, "Could not register location listener.", e);
//        }


//        buildGoogleApiClient();
//
//        mGoogleApiClient.connect();
//
//        if (mGoogleApiClient.isConnected()) {
//            startLocationUpdates();
//        }
        Log.d(TAG, "registerLocationListener :");
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }

        if (!mGoogleApiClient.isConnected()) {
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(LocationServices.API)
//                .addApi(ActivityRecognition.API)
                .build();

        createLocationRequest();
    }

    GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    };


//    GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
//
//    };


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Unregisters the location manager.
     */
    private void unregisterLocationListener() {
//        if (myTracksLocationManager == null) {
//            Log.e(TAG, "locationManager is null.");
//            return;
//        }
//        myTracksLocationManager.removeLocationUpdates(locationListener);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);
        mGoogleApiClient.disconnect();
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {


    }


    @Override
    public void onDestroy() {

        // Reverse order from onCreate
        showNotification(false);

        handler.removeCallbacks(registerLocationRunnable);
        unregisterLocationListener();

        // unregister sharedPreferences before shutting down splitExecutor and voiceExecutor
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);


        // This should be the next to last operation

        super.onDestroy();
    }


    /**
     * Returns true if the service is recording.
     */
    public boolean isRecording() {
        return recordingTrackId != PreferencesUtils.RECORDING_TRACK_ID_DEFAULT;
    }

    /**
     * Returns true if the current recording is paused.
     */
    public boolean isPaused() {
        return recordingTrackPaused;
    }

    /**
     * Shows the notification.
     *
     * @param isGpsStarted true if GPS is started
     */
    private void showNotification(boolean isGpsStarted) {
        if (isRecording()) {
            if (isPaused()) {
                stopForegroundService();
            } else {
                Intent intent = new Intent(this, SlideMainActivity.class)
                        .putExtra("tetstt", recordingTrackId);
                PendingIntent pendingIntent = TaskStackBuilder.create(this)
                        .addParentStack(SlideMainActivity.class).addNextIntent(intent)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                startForegroundService(pendingIntent, R.string.track_record_notification);
            }
            return;
        } else {
            // Not recording
            if (isGpsStarted) {
                Intent intent = new Intent(this, SlideMainActivity.class);
                PendingIntent pendingIntent = TaskStackBuilder.create(this)
                        .addNextIntent(intent).getPendingIntent(0, 0);
                startForegroundService(pendingIntent, R.string.gps_starting);
            } else {
                stopForegroundService();
            }
        }
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
                    mGoogleApiClient, mLocationRequest, locationListener);

//             ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates()

//            Intent intent = new Intent(this, ActivityRecognitionIntentService.class);
//            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, 3000, pendingIntent);

        } catch (Exception ex) {


        }
    }

    protected void startForegroundService(PendingIntent pendingIntent, int messageId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setContentIntent(
                pendingIntent).setContentText(getString(messageId))
                .setContentTitle(getString(R.string.app_name)).setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher).setWhen(System.currentTimeMillis());
        startForeground(1, builder.build());
    }
}