package com.driver.hire_me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.adaptor.DistanceAndTimeViewModel;
import com.adaptor.TripModel;
import com.adaptor.UserModel;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.DriverModel;
import com.app.GoogleSDRoute;
import com.app.Route;
import com.com.driver.webservice.CategoryActors;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.DriverConstants;
import com.com.driver.webservice.DriverHelperMethods;
import com.com.driver.webservice.DriverUpdateLocation;
import com.com.driver.webservice.NearByUsersUtil;
import com.com.driver.webservice.SingleObject;
import com.custom.CustomProgressDialog;
import com.fonts.Fonts;
import com.github.kayvannj.permission_utils.Func2;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.maps.android.SphericalUtil;
import com.grepix.grepixutils.CloudResponse;
import com.grepix.grepixutils.ErrorJsonParsing;
import com.grepix.grepixutils.Utils;
import com.grepix.grepixutils.WebServiceUtil;
import com.helper.AppUtils;
import com.service.DriverLocationServices;
import com.service.LocationService;
import com.service.LocationTrackService;
import com.service.MyFirebaseInstanceIDService;
import com.service.PreferencesUtils;
import com.service.TrackRecordingServiceConnectionUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import static com.helper.ILocationConstants.LOACTION_ACTION;
import static com.helper.ILocationConstants.LOCATION_MESSAGE;
import static com.helper.ILocationConstants.PERMISSION_ACCESS_LOCATION_CODE;

public class SlideMainActivity extends Activity implements LocationListener,
        ConnectionCallbacks, OnConnectionFailedListener, TextToSpeech.OnInitListener, InterfaceRefreshProfile{

    private String d_available = "1";
    private String d_busy = "0";
    private static String is_required_drawRoute = "No";

    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    TextView tv_latitude, tv_longitude, tv_address, tv_area, tv_locality;
    SharedPreferences mPref;
    SharedPreferences.Editor medit;
    Double latitude, longitude;
    Geocoder geocode2;

    private boolean tripRequest;
    private CharSequence mTitle;
    String Driver_trip_status = "Home";
    private static TextView ad_title, tvDistanceYellow, tvTimeYellow, tvDrawerProfileName;

    DistanceAndTimeViewModel distanceAndTimeViewModel;

    private String hide_drawer;
    Timer timer = new Timer();
    static boolean isDriver_lat_lng = false;

    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;

    private Typeface drawerListTypeface;
    static public String distances;
    static public String durations;

    static public ArrayList<String> html_instructions = new ArrayList<String>();

    static public ArrayList<String> maneuver = new ArrayList<String>();
    static public ArrayList<String> dis = new ArrayList<String>();
    static public ArrayList<String> dur = new ArrayList<String>();
    static public ArrayList<Double> starting_lat = new ArrayList<Double>();
    static public ArrayList<Double> starting_long = new ArrayList<Double>();
    static public ArrayList<Double> ending_lat = new ArrayList<Double>();
    static public ArrayList<Double> ending_long = new ArrayList<Double>();
    private AcceptView acceptView;
    private DistanceAndTime distanceAndTimeView;
    private DestinationActivity destinationActivity;
    String tripstatus = "null";
    public static final String PREFS_NAME = "MyPrefsFile";

    private static LinearLayout linearBackNext;
    CountDownTimer count_downt_timer;
    CharSequence caption1;

    private String[] navMenuTitles;
    // ******************* Sliding Menu Variable Start ********************
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ImageView ivSlideScreen;

    private ActionBarDrawerToggle mDrawerToggle;
    ImageView drawerbut;

    private TypedArray navMenuIcons;
    private CharSequence mDrawerTitle;
    SingleObject checkUploadedData;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    // **************************  Google Map Variable Start **********************
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;
    LocationListener loctionListener;
    public GoogleMap mGoogleMap;
    String riderid;
    String acc1;
    protected static final String TAG = "SlideMainActivity";

    String userdetailsid = "3";

    String tripid = null, autotripid;
    String checkuserdetails;

    Controller controller;

    GoogleApiClient googleApiClient = null;

    boolean mUpdatesRequested = false;
    private LatLng center;
    private LinearLayout markerLayout;
    private Geocoder geocoder;
    private List<Address> addresses;

    LatLng latLong;
    URL proimg;
    Bitmap bitmap1;
    String profile_userfname, profile_userlname, profile_paypal_useremail, profile_useremail, profile_usermobile,
            profile_userprofilepic, profile_userpassword;
    private String profile_status;

    boolean logoutcheck = true;
    String trip_driver_status, driver_current_status = "Home";
    RelativeLayout ad,mainRelativeLayout;
    ImageView ad_img;
    TextView ad_tagline,nodocumentalerttext;
    ImageView ad_close;

    Bitmap bitmap;
    private boolean showad = true;


    public String accept = null;
    String regionaddress;

    ProgressBar progressbar;
    LinearLayout touch;
    LinearLayout full;
    int tollamt = 0;
    Button powered;
    TextView touchtext;
    ImageView marker;
    Double lat1, lat2, lat3, lat4;
    Double plat1, plat2, plat3, plat4;
    Double begintrip2lat1, begintrip2lat2;
    double x, y;
    Double startlat;
    Double startlng;
    String User_id, fbuserproimg, WhoLogin, checkpassword, location;
    Button account;
    private static Button fullbutton;
    String status;
    CountDownTimer countDownTimer;
    int i;
    boolean checkonclick = false;
    boolean timercheck = true;
    private boolean whilecheck = true;
    private boolean receivecheck = true;
    private boolean usercheck = false;
    private boolean startstopcheck = false;
    private boolean requestcancelcheck = false;
    private boolean gcmcheck = true;
    private boolean invalidrider = true;
    private boolean checktripend = false;
    String acc = "null";
    String amountf;
    float distance, distancedirection, distanceBtwUserDriver;
    Double ddistance;
    float loc;
    float loc1 = 0, loc2 = 0;
    float wait = 0;
    float amount = 0;
    float totalcheck;
    float tax = 5;
    float finalwait = 0;
    float waitcharge = 0;
    float total = 0;
    float driveramount = 0;
    float taxamount = 0;
    float begintrip2waitcharge = 0;
    float begintrip2amount = 0;
    float begintrip2distance;
    float wait1 = 0, wait2 = 0, amount1 = 0, amount2 = 0, total1 = 0, total2 = 0, distance1 = 0, distance2 = 0;
    String wait1s, wait2s, amount1s, amount2s, total1s, total2s, distance1s, distance2s;
    float precent1, precent2;
    int minfare = 0;
    String currentDateandTime;
    private boolean farecheck = true;
    //private boolean drivercheck=true;
    private boolean Mailcheck = false;
    private boolean accheck;
    private boolean checkfixedamount;
    boolean secondriderflag = false;
    boolean arrivecheck = false;
    String pickups, drops, waits, taxs, totals, arrives, driveramounts;
    private CustomProgressDialog progressDialog;
    int time = 0;
    String FixedAmount, IsFixed, PresentageAmount;
    int FixAmount;
    int Presentage;
    int getusercount = 0;
    int getusercount1 = 0;
    String pickup1;
    // **************************  Google Map  Variable End **********************
    String driverfnamemain, driverlnamemain, drivernamemain, drivermobilemain, driveremail, driverrate;

    String phoneNo;
    String message;
    String reqmessage;
    String text;
    String cancelreason;
    LatLng origin;
    LatLng dest;
    // ************************* Web Variable ************************
    String googleapikey = "";
    private JSONObject driver_jsonobj;
    private String driverjson_status;
    RelativeLayout RL, ET;
    String driveruserid, driverstatus;
    static double driverlong;
    static double driverlat;
    private UserModel userModel;
    LatLng mark;

    String timer1 = null;
    String timeStart;
    String timeStop;

    String currentTime, approxTime, driverDistance;
    Controller aController;
    Bitmap logobitmap;
    String droplat, droplong;
    double droplatd, droplongd;
    double endtlat, endmlng;
    String secondriderid;
    float begintrip2total;

    RelativeLayout navrl;

    String rideshare;
    String availabilitystatus;
    int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 1;

    // ******************** Driver car category details ********************

    String categoryname, max_size, min_fare, price_minute, price_km;
    float max_sizef, min_faref, price_minutef, price_kmf;

    // ********************** current Location Variable **********

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 3; // in Meters

    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 5000; // in Milliseconds

    protected LocationManager locationManager;

    private WaveDrawable waveDrawable;

    String regionaddress1;
    Boolean first = true;

    // Voice navication
    String maneuver12;
    int count;
    double val = 0.1;
    // private TextToSpeech tts;
    int delay = 1000; // delay for 1 sec.
    int period = 5000; // repeat every 10 sec.
    Timer accepttimer = new Timer();
    Timer begintriptimer = new Timer();
    String country;
    //private static Switch switchAvailable;
    private static Switch switchAvailable1, switchDrawerAvailaiabity;
    private ClientPickedUpView clientPickedUpView;
    private static Button goButton;
    private static Button goButton1;
    private BackToOrderView backToOrderView;
    private String reasonMsg;
    private String distancebtw;
    private TextView tvSwitchText;

    SingleObject singleObject = SingleObject.getInstance();
    private AddressLocationView addressLocationView;
    private UserInfoView userInfoView;
    //	private String refreshedToken = null;

    private ArrayList<CategoryActors> categoryResponseList;

    private static double driverLat;
    private static double driverLong;
    private Location gpslocation;
    private boolean isPicked;
    private String fullbuttonText = null;
    private String gobuttonText = null;
    private ImageView imageV;
    private boolean driver_rejected;
    boolean isRequest = true;


    /**
     * This method is used  to add marker on the google map of near by user
     * This toast is set for MyLocationListener class
     * //	Toast.makeText(getApplicationContext(),"Location is :: " + location,Toast.LENGTH_LONG).show();
     */
    private Marker markeruser;
    private Handler handlertime;
    private Handler handlerDistance;
    private ArrayList<DriverConstants> constantsList;
    private TextView distance_temp;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private double tripTax;
    private String driverStatus;
    private MediaPlayer notificationSound;
    private Timer timertast;
    private ImageButton directionButton;


    public void addMarker(ArrayList<LatLng> userLatLngs) {
        if (mGoogleMap != null) {
            LatLng mark1 = new LatLng(driverlat, driverlong);
            markeruser.remove();
            for (LatLng latlng : userLatLngs) {
                MarkerOptions marker = new MarkerOptions();
                marker.position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_20));
                markeruser = mGoogleMap.addMarker(marker);
            }

        } else {
        }
    }

    Handler handler;

    /*
            Location updates after each 20 secondes
     */

    double lastUpdateLat=0.0;
    double lastUpadateLang=0.0;


    public void workerForTimer() {
        if (ActivityCompat.checkSelfPermission(SlideMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SlideMainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            Location gpslocation2;
            gpslocation2 = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            currentlatitude=gpslocation2.getLatitude();

            currentlongitude=gpslocation2.getLongitude();
            final DriverUpdateLocation driverUpdateLocation = new DriverUpdateLocation(getApplicationContext());
            if (currentlatitude == 0.0 || currentlongitude == 0.0) {
                return;
            } else {
                Location locationA = new Location("point A");
                locationA.setLatitude(lastUpdateLat);
                locationA.setLongitude(lastUpadateLang);
                Location locationB = new Location("point B");
                locationB.setLatitude(currentlatitude);
                locationB.setLongitude(currentlongitude);
                double distance = locationA.distanceTo(locationB);
               // if(distance>=100){
                    updateDriverProfileApi(currentlatitude, currentlongitude);
                    lastUpdateLat=currentlatitude;
                    lastUpadateLang=currentlongitude;

              //  }
                controller.setD_lat(currentlatitude);
                controller.setD_lng(currentlongitude);
                driverlat = currentlatitude;
                driverlong = currentlongitude;
                controller.pref.setDriver_Lat(String.valueOf(driverlat));
                controller.pref.setDriver_Lng(String.valueOf(driverlong));
                controller.setD_lat(driverlat);
                controller.setD_lng(driverlong);
                String tripstatus=controller.pref.getTRIP_STATUS2();
                if(tripstatus.equalsIgnoreCase("accept")||tripstatus.equalsIgnoreCase("arrive")||tripstatus.equalsIgnoreCase("begin")){
                    markeruser.remove();

                }else{
                    getdriverdetails(driverlat, driverlong);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isUpdate = false;

    public void updateDriverProfileApi(double lat, double lng) {

        if (isUpdate) {
            return;
        } else {
            isUpdate = true;
            // final Controller controller= (Controller) context;
            SingleObject singleObject = SingleObject.getInstance();

            final String driver_apikey = controller.pref.getAPI_KEY();
            final String driverId = controller.pref.getDRIVER_ID();
            final String d_lat = Double.toString(lat);
            final String d_lng = Double.toString(lng);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PROFILE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            isUpdate = false;
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            isUpdate = false;
                            // dialog.dismiss();
                            if (error instanceof NoConnectionError) {
                            } else if (error instanceof ServerError) {
                                String d = new String(error.networkResponse.data);
                                try {
                                    JSONObject jso = new JSONObject(d);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", driver_apikey);
                    params.put("driver_id", driverId);
                    params.put("d_lat", d_lat);
                    params.put("d_lng", d_lng);

                    return params;

                }

            };


            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    500000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(SlideMainActivity.this);
            requestQueue.add(stringRequest);

        }


    }



    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_activity_main);

        controller = (Controller) getApplicationContext();
        singleObject = SingleObject.getInstance();
        getActionBar().hide();
        is_required_drawRoute = "No";
        singleObject.setIsChangeMag(is_required_drawRoute);
        driverStatus = d_available;
        progressDialog =new CustomProgressDialog(SlideMainActivity.this);


        getConstantApi();
        getCarCategoryApi();
        controller.setShowdialog(false);
        setTripRequest(false);

        try{
            if (!controller.pref.getTRIP_STATUS2().equals("begin")) {
                controller.pref.setCalCulatedDistance(0.0);
            }
        }catch (Exception e){
            controller.pref.setTRIP_STATUS2("no");
            controller.pref.setCalCulatedDistance(0.0);
        }


        controller.setFareReviewCalled("notcalled");



       // setupMap();


        goButton = (Button) findViewById(R.id.fullbutton2);
        fullbutton = (Button) findViewById(R.id.fullbutton);
        ivSlideScreen = (ImageView) findViewById(R.id.iv_slide_screen_id);
        drawerListTypeface = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_CONDENCE);
        acceptView = new AcceptView(this, findViewById(R.id.pickup_request), drawerListTypeface);
        tvSwitchText = (TextView) findViewById(R.id.tv_availiabiliyt_text_id);
        distance_temp = (TextView) findViewById(R.id.distance_temp);
        tvDistanceYellow = (TextView) findViewById(R.id.tv_distance_yellow);
        tvTimeYellow = (TextView) findViewById(R.id.tv_time_yellow);
        progressbar = (ProgressBar) findViewById(R.id.progresstimer);
        linearBackNext = (LinearLayout) findViewById(R.id.linear_back_next_layout_id);
        imageV = (ImageView) findViewById(R.id.iii);
        addressLocationView = new AddressLocationView(SlideMainActivity.this, findViewById(R.id.location_address_layout_id), drawerListTypeface);
        addressLocationView.hide();
        driverStatus = singleObject.getDriver_is_available();

        ProfileDetails();

        ivSlideScreen.setVisibility(View.GONE);


        checkOfflineStatus();

        try{
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if (refreshedToken != null) {
                MyFirebaseInstanceIDService.driverTokenUpateProfile(getApplicationContext(), controller, refreshedToken);
            }
        }catch (Exception e){

        }


        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            // Do something for lollipop and above versions
            int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            int hasSMSPermission = checkSelfPermission(Manifest.permission.SEND_SMS);

            List<String> permissions = new ArrayList<String>();

            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {

                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

            }

            if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {

                permissions.add(Manifest.permission.SEND_SMS);

            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            }
        }



        linearBackNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                destinationActivity.hide();
                linearBackNext.setVisibility(View.GONE);

            }
        });

        Intent i = getIntent();
        location = i.getStringExtra("location");
        timer1 = i.getStringExtra("timer");
        userdetailsid = controller.pref.getTRIP_ID();


        destinationActivity = new DestinationActivity(this, findViewById(R.id.endtrip_request));
        destinationActivity.hide();
        destinationActivity.setDestinationActivityCallBack(new DestinationActivity.DestinationActivityCallBack() {
            @Override
            public void onYESButtonCliked() {

                updateTripStatusApi("end", controller.pref.getTRIP_ID(), "");

            }

            @Override
            public void onNOButtonClicked() {
                backToOrderView = new BackToOrderView(SlideMainActivity.this, findViewById(R.id.no_client_reached));

                backToOrderView.show();
                showDistanceAndTimeView();

                backToOrderView.setBackToOrderCallback(new BackToOrderView.BackToOrderViewCallback() {
                    @Override
                    public void onOkButtonClicked() {
                        //	reasonMsg = backToOrderView.editText.getText().toString();
                        String reasonMsg1 = backToOrderView.editText.getText().toString();
                        reasonMsg = reasonMsg1.trim();
                        if (reasonMsg.length() == 0) {
                            Toast.makeText(getApplicationContext(), "Please give reason..." + reasonMsg, Toast.LENGTH_LONG).show();

                        } else {
                            //backToOrderView.okButton.setEnabled(true);
                            driverUpdateProfile("1", false, "0");
                            updateTripStatusApi("driver_cancel_at_drop", controller.pref.getTRIP_ID(), reasonMsg);


                        }

                    }

                    @Override
                    public void onBackButtonClicked() {

                    }

                    @Override
                    public void onNextButtonClicked() {

                    }
                });

            }

        });
        LocalBroadcastManager.getInstance(SlideMainActivity.this).registerReceiver(mNotificationReceiver, new IntentFilter("some_custom_id"));
        clientPickedUpView = new ClientPickedUpView(SlideMainActivity.this, findViewById(R.id.client_pickup_view));
        clientPickedUpView.hide();

        clientPickedUpView.setClientPickedUpCallback(new ClientPickedUpView.ClientPickedUpCallback() {

            public void onYesButtonClicked() {
                //tts.speak("Your Trip Begin now", TextToSpeech.QUEUE_FLUSH, null);
                changeAfterClientPick();
            }

            public void onNoButtonClicked() {

                clientPickedUpView.hide();
                backToOrderView = new BackToOrderView(SlideMainActivity.this, findViewById(R.id.no_client_reached));

                backToOrderView.show();
                backToOrderView.relativeLayout.setVisibility(View.VISIBLE);
                backToOrderView.linearbackNext.setVisibility(View.GONE);
                setClientPickedupView(false);

                setDriver_rejected(true);

                showDistanceAndTimeView();

                //  String reasonMsg = backToOrderView.editText.getText().toString();
                backToOrderView.setBackToOrderCallback(new BackToOrderView.BackToOrderViewCallback() {
                    // for pickedup view or after pickedup button clicked
                    @Override
                    public void onOkButtonClicked() {


                        String reasonMsg1 = backToOrderView.editText.getText().toString();
                        reasonMsg = reasonMsg1.trim();

                        if (reasonMsg.length() == 0) {
                            Toast.makeText(getApplicationContext(), "Please give reason...", Toast.LENGTH_LONG).show();

                        } else {


                            // sendNotificationToUser("driver_cancel");
                            updateTripStatusApi("driver_cancel_at_pickup", controller.pref.getTRIP_ID(), reasonMsg);


                        }
                    }

                    @Override
                    public void onBackButtonClicked() {

                    }

                    @Override
                    public void onNextButtonClicked() {


                    }
                });

            }
        });

        approxTime = "26";
        distancebtw = "29";
        distanceAndTimeView = new DistanceAndTime(this, findViewById(R.id.distance_time_set), distancebtw, approxTime, currentTime);
        distanceAndTimeView.hide();

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        googleapikey = getResources().getString(R.string.googleapikey1);
        regionaddress1 = sharedPreferences.getString(" regionaddress", null);

        status = sharedPreferences.getString("availabilitystatus", null);
        //switchAvailable1.setChecked(true);
        driverstatus = availabilitystatus;
        touchtext = (TextView) findViewById(R.id.touchtext);
        touch = (LinearLayout) findViewById(R.id.touch);       //progress bar layout
        full = (LinearLayout) findViewById(R.id.full);
        markerLayout = (LinearLayout) findViewById(R.id.locationMarker);
        marker = (ImageView) findViewById(R.id.marker);
        account = (Button) findViewById(R.id.account);
        account.setVisibility(View.INVISIBLE);
        ad = (RelativeLayout) findViewById(R.id.ad_layout);
        ad.setVisibility(View.GONE);
        ad_img = (ImageView) findViewById(R.id.ad_img);
        ad_title = (TextView) findViewById(R.id.adTitle);
        ad_tagline = (TextView) findViewById(R.id.tag_line);
        nodocumentalerttext = findViewById(R.id.tv_document_alert);

        checkDocumentUploaded();

        ad_close = (ImageView) findViewById(R.id.close);

        isDriver_lat_lng = false;


        navMenuTitles = getResources().getStringArray(R.array.profile_list);
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navrl = (RelativeLayout) findViewById(R.id.rv);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        drawerbut = (ImageView) findViewById(R.id.icdrawer);

        handler = new Handler();
        handler.postDelayed(handlerRunnable, 2000);


        timertast = new Timer();
        timertast.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
             SlideMainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        workerForTimer();
                        handler.postDelayed(this, 15000);
                    }
                });
            }
        }, 0, 15000);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        mDrawerLayout.openDrawer(navrl);
        mDrawerLayout.closeDrawer(navrl);

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        //Review history
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems, drawerListTypeface);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);



        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {

                getActionBar().setTitle(R.string.arcane_driver);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(R.string.arcane_driver);
            }
        };

        drawerbut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDrawerLayout.openDrawer(navrl);


            }

        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // ******************* Current Location function *************

        aController = (Controller) getApplicationContext();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {


            locationManager.requestLocationUpdates(

                    LocationManager.GPS_PROVIDER,

                    MINIMUM_TIME_BETWEEN_UPDATES,

                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,

                    new MyLocationListener()

            );
        }


        if (fullbutton.getText().length() == 0) {
            fullbutton.setVisibility(View.GONE);
        } else if (goButton.getText().length() == 0) {
            goButton.setVisibility(View.GONE);
        } else {

        }

        /**
         Initially full layout and fullbutton should hide, in fullbutton is in full layout so both should be hide
         **/


        full.setVisibility(View.GONE);
        fullbutton.setVisibility(View.GONE);


        try {
            fullbuttonText = controller.pref.getFULLBUTTON_CAPTION();
            gobuttonText = controller.pref.getGOBUTTON_CAPTION();

            if (fullbuttonText.length() == 0 && gobuttonText.length() == 0) {
                controller.pref.setFULLBUTTON_CAPTION("first");
                controller.pref.setGOBUTTON_CAPTION("second");

                fullbuttonText = controller.pref.getFULLBUTTON_CAPTION();
                gobuttonText = controller.pref.getGOBUTTON_CAPTION();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }



        // *************************************************

        touch.setVisibility(View.GONE);
        marker.setEnabled(false);
        marker.setVisibility(View.INVISIBLE);
        marker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                checkonclick = true;
                v.clearAnimation();

            }
        });

        String device_token = controller.pref.getD_DEVICE_TOKEN();



        fullbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (net_connection_check()) {
                    CharSequence caption;
                    //caption = fullbutton.getText().toString();
                    caption = controller.pref.getFULLBUTTON_CAPTION();
                    controller.pref.setFULLBUTTON_CAPTION((String) caption);
                    if (((String) caption).equalsIgnoreCase("Go")) {
                        controller.pref.setFULLBUTTON_CAPTION("Pick");
                        controller.pref.setGOBUTTON_CAPTION("");
                        controller.pref.setLocalTripState("Pick");

                        fullbutton.setText(R.string.pick);
                        ifGoCaption();

                    } else if (((String) caption).equalsIgnoreCase("Pick")) {

                        if (controller.isShowYesNowDialog()){
                            isPickCaption();

                            clientPickedUpView.show();
                        }else{
                            changeAfterClientPick();
                        }



                    } else {

                    }
                }
            }
        });

        goButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CharSequence caption2;
                caption2 = controller.pref.getGOBUTTON_CAPTION();

                if (((String) caption2).equalsIgnoreCase("Go")) {
                    isSecondGoCaption();
                } else if (((String) caption2).equalsIgnoreCase("End Trip") || caption2 == "End Trip") {
                    if(controller.isShowYesNowDialog()){
                        controller.pref.setIS_FARESUMMARY_OPEN("open_destination_view");
                        showDistanceAndTimeView();

                        // goButton.setEnabled(true);
                        goButton.setVisibility(View.GONE);
                        goButton.setText("");
                        controller.pref.setGOBUTTON_CAPTION("");
                        mGoogleMap.clear();
                        // mGoogleMap.setTrafficEnabled(true);
                        distanceAndTimeView.hide();
                        destinationActivity.show();
                    }else{
                        destinationActivity.hide();
                        updateTripStatusApi("end", controller.pref.getTRIP_ID(), "");
                    }




                } else {

                }

            }

        });


        directionButton = (ImageButton) findViewById(R.id.direction_icon);
        directionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String dirUrl;

                if (controller.pref.getTRIP_STATUS2().equals("begin")) {
                    dirUrl = "http://maps.google.com/maps?saddr="
                            + controller.pref.getTRIP_SCHEDULED_PICK_LAT() + "," + controller.pref.getTRIP_SCHEDULED_PICK_LNG() +
                            "&daddr=" + controller.pref.getTRIP_SCHEDULED_DROP_LAT() + "," + controller.pref.getTRIP_SCHEDULED_DROP_LNG();
                } else {
                    dirUrl = "http://maps.google.com/maps?saddr="
                            + controller.pref.getD_LAT() + "," + controller.pref.getD_LNG() +
                            "&daddr=" + controller.pref.getTRIP_SCHEDULED_PICK_LAT() + "," + controller.pref.getTRIP_SCHEDULED_PICK_LNG();
                }
                // startService(new Intent(SlideMainActivity.this, ChatHeadService.class));
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(dirUrl));
                startActivity(intent);

            }
        });


        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();

        } else { // Google Play Services are available
            // Getting reference to the SupportMapFragment
            // Create a new global location parameters object
            mLocationRequest = LocationRequest.create();
                /*
                 * Set the update interval
                 */
            mLocationRequest.setInterval(GData.UPDATE_INTERVAL_IN_MILLISECONDS);
            // Use high accuracy
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            // Set the interval ceiling to one minute
            mLocationRequest.setFastestInterval(GData.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
            // Note that location updates are off until the user turns them on
            mUpdatesRequested = false;

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            googleApiClient.connect();
        }

// **************************  Google Map Code End **********************

        GCMRegistrar.checkDevice(this);
        // Make sure the manifest permissions was properly set
        GCMRegistrar.checkManifest(this);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));

        fn_permission();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        try {
            if (Integer.parseInt(controller.pref.getTRIP_ID()) > 0) {
                // getTripState();
                checkTripState();
                handleProfileUpdate();
            }

        } catch (Exception e) {

        }

        locationReceiver = new LocationReceiver();
        startDistanceServiceWithDistance();

        //  startDistanceServiceWithDistance();
    }

    private void checkDocumentUploaded() {
        singleObject = SingleObject.getInstance();
        singleObject.driverLoginParseApi(controller.pref.getSIGN_IN_RESPONSE());

            if (singleObject.getD_license_image_path()==null||singleObject.getD_license_image_path().equals("")||
                    singleObject.getD_license_image_path().equals("null")||singleObject.getD_rc_image_path()==null||singleObject.getD_rc_image_path().equals("")
                    ||singleObject.getD_rc_image_path().equals("null")||singleObject.getD_insurance_image_path()==null
                    ||singleObject.getD_insurance_image_path().equals("")||singleObject.getD_insurance_image_path().equals("null"))

            {
                nodocumentalerttext.setVisibility(View.VISIBLE);

                nodocumentalerttext.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SlideMainActivity.this,DriverProfileActivity.class);
                        startActivity(intent);
                    }
                });
            }else {
                nodocumentalerttext.setVisibility(View.GONE);
            }
    }


    private void changeAfterClientPick() {
        LinearLayout linear = (LinearLayout) findViewById(R.id.full);
        linear.setVisibility(View.GONE);
        fullbutton.setText("");
        controller.pref.setFULLBUTTON_CAPTION("");
        fullbutton.setVisibility(View.GONE);

        driverStatus = d_busy;

        goButton.setVisibility(View.VISIBLE);
        goButton.setText(R.string.begin_trip);
        controller.pref.setGOBUTTON_CAPTION("Go");
        controller.pref.setLocalTripState("picked");

        controller.pref.setTIME_DISTANCE_VIEW(true);
        clientPickedUpView.hide();
        setClientPickedupView(false);

        showDistanceAndTimeView();
    }

    private void checkOfflineStatus() {
        try{
            SingleObject obj = SingleObject.getInstance();
            obj.driverLoginParseApi(controller.pref.getSIGN_IN_RESPONSE());
            switchAvailable1 = (Switch) findViewById(R.id.available_switch);
            switchAvailable1.setChecked(obj.getDriver_is_available().equals("1")?true:false);
            tvSwitchText.setText(obj.getDriver_is_available().equals("1")?R.string.availability_ON:R.string.availability_OFF);
            tvSwitchText.setTypeface(drawerListTypeface);
            switchAvailable1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        switchAvailable1.setChecked(true);
                        tvSwitchText.setText(R.string.availability_ON);
                        driverUpdateProfile("1", false, "0");
                        isSearchingUser = false;
                    } else {
                        switchAvailable1.setChecked(false);
                        tvSwitchText.setText(R.string.availability_OFF);
                        driverUpdateProfile("0", false, "0");
                    }
                }
            });
        }catch (Exception e){

        }

    }

    private void ProfileDetails() {
        tvDrawerProfileName = (TextView) findViewById(R.id.tv_drawerProfileName);
        tvDrawerProfileName.setText(singleObject.getdFname());
        tvDrawerProfileName.setTypeface(drawerListTypeface);
        boolean profile_img = controller.pref.getPROFILE_IMAGE();
        if (profile_img == true) {
            try {
                String fullpath = controller.pref.getDRIVER_PROFILE_IMG_PATH();
                Picasso.with(SlideMainActivity.this).load(fullpath).into(imageV);
            } catch (Exception e) {

            }

        } else {
            imageV.setBackgroundResource(R.drawable.circleduserwhite);
        }
    }




    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        unregisterReceiver(broadcastReceiverDriverLocation);
    }


    BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try{
                if (intent.getAction().equals(com.app.Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(com.app.Config.TOPIC_GLOBAL);

                } else if (intent.getAction().equals(com.app.Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                }
                Map<String, String> newMessageData = controller.getNotificationMessage();
                String newMessage = newMessageData.get(Constants.Keys.TRIP_STATUS);
                if (newMessage != null || !newMessage.equals("null")) {
                    if (newMessage.equalsIgnoreCase(Constants.Keys.Request)) {
                        cometRequestNotificaton();
                    } else if (newMessage.equals("Split Mode")) {
                        Driver_trip_status = "Split Mode";
                        secondriderflag = true;
                        getdroplocation();
                        rideshare = "yes";
                    } else if (newMessage.equalsIgnoreCase("cancel")) {
                        if (timercheck) {
//                        sendacktorider();
                        } else {
                            requestcanceled();
                        }
                    } else {

                    }
                }
            }catch (Exception e){

            }

        }

    };

 /*   protected void finalize() throws Throwable {
        if (mHandleMessageReceiver != null) {
            this.unregisterReceiver(mHandleMessageReceiver);
        }
        super.finalize();
    }*/
    private boolean isNewRequest=false;
    public void onResume() {
        super.onResume();
        ProfileDetails();
        checkDocumentUploaded();
        setupMap();
        registerReceiver(broadcastReceiverDriverLocation, new IntentFilter(DriverLocationServices.str_receiver));


        controller.setDocUpdate(false);
        String check = fullbutton.getText().toString();
        if (controller.pref.getIsPush()) {
            controller.pref.setIsPush(false);
            if (controller.pref.getTRIP_STATUS2().equalsIgnoreCase("request")) {
                acceptView.hide();
                isNewRequest=true;
                touch.setVisibility(View.GONE);
                tvDistanceYellow.setVisibility(View.GONE);
                tvTimeYellow.setVisibility(View.GONE);
                cometRequestNotificaton();
            }

        }else{
        if (tripModelTemp == null ) {
            if(controller.pref.getTRIP_ID()==null||controller.pref.getTRIP_ID().equals("0")||controller.pref.getTRIP_ID().equals("")||isNewRequest){
            }else{
                cometRequestNotificaton();
            }

        }
    }




        drawerbut.setVisibility(View.VISIBLE);

        //getCarCategoryApi();


    }

    Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            workerForTimer();
            handler.postDelayed(this, 15000);
        }
    };



    Runnable handlerUpdateTime = new Runnable() {
        @Override
        public void run() {
            showDistanceAndTimeView();
            handler.postDelayed(this, 1000);
        }
    };


    // **************************  Google Map Function Start **********************

    public void requestcanceled() {

        requestcancelcheck = true;

        String diatitle = "Request cancelled.";
        String msg = "Request cancelled by Rider.";

        marker.setEnabled(false);
      //  mGoogleMap.clear();
        fullbutton.setVisibility(View.GONE);
        usercheck = false;
        touch.setVisibility(View.GONE);
        marker.setVisibility(View.INVISIBLE);
        checkonclick = false;
        timercheck = true;
        whilecheck = true;
        accheck = true;
        receivecheck = true;

        acc1 = acc;

        registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));
        dialogshow(diatitle, msg);

    }

    boolean isFirst = true;

    @SuppressLint("NewApi")
    private void setupMap() {
        try {
            ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                    SingleObject obj = SingleObject.getInstance();
                    if (isFirst) {
                        markeruser = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.no_image_row)));
                    }


                    is_required_drawRoute = obj.getIsChangeMag();
                    if (is_required_drawRoute.equalsIgnoreCase("Yes1") || is_required_drawRoute == "Yes1") {

                        try {
                            String destLat = controller.pref.getTRIP_SCHEDULED_PICK_LAT();
                            double dsLat = Double.parseDouble(destLat);
                            String destLng = controller.pref.getTRIP_SCHEDULED_PICK_LNG();
                            double dsLng = Double.parseDouble(destLng);
                            String originLat = controller.pref.getD_LAT();
                            double orLat = Double.parseDouble(originLat);
                            String originLng = controller.pref.getD_LNG();
                            double orLng = Double.parseDouble(originLng);

                            drawDriverRoute(dsLat, dsLng, orLat, orLng);

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (is_required_drawRoute.equalsIgnoreCase("Yes2") || is_required_drawRoute == "Yes2") {
                        try {
                            String pickLat = controller.pref.getTRIP_SCHEDULED_PICK_LAT();
                            double pikLat = Double.parseDouble(pickLat);
                            String pickLng = controller.pref.getTRIP_SCHEDULED_PICK_LNG();
                            double pikLng = Double.parseDouble(pickLng.trim());
                            String dropLat = controller.pref.getTRIP_SCHEDULED_DROP_LAT();
                            double drop_Lat = Double.parseDouble(dropLat.trim());
                            String dropLng = controller.pref.getTRIP_SCHEDULED_DROP_LNG();
                            double drop_Lng = Double.parseDouble(dropLng);
                            drawDriverRoute(drop_Lat, drop_Lng, pikLat, pikLng);

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                    }

                }
            });



            if (mGoogleMap != null) {
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
            // Zoom Control Position
            View zoomControls = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getView().findViewById(0x1);

            if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                // ZoomControl is inside of RelativeLayout
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

                // Align it to - parent top|left
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                // Update margins, set to 10dp
                final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics());
                params.setMargins(margin, margin, margin, margin + 200);

            }

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
            if (mGoogleMap != null) {

                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
            View locationButton = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getView().findViewById(2);

// and next place it, for exemple, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            rlp.setMargins(0, 230, 30, 0);

            if (usercheck == false) {
                if (mGoogleMap != null) {
                    gpslocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    if (gpslocation == null) {
                        // last save location  if   current location is not found
                        driverlat = Double.parseDouble(controller.pref.getDriver_Lat());
                        driverlong = Double.parseDouble(controller.pref.getDriver_Lng());
                        latLong = new LatLng(driverlat, driverlong);
                    } else {
                        latLong = new LatLng(gpslocation.getLatitude(), gpslocation.getLongitude());
                        if (latLong == null) {
                            driverlat = Double.parseDouble(controller.pref.getDriver_Lat());
                            driverlong = Double.parseDouble(controller.pref.getDriver_Lng());
                            latLong = new LatLng(driverlat, driverlong);
                        } else {
                            latLong = new LatLng(gpslocation.getLatitude(), gpslocation.getLongitude());
                            driveruserid = User_id;
                            driverlat = gpslocation.getLatitude();
                            driverlong = gpslocation.getLongitude();

                            controller.pref.setDriver_Lat(String.valueOf(driverlat));
                            controller.pref.setDriver_Lng(String.valueOf(driverlong));

                            controller.setD_lat(driverlat);
                            controller.setD_lng(driverlong);

                        }
                    }


                }

//

                if (logoutcheck) {
//					Driverdetails();
                }
//                Drivername();
            } else if (usercheck == true) {
                latLong = new LatLng(userModel.userlat, userModel.userlng);
            } else {

                latLong = new LatLng(12.9667, 77.5667);

            }


            if (mGoogleMap != null) {
                mGoogleMap.setMyLocationEnabled(true);

                double driver_lat = Double.parseDouble(controller.pref.getDriver_Lat());
                double driver_lng = Double.parseDouble(controller.pref.getDriver_Lng());

                LatLng mark1 = new LatLng(driver_lat, driver_lng);


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        // .target(latLong).zoom(15f).tilt(0).build();
                        .target(latLong).zoom(16f).tilt(0).build();

                mGoogleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        Location location1 = location;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        setupMap();


    }

    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        //startService(new Intent(getApplicationContext(), DriverLocationServices.class));
       /* if(controller.pref.getTRIP_STATUS2().equals("begin")){
            Intent intent = new Intent(this, LocationTrackService.class);
            bindService(intent, serviceConnection, BIND_NOT_FOREGROUND);
        }
*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        if (controller.pref.getTRIP_STATUS2().equals("begin")) {
//            TrackRecordingServiceConnectionUtils.startConnection(this, trackRecordingServiceConnection);

//            Intent intent = new Intent(this, LocationTrackService.class);
//            bindService(intent, serviceConnection, BIND_NOT_FOREGROUND);

            /**
             * Runtime permissions are required on Android M and above to access User's location
             */
            if (AppUtils.hasM() && !(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                askPermissions();

            } else {

                // startDistanceServiceWithDistance();

            }

        } else {
//            TrackRecordingServiceConnectionUtils.startConnection(this, trackRecordingServiceConnection);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, new IntentFilter(LOACTION_ACTION));

        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    public void askPermissions() {
        Context context = SlideMainActivity.this;
        AppCompatActivity activity = (AppCompatActivity) context;
        mBothPermissionRequest = PermissionUtil.with(activity).request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).onResult(
                new Func2() {
                    @Override
                    protected void call(int requestCode, String[] permissions, int[] grantResults) {

                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                            startDistanceServiceWithDistance();

                        } else {

                            Toast.makeText(SlideMainActivity.this, "permission_denied", Toast.LENGTH_LONG).show();
                        }
                    }

                }).ask(PERMISSION_ACCESS_LOCATION_CODE);

    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        if (handler != null) {
            handler.removeCallbacks(handlerRunnable);
        }
        try{
            timertast.cancel();
        }catch (Exception e){

        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
        googleApiClient.disconnect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    public void onDisconnected() {

        // TODO Auto-generated method stub

    }

    @Override
    public void onProfileRefresh() {
        ProfileDetails();
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SlideMain Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    // **************************  Google Map  Function End **********************

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {

            //	Toast.makeText(getApplicationContext(),"Location is :: " + location,Toast.LENGTH_LONG).show();

            if (net_connection_check()) {

                String message = String.format(

                        "New Location \n Longitude: %1$s \n Latitude: %2$s",

                        location.getLongitude(), location.getLatitude());

                // *************************** GPS LOCATION ***************************


                driveruserid = User_id;
                driverlat = location.getLatitude();
                driverlong = location.getLongitude();


                Location driverLocation = new Location("user location");
                driverLocation.setLatitude(driverlat);
                driverLocation.setLongitude(driverlong);

                aController.setDriverLocation(driverLocation);

                String drivercurrentaddress = lattoaddress(driverlat, driverlong);


                if (!checktripend && mGoogleMap != null) {
                    //  mGoogleMap.clear();


                }
                LatLng mark1 = new LatLng(driverlat, driverlong);

                if (logoutcheck) {
//			Driverdetails();
                }

                LatLng target = new LatLng(driverlat, driverlong);

                String check = fullbutton.getText().toString();
                if (acc.equals("yes") || check.equals("End Trip")) {
                    if (check.equals("End Trip")) {
                        //  mGoogleMap.clear();
                        //   mGoogleMap.setTrafficEnabled(true);
                    }
                    origin = new LatLng(driverlat, driverlong);
                  /*  try{
                        String url = getDirectionsUrl(origin, dest);
                        drawMarker(dest);
                        DownloadTask downloadTask = new DownloadTask();
                        //Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                    }catch (Exception e){

                    }*/

//                    checkOffRouteAndRedrwaRoute(location);

                }

                checkOffRouteAndRedrwaRoute(location);
                if (location.hasBearing() && mGoogleMap != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(target)             // Sets the center of the map to current location
                            .zoom(16)
                            .bearing(location.getBearing()) // Sets the orientation of the camera to east
                            .tilt(0)                   // Sets the tilt of the camera to 0 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }

        public void onStatusChanged(String s, int i, Bundle b) {

        }

        public void onProviderDisabled(String s) {

        }

        public void onProviderEnabled(String s) {

        }

    }


    @SuppressLint("NewApi")
    public String lattoaddress(Double lat123, Double lng123) {

        Double pickuplatd;
        Double pickuplongd;
        pickuplatd = lat123;
        pickuplongd = lng123;
        StringBuilder str1;
        geocoder = new Geocoder(SlideMainActivity.this, Locale.ENGLISH);
        try {
            addresses = geocoder.getFromLocation(pickuplatd, pickuplongd, 1);
            str1 = new StringBuilder();
            if (Geocoder.isPresent()) {

                if (!addresses.isEmpty()) {
                    Address returnAddress = addresses.get(0);
                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    str1.append(localityString + "");
                    str1.append(city + "" + region_code + "");
                    str1.append(zipcode + "");

                }

            } else {
            }
            if (addresses.size() == 0) {
                return "";
            }
            return addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + " ";
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        return "";
    }

    public void onBackPressed() {
//        super.onBackPressed();

        if (mDrawerLayout.isDrawerOpen(navrl)) {
            mDrawerLayout.closeDrawer(navrl);
        } else {
            finish();
            int pid = Process.myPid();
            Process.killProcess(pid);
            System.exit(0);
        }
    }

    /**
     * This method is used to begin trip
     */



    private void dialogshow(String title, String msg) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (acc1.equals("no")) {
//                    sendacktorider();
                    Intent fare = new Intent(SlideMainActivity.this, SlideMainActivity.class);
                    fare.putExtra("userid", User_id);
                    fare.putExtra("fbuserproimg", fbuserproimg);
                    fare.putExtra("whologin", WhoLogin);
                    fare.putExtra("password", checkpassword);
                    startActivity(fare);
                    finish();
                }
                dialog.cancel();
            }
        });

        builder.show();

    }

    public String getcurrenttime() {
        startstopcheck = true;

        DateFormat df = new SimpleDateFormat("HH:mm:ss");

    /*getting current date time using calendar class
     * An Alternative of above*/
        Calendar calobj = Calendar.getInstance();

        return df.format(calobj.getTime());

    }


    private void buildAlertMessageNoGps() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.Dialog);
        builder.setMessage(R.string.gps_disabled)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onConnectionSuspended(int arg0) {


    }


    @Override
    public void onInit(int status) {

    }

    @Override
    protected void onDestroy() {


        endDistanceService();

        try {
            // Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mNotificationReceiver);




            //Clear internal resources.
            GCMRegistrar.onDestroy(this);

        } catch (Exception e) {

        }
        super.onDestroy();
    }



    private void drawMarker(LatLng point) {
        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(point);
        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */

        String check = fullbutton.getText().toString();
        System.out.println("text of fullbutton is = " + check);
        //String check = goButton1.getText().toString();
        if (check == "Go" || check.equals("Go")) {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
        }
        /*if(check=="Pick"||check.equals("Pick"))
        {
			options.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
		//	options.icon(BitmapDescriptorFactory.fromResource(R.drawable.manmap));
		}*/
        if (check == "End Trip" || check.equals("End Trip")) {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
        } else {
            //	options.icon(BitmapDescriptorFactory.fromResource(R.drawable.manmap));
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_20));
        }

        // Add new marker to the Google Map Android API V2
        mGoogleMap.addMarker(options);
    }


//

    public void getdroplocation() {
        LatLng dropLatLng = DriverHelperMethods.getDroploc(userdetailsid);
        if (dropLatLng != null) {
            droplat = "" + dropLatLng.latitude;
            droplong = "" + dropLatLng.longitude;
            droplatd = Double.parseDouble(droplat);
            droplongd = Double.parseDouble(droplong);
            StringBuilder str1;
            try {
                geocoder = new Geocoder(SlideMainActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(droplatd, droplongd, 1);
                str1 = new StringBuilder();
                if (Geocoder.isPresent()) {
                    Address returnAddress = addresses.get(0);
                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();
                    str1.append(localityString + "");
                    str1.append(city + "" + region_code + "");
                    str1.append(zipcode + "");
                    acceptView.setPickupLocation(addresses.get(0).getAddressLine(0) + ","
                            + addresses.get(0).getAddressLine(1) + " ");

//							rideraddressText.setText(addresses.get(0).getAddressLine(0)+","
//									+ addresses.get(0).getAddressLine(1) + " ");

                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            checktripend = true;  /// set accept no for to stop the polyline draw in user location in onLocation listener
            origin = new LatLng(driverlat, driverlong);
            dest = new LatLng(droplatd, droplongd);
            drawMarker(dest);
            // mGoogleMap.clear();
           /* drawMarker(dest);    /// When you start working on poly line to enable the line to draw marker in desination location
            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);
            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);*/
        }
    }


//**************************  Sliding Menu Function Start **********************

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     * Displaying fragment view for selected nav drawer list item
     */
    @SuppressWarnings("unused")
    @SuppressLint("NewApi")
    private void displayView(int position) {
        // update the main content by replacing fragments

        switch (position) {
            case 0:
                InterfaceRefreshProfile refreshProfile = this;
                mDrawerLayout.closeDrawer(navrl);
                System.out.println("Case 0");
                controller.setRefreshInterface(refreshProfile);

                    Intent profile = new Intent(getApplicationContext(), DriverProfileActivity.class);
                    startActivity(profile);


                break;

            case 1:

//                    if (mHandleMessageReceiver != null) {
//                        unregisterReceiver(mHandleMessageReceiver);
//
//                    }
                    mDrawerLayout.closeDrawer(navrl);
                    Intent history = new Intent(getApplicationContext(), Myreview.class);

                    startActivity(history);

                    //	finish();
                    break;


            case 2:

                    mDrawerLayout.closeDrawer(navrl);
//                    if (mHandleMessageReceiver != null) {
//                        unregisterReceiver(mHandleMessageReceiver);
//                    }
                    //Intent brain = new Intent(getApplicationContext(), BrainTreeMain.class);
                    Intent brain = new Intent(getApplicationContext(), RatingActivity.class);
                    brain.putExtra("userid", User_id);
                    brain.putExtra("fbuserproimg", fbuserproimg);

                    brain.putExtra("whologin", WhoLogin);
                    brain.putExtra("password", checkpassword);
                    brain.putExtra("from", "slide");

                    startActivity(brain);

                    //finish();
                    break;


            case 3:

                if (Utils.net_connection_check(SlideMainActivity.this)) {

                    showdialog(true, getResources().getString(R.string.do_you_want_to_exit_now));

                }

                break;

            case 4:
                if (Utils.net_connection_check(SlideMainActivity.this)) {


                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);

                    //change the type of data you need to share,
                    // //for image use "image/*"

                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, Constants.Urls.URL_TO_SHARE);
                    startActivity(Intent.createChooser(intent, "Share"));

                    break;
                }

            case 5:
                if (Utils.net_connection_check(SlideMainActivity.this)) {

                    showdialog(false, getResources().getString(R.string.do_you_want_to_deactivate_now));
                    break;
                }

            case 6:
                if (Utils.net_connection_check(SlideMainActivity.this)) {

                    String[] email = {Constants.Urls.EMAIL_FOR_SUPPORT};
                    shareToGMail(email, "Hire Me Driver App Support", "");
                    break;
                }

        }
    }


    public void shareToGMail(String[] email, String subject, String content) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(emailIntent);
    }

    public void logoutApi(final Controller controller) {
        this.controller = controller;
        progressDialog.showDialog();
        //final Controller controller= (Controller) getApplicationContext();
        SingleObject singleObject = SingleObject.getInstance();
        final String driver_apikey = controller.pref.getAPI_KEY();
        final String driverId = singleObject.getDriverId();




        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
        params.put("driver_id", driverId);
        params.put("d_is_available", "0");

        WebServiceUtil.excuteRequest(this, params, Constants.UPDATE_PROFILE, new WebServiceUtil.DeviceTokenServiceListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
               progressDialog.dismiss();
                if (isUpdate) {
                    controller.pref.signOut();
                    controller.pref.setIsLogin(false);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Connection Failed", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;

        getActionBar().setTitle(R.string.arcane_driver);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
    }

    public boolean net_connection_check() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();


        if (!connection) {

            Toast toast = Toast.makeText(getApplicationContext(), R.string.there_is_no_network_please_connect, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 70);
            toast.show();
        }
        return connection;
    }

    boolean isSearchingUser = false;

    public void getdriverdetails(double latitude, double longitude) {
        if (isSearchingUser) {
            return;
        } else {
            isSearchingUser = true;
            new NearByUsersUtil().getNearByUsers(getApplicationContext(), latitude, longitude, new NearByUsersUtil.WebServiceCallBack() {
                public void onComplete(Object data, String error) {
                    isSearchingUser = false;
                    if (data != null) {
                        ArrayList<LatLng> userLatLngs = (ArrayList<LatLng>) data;
                        addMarker(userLatLngs);

                    } else {
                    }
                }
            });
        }


    }

    public void showdialog(final boolean islogout, String messag) {
        final Dialog dialog = new Dialog(SlideMainActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_layout);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        TextView title2 = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        title2.setTypeface(drawerListTypeface);
        Button yes = (Button) dialog.findViewById(R.id.yes_btn);
        yes.setTypeface(drawerListTypeface);
        Button no = (Button) dialog.findViewById(R.id.no_btn);
        no.setTypeface(drawerListTypeface);
        title2.setText(messag);
        yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (islogout) {
                    logoutApi(controller);
                } else {
                    driverUpdateProfile("0", true, "0");
                }

            }
        });
        no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void acceptDeclineUserRequest() {
        acceptView.view.setEnabled(false);
        acceptView.setAcceptViewCallBack(new AcceptView.AcceptViewCallBack() {
            @Override
            public void onAcceptButtonCliked() {
                // TODO:  call new webservice(api) for the accept trip request  then  engage in the trip otherwise  go the home
                controller.stopNotificationSound();
                buttonTimer = new Timer();
                if(isTripStatusUpdating){
                    return;
                }else{
                    isTripStatusUpdating=true;
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("trip_status", "accept");
                    params.put("api_key", controller.pref.getAPI_KEY());
                    params.put(Constants.Keys.DRIVER_ID, controller.pref.getDRIVER_ID());
                    params.put(Constants.Keys.TRIP_ID, controller.pref.getTRIP_ID());
                    params.put(Constants.Keys.TRIP_STATUS, Constants.TripStatus.ACCEPT);
                    System.out.println("Accept Params : " + params);
                    WebServiceUtil.excuteRequest(SlideMainActivity.this, params, Constants.Urls.URL_ACCEPT_TRIP_REQUEST, new WebServiceUtil.DeviceTokenServiceListener() {
                        @Override
                        public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                            isTripStatusUpdating=false;
                            if (isUpdate) {
                                acceptView.hide();
                                touch.setVisibility(View.GONE);
                                ivSlideScreen.setVisibility(View.GONE);
                                ivSlideScreen.setEnabled(true);
                                tvDistanceYellow.setVisibility(View.GONE);
                                tvTimeYellow.setVisibility(View.GONE);
                                full.setVisibility(View.VISIBLE);
                                fullbutton.setVisibility(View.VISIBLE);
                                directionButton.setVisibility(View.VISIBLE);
                                sendNotificationToUser("accept");
                                handleProfileUpdate();
                                updateUIForAcceptRequest(data.toString());
                            } else {
                                isTripStatusUpdating=false;
                                acceptView.view.setEnabled(true);
                                updateUIForRejectRequest();
                                directionButton.setVisibility(View.GONE);
                            }
                        }
                    });
                }



            }

            @Override
            public void onDeclineButtonClicked() {
                updateUIForRejectRequest();
                controller.stopNotificationSound();
                Toast.makeText(SlideMainActivity.this, R.string.request_canceled, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateUIForRejectRequest() {
        controller.pref.setTRIP_ID("0");
        controller.setIsNotiCome(false);
        count_downt_timer.cancel();
        driverStatus = d_available;
        setHide_drawer("No");
        driverUpdateProfile("1", false, "0");
        isSearchingUser = false;
        touch.setVisibility(View.GONE);
        tvDistanceYellow.setVisibility(View.GONE);
        tvTimeYellow.setVisibility(View.GONE);
        fullbutton.setText("");
        goButton.setText("");
        directionButton.setVisibility(View.GONE);
        ivSlideScreen.setVisibility(View.GONE);
       Intent intent = new Intent(getApplicationContext(), SlideMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void updateUIForAcceptRequest(String response) {
        setTripRequest(false);
        controller.setIsNotiCome(false);

        fullbutton.setText(R.string.go);
        controller.pref.setFULLBUTTON_CAPTION("Go");
        controller.pref.setLocalTripState("Go");
        controller.pref.setGOBUTTON_CAPTION("");
        userInfoView.show();
        goButton.setText(R.string.dummy);
        mGoogleMap.clear();
        SingleObject object = SingleObject.getInstance();
        object.setIsChangeMag("Yes1");
        setupMap();
        addressLocationView.show();
        addressLocationView.setLocationTitle(getText(R.string.pickup_location));

        setHide_drawer("No");
        //
        controller.pref.setTRIP_STATUS2("accept");
        acc = "yes";



    }

    public void handleProfileUpdate() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SlideMainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(controller.pref.getTRIP_STATUS2().equals("cancel") || controller.pref.getTRIP_STATUS2().equals("driver_cancel_at_pickup")){
                            if(timer!=null)
                            {
                                timer.cancel();
                                driverUpdateProfile("1",false,"0");
                                controller.stopNotificationSound();
                            }
                        }
                        final TripModel tripModel = new TripModel();
                        tripModel.driver_apikey = controller.pref.getAPI_KEY();
                        tripModel.tripId = controller.pref.getTRIP_ID();
                        tripModel.tripStatus = controller.pref.getTRIP_STATUS2();
                        if (controller.pref.getTRIP_STATUS2().equals("accept") ||
                                controller.pref.getTRIP_STATUS2().equals("arrive") ||
                                controller.pref.getTRIP_STATUS2().equals("request"))
                        {
                            getTrip(tripModel);
                        } else
                            {

                            }
                 }
                });
            }
        }, 0, 5000);//put here time 1000 milliseconds=1 second
    }

    Timer buttonTimer;
    Location pickupLoction;

    public void getTrip(final TripModel tripModel) {
        WebServiceUtil.excuteRequest(SlideMainActivity.this, tripModel.getParamsforGetTrip(), Constants.GET_TRIP_URL, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                progressDialog.dismiss();
                if (isUpdate) {
                    System.out.println("!!!!-----Get Trip Api called");
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (res.isStatus()) {
                        if (isRequest) {
                            isRequest = false;
                            progressDialog.dismiss();

                        }
                        if (response != null) {
                            SingleObject obj = SingleObject.getInstance();
                            controller.pref.setTIME_DISTANCE_VIEW(false);
                            boolean isParseRe = obj.parsingTripResponse(response, tripModel);
                            if (isParseRe) {
                                boolean trip_request = getTripRequest();
                                controller.pref.saveTripData(tripModel);
                                controller.pref.setTRIP_ID(tripModel.tripId);
                                tripModelTemp = tripModel;
                                controller.setCurrentTripModel(tripModel);
                                try{
                                    userInfoView = new UserInfoView(SlideMainActivity.this, findViewById(R.id.user_info_layout),tripModel);
                                    userInfoView.hide();
                                }catch (Exception e){

                                }
                                String ss1 = tripModel.trip.getTrip_distance();
                                controller.pref.setTRIP_STATUS2(tripModel.trip.getTrip_status());
                                controller.pref.setTRIP_DISTANCE(ss1);
                                String ss = controller.pref.getTRIP_DISTANCE();
                                String ss2 = tripModel.trip.getTrip_pickup_time();
                                controller.pref.setTRIP_PICKUP_TIME(ss2);
                                String droptime = controller.pref.getTRIP_DROP_TIME();
                                String modified_time = controller.pref.getTRIP_MODIFIED_TIME();
                                String ss4 = controller.pref.getTRIP_FARE();

                                try {

                                    if (ss != null && ss.length() != 0 && droptime != null && droptime.length() != 0 && modified_time != null && modified_time.length() != 0) {
                                        controller.pref.setTIME_DISTANCE_VIEW(true);
                                        DistanceAndTimeViewModel modelObj = new DistanceAndTimeViewModel(ss, droptime, modified_time);
                                        setDistanceAndTimeViewModel(modelObj);
                                    } else {
                                        controller.pref.setTIME_DISTANCE_VIEW(false);
                                        DistanceAndTimeViewModel modelObj = new DistanceAndTimeViewModel("30", "26", "10:13");
                                        setDistanceAndTimeViewModel(modelObj);
                                    }

                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    String user_lat = tripModel.trip.getTrip_scheduled_pick_lat();
                                    String user_lng = tripModel.trip.getTrip_scheduled_pick_lng();
                                    Double userlat = Double.valueOf(user_lat + 0);
                                    Double userlng = Double.valueOf(user_lng + 0);
                                    double driver_lat = Double.parseDouble(controller.pref.getDriver_Lat());
                                    double driver_lng = Double.parseDouble(controller.pref.getDriver_Lng());


                                    //Calculating the distance in meters
                                    LatLng from = new LatLng(userlat, userlng);
                                    LatLng to = new LatLng(driver_lat, driver_lng);
                                    Double distance2 = SphericalUtil.computeDistanceBetween(from, to);
                                    double pickedupDistance = distance2 / 1000;
                                    double pickdis = roundToDecimals(pickedupDistance, 2);
                                    String pickdist = String.valueOf(pickdis);

                                    //	For example spead is 10 meters per minute.
                                    int speedIs10MetersPerMinute = 20;
                                    double estimatedDriveTimeInMinutes1 = pickedupDistance / speedIs10MetersPerMinute;
                                    double estimatedDriveTimeInMinutes = roundToDecimals(estimatedDriveTimeInMinutes1, 2);

                                    if (estimatedDriveTimeInMinutes < 1.0) {

                                        tvDistanceYellow.setText(pickdist + " "+controller.checkDistanceUnit());
                                        tvTimeYellow.setText("1" + " min");
                                    } else {
                                        String esttime = String.valueOf(estimatedDriveTimeInMinutes);
                                        tvDistanceYellow.setText(pickdist + " "+controller.checkDistanceUnit());
                                        tvTimeYellow.setText(esttime + " min");
                                    }
                                    addressLocationView.setLocationAddress(controller.pref.getTRIP_FROM_LOC());
                                    acceptView.setPickupLocation(controller.pref.getTRIP_FROM_LOC());


                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                if (tripModel.trip.getTrip_status().equalsIgnoreCase("request")) {
                                    tvDistanceYellow.setVisibility(View.VISIBLE);
                                    tvTimeYellow.setVisibility(View.VISIBLE);
                                    acceptDeclineUserRequest();
                                    acceptView.show();
                                    setCountDownTimer();
                                    isNewRequest=false;

                                    if(controller.isFromBackground){
                                        controller.stopNotificationSound();
                                        controller.playNotificationSound();
                                    }
                                    touch.setVisibility(View.VISIBLE);
                                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                    drawerbut.setVisibility(View.GONE);
                                } else if (tripModel.trip.getTrip_status().equalsIgnoreCase("end")||tripModel.trip.getTrip_status().equals("driver_cancel_at_drop")) {

                                    updateButtons(tripModel.trip.getTrip_status());

                                }else if(tripModel.trip.getTrip_status().equalsIgnoreCase("cancel")){
                                   if(timer!=null){
                                    timer.cancel();
                                   }
                                   // updateButtons(tripModel.trip.getTrip_status());
                                    showRiderDialog(true);

                                }

                                else{
                                   // checkTripState();
                                    updateButtons(tripModel.trip.getTrip_status());
                                }
                            }
                        }

                        } else {
                        Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                } else {
                 //   Toast.makeText(getApplicationContext(), R.string.internet_error, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean isTripStatusUpdating=false;

    private void updateTripStatusApi(final String trip_status, String trip_id, String trip_cancel_reason) {
        if(Utils.net_connection_check(SlideMainActivity.this)){
            if(isTripStatusUpdating){
                return;
            }else {
                isTripStatusUpdating=true;
                final TripModel tripModel = new TripModel();
                tripModel.driver_apikey = controller.pref.getAPI_KEY();
                tripModel.tripId = trip_id;
                tripModel.tripStatus = trip_status;
                tripModel.trip_reason = trip_cancel_reason;

                if (trip_status.equals("end") || trip_status.equals("driver_cancel_at_drop")) {
                    tripModel.tripAmount = String.format("%.02f", calculateFare());
                    tripModel.tripDistance = String.format("%.02f", distanceCover);
                    tripModel.taxAmount = String.format("%.02f", tripTax);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    tripModel.drop_time = simpleDateFormat.format(new Date());
                }
                if (trip_status.equals("begin")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    tripModel.pick_time = simpleDateFormat.format(new Date());

                }
                WebServiceUtil.excuteRequest(this, tripModel.getParams(), Constants.UPDATE_TRIP_URL, new WebServiceUtil.DeviceTokenServiceListener() {
                    @Override
                    public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                        isTripStatusUpdating=false;
                        if (isUpdate) {
                            JSONObject jsonRootObject = null;
                            try {
                                jsonRootObject = new JSONObject(data.toString());
                                int response = jsonRootObject.getInt("response");
                                if (response == 1) {
                                    if (trip_status.equals("end") || trip_status.equals("driver_cancel_at_drop")) {
                                        controller.pref.setTRIP_PAY_AMOUNT("" + calculateFare());
                                        isRequest=true;
                                        progressDialog.showDialog();
                                        getTrip(tripModel);
                                    } else {

                                        updateButtons(trip_status);
                                    }

                                    sendNotificationToUser(trip_status);
                                    //     update trip
                                } else {
                                    // not update
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }


    }

    private double calculateFare() {
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();
            String tripEndTime = df.format(dateobj);
            String tripStartTime = controller.pref.getTripStartTime();

            long minuts = getTimeDifference(tripStartTime, tripEndTime);

            CategoryActors driverCat = categoryResponseList.get(0);
            double serviceTaxPercent = 0.0;
            for (DriverConstants driverConstants : constantsList) {
                if (driverConstants.getConstant_key().equalsIgnoreCase("service_tax")) {
                    serviceTaxPercent = Double.parseDouble(driverConstants.getConstant_value());
                }

            }

            double distanceUnit = controller.checkDistanceUnit().equalsIgnoreCase("km") ? (distanceCover) : (distanceCover * 0.000621371);
//
            double distanceto = distanceUnit - 1;
            double distancetoCalculate = Math.ceil(distanceto * 10) / 10;
            distancetoCalculate = distancetoCalculate >= 0 ? distancetoCalculate : 0;

            double totalPrice = Double.parseDouble(driverCat.getCat_base_price()) + (distancetoCalculate * Double.parseDouble(driverCat.getCat_fare_per_km()) + minuts * Double.parseDouble(driverCat.getCat_fare_per_min()));
            // float tax1 = totalPrice *self.service_tax_percentage/100;
            totalPrice = totalPrice + tripTax;

            if (driverCat.getCat_is_fixed_price().equals("0")) {
                double primepercentage = (totalPrice * Double.parseDouble(driverCat.getCat_prime_time_percentage())) / 100.0;
                totalPrice = totalPrice + primepercentage;
            }
            return totalPrice;

        } catch (Exception e) {
            return 0.0;
        }
    }

    private long getTimeDifference(String tripStartTime, String tripEndTime) {
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        long difference = 0;
        try {
            Date st = df.parse(tripStartTime);
            Date ed = df.parse(tripEndTime);
            long diffInMilli = ed.getTime() - st.getTime();
            difference = diffInMilli / (60 * 1000);
            return difference;
        } catch (ParseException e) {
            e.printStackTrace();
            return difference;
        }


    }


    boolean isCalling = false;

    private void driverUpdateProfile(String is_available, final boolean isdeactivate, final String isVarified) {
        if (isCalling) {
            return;
        } else {
            try{
            progressDialog.showDialog();
            isCalling = true;
            final Controller controller = (Controller) getApplicationContext();
            final SingleObject singleObject = SingleObject.getInstance();

            final String driver_apikey = controller.pref.getAPI_KEY();
            final String driverId = controller.pref.getDRIVER_ID();
            final String d_availibility = is_available;
            final String device_token = controller.pref.getD_DEVICE_TOKEN();

                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", driver_apikey);
                params.put("driver_id", driverId);
                params.put("d_is_available", d_availibility);
                params.put("d_device_token", device_token);
                if (isdeactivate) {
                    params.put("active", isVarified);
                }
                params.put("d_device_type", "Android");
                params.put("d_degree", "176.555");

                WebServiceUtil.excuteRequest(SlideMainActivity.this, params, Constants.UPDATE_PROFILE, new WebServiceUtil.DeviceTokenServiceListener() {
                    @Override
                    public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                        isCalling = false;
                        try{
                            progressDialog.dismiss();
                        }catch (Exception e){

                        }
                        if (isUpdate) {
                            String response = data.toString();
                            ErrorJsonParsing parser = new ErrorJsonParsing();
                            CloudResponse res = parser.getCloudResponse("" + response);
                            if (res.isStatus()) {


                                if (response != null) {
                                    controller.pref.setSIGN_IN_RESPONSE(response);
                                    controller.setSignInResponse(response);
                                    SingleObject obj = SingleObject.getInstance();
                                    obj.driverUpdateProfileParseApi(response);
                                    //	if(parsed) {
                                    String d_is_available = obj.getDriver_is_available();
                                    controller.pref.setD_IS_AVAILABLE(d_is_available);
                                    String dlat = obj.getD_Lat();
                                    String dlng = obj.getD_Lng();
                                    controller.setDriverLat(dlat);
                                    controller.setDriverLng(dlng);
                                    if (isdeactivate) {
                                        logoutApi(controller);
                                    }
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                          //  Toast.makeText(getApplicationContext(), R.string.internet_error, Toast.LENGTH_LONG).show();
                        }
                    }
                });




        }catch (Exception e){

            }
        }}


    public void sendNotificationToUser(String tripStatus) {
        if (tripModelTemp != null && tripModelTemp.user != null) {
            String u_device_type = tripModelTemp.user.getU_device_type();
            Map<String, String> params = new HashMap<String, String>();
            String notificationMessage = "change message:";
            if (tripStatus.equalsIgnoreCase(Constants.TripStatus.ACCEPT)) {
                notificationMessage = Constants.Message.ACCEPTED;
            } else if (tripStatus.equalsIgnoreCase("driver_cancel_at_pickup")) {
                notificationMessage = Constants.Message.DRIVER_CANCEL;
            } else if (tripStatus.equalsIgnoreCase("go")) {
                notificationMessage = "Your request accept";
            } else if (tripStatus.equalsIgnoreCase(Constants.TripStatus.END)||tripStatus.equalsIgnoreCase("driver_cancel_at_drop")) {
//            end
                notificationMessage = Constants.Message.END;
            } else if (tripStatus.equalsIgnoreCase(Constants.TripStatus.ARRIVE)) {
                notificationMessage = Constants.Message.ARRIVE;
//            arrive
            } else if (tripStatus.equalsIgnoreCase(Constants.TripStatus.BEGIN)) {
                notificationMessage = Constants.Message.BEGIN;
//            begin
            }
            params.put("message", notificationMessage);
            params.put("trip_id", tripModelTemp.trip.getTrip_id());
            params.put("trip_status", tripStatus);
            if (u_device_type.equalsIgnoreCase(Constants.Keys.ANDROID)) {
                params.put(Constants.Keys.ANDROID, tripModelTemp.user.getU_device_token());
            } else {
                params.put(Constants.Keys.IOS, tripModelTemp.user.getU_device_token());
            }
            notificationStatusApi(params, Constants.URL_COMMAN_NOTIFICATION, tripStatus);
        } else {
            tripModelTemp=controller.getCurrentTripModel();
            if(tripModelTemp != null && tripModelTemp.user != null){
                sendNotificationToUser(tripStatus);
            }
        }
    }


    private void notificationStatusApi(final Map<String, String> params, String noti_url, final String status) {
        WebServiceUtil.excuteRequest(this, params, noti_url, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                }
            }
        });
    }

    private void updateButtons(String status) {
        controller.pref.setTRIP_STATUS2(status);
        controller.pref.setLocalTripState(status);
        if (status.equals("accept")) {
            acceptView.hide();
            touch.setVisibility(View.GONE);
            ivSlideScreen.setVisibility(View.GONE);
            ivSlideScreen.setEnabled(true);
            tvDistanceYellow.setVisibility(View.GONE);
            tvTimeYellow.setVisibility(View.GONE);
            full.setVisibility(View.VISIBLE);
            fullbutton.setVisibility(View.VISIBLE);
            updateUIForAcceptRequest("");
        }
        if (status.equals("begin")) {
            try{
                userInfoView.hide();
                timer.cancel();
            }catch (Exception e){

            }

            showDistanceAndTimeView();
            controller.pref.setGOBUTTON_CAPTION("End Trip");
            goButton.setText(getText(R.string.end_trip));
            controller.pref.setGOBUTTON_CAPTION("End Trip");
            addressLocationView.setLocationTitle(getText(R.string.drop_location));
            addressLocationView.setLocationAddress(controller.pref.getTRIP_TO_LOC());
            mGoogleMap.clear();
            is_required_drawRoute = "Yes2";
            SingleObject obj = SingleObject.getInstance();
            obj.setIsChangeMag("Yes2");
            setupMap();
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();
            controller.pref.setTripStartTime(df.format(dateobj));
            controller.pref.setCalculateDistance(true);
            isSearchingUser = true;
        }
        if (status.equals("arrive")) {
            controller.pref.setCalculateDistance(false);
            fullbutton.setEnabled(true);
            fullbutton.setText(getText(R.string.pick));
            controller.pref.setFULLBUTTON_CAPTION("Pick");
            distanceAndTimeView.show();
            accepttimer.cancel();
            driverStatus = d_busy;
            arrives = "yes";
            fullbutton.setEnabled(true);
            handlertime = new Handler();
            handlertime.postDelayed(handlerUpdateTime, 2000);
            setupMap();
            isSearchingUser = true;
        }


        if (status.equals("begin")) {
            controller.pref.setCalCulatedDistance(0);
            lat1 = driverlat;
            lat2 = driverlong;
            account.setVisibility(View.INVISIBLE);
            controller.pref.setCalculateDistance(true);
            showDistanceAndTimeView();
            driverStatus = d_busy;
            isSearchingUser = true;
            //startDistanceService();
            startDistanceServiceWithDistance();

        }
        if (status.equals("end")) {
            controller.pref.setCalculateDistance(false);
            controller.pref.setCalCulatedDistance(0);
            controller.pref.setTripStartTime("");
            directionButton.setVisibility(View.GONE);
          /* Intent service= new Intent(this, LocationTrackService.class);
            stopService(service);
            endDistanceService();*/

            if (TrackRecordingServiceConnectionUtils.isRecordingServiceRunning(this, LocationService.class)) {
                serviceIntent = new Intent(this, LocationService.class);

                stopService(serviceIntent);
            }

            try {
                if (mHandleMessageReceiver != null) {
                    unregisterReceiver(mHandleMessageReceiver);
                }
                if (handlertime != null) {
                    handlertime.removeCallbacks(handlerUpdateTime);
                }
            } catch (Exception e) {

            }

            farecheck = false;
            driverStatus = d_available;
            driverUpdateProfile("1", false, "0");
            isSearchingUser = false;
            goButton.setText("");
            controller.pref.setGOBUTTON_CAPTION("");
            SingleObject obj = SingleObject.getInstance();
            obj.setfareSummaryLinearLayout(false);
            controller.pref.setIS_FARESUMMARY_OPEN("yes_open");
            destinationActivity.view.setVisibility(View.GONE);
            isSearchingUser = false;
            Intent intent = new Intent(getApplicationContext(), FareSummaryActivity.class);
            startActivity(intent);
            finish();
        }

        if (status.equals("driver_cancel_at_drop")) {
            controller.pref.setTIME_DISTANCE_VIEW(false);

            controller.pref.setIS_FARESUMMARY_OPEN("yes_open");
            directionButton.setVisibility(View.GONE);
            driverStatus = d_available;
            goButton.setText("");
            controller.pref.setGOBUTTON_CAPTION("");


            driverUpdateProfile("1", false, "0");
            isSearchingUser = false;
            switchAvailable1.setChecked(true);

            Intent service= new Intent(this, LocationTrackService.class);
            stopService(service);

            distanceAndTimeView.hide();
            addressLocationView.hide();
            mGoogleMap.clear();
            backToOrderView.hide();
            controller.pref.setCalculateDistance(false);
            controller.pref.setCalCulatedDistance(0);
            controller.pref.setTripStartTime("");
            try {
                if (mHandleMessageReceiver != null) {
                    unregisterReceiver(mHandleMessageReceiver);
                }
                if (handlertime != null) {
                    handlertime.removeCallbacks(handlerUpdateTime);
                }
            } catch (Exception e) {

            }


            farecheck = false;
            driverStatus = d_available;
            isSearchingUser = false;
            goButton.setText("");
            controller.pref.setGOBUTTON_CAPTION("");
            SingleObject obj = SingleObject.getInstance();
            obj.setfareSummaryLinearLayout(false);
            controller.pref.setIS_FARESUMMARY_OPEN("yes_open");
            destinationActivity.view.setVisibility(View.GONE);
            isSearchingUser = false;

            endDistanceService();

            Intent intent = new Intent(getApplicationContext(), FareSummaryActivity.class);
            startActivity(intent);
            finish();

        }
        if (status.equals("cancel")) {
            controller.pref.setTRIP_ID("0");
            controller.setIsNotiCome(false);
//            count_downt_timer.cancel();
            driverStatus = d_available;
            setHide_drawer("No");
            driverUpdateProfile("1", false, "0");
            isSearchingUser = false;
            directionButton.setVisibility(View.GONE);
            touch.setVisibility(View.GONE);
            tvDistanceYellow.setVisibility(View.GONE);
            tvTimeYellow.setVisibility(View.GONE);

            fullbutton.setText("");
            goButton.setText("");

            //for lock the screen before click accept or decline button
            ivSlideScreen.setVisibility(View.GONE);
            ivSlideScreen.setEnabled(true);

            Intent intent = new Intent(getApplicationContext(), SlideMainActivity.class);
            startActivity(intent);
            finish();
        }
        if (status.equals("driver_cancel_at_pickup")) {
            controller.pref.setTRIP_ID("0");
            controller.setIsNotiCome(false);
            if(count_downt_timer!=null) {
                count_downt_timer.cancel();
            }
            driverStatus = d_available;
            setHide_drawer("No");
            driverUpdateProfile("1", false, "0");
            isSearchingUser = false;
            directionButton.setVisibility(View.GONE);
            touch.setVisibility(View.GONE);
            tvDistanceYellow.setVisibility(View.GONE);
            tvTimeYellow.setVisibility(View.GONE);

            fullbutton.setText("");
            goButton.setText("");

            //for lock the screen before click accept or decline button
            ivSlideScreen.setVisibility(View.GONE);
            ivSlideScreen.setEnabled(true);

            Intent intent = new Intent(getApplicationContext(), SlideMainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    Handler handlerLocation = new Handler();
    private Location currentLocation;
    private
    Runnable runnableReadLocation = new Runnable() {
        @Override
        public void run() {
            if (locationRecord != null) {
                try{
                    currentLocation=locationRecord.getCurrentLocation();
                     currentlatitude= currentLocation.getLatitude();
                     currentlongitude= currentLocation.getLongitude();
                    distanceCover=locationRecord.coverDistance()/1000.0f;
                }catch (Exception e){

                }

            } else {

            }
            handlerLocation.postDelayed(this, 500);
        }
    };


    private ILocationRecord locationRecord;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            locationRecord = ILocationRecord.Stub.asInterface((IBinder) service);
            handlerLocation.post(runnableReadLocation);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void startDistanceService() {
        Intent intent = new Intent(this, LocationTrackService.class);
        intent.putExtra("Latitute",Double.parseDouble(controller.pref.getTRIP_SCHEDULED_PICK_LAT()));
        intent.putExtra("Longitute",Double.parseDouble(controller.pref.getTRIP_SCHEDULED_PICK_LNG()));
        startService(intent);
        bindService(intent, serviceConnection, BIND_NOT_FOREGROUND);
    }

    private void endDistanceService(){
        try{
            unbindService(serviceConnection);
        }catch (Exception e){

        }

    }

    private boolean isLoadingRouteByWayPoints;
    private ArrayList<LatLng> driverFollowedWayPoints;
    private Location preLocation;
    private GoogleSDRoute.RouteResponse mRouteResponse;
    private ArrayList<Polyline> polylines;
    private GoogleSDRoute googleSDRoute;
    Polyline polyline;
    Marker marker12;
    private Intent serviceIntent;

    private void drawDriverRoute(double destLat, double destLng, double originLat, double originLng) {

        dest = new LatLng(destLat, destLng);
        origin = new LatLng(originLat, originLng);


        isLoadingRouteByWayPoints = false;
        driverFollowedWayPoints = null;
        preLocation = null;
        googleSDRoute = new GoogleSDRoute(this, origin, dest);
        googleSDRoute.getRoute(new GoogleSDRoute.GoogleSDRouteCallBack() {
            @Override
            public void onCompleteSDRoute(GoogleSDRoute.RouteResponse routeResponse, Exception ex) {
                mRouteResponse = routeResponse;
                if (routeResponse != null) {
                    if (polylines != null) {
                        for (Polyline polyline : polylines) {
                            polyline.remove();
                        }
                        polylines.clear();
                    }
                    mGoogleMap.clear();  // clear map  on  drawRoute first time

                    controller.setTripDistance(routeResponse.distance);
                    controller.setTripDuration(routeResponse.duration);
                    controller.pref.setTRIP_DISTANCE_CHANGABLE(routeResponse.distance);
                    controller.pref.setTRIP_DURATION_CHANGABLE(routeResponse.duration);
                    aController.setPickDistances(routeResponse.distance);
                    aController.setPickDurations(routeResponse.duration);
                    polylines = routeResponse.addRouteOnMap(mGoogleMap);
                    handleRouteDataAfterPolyLine(routeResponse);
                }
            }

            @Override
            public void onErrorSDRoute(VolleyError routeResponse) {

            }
        });

        /*  drawMarker(dest);    /// When you start working on poly line to enable the line to draw marker in desination location
        // Getting URL to the Google Directions API
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //the include method will calculate the min and max bound.
        builder.include(origin);
        builder.include(dest);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.35); // offset from edges of the map 10% of screen
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mGoogleMap.animateCamera(cu);
        String url = getDirectionsUrl(origin, dest);
        DownloadTask downloadTask = new DownloadTask();
        System.out.println("After call download task");
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);*/

      /*  try {
            DirectionFinderListener finderListener=this;
            new DirectionFinder(finderListener,origin.latitude+","+origin.longitude,dest.latitude+","+dest.longitude,"").execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

    }

    private void handleRouteDataAfterPolyLine(GoogleSDRoute.RouteResponse routeResponse) {
        if (routeResponse.maneuver.size() > 1) {
            plat1 = driverlat;
            plat2 = driverlong;

            Location locationA = new Location("point A");
            System.out.println("after start location assign");
            locationA.setLatitude(plat1);
            locationA.setLongitude(plat2);
            plat3 = routeResponse.ending_lat.get(1);
            plat4 = routeResponse.ending_long.get(1);


            Location locationB = new Location("point B");

//            System.out.println("after end location assign");

            locationB.setLatitude(plat3);
            locationB.setLongitude(plat4);

//            System.out.println("Start Location " + locationA);
//            System.out.println("End Location " + locationB);
            distancedirection = locationA.distanceTo(locationB);

//            System.out.println("minusdistance= " + distancedirection);

            distancedirection = locationA.distanceTo(locationB) / 1000;

            System.out.println("after converted meter in to km:" + distancedirection);

            String s = String.format("%.1f", distancedirection);
            System.out.println("after converting distance into decimal in string:" + s);

            float p = Float.parseFloat(s);


            if (routeResponse.maneuver.get(1).equals("No side")) {

            } else {

                String split = String.valueOf(routeResponse.dis.get(0));
                String[] splitarray = split.split(" ");
                String getval = splitarray[0];
                String getmkm = splitarray[1];
                System.out.println("split val:" + getval);
                System.out.println("split km " + getmkm);


                float textfloat = Float.parseFloat(getval);

                System.out.println("after float");

                if (getmkm.matches("km")) {

                    System.out.println("inside if km loop");

// 1.30
                    if (p <= val) {

                        System.out.println("inside speach" + maneuver12);

                        //  tts.speak(maneuver.get(1), TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        System.out.println("inside else in km loop");

                    }
                } else {
                    System.out.println("inside else ");

                    float k = (textfloat) / 1000;


                    if (k <= val) {
                        System.out.println("insidespeach" + maneuver12);

                        // tts.speak(maneuver.get(1), TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        System.out.println("inside else in m loop");

                    }
                }
            }
            System.out.println("end of loooppp...");
        }

    }

    private void checkOffRouteAndRedrwaRoute(Location location) {
        if (mRouteResponse != null) {
            if (polylines == null) {
                polylines = new ArrayList<>();
            }

            if (preLocation != null) {
                float diffDistance = preLocation.distanceTo(location);

                ArrayList<LatLng> lastNearstLoc = mRouteResponse.getLastNearstLoc(location);
                PolylineOptions polylineOptionses = new PolylineOptions();
                if(lastNearstLoc==null)
                    return;
                polylineOptionses.addAll(lastNearstLoc);
                polylineOptionses.width(8);
                polylineOptionses.color(Color.RED);
                polylineOptionses.geodesic(true);
                polylineOptionses.zIndex(4);


                if (polyline != null) {
                    polyline.remove();
                }
                polyline = mGoogleMap.addPolyline(polylineOptionses);
                if(marker12!=null)
                {

                    marker12.remove();
                }
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.unnamed_cpoy));
                markerOptions.anchor(20,20);
//                markerOptions.rotation(location.getBearing());
                marker12=mGoogleMap.addMarker(markerOptions);



                if (diffDistance > 50) {
                    preLocation = location;

                    if (driverFollowedWayPoints == null) {
                        driverFollowedWayPoints = new ArrayList<>();
                    }
                    if (driverFollowedWayPoints.size() > 6) {
                        driverFollowedWayPoints.clear();
                    }
                    driverFollowedWayPoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
//                        PolylineOptions polylineOptions = new PolylineOptions();
//                        polylineOptions.width(8);
//                        polylineOptions.color(Color.BLUE);
//                        polylineOptions.geodesic(true);
//                        polylineOptions.zIndex(5);
//                        polylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
//                        polylineOptions.add(new LatLng(preLocation.getLatitude(), preLocation.getLongitude()));
//                        if (mGoogleMap != null) {
//                            polylines.add(mGoogleMap.addPolyline(polylineOptions));
//                        }
////                         Polyline polyline;
//                        ArrayList<LatLng> lastNearstLoc = mRouteResponse.getLastNearstLoc(location);
//                        PolylineOptions polylineOptions1 = new PolylineOptions();
//                        polylineOptions1.addAll(lastNearstLoc);
//                        polylineOptions1.width(8);
//                        polylineOptions1.color(Color.RED);
//                        polylineOptions1.geodesic(true);
//                        polylineOptions1.zIndex(5);
//                        polylineOptions1.add(new LatLng(preLocation.getLatitude(), preLocation.getLongitude()));
//                        polylineOptions1.add(new LatLng(location.getLatitude(), location.getLongitude()));
//
//                        if (mGoogleMap != null) {
//                            polylines.add(mGoogleMap.addPolyline(polylineOptions1));
//                        }

                    if (mRouteResponse != null) {
                        if(true)
                            return;
                        boolean onOffRoute = mRouteResponse.isOnOffRoute(location);

//                        onOffRoute = true;
                        if (onOffRoute) {
                            Log.d("debug", "off route");
                            // refresh route by  using waypoints;
                            if (googleSDRoute != null) {
                                ArrayList<LatLng> wayPoints = new ArrayList<>();
                                wayPoints.addAll(driverFollowedWayPoints);
                                wayPoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
                                isLoadingRouteByWayPoints = true;
                                Log.d("debug", "googleSDRoute");
                                googleSDRoute.getRoute(new GoogleSDRoute.GoogleSDRouteCallBack() {
                                    @Override
                                    public void onCompleteSDRoute(GoogleSDRoute.RouteResponse routeResponse, Exception ex) {
                                        Log.d("debug", "offonCompleteSDRoute");
                                        isLoadingRouteByWayPoints = false;
                                        if (routeResponse != null) {
                                            mRouteResponse = routeResponse;
//                                        ArrayList<Polyline> prePolylines = polylines;
                                            mGoogleMap.clear();
                                            polylines = routeResponse.addRouteOnMap(mGoogleMap, false);
//                                        if (prePolylines != null) {
//                                            for (int i = 0; i < prePolylines.size(); i++) {
//                                                prePolylines.get(i).remove();
//                                            }
//                                            prePolylines.clear();
//                                        }
                                        }
                                    }

                                    @Override
                                    public void onErrorSDRoute(VolleyError routeResponse) {
                                        isLoadingRouteByWayPoints = false;
                                        Log.d("debug", "onErrorSDRoute");
                                    }
                                }, wayPoints);
                            }
                        }
                    } else {
                        Log.d("debug", "on route");
                    }
                }
            } else {
                preLocation = location;
            }

        }
    }

    private LocationReceiver locationReceiver;
    private PermissionUtil.PermissionRequestObject mBothPermissionRequest;

    public void startDistanceServiceWithDistance() {
        if (controller.pref.getTRIP_STATUS2().equals("begin")) {
            //Toast.makeText(context,"Service Start",Toast.LENGTH_LONG).show();

            serviceIntent = new Intent(this, LocationService.class);
            boolean recordingServiceRunning = TrackRecordingServiceConnectionUtils.isRecordingServiceRunning(this, LocationService.class);
            if (PreferencesUtils.getBoolean(this, R.string.is_recording_start, false) == false) {
                PreferencesUtils.getBoolean(this, R.string.is_recording_start, true);
                PreferencesUtils.setFloat(this, R.string.recorded_distance, 0);
            }
            controller.pref.setCalCulatedDistance(PreferencesUtils.getFloat(this, R.string.recorded_distance));

//            PreferencesUtils.getFloat(this, R.string.recorded_distance));
            distance_temp.setText("" + String.format("%.03f", controller.pref.getCalCulatedDistance()));
            if (!recordingServiceRunning) {
                startService(serviceIntent);
            }
        }


    }

    private class LocationReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {


            if (null != intent && intent.getAction().equals(LOACTION_ACTION)) {

                String locationData = intent.getStringExtra(LOCATION_MESSAGE);
                distanceCover = Double.parseDouble(locationData);
                controller.pref.setCalCulatedDistance(distanceCover);
                distance_temp.setText("" + String.format("%.03f", controller.pref.getCalCulatedDistance()));

                if (Controller.isActivityVisible()) {

                    try {

                        Bundle extras = intent.getExtras();

                        float c_lat = Float.parseFloat(intent.getStringExtra("c_lat"));
                        float c_lng = Float.parseFloat(intent.getStringExtra("c_lng"));
                        Location location = new Location("");
                        location.setLatitude(c_lat);
                        location.setLongitude(c_lng);

                        checkOffRouteAndRedrwaRoute(location);
                        LatLng target = new LatLng(c_lat, c_lng);
                        if (mGoogleMap != null) {
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(target)             // Sets the center of the map to current location
                                    .zoom(16)
                                    .bearing(location.getBearing()) // Sets the orientation of the camera to east
                                    .tilt(0)                   // Sets the tilt of the camera to 0 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }


    private void setCountDownTimer() {

        count_downt_timer = new CountDownTimer(30000, 1000) {
            int time = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                boolean bool = controller.getNotiCome();
                float pro;
                pro = millisUntilFinished / 30000.0f * 100;
                long seconds = millisUntilFinished / 1000;
                progressbar.setProgress(time + (int) pro);
                Object aobj[] = new Object[1];
                aobj[0] = Long.valueOf(seconds % 60L);
                touchtext.setText(String.format("%02d", aobj));
                touchtext.setText("Touch in " + String.format("%02d", aobj) + "sec to accept");
            }

            @Override
            public void onFinish() {
                touch.setVisibility(View.GONE);
                tvDistanceYellow.setVisibility(View.GONE);
                tvTimeYellow.setVisibility(View.GONE);
                acceptView.hide();
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
                drawerbut.setVisibility(View.VISIBLE);
                controller.stopNotificationSound();

            }
        };

        count_downt_timer.start();
    }

    private void ifGoCaption() {
        isSearchingUser = true;
        updateTripStatusApi("arrive", controller.pref.getTRIP_ID(), "");

    }

    private void isPickCaption() {
        driverStatus = d_busy;
        driverUpdateProfile("0", false, "0");
        isSearchingUser = true;
        distanceAndTimeView.show();
        setClientPickedupView(true);

    }

    private void isSecondGoCaption() {
        updateTripStatusApi("begin", controller.pref.getTRIP_ID(), "");
    }

    private void setClientPickedupView(boolean isPicked) {
        this.isPicked = isPicked;
    }

    private boolean getClientPickedupView() {
        return isPicked;
    }



    private void checkTripState() {
        try {

            String isTripComplete = controller.pref.getIS_FARESUMMARY_OPEN();
            String d_available = controller.pref.getD_IS_AVAILABLE();
            boolean bool = controller.getNotiCome();
            fullbuttonText = controller.pref.getFULLBUTTON_CAPTION();
            gobuttonText = controller.pref.getGOBUTTON_CAPTION();
            boolean isPicked = getClientPickedupView();

            if (fullbuttonText.equalsIgnoreCase("Pick") || gobuttonText.equalsIgnoreCase("Go") || gobuttonText.equalsIgnoreCase("End Trip")) {

                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                drawerbut.setVisibility(View.GONE);

                driverUpdateProfile("0",false,"1");
                isSearchingUser=true;
            }

            if (fullbuttonText.equalsIgnoreCase("Go") || fullbuttonText.equalsIgnoreCase("Pick")) {

                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                drawerbut.setVisibility(View.GONE);
                handler.removeMessages(0);
                handler.removeCallbacksAndMessages(null);
                SingleObject object = SingleObject.getInstance();
                object.setIsChangeMag("Yes1");
                setupMap();
                goButton.setText("");
                controller.pref.setGOBUTTON_CAPTION("");

                full.setVisibility(View.VISIBLE);
                fullbutton.setVisibility(View.VISIBLE);

                addressLocationView.show();
                addressLocationView.setLocationTitle("Pickup Location");
                addressLocationView.setLocationAddress(controller.pref.getTRIP_FROM_LOC());
                is_required_drawRoute = "Yes";
                acc = "yes";
                try{
                    userInfoView.show();
                }catch (Exception e){

                }
                if (controller.pref.getFULLBUTTON_CAPTION().equalsIgnoreCase("Pick")) {
                    ifGoCaption();
                }

            } else if (gobuttonText.equalsIgnoreCase("Go") || gobuttonText.equalsIgnoreCase("End Trip")) {
                fullbutton.setText("");
                controller.pref.setFULLBUTTON_CAPTION("");
                goButton.setEnabled(true);
                handler.removeMessages(0);
                handler.removeCallbacksAndMessages(null);
                LinearLayout linear = (LinearLayout) findViewById(R.id.full);
                linear.setVisibility(View.GONE);



                if (gobuttonText.equalsIgnoreCase("Go")) {
                    controller.pref.setTIME_DISTANCE_VIEW(true);
                    showDistanceAndTimeView();
                    SingleObject object = SingleObject.getInstance();
                    object.setIsChangeMag("Yes1");
                    setupMap();
                }

                clientPickedUpView.hide();
                setClientPickedupView(false);

                if (gobuttonText.equalsIgnoreCase("End Trip")) {
                    goButton.setText("End Trip");
                    showDistanceAndTimeView();
                    SingleObject singleObject = SingleObject.getInstance();
                    is_required_drawRoute = "Yes2";
                    singleObject.setIsChangeMag(is_required_drawRoute);
                    setupMap();
                }

                fullbutton.setVisibility(View.GONE);
                goButton.setVisibility(View.VISIBLE);

            } else if (isTripComplete.equalsIgnoreCase("yes_open")) {
                final TripModel tripModel = new TripModel();
                tripModel.tripId = controller.pref.getTRIP_ID();
                tripModel.driver_apikey = controller.pref.getAPI_KEY();
                if(controller.pref.getTRIP_STATUS2().equals("end")){
                    isRequest=true;
                progressDialog.showDialog();

                    getTrip(tripModel);
                }


            }else if (isTripComplete.equalsIgnoreCase("open_destination_view")) {
                handler.removeMessages(0);
                handler.removeCallbacksAndMessages(null);

                destinationActivity.show();
            } else if (bool == true) {
                if (d_available.equals("1") || d_available == "1") {

                    cometRequestNotificaton();
                }


            }


            else {
                /**
                 * when driver is available, then drawer should reset
                 * */

                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
                drawerbut.setVisibility(View.VISIBLE);

                full.setVisibility(View.GONE);
                fullbutton.setVisibility(View.GONE);
                driverUpdateProfile("1",false,"1");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showDistanceAndTimeView() {

        currentTime = getcurrenttime();
        approxTime = controller.pref.getTRIP_DURATION_CHANGABLE();
        distancebtw = controller.pref.getTRIP_DISTANCE_CHANGABLE();
        try {
            boolean distancetime = controller.pref.getTIME_DISTANCE_VIEW();
            boolean driver_rej1 = getDriver_rejected();
            if (driver_rej1 == true) {

                distanceAndTimeView = new DistanceAndTime(SlideMainActivity.this, findViewById(R.id.distance_time_set1), distancebtw, approxTime, currentTime);
                distanceAndTimeView.show();
            } else if (distancetime == true) {
                distanceAndTimeView = new DistanceAndTime(SlideMainActivity.this, findViewById(R.id.distance_time_set), distancebtw, approxTime, currentTime);
                distanceAndTimeView.show();

            } else {
                distanceAndTimeView = new DistanceAndTime(SlideMainActivity.this, findViewById(R.id.distance_time_set), distancebtw, approxTime, currentTime);
                distanceAndTimeView.show();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private TripModel tripModelTemp;

    public void cometRequestNotificaton() {
        setTripRequest(true);

        handler.removeMessages(0);
        handler.removeCallbacksAndMessages(null);
        ivSlideScreen.setVisibility(View.VISIBLE);

        setHide_drawer("Yes");

        if(controller.pref.getTRIP_ID()==null||controller.pref.getTRIP_ID().equals("0")){
            return; }

        final TripModel tripModel = new TripModel();
        tripModelTemp = tripModel;
        tripModel.driver_apikey = controller.pref.getAPI_KEY();
        tripModel.tripId = controller.pref.getTRIP_ID();
        tripModel.tripStatus = controller.pref.getTRIP_STATUS2();

        // initially set blank value thats why we get updated value
        controller.pref.setTRIP_TO_LOC("");
        controller.pref.setTRIP_FROM_LOC("");
        controller.pref.setTRIP_DISTANCE("");
        controller.pref.setTRIP_PICKUP_TIME("");
        if (isRequest) {
          progressDialog.showDialog();
        }
        getTrip(tripModel);
        handler.removeMessages(0);
        handler.removeCallbacksAndMessages(null);
    }

    public static double roundToDecimals(double d, int c) {
        int temp = (int) (d * Math.pow(10, c));
        return ((double) temp) / Math.pow(10, c);
    }

    public void setDistanceAndTimeViewModel(DistanceAndTimeViewModel distanceAndTimeViewModel) {
        this.distanceAndTimeViewModel = distanceAndTimeViewModel;
    }

    public void setHide_drawer(String hide_drawer) {
        this.hide_drawer = hide_drawer;
    }


    public boolean getTripRequest() {
        return tripRequest;
    }

    public void setTripRequest(boolean tripRequest) {
        this.tripRequest = tripRequest;
    }

    public boolean getDriver_rejected() {
        return driver_rejected;
    }

    public void setDriver_rejected(boolean driver_rejected) {
        this.driver_rejected = driver_rejected;
    }

    private void getCarCategoryApi() {
        final String driver_apikey = controller.pref.getAPI_KEY();
        final SingleObject obj = SingleObject.getInstance();
        obj.driverLoginParseApi(controller.pref.getSIGN_IN_RESPONSE());
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
      //  params.put("category_id", obj.getCategory_id());
        WebServiceUtil.excuteRequest(SlideMainActivity.this, params, Constants.GET_CAR_CATEGORY, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (res.isStatus()) {
                        controller.pref.setCatagoryResponce(response);
                        SingleObject obj = SingleObject.getInstance();
                        categoryResponseList = obj.driverCarCategoriesParseApi(response);

                    } else {
                      //  getCarCategoryApi();
                       // Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                } else {
                 //   getCarCategoryApi();
                  //  Toast.makeText(getApplicationContext(), R.string.internet_error, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void getConstantApi() {
        final String driver_apikey = controller.pref.getAPI_KEY();
        final SingleObject obj = SingleObject.getInstance();
        obj.driverLoginParseApi(controller.pref.getSIGN_IN_RESPONSE());
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
        WebServiceUtil.excuteRequest(SlideMainActivity.this, params, Constants.GET_CONSTANTS_API, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {

                if (isUpdate) {
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (data!=null) {
                        SingleObject obj = SingleObject.getInstance();
                        constantsList = obj.driverConstantParseApi(response);
                        controller.setConstantsList(constantsList);

                    } else {
                        getConstantApi();
                        Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    getConstantApi();
                  //  Toast.makeText(getApplicationContext(), R.string.internet_error, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(SlideMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))) {


            } else {
                ActivityCompat.requestPermissions(SlideMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;

                } else {
                    Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    private double currentlatitude;
    private Double currentlongitude;
    private Double distanceCover = 0.0;

    protected BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                if(timer!=null){
                    timer.cancel();
                }
                showRiderDialog(true);

            } catch (Exception e) {

            }
        }


    };

    private boolean isdialogShowing = false;


    //Rider canclled trip popup
    public void showRiderDialog(boolean isUserMessage) {
        if (isdialogShowing) {
            return;
        }
        isdialogShowing = true;
        try {

            final Dialog dialog = new Dialog(SlideMainActivity.this);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.trip_cancelled_layout);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            TextView title2 = (TextView) dialog.findViewById(R.id.tv_dialog_title);
            title2.setTypeface(drawerListTypeface);

            // TextView text = (TextView) dialog.findViewById(R.id.alert_message);
            Button yes = (Button) dialog.findViewById(R.id.yes_btn);
            yes.setTypeface(drawerListTypeface);
            title2.setText(isUserMessage ? getString(R.string.rider_has_cancelled_trip):  getString(R.string.trip_cancelled));
            // text.setText(message);
            yes.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    isdialogShowing = false;
                    controller.stopNotificationSound();
                    updateButtons("cancel");

                }
            });

            dialog.show();
        } catch (Exception e) {
            isdialogShowing = false;
            updateButtons("cancel");
        }

    }
    private BroadcastReceiver broadcastReceiverDriverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };
}


//****************************************
