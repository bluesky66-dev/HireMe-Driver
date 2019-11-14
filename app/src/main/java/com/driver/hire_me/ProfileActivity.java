package com.driver.hire_me;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adaptor.AppController;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

public class ProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer, linearChangePswd;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    //private Toolbar mToolbar;

    protected static final String TAG = "Profile Activity";
    Button Cancel, Edit, Signout;
    public static final String PREFS_NAME = "MyPrefsFile";
    private EditText fname, lname, email, mob, password, payemail;
    private EditText etOldPswd, etNewPswd, etConfirmPswd;
    String checkpassword;
    String prefkey;
    private JSONObject profile_jsonobj, remove_jsonobj;
    private String profile_status, remove_status;
    private String User_id;
    private String profile_inputline, remove_inputline;
    ProgressDialog mDialog;
    private GoogleApiClient mGoogleApiClient;

    String location, accept;

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    String profile_userfname, profile_userlname, profile_paypal_useremail, profile_useremail, profile_usermobile,
            profile_userprofilepic, profile_userpassword;
    String fbuserproimg;
    String Liveurl = "";

    URL proimg;
    Bitmap bitmap;

    ImageView loghide, hrhide;
    ImageView profileimagezoom;


    String WhoLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_profile);
        setContentView(R.layout.activity_profile);


        bindActivity();

        //mToolbar.setTitle("");
//		mAppBarLayout.addOnOffsetChangedListener(this);

        //	setSupportActionBar(mToolbar);
//		startAlphaAnimation(mTitle, 0, View.INVISIBLE);

		/*CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);*/

        Intent i = getIntent();
        User_id = i.getStringExtra("userid");
        fbuserproimg = i.getStringExtra("fbuserproimg");
        checkpassword = i.getStringExtra("password");
        System.out.println("Profile Activity Password" + checkpassword);
        System.out.println("Edit Profile Profile_pic" + fbuserproimg);
        WhoLogin = i.getStringExtra("whologin");
        System.out.println("Profile Page WhoLogin" + WhoLogin);
        location = i.getStringExtra("location");
        accept = i.getStringExtra("accept");

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl = sharedPreferences.getString("liveurl", null);

        System.out.println("The Live URL Is " + Liveurl);

      //  SharedPreferences settings = getSharedPreferences(SigninActivity.PREFS_NAME, 1);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
       // boolean trLoggedIn = settings.getBoolean("trLoggedIn", false);

        //fbuserproimg = settings.getString("fbuserproimg",prefkey);


        fname = (EditText) findViewById(R.id.pfirstname);
        lname = (EditText) findViewById(R.id.plastname);
        email = (EditText) findViewById(R.id.pemailadd);
        payemail = (EditText) findViewById(R.id.ppayemailadd);
        mob = (EditText) findViewById(R.id.pmobileno);

        password = (EditText) findViewById(R.id.ppassword);

        profileimagezoom = (ImageView) findViewById(R.id.profileimagezoom);
        loghide = (ImageView) findViewById(R.id.imageView5);
        hrhide = (ImageView) findViewById(R.id.ImageView01);


        System.out.println("Who login now" + WhoLogin);
        if (WhoLogin.equals("FaceBook") || WhoLogin.equals("GooglePlus")) {
            System.out.println("FaceBook SignIN" + WhoLogin);
            password.setVisibility(View.INVISIBLE);
            loghide.setVisibility(View.INVISIBLE);
            hrhide.setVisibility(View.INVISIBLE);
        } else if (WhoLogin.equals("SignIn")) {

            System.out.println("Normal SignIN" + WhoLogin);
            password.setVisibility(View.VISIBLE);
            loghide.setVisibility(View.VISIBLE);
            hrhide.setVisibility(View.VISIBLE);

        } else {
            System.out.println("Normal SignIN" + WhoLogin);
            password.setVisibility(View.VISIBLE);
            loghide.setVisibility(View.VISIBLE);
            hrhide.setVisibility(View.VISIBLE);
        }


        final String url = Liveurl + "display_details2?user_id=" + User_id;
        System.out.println("URL is" + url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                profile_jsonobj = response.getJSONObject(i);
                                profile_status = profile_jsonobj.getString("status");
                                User_id = profile_jsonobj.getString("id");
                                profile_userfname = profile_jsonobj.getString("firstname");
                                profile_userlname = profile_jsonobj.getString("lastname");
                                profile_useremail = profile_jsonobj.getString("email");

                                profile_usermobile = profile_jsonobj.getString("mobile_no");

                                if (!profile_userfname.equals("null")) {
                                    fname.setText(profile_userfname);
                                } else {
                                    StringTokenizer tokens = new StringTokenizer(profile_useremail, "@");

                                    String first_string = tokens.nextToken();
                                    String File_Ext = tokens.nextToken();
                                    System.out.println("First_string : " + first_string);
                                    System.out.println("File_Ext : " + File_Ext);

                                    fname.setText(first_string);
                                }

                                if (profile_userlname.equals("null")) {
                                    lname.setText("");
                                } else {
                                    lname.setText(profile_userlname);
                                }

                                System.out.println("FNAME IS" + profile_userfname);
                                System.out.println("LNAME IS" + profile_userlname);

                                String image = profile_jsonobj.getString("prof_pic");
                                System.out.println("Profile Image" + image);
                                if (profile_jsonobj.getString("prof_pic").equals("null") || profile_jsonobj.getString("prof_pic").equals("")) {
                                    profile_userprofilepic = fbuserproimg;
                                    System.out.print("Insite the Profile Activity Profile image IF" + profile_userprofilepic);
                                } else {
                                    profile_userprofilepic = profile_jsonobj.getString("prof_pic");
                                    System.out.print("Insite the Profile Activity Profile image ELSE" + profile_userprofilepic);
                                }

                                System.out.print("Insite the Profile Activity Profile image" + profile_userprofilepic);
                                profile_userpassword = profile_jsonobj.getString("password");

                                Log.d("OUTPUT IS", profile_status);
                                if (profile_status.matches("Success")) {

                                    email.setText(profile_useremail);
                                    mob.setText(profile_usermobile);
                                    password.setText(profile_userpassword);

                                    proimg = new URL(profile_userprofilepic);
                                    bitmap = BitmapFactory.decodeStream(proimg.openStream());

                                    profileimagezoom.setImageBitmap(bitmap);

                                    System.out.println("First NAME IS" + profile_userfname);
                                    System.out.println("LAst NAME IS" + profile_userlname);

                                    fname.setEnabled(false);
                                    lname.setEnabled(false);
                                    email.setEnabled(false);
                                    payemail.setEnabled(false);
                                    mob.setEnabled(false);
                                    password.setEnabled(false);

                                    profileimagezoom.setClickable(false);

                                } else if (profile_status.matches("Invalid")) {
                                    Toast.makeText(getApplicationContext(), "Invalid User ID", Toast.LENGTH_LONG).show();

                                }
                            } catch (MalformedURLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                        findViewById(R.id.progressBar1).setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    System.out.println("No internet connection");
                    Toast toast = Toast.makeText(getApplicationContext(), "There is no network please connect.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 70);
                    toast.show();
                }
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    private void bindActivity() {
        //	mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        //	mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);

        linearChangePswd = (LinearLayout) findViewById(R.id.linear_change_pswd);
        etOldPswd = (EditText) findViewById(R.id.oldpswd);
        etNewPswd = (EditText) findViewById(R.id.newpswd);
        etConfirmPswd = (EditText) findViewById(R.id.confirmpswd);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.pback:
                cancel();
                return true;
            case R.id.editprofile:
                edit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        //	v.startAnimation(alphaAnimation);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")


    public void cancel() {
        Intent can = new Intent(getApplicationContext(), SlideMainActivity.class);
        can.putExtra("userid", User_id);
        can.putExtra("fbuserproimg", fbuserproimg);
        can.putExtra("whologin", WhoLogin);
        can.putExtra("password", checkpassword);
        System.out.println("Profile Activity Fb Profile imag" + fbuserproimg);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        startActivity(can);
        finish();

    }

    public void edit() {
        Intent edit = new Intent(getApplicationContext(), EditProfileActivity.class);
        edit.putExtra("userid", User_id);
        edit.putExtra("fbuserproimg", fbuserproimg);
        System.out.println("Profile Activity Fb Profile imag" + fbuserproimg);
        edit.putExtra("whologin", WhoLogin);
        edit.putExtra("password", checkpassword);

        //	Bundle bndlanimation =ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in,R.anim.trans_right_out).toBundle();
        startActivity(edit);
        finish();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void onBackPressed() {
        Intent can = new Intent(getApplicationContext(), SlideMainActivity.class);
        can.putExtra("userid", User_id);
        can.putExtra("fbuserproimg", fbuserproimg);
        can.putExtra("whologin", WhoLogin);
        System.out.println("Profile Activity Fb Profile imag" + fbuserproimg);
        can.putExtra("accept", accept);

        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        startActivity(can, bndlanimation);
        finish();
    }

    /**
     * Sign-out from google
     */

    public boolean net_connection_check() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();


        if (!connection) {
            dialogcalling();
        }
        return connection;
    }

    public void dialogcalling() {
        final Dialog dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar);

        // Setting dialogview
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        // dialog.setTitle("There is no network please conect");
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(true);

        dialog.show();

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });
        handler.postDelayed(runnable, 3000);


    }

}
	
