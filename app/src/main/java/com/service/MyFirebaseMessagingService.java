package com.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.driver.hire_me.Config;
import com.driver.hire_me.Controller;
import com.driver.hire_me.FareSummaryActivity;
import com.driver.hire_me.R;
import com.driver.hire_me.SlideMainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    private static NotificationManager mNotificationManager;

    Controller controller;
    private int count;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "RemoteMessage : " + remoteMessage.getData());
        controller = (Controller) getApplicationContext();

        Map<String, String> data = ((Map<String, String>) remoteMessage.getData());
        String message = data.get("price");
        String tripStatus = data.get("trip_status");
        String tripId = data.get("trip_id");
        controller.pref.setTRIP_STATUS2(tripStatus);
        controller.pref.setTRIP_ID(tripId);
        controller.setNotificationMessage(data);
        Log.e(TAG, "Trip Status : " + tripStatus +"\n isVisibale "+Controller.isActivityVisible());
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        controller.pref.setIsPush(true);
        if (!Controller.isActivityVisible()) {

            //Some Vars
            final int NOTIFICATION_ID = 1; //this can be any int
            String title = getString(R.string.app_name);

            //   controller.setIspush(true);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.appicon_driver);
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.addLine(message);

            /*android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.appicon_driver)
                    .setContentTitle("HireMe")
                    .setContentText(message)
                    .setStyle(inboxStyle)
                    .setTicker("HireMe")
                    .setLargeIcon(largeIcon)
                    .setAutoCancel(true);*/
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.appicon_driver);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(message);
            mBuilder.setLights(Color.RED, 3000, 3000);
            mBuilder.setSound(uri);
            mBuilder.setAutoCancel(true);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.getNotification().flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;



// Creates an explicit intent for an Activity in your app
            if (controller.pref.getTRIP_STATUS2().equals("Cash")) {

                Intent resultIntent = new Intent(this, FareSummaryActivity.class);

// your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(SlideMainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);

                //PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                final PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,

                                resultIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);
                mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                final int id = 0;
                mNotificationManager.notify(id, mBuilder.build());

            } else {
                Intent resultIntent = new Intent(this, SlideMainActivity.class);

// your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(SlideMainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);

                //PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                final PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,

                                resultIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);
                 mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                final int id = 0;
                mNotificationManager.notify(id, mBuilder.build());
                if(controller.pref.getTRIP_STATUS2().equals("request")){
                    controller.isFromBackground=true;
                    controller.playNotificationSound();
                }
            }


            //  removeNotification(id);


          /*  final Timer buttonTimer = new Timer();
            buttonTimer.schedule(new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            controller.setIsNotiCome(true);

                        }
                    });
                    buttonTimer.cancel();
                }
            }, 50000);*/

            // controller.setIsNotiCome(false);
            count = 7;
            float conte = count;
            controller.setCounter(conte);
            controller.setIsNotiCome(true);
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {


                @Override
                public void run() {
                    count++;
                    try {
                        if (mNotificationManager != null) {
                            mNotificationManager.cancel(0);
                        }
                        if (count < 35) {
                            controller.setIsNotiCome(true);

                            float cont = count;
                            controller.setCounter(cont);
                            System.out.println("in timer==== " + count);
                            System.out.println("in timer====45 " + controller.getNotiCome());
                        } else {
                            System.out.println("in timer else==== ");
                            System.out.println("in timer====46 " + controller.getNotiCome());

                            controller.setIsNotiCome(false);
                            controller.setCounter(0.0f);
                            if (mNotificationManager != null) {
                                mNotificationManager.cancel(0);
                            }
                            timer.cancel();

                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //  timer.cancel();
            }, 35000, 1000);
            // System.out.println("in timer====2 ");
        } else {


            //     String message1="";
//            if (data.size() > 0) {
//
//                RemoteMessage.Notification notification = remoteMessage.getNotification();
//
//            }
//
//
//            if (remoteMessage.getNotification() != null) {
//
//                RemoteMessage.Notification notification = remoteMessage.getNotification();
//
//                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//                Log.d(TAG, "Message Notification Body2: " + notification);
//            }


        /* else if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message1);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else {
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), SlideMainActivity.class);
            resultIntent.putExtra("message", message1);

        }*/


  /*      if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message1);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), SlideMainActivity.class);
            resultIntent.putExtra("message", message1);

        }*/

            if (controller == null) {
                controller.displayMessageOnScreen(this, message);
            }


            generateNotification(this, message, tripStatus, controller);

//            for (Map.Entry<String, String> entry : data.entrySet())
//            {
//                System.out.println(entry.getKey() + "/" + entry.getValue());
//            }

//            JSONObject json = new JSONObject(remoteMessage.getData().toString());

//            Log.e(TAG, "Notification Json: " + message1);

/*
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("HireMeDriver")
                    .setContentText( message)
                    .setTicker("HireMeDriver Message Alert!")
                    .setLargeIcon(largeIcon)
                    .setAutoCancel(true);


// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(this, SlideMainActivity.class);

// your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack( SlideMainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);

            //PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            final PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,

                            resultIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(0, mBuilder.build());*/


        }
    }

   /* private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            System.out.println("push notifi in foreground");

            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            System.out.println("push notifi in background  ");

            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
  //  private void handleDataMessage(String message) {
      //  Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = "HireMeDriver";
            String message = data.getString("price");
         //   String message = controller.pref.getTRIP_STATUS2();


   //         Log.e(TAG, "title: " + title);
       //     Log.e(TAG, "message: " + message);


       *//*     Map<String,String> data1= (Map<String, String>) remoteMessage.getData();
          //  message1 = data1.get("price");
            String message1 = data1.get("price");

            StringTokenizer st = new StringTokenizer(message1);
            // printing next token
            String message2=st.nextToken(",");
            String trip_id=st.nextToken(",");
            System.out.println("trip_status= "+ message2);
            System.out.println("user_id is=" + trip_id);

            Log.i(TAG, "notifi_mesg = " + message1);
            Log.i(TAG, "notifi_mesg_status = " + message2);
            Log.i(TAG, "notifi_mesg trip_id = " + trip_id);

            controller.pref.setTRIP_STATUS2(message2);
            controller.pref.setTRIP_ID(trip_id);

            controller.setNotificationMessage(message2);*//*

            System.out.println("app in background9 ");

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                System.out.println("app in background3 ");

                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                System.out.println("app in background4 ");

                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), SlideMainActivity.class);
                resultIntent.putExtra("message", message);

            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }*/

    /**
     * Showing notification with text only
     *//*
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    */

    /**
     * Showing notification with text and image
     *//*
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }*/

    //   }
    @SuppressWarnings("deprecation")
//    private static void generateNotification(Context context, String message) {
    private static void generateNotification(Context context, String message, String status, Controller controller) {
        Log.e(TAG, "Message : " + message +"   Status "+status);
        //Some Vars
        final int NOTIFICATION_ID = 1; //this can be any int
        String title = context.getString(R.string.app_name);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Building the Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.appicon_driver);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setLights(Color.RED, 3000, 3000);
        builder.setSound(uri);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.getNotification().flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        ;
        //  if(message.equals("paid"))

        if (controller.pref.getTRIP_STATUS2().equals("Cash")||controller.pref.getTRIP_STATUS2().equals("accept_payment_promo")||controller.pref.getTRIP_STATUS2().equals("end")||controller.pref.getTRIP_STATUS2().equals("Paid")) {

            System.out.println("Trip message ==== " + controller.pref.getTRIP_STATUS2());

            Intent notificationIntent = new Intent(context, FareSummaryActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            builder.setContentIntent(intent);

            Intent i = new Intent();
            String mPackage = context.getPackageName();
            // String mClass = ".FareSummaryActivity";
            String mClass = ".FareSummaryActivity";
            i.setComponent(new ComponentName(mPackage, mPackage + mClass));
            i.addFlags(i.FLAG_ACTIVITY_SINGLE_TOP | i.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        } else if (message.equals("manualpay")) {

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

        } else if (message.equals("promopay")) {
            Intent notificationIntent = new Intent(context, FareSummaryActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            builder.setContentIntent(intent);

            Intent i = new Intent();
            String mPackage = context.getPackageName();
            String mClass = ".FareSummaryActivity";
            i.setComponent(new ComponentName(mPackage, mPackage + mClass));
            i.addFlags(i.FLAG_ACTIVITY_SINGLE_TOP | i.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        } else  {
            Intent notificationIntent = new Intent(context, SlideMainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            builder.setContentIntent(intent);
            Intent i = new Intent();
            String mPackage = context.getPackageName();
            String mClass = ".SlideMainActivity";
            i.setComponent(new ComponentName(mPackage, mPackage + mClass));
            i.putExtra("message", message);
            i.addFlags(i.FLAG_ACTIVITY_SINGLE_TOP | i.FLAG_ACTIVITY_NEW_TASK);
            if(controller.pref.getTRIP_STATUS2().equals("cancel")) {
                Intent intnt = new Intent("some_custom_id");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intnt);
              //  Toast.makeText(controller, "Rider cancelled Trip", Toast.LENGTH_SHORT).show();
            }else{
                controller.isFromBackground = false;
                controller.playNotificationSound();  
            }
            context.startActivity(i);
        }

        Intent intent = new Intent();
        intent.setAction(Config.DISPLAY_MESSAGE_ACTION);
         context.sendBroadcast(intent);
        //   final NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {


                mNotificationManager.cancel(NOTIFICATION_ID);
                timer.cancel();

            }
        }, 10000, 1000);
    }


}
