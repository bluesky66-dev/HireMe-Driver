package com.driver.hire_me;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.animation.Animation;

import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.GoogleApiClient;

import io.fabric.sdk.android.Fabric;

public class SplashScreenActivity extends FragmentActivity {

	private static final int SPLASH_SHOW_TIME = 5000;
	private static final int MY_PERMISSIONS_REQUEST_LOC = 30;
	// Animation
	Animation animZoomIn,animZoomout,blink;
	private Controller controller;
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	private double latitude;
	private double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_driver);
		controller=(Controller)getApplicationContext();
		controller.setShowYesNowDialog(Constants.Keys.IS_YSE_NO_DIALOG);
		new BackgroundSplashTask().execute();
//		 helperMethods=new HelperMethods(SplashScreenActivity.this);
//		///if (helperMethods.net_connection_check()) {
//			getcategory();
//	//	}
	}



	@Override
	protected void onResume() {
		super.onResume();

	}

	private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			// I have just give a sleep for this thread
			// if you want to load database, make
			// network calls, load images
			// you can do here and remove the following
			// sleep

			// do not worry about this Thread.sleep
			// this is an async task, it will not disrupt the UI
			try {
				Thread.sleep(SPLASH_SHOW_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			try {
				String driver_apikey=controller.pref.getAPI_KEY();
				//   if (driver_apikey != null && d_availibility =="1" ) {
				if (driver_apikey != null  ) {
					//String driver_apikey=controller.pref.getAPI_KEY();
					SingleObject obj = SingleObject.getInstance();
					String signin_response = controller.pref.getSIGN_IN_RESPONSE();
					obj.driverLoginParseApi(signin_response);

					//   driverAvailableUpdateProfile();
							Intent intent = new Intent(getApplicationContext(), SlideMainActivity.class);
							startActivity(intent);
							finish();

				}
				else {

					Intent intent=new Intent(SplashScreenActivity.this,HomeActivity.class);
					startActivity(intent);
					finish();

					//  String signin_response = controller.pref.getSIGN_IN_RESPONSE();
				}
			}catch (NullPointerException e){
				e.printStackTrace();
				Intent intent=new Intent(SplashScreenActivity.this,HomeActivity.class);
				startActivity(intent);
				finish();
			}




	}

	}


}
