package com.driver.hire_me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GoOnlineActivity extends Activity {

	Button account,online,online1;
	
    String User_id,fbuserproimg,WhoLogin,checkpassword,location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_go_online);
		getActionBar().hide();
		
		Intent i= getIntent();
		User_id= i.getStringExtra("userid");
		fbuserproimg=i.getStringExtra("fbuserproimg");
		WhoLogin=i.getStringExtra("whologin");
		checkpassword=i.getStringExtra("password");
		location=i.getStringExtra("location");
		
		
		account=(Button)findViewById(R.id.account);
		online=(Button)findViewById(R.id.online);
		online1=(Button)findViewById(R.id.online1);
		
		account.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent profile=new Intent(getApplicationContext(),ProfileActivity.class);
				profile.putExtra("userid", User_id);
				profile.putExtra("fbuserproimg",fbuserproimg);
				System.out.println("Slide Main Activity Fb Profile imag"+fbuserproimg);
				profile.putExtra("whologin",WhoLogin);
				profile.putExtra("password", checkpassword);
				System.out.println("Slide Main Activity WhoLogin"+WhoLogin);
				startActivity(profile);
				finish();
			}
		});
		
		online.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent slide=new Intent(getApplicationContext(),SlideMainActivity.class);
				slide.putExtra("userid", User_id);
				slide.putExtra("fbuserproimg",fbuserproimg);
				System.out.println("Slide Main Activity Fb Profile imag"+fbuserproimg);
				slide.putExtra("whologin",WhoLogin);
				slide.putExtra("password", checkpassword);
				System.out.println("Slide Main Activity WhoLogin"+WhoLogin);
				startActivity(slide);
				finish();
			}
		});
	}

	public void onBackPressed()
 	{
 		
 	}
}
