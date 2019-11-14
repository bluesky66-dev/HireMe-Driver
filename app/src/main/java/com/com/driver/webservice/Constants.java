package com.com.driver.webservice;

/**
 * Created by grepixinfotech on 22/07/16.
 */
public interface Constants {
    public static String SETTINGS_NAME="fhjadfjksahdfsdsa.czxch";
    //    public static final String DRIVER_BASE_URL = "http://35.160.185.249/Taxi/index.php";
    public static final String DRIVER_BASE_URL = "http://52.17.80.102/jsmedia/1.0.3/index.php";

    public static final String UPDATE_PROFILE = DRIVER_BASE_URL + "/driverapi/updatedriverprofile?";
    public static final String FORGET_PASSWORD = DRIVER_BASE_URL + "/driverapi/forgetpassword?";
    public static final String DRIVER_TRIP_HISTORY = DRIVER_BASE_URL + "/tripapi/gettrips?";
    public static final String GET_CAR_CATEGORY = DRIVER_BASE_URL + "/categoryapi/getcategories?";
    public static final String GET_CAR_NAMES = DRIVER_BASE_URL + "/carapi/getcars?";
    public static final String GET_CATEGORIES = DRIVER_BASE_URL + "/categoryapi/getcategories?";
    public static final String UPDATE_TRIP_URL = DRIVER_BASE_URL + "/tripapi/updatetrip?";
    public static final String GET_TRIP_URL = DRIVER_BASE_URL + "/tripapi/gettrips?";
    public static final String GET_NEAR_BY_USERS = DRIVER_BASE_URL + "/userapi/getnearbyuserlists?";
    public static final String GET_CONSTANTS_API = DRIVER_BASE_URL + "/constantapi/getconstants?";
    public static final String D_LICENSE_IMAGE_PATH = "driver_license";
    public static final String D_PROFILE_IMAGE_PATH = "driver_image";
    public static final String D_RC_IMAGE_PATH = "driver_rc";
    public static final String D_INSURANCE_IMAGE_PATH = "driver_insurance";
    public static final String IMAGE_BASE_URL = "http://52.17.80.102/jsmedia/images/originals/";

    public static final String CAR_DETAIL="http://appicial.com/sareeie/SareeirApp/cardata.php";





//    public static final String IOS_NOTIFICATION_URL = "http://35.160.185.249/Taxi1.2/push/RiderPushNotification.php?";
//    public static final String ANDROID_NOTIFICATION_URL = "http://35.160.185.249/Taxi1.2/push/RiderAndroidPushNotification.php?";

    public static final String URL_COMMAN_NOTIFICATION = "http://appicial.com/HireTaxi/push/RiderAndroidIosPushNotification.php";
    public final String End_Trip = "End Trip";
    public final String Begin_Trip = "Begin Trip";
    public final String Arrive = "Arrive";
    public final String Accept_yes = "Accept yes";
    public final String Request = "request";
    public final String Home_Page = "Home Page";
    public final String Get_Request = "Get Request";

    public interface Urls {
       // public final String URL_BASE = "http://appicial.com/HireTaxi/1.0.1/index.php";
        public final String URL_DRIVER_REGISTER = DRIVER_BASE_URL + "/driverapi/registration?";
        public final String URL_DRIVER_LOGIN = DRIVER_BASE_URL + "/driverapi/login?";
        public final String URL_ACCEPT_TRIP_REQUEST = DRIVER_BASE_URL + "/tripapi/tripaccept?";
        public final String URL_TO_SHARE = "https://play.google.com/store/apps/details?id=com.driver.hire_me&hl=en";
        String EMAIL_FOR_SUPPORT = "info@appicial.com";
        public final String URL_CREATE_TRIP = DRIVER_BASE_URL +"/tripapi/save?";
        String URL_USER_UPDATE_TRIP = DRIVER_BASE_URL + "/tripapi/updatetrip?";
        String URL_PAYMENT_SAVE = DRIVER_BASE_URL + "/paymentapi/save?";
    }

    public interface Keys {
        public final String Request = "request";
        public final String TRIP_ID = "trip_id";
        public final String DRIVER_ID = "driver_id";
        public final String TRIP_STATUS = "trip_status";
        public final String ANDROID = "android";
        public final String IOS = "ios";
        public final boolean IS_YSE_NO_DIALOG = true;


        String API_KEY = "api_key";
        String TRIP_DATE = "trip_date";
        String TRIP_PICK_LOC = "trip_from_loc";
        String TRIP_DEST_LOC = "trip_to_loc";
        String TRIP_PAY_AMOUNT = "trip_pay_amount";
        String TRIP_PAY_MODE = "trip_pay_mode";
        String TRIP_PAY_STATUS = "trip_pay_status";

        String TRIP_DISTANCE = "trip_distance";



    }


    public interface TripStatus {
        public final String ACCEPT = "accept";
        public final String ARRIVE = "arrive";
        public final String BEGIN = "begin";
        public final String END = "end";
        public final String REJECTED = "rejected";
        public final String DRIVER_CANCEL = "driver_cancel";

    }
    public interface Message{
        //TRIP MESSAGES
        public final String REQUEST = "Hey, You received a new Trip Request. Act Fast";
        public final String REJECTED = "Ohh, Your Trip Request has been cancelled. Try Again";
        public final String ACCEPTED = "Wow, Your Trip Request has been confirmed. Get Ready";
        public final String END = "Your trip completed successfully. Thanks";
        public final String ARRIVE = "Hey, Cab will arrive soon. Be Ready";
        public final String BEGIN = "Your trip has been started. Good Luck";
        public final String DRIVER_CANCEL = "Driver has cancelled the Trip. Ask him";
        public final String PAID = "Payment done. Thanks";
        public final String CASH = "Please collect cash from Rider.";
        public final String PAYPAL = "User made the payment.";

    }

}
