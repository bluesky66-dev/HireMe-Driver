package com.driver.hire_me;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.adaptor.AppController;
import com.adaptor.Driver1;
import com.adaptor.DriverModel;
import com.adaptor.GetCars;
import com.adaptor.LruBitmapCache;
import com.adaptor.TripModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.app.AuthData;
import com.com.driver.webservice.CarActors;
import com.com.driver.webservice.CategoryActors;
import com.com.driver.webservice.DriverConstants;
import com.com.driver.webservice.SingleObject;
import com.crashlytics.android.Crashlytics;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import io.fabric.sdk.android.Fabric;

public class Controller extends AppController implements Application.ActivityLifecycleCallbacks {

    public Pref pref;
   // public String signInResponse;

    public double dLat, dLong;

    public ArrayList<GetCars> carList;

    public static final String TAG = Controller.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Controller mInstance;
    private ArrayList<CategoryActors> categoryResponseList;
    private ArrayList<CarActors> carNameList;
    private double d_lat, d_lng, u_lat, u_lng;
    private String d_licence_path, d_id_proof_path, car_category, car_name;

    private String car_id;
    private String refreshlayout;
    private int refreshValue = 0;
    private static boolean isActive;
    private float counter;
    private String tripDistance;
    private String tripDuration;
    private boolean isDocUpdate;

    public boolean isShowYesNowDialog() {
        return isShowYesNowDialog;
    }

    public void setShowYesNowDialog(boolean showYesNowDialog) {
        isShowYesNowDialog = showYesNowDialog;
    }

    private boolean isShowYesNowDialog;


    private MediaPlayer notificationSound;
    public boolean isAlreadyPlay=false;
    public boolean isFromBackground=false;

    public void playNotificationSound(){
        if(!isAlreadyPlay){
            try{
                if(notificationSound.isPlaying()){
                    notificationSound.setLooping(false);
                    stopNotificationSound();
                    // playSoundNow();
                }else{
                    playSoundNow();
                }
            }catch (Exception e){
                playSoundNow();
            }
        }


    }

    private void playSoundNow() {
        notificationSound = MediaPlayer.create(this, R.raw.request_sound);
        //  notificationSound.setLooping(true);
        notificationSound.start();
        isAlreadyPlay=true;
    }

    public void stopNotificationSound(){
        try{
            if(notificationSound.isPlaying()){
                notificationSound.stop();
                notificationSound.release();
            }
        }catch (Exception e){

        }
        isAlreadyPlay=false;

    }



    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    private String car_model;


    public boolean isDocUpdate() {
        return isDocUpdate;
    }

    public void setDocUpdate(boolean docUpdate) {
        isDocUpdate = docUpdate;
    }

    public SingleObject getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(SingleObject profileDetails) {
        this.profileDetails = profileDetails;
    }

    private SingleObject profileDetails;

    private String DriverLat, DriverLng;


    private String fareReviewCalled;

    private boolean isNotiCome;

    private com.app.DriverModel loggedDriver;

    private TripModel currentTripModel;


    String checkDistanceUnit() {
         String distanceUnit = "";
        for (DriverConstants driverConstants : getConstantsList()) {
            if (driverConstants.getConstant_key().equalsIgnoreCase("distance_paramiter")) {
               distanceUnit = driverConstants.getConstant_value();
            }

        }
        return  distanceUnit;
    }

    public String currencyUnit() {
         String distanceUnit = "";
        for (DriverConstants driverConstants : getConstantsList()) {
            if (driverConstants.getConstant_key().equalsIgnoreCase("currency")) {
               distanceUnit = driverConstants.getConstant_value();
            }

        }
        return  distanceUnit;
    }


    protected PowerManager.WakeLock mWakeLock;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        pref = new Pref(this);
        mInstance = this;
       // MultiDex.install(this);
        registerActivityLifecycleCallbacks(this);
        handleLoggedResponse();

        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }


    public void handleLoggedResponse() {
        String siginResponse = pref.getSIGN_IN_RESPONSE();
        if (siginResponse != null) {
        loggedDriver=    com.app.DriverModel.parseJson(siginResponse);
            AuthData.getInstance().setDriverModel(loggedDriver);
           /* try {
                JSONObject jsonObject = new JSONObject(siginResponse);
                JSONObject childObject = jsonObject.getJSONObject("response");
                loggedDriver = Driver1.parseJson(childObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    }


    public boolean isDriverLoggedIn() {
        return this.loggedDriver != null;
    }

    public com.app.DriverModel getLoggedDriver() {
        return loggedDriver;
    }


    public TripModel getCurrentTripModel() {
        return currentTripModel;
    }

    public void setCurrentTripModel(TripModel currentTripModel) {
        this.currentTripModel = currentTripModel;
        this.pref.saveTripData(currentTripModel);
    }

    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }


    public static synchronized Controller getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    private final int MAX_ATTEMPTS = 5;
    private final int BACKOFF_MILLI_SECONDS = 2000;
    private final Random random = new Random();
    private String pickDistances;
    private String pickDurations;

    private String d_rating;
    private String trip_promo_amt;

    public String trip_to_loc, trip_from_loc;
//    public TripHistoryActors tripActor;


    public boolean showdialog;

    public ArrayList<DriverConstants> getConstantsList() {
        return constantsList;
    }

    public void setConstantsList(ArrayList<DriverConstants> constantsList) {
        this.constantsList = constantsList;
    }

    public ArrayList<DriverConstants> constantsList;


    public String getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(String tripDistance) {
        this.tripDistance = tripDistance;
    }

    public String getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(String tripDuration) {
        this.tripDuration = tripDuration;
    }

    public float getCounter() {
        return counter;
    }

    public void setCounter(float counter) {
        this.counter = counter;
    }

    public boolean getNotiCome() {
        return isNotiCome;
    }

    public void setIsNotiCome(boolean isNotiCome) {
        this.isNotiCome = isNotiCome;
    }

    public boolean isShowdialog() {
        return showdialog;
    }

    public void setShowdialog(boolean showdialog) {
        this.showdialog = showdialog;
    }

    public int getRefreshValue() {
        return refreshValue;
    }

    public void setRefreshValue(int refreshValue) {
        this.refreshValue = refreshValue;
    }

    public String getRefreshlayout() {
        return refreshlayout;
    }

    public void setRefreshlayout(String refreshlayout) {
        this.refreshlayout = refreshlayout;
    }


    public String getTrip_promo_amt() {
        return trip_promo_amt;
    }

    public void setTrip_promo_amt(String trip_promo_amt) {
        this.trip_promo_amt = trip_promo_amt;
    }

    public String getCar_id() {
        return car_id;
    }

    public String getD_rating() {
        return d_rating;
    }

    public void setD_rating(String d_rating) {
        this.d_rating = d_rating;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public ArrayList<GetCars> getCarList() {
        return carList;
    }

    public void setCarList(ArrayList<GetCars> carList) {
        this.carList = carList;
    }

    public String getTrip_to_loc() {
        return trip_to_loc;
    }

    public void setTrip_to_loc(String trip_to_loc) {
        this.trip_to_loc = trip_to_loc;
    }

    public String getTrip_from_loc() {
        return trip_from_loc;
    }

    public void setTrip_from_loc(String trip_from_loc) {
        this.trip_from_loc = trip_from_loc;
    }


    public String getD_licence_path() {
        return d_licence_path;
    }

    public void setD_licence_path(String d_licence_path) {
        this.d_licence_path = d_licence_path;
    }

    public String getD_id_proof_path() {
        return d_id_proof_path;
    }

    public void setD_id_proof_path(String d_id_proof_path) {
        this.d_id_proof_path = d_id_proof_path;
    }

    public String getCar_category() {
        return car_category;
    }

    public void setCar_category(String car_category) {
        this.car_category = car_category;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public double getdLong() {
        return dLong;
    }

    public void setdLong(double dLong) {
        this.dLong = dLong;
    }

    public double getD_lat() {
        return d_lat;
    }

    public void setD_lat(double d_lat) {
        this.d_lat = d_lat;
    }

    public double getD_lng() {
        return d_lng;
    }

    public void setD_lng(double d_lng) {
        this.d_lng = d_lng;
    }

    public double getU_lat() {
        return u_lat;
    }

    public void setU_lat(double u_lat) {
        this.u_lat = u_lat;
    }

    public double getU_lng() {
        return u_lng;
    }

    public void setU_lng(double u_lng) {
        this.u_lng = u_lng;
    }

    public Map<String, String> getNotificationMessage() {
        return notificationMessage;
    }

    private Map<String, String> notificationMessage;

    public void setNotificationMessage(Map<String, String> notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public ArrayList<CategoryActors> getCategoryResponseList() {
        return categoryResponseList;
    }

    public void setCategoryResponseList(ArrayList<CategoryActors> categoryResponseList) {
        this.categoryResponseList = categoryResponseList;
    }

    public ArrayList<CarActors> getCarNameList() {
        return carNameList;
    }

    public void setCarNameList(ArrayList<CarActors> carNameList) {
        this.carNameList = carNameList;
    }


 /*   public String getSignInResponse() {
        return signInResponse;
    }*/

    public void setSignInResponse(String signInResponse) {
      //  this.signInResponse = signInResponse;

        pref.setSIGN_IN_RESPONSE(signInResponse);
    loggedDriver=    com.app.DriverModel.parseJson(signInResponse);

        AuthData.getInstance().setDriverModel(loggedDriver);
    }

    public String getPickDistances() {
        return pickDistances;
    }

    public void setPickDistances(String pickDistances) {
        this.pickDistances = pickDistances;
    }

    public String getPickDurations() {
        return pickDurations;
    }

    public void setPickDurations(String pickDurations) {
        this.pickDurations = pickDurations;
    }

    public String getPickupLocation1() {
        return pickupLocation1;
    }

    public void setPickupLocation1(String pickupLocation1) {
        this.pickupLocation1 = pickupLocation1;
    }


    private String pickupLocation1;
    private Location pickupLocation;
    private Location driverLocation;

    public String getFareReviewCalled() {
        return fareReviewCalled;
    }

    public void setFareReviewCalled(String fareReviewCalled) {
        this.fareReviewCalled = fareReviewCalled;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(Location pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Location getDriverLocation() {
        return driverLocation;
    }

    public void setDriverLocation(Location driverLocation) {
        this.driverLocation = driverLocation;
    }

    public String getDriverLat() {
        return DriverLat;
    }

    public void setDriverLat(String driverLat) {
        DriverLat = driverLat;
    }

    public String getDriverLng() {
        return DriverLng;
    }

    public void setDriverLng(String driverLng) {
        DriverLng = driverLng;
    }

    void register(final Context context, final String regId) {

        Log.i(Config.TAG, "registering device (regId = " + regId + ")");


        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);

        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {

            Log.d(Config.TAG, "Attempt #" + i + " to register");

            //Send Broadcast to Show message on screen
            displayMessageOnScreen(context, context.getString(
                    R.string.server_registering, i, MAX_ATTEMPTS));

            // Post registration values to web server

            GCMRegistrar.setRegisteredOnServer(context, true);

            //Send Broadcast to Show message on screen
            String message = context.getString(R.string.server_registered);
            displayMessageOnScreen(context, message);

            return;
        }

        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);

        //Send Broadcast to Show message on screen
        displayMessageOnScreen(context, message);
    }

    // Unregister this account/device pair within the server.
    void unregister(final Context context, final String regId) {

        Log.i(Config.TAG, "unregistering device (regId = " + regId + ")");


        GCMRegistrar.setRegisteredOnServer(context, false);
        String message = context.getString(R.string.server_unregistered);
        displayMessageOnScreen(context, message);
    }

    // Checking for all possible internet providers
    public boolean isConnectingToInternet() {

        ConnectivityManager connectivity =
                (ConnectivityManager) getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    // Notifies UI to display a message.
    public void displayMessageOnScreen(Context context, String message) {

        Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(Config.EXTRA_MESSAGE, message);
        System.out.println("Message send to Broad Cast from controller" + message);
        // Send Broadcast to Broadcast receiver with message
        context.sendBroadcast(intent);

    }


    public static boolean isActivityVisible() {
        return isActive;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        isActive = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        isActive = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    InterfaceRefreshProfile refreshInterface;
    public void setRefreshInterface(InterfaceRefreshProfile refreshInterface) {
        this.refreshInterface = refreshInterface;
    }

    public InterfaceRefreshProfile getRefreshInterface() {
        return refreshInterface;
    }
}
