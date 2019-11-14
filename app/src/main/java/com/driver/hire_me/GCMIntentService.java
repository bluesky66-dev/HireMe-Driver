package com.driver.hire_me;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import java.util.Timer;
import java.util.TimerTask;

public class GCMIntentService extends GCMBaseIntentService {


	private static final String TAG = "GCMIntentService";

    private Controller aController = null;

    public GCMIntentService() {
    	// Call extended class Constructor GCMBaseIntentService
        super(Config.GOOGLE_SENDER_ID);

    }

    /**
     * Method called on device registered
     **/

    @Override
    protected void onRegistered(Context context, String registrationId) {
    	
    	//Get Global Controller Class object (see application tag in AndroidManifest.xml)
    	if(aController == null)
           aController = (Controller) getApplicationContext();
    	
        Log.i(TAG, "Device registered: regId = " + registrationId);
        aController.displayMessageOnScreen(context, "Your device registred with GCM");

    }

    /**
     * Method called on device unregistred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
    	if(aController == null)
            aController = (Controller) getApplicationContext();
        Log.i(TAG, "Device unregistered");
        aController.displayMessageOnScreen(context, getString(R.string.gcm_unregistered));

    }

    /**
     * Method called on Receiving a new message from GCM server
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
    	
    	if(aController == null)
            aController = (Controller) getApplicationContext();
    	
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("request");
        
        System.out.println("MEssage from GCM Siva"+message);
        aController.displayMessageOnScreen(context, message);
        // notifies user
        generateNotification(context, message);

    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
    	
    	if(aController == null)
            aController = (Controller) getApplicationContext();
    	
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        aController.displayMessageOnScreen(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
    	
    	if(aController == null)
            aController = (Controller) getApplicationContext();
    	
        Log.i(TAG, "Received error: " + errorId);
        aController.displayMessageOnScreen(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
    	
    	if(aController == null)
            aController = (Controller) getApplicationContext();
    	
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        aController.displayMessageOnScreen(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }


    
    @SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message) {

        //Some Vars
        final int NOTIFICATION_ID = 1; //this can be any int
        String title = context.getString(R.string.app_name);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Building the Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.arcanelogo);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setLights(Color.RED, 3000, 3000);
        builder.setSound(uri);																																																																									
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.getNotification().flags= Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;;
        if(message.equals("paid"))
        {
   /*         Intent notificationIntent = new Intent(context, FareActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            builder.setContentIntent(intent);

            Intent i = new Intent();
            String mPackage = context.getPackageName();
            String mClass = ".FareActivity";
            i.setComponent(new ComponentName(mPackage,mPackage+mClass));
            i.addFlags(i.FLAG_ACTIVITY_SINGLE_TOP |i.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);*/

        }
        else if(message.equals("manualpay"))
        {
           /* Intent notificationIntent = new Intent(context, FareActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            builder.setContentIntent(intent);

            Intent i = new Intent();
            String mPackage = context.getPackageName();
            String mClass = ".FareActivity";
            i.setComponent(new ComponentName(mPackage,mPackage+mClass));
            i.addFlags(i.FLAG_ACTIVITY_SINGLE_TOP |i.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);*/
        }
        else if(message.equals("promopay"))
        {
            Intent notificationIntent = new Intent(context, FareActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            builder.setContentIntent(intent);

            Intent i = new Intent();
            String mPackage = context.getPackageName();
            String mClass = ".FareActivity";
            i.setComponent(new ComponentName(mPackage,mPackage+mClass));
            i.addFlags(i.FLAG_ACTIVITY_SINGLE_TOP |i.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else
        {
            Intent notificationIntent = new Intent(context, SlideMainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            builder.setContentIntent(intent);
            Intent i = new Intent();
            String mPackage = context.getPackageName();
            String mClass=".SlideMainActivity";
            i.setComponent(new ComponentName(mPackage,mPackage+mClass));
            i.putExtra("message",message);
            i.addFlags(i.FLAG_ACTIVITY_SINGLE_TOP |i.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }


        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	notificationManager.cancel(NOTIFICATION_ID);
                timer.cancel();
            }
        }, 10000, 1000);
    }

}
