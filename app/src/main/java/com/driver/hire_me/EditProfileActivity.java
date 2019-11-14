package com.driver.hire_me;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class EditProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private static final int RESULT_Gallery_IMAGE = 323;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    Button cancel1, done1;

    Button edpchangepassword;

    Bitmap editBitmap;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final ImageView edpprofileimg = null;
    EditText edpfname, edplname, edpemail, edppayemail, edpmob, edppassword, edpconfirmpassword;


    protected static final String TAG = null;

    private JSONArray edprofile_jsonarray;
    private JSONObject edprofile_jsonobj;
    private String edprofile_status;
    private String edprofile_inputline;
    String edpcheckpassword;
    String get_newpassword = "null";

    String edprofile_userfname, edprofile_userlname, edprofile_useremail, edprofile_pay_useremail, edprofile_usermobile, edprofile_userprofilepic, edprofile_userpassword;
    String User_id;
    String fbuserproimg;
    String Liveurl = "";
    String Liveurl1 = "";
    URL edproimg;
    Bitmap edbitmap;
    SigninActivity sa;
    String WhoLogin;
    String checkpassword;
    ProgressDialog pDialog;

    ImageView profileimgezoom;


    private static final int RESULT_LOAD_IMAGE = 3;
    private static final int CAMERA_REQUEST = 1;
    private long lastClickTime = 0;
    private static final int PICK_FROM_CAMERA = 1;
    private String imagepath;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        bindActivity();

        mToolbar.setTitle("");
        mAppBarLayout.addOnOffsetChangedListener(this);

        setSupportActionBar(mToolbar);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);


        Intent i = getIntent();
        User_id = i.getStringExtra("userid");
        fbuserproimg = i.getStringExtra("fbuserproimg");
        WhoLogin = i.getStringExtra("whologin");
        edpcheckpassword = i.getStringExtra("password");

        System.out.println("Edit profile page get String" + edpcheckpassword);

        System.out.println("USer ID" + User_id);
        System.out.println("Edit Profile Facebook" + fbuserproimg);

        edpchangepassword = (Button) findViewById(R.id.changepassword);

        edpfname = (EditText) findViewById(R.id.edpfirstname);
        edplname = (EditText) findViewById(R.id.edplastname);
        edpemail = (EditText) findViewById(R.id.edpemailadd);
        edppayemail = (EditText) findViewById(R.id.edppayemailadd);
        edpmob = (EditText) findViewById(R.id.edpmobileno);

        edppassword = (EditText) findViewById(R.id.edppassword);

        profileimgezoom = (ImageView) findViewById(R.id.edpprofileimagezoom);

        edpemail.setEnabled(false);

        cancel1 = (Button) findViewById(R.id.pback);
        done1 = (Button) findViewById(R.id.done1);


        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl = sharedPreferences.getString("liveurl", null);
        Liveurl1 = sharedPreferences.getString("liveurl1", null);

        System.out.println("The Live URL Is " + Liveurl);

        System.out.println("Edit Profile Who login now" + WhoLogin);
        System.out.println("Who login now" + WhoLogin);

        if (net_connection_check()) {

            if (WhoLogin.equals("FaceBook") || WhoLogin.equals("GooglePlus")) {
                System.out.println("FaceBook SignIN" + WhoLogin);
                edppassword.setVisibility(View.INVISIBLE);
                edpchangepassword.setVisibility(View.INVISIBLE);
            } else if (WhoLogin.equals("SignIn")) {
                System.out.println("Normal SignIN" + WhoLogin);
                edppassword.setVisibility(View.INVISIBLE);

            } else {
                System.out.println("Normal SignIN" + WhoLogin);
                edppassword.setVisibility(View.INVISIBLE);

            }
            System.out.println("Before try");


            final String url = Liveurl + "display_details?user_id=" + User_id;
            System.out.println("URL is" + url);
            // Creating volley request obj
            JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {


                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    edprofile_jsonobj = response.getJSONObject(i);
                                    edprofile_status = edprofile_jsonobj.getString("status");
                                    edprofile_userfname = edprofile_jsonobj.getString("firstname");
                                    edprofile_userlname = edprofile_jsonobj.getString("lastname");
                                    edprofile_useremail = edprofile_jsonobj.getString("email");
                                    edprofile_pay_useremail = edprofile_jsonobj.getString("paypalemail");
                                    edprofile_usermobile = edprofile_jsonobj.getString("mobile_no");
                                    edprofile_userpassword = edprofile_jsonobj.getString("password");

                                    String image = edprofile_jsonobj.getString("prof_pic");
                                    System.out.println("Profile Image" + image);
                                    if (edprofile_jsonobj.getString("prof_pic").equals("null")) {
                                        edprofile_userprofilepic = fbuserproimg;
                                    } else {
                                        edprofile_userprofilepic = edprofile_jsonobj.getString("prof_pic");
                                    }
                                    Log.d("OUTPUT IS", edprofile_status);
                                    if (edprofile_status.matches("Success")) {


                                        if (edprofile_userfname.equals("null")) {
                                            edpfname.setText("");

                                        } else {
                                            edpfname.setText(edprofile_userfname);
                                        }
                                        if (edprofile_userlname.equals("null")) {
                                            edplname.setText("");
                                        } else {
                                            edplname.setText(edprofile_userlname);
                                        }

                                        if (!edprofile_userfname.equals("null")) {
                                            edpfname.setText(edprofile_userfname);
                                        } else {
                                            StringTokenizer tokens = new StringTokenizer(edprofile_useremail, "@");

                                            String first_string = tokens.nextToken();
                                            String File_Ext = tokens.nextToken();
                                            System.out.println("First_string : " + first_string);
                                            System.out.println("File_Ext : " + File_Ext);

                                            edpfname.setText(first_string);
                                        }

                                        if (edprofile_userlname.equals("null")) {
                                            edplname.setText("");
                                        } else {
                                            edplname.setText(edprofile_userlname);
                                        }

                                        if (edprofile_pay_useremail.equals("Paypalid") || edprofile_pay_useremail == "Paypalid") {
                                            edppayemail.setText("");
                                        } else {
                                            edppayemail.setText(edprofile_pay_useremail);
                                        }

                                        edpemail.setText(edprofile_useremail);

                                        edpmob.setText(edprofile_usermobile);

                                        edproimg = new URL(edprofile_userprofilepic);
                                        edbitmap = BitmapFactory.decodeStream(edproimg.openStream());

                                        profileimgezoom.setImageBitmap(edbitmap);


                                    } else if (edprofile_status.matches("Invalid")) {
                                        Toast.makeText(getApplicationContext(), "Invalid User ID", Toast.LENGTH_LONG).show();

                                    } else {

                                        Toast toast = Toast.makeText(getApplicationContext(), "Kindly double check your credential", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (MalformedURLException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();

                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                            findViewById(R.id.progressBar1).setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());


                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(movieReq);
        }


        //******************************  Edit Profile Picture To capture and  Galary  ********
        profileimgezoom.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                android.support.v7.app.AlertDialog.Builder builder =
                        new android.support.v7.app.AlertDialog.Builder(EditProfileActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setMessage("Please select a Profile image");

                builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent cameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);

                        startActivityForResult(cameraIntent, CAMERA_REQUEST);


                    }
                });
                builder.setNeutralButton("GALLERY", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Intent i = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_IMAGE);


                    }
                });


                builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                });


                builder.show();
            }
        });


        edpchangepassword.setOnClickListener(new View.OnClickListener() {
            ImageView pcancel, psave;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Dialog d = new Dialog(EditProfileActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.activity_password);

                pcancel = (ImageView) d.findViewById(R.id.passwordcancel);
                psave = (ImageView) d.findViewById(R.id.passwordsave);
                //  d.setTitle("Change Password");

                d.show();
                pcancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        d.dismiss();
                    }
                });


                psave.setOnClickListener(new View.OnClickListener() {
                    EditText oldpassword = (EditText) d.findViewById(R.id.oldpswd);
                    EditText newpassword = (EditText) d.findViewById(R.id.newpswd);
                    EditText confirmpassword = (EditText) d.findViewById(R.id.confirmpswd);

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        System.out.println("Old Password" + oldpassword.getText().toString());
                        System.out.println("Check old Password" + edpcheckpassword);

                        if (oldpassword.getText().toString().equals("")) {

                            oldpassword.setError("Required");
                        } else if (newpassword.getText().toString().equals("")) {

                            newpassword.setError("Required");
                        } else if (confirmpassword.getText().toString().equals("")) {

                            confirmpassword.setError("Required");
                        } else if (!oldpassword.getText().toString().equals(edpcheckpassword)) {

                            oldpassword.setError("Incorrect old password");
                        } else if (!confirmpassword.getText().toString().equals(newpassword.getText().toString())) {

                            confirmpassword.setError("Password Mismatch");
                        } else if (newpassword.getText().toString().equals("") && oldpassword.getText().toString().equals("") && confirmpassword.getText().toString().equals("")) {

                            oldpassword.setError("Required");
                        } else if (confirmpassword.getText().toString().equals(newpassword.getText().toString()) && confirmpassword.getText().toString().equals(oldpassword.getText().toString())) {

                            confirmpassword.setError("New password and old password same");
                        } else if (confirmpassword.getText().toString().equals(newpassword.getText().toString())) {

                            final AlertDialog alertDialog4 = new AlertDialog.Builder(EditProfileActivity.this).create();
                            String get_oldpassword = oldpassword.getText().toString();
                            get_newpassword = newpassword.getText().toString();
                            String get_confirmpassword = confirmpassword.getText().toString();

                            edprofile_userpassword = get_newpassword;

                            d.dismiss();

                        }
                    }
                });

            }
        });
    }


    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private void bindActivity() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.pback:
                cancel();
                return true;
            case R.id.done1:
                done1();
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
        v.startAnimation(alphaAnimation);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void done() {


        edpcheckpassword = edprofile_userpassword;
        final String url = Liveurl + "update_details?firstname=" + edprofile_userfname + "&lastname=" + edprofile_userlname + "&mobile_no=" + edprofile_usermobile + "&email=" + edprofile_useremail + "&paypalemail=" + edprofile_pay_useremail + "&user_id=" + User_id + "&prof_pic=" + edprofile_userprofilepic + "&password=" + edprofile_userpassword;
        System.out.println("URL is" + url);
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                edprofile_jsonobj = response.getJSONObject(i);
                                edprofile_status = edprofile_jsonobj.getString("status");
                                Log.d("OUTPUT IS", edprofile_status);
                                if (edprofile_status.matches("Success")) {


                                    Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();

                                    Intent done = new Intent(getApplicationContext(), ProfileActivity.class);
                                    done.putExtra("userid", User_id);
                                    done.putExtra("fbuserproimg", fbuserproimg);
                                    System.out.println("EditProfile Activity Fb Profile imag" + fbuserproimg);
                                    done.putExtra("whologin", WhoLogin);
                                    done.putExtra("password", edprofile_userpassword);
                                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                                    //ProfileActivity.this.progressDialog = ProgressDialog.show(ProfileActivity.this, "", "Loading...",true);

                                    startActivity(done, bndlanimation);

                                    finish();
                                } else if (edprofile_status.matches("Invalid Id")) {
                                    Toast.makeText(getApplicationContext(), "Missing Fields ", Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    private void passwordalart() {

        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage("Do You Want Change Password");

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {

                edprofile_userpassword = edppassword.getText().toString();
                done();
                System.out.println("YES User Password" + edprofile_userpassword);

            }
        });
        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                edprofile_userpassword = edpcheckpassword;
                done();
                System.out.println("NO User Password" + edprofile_userpassword);

            }
        });


        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });


        builder.show();

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            profileimgezoom.setImageBitmap(photo);
            imagepath = ImageWrite(photo);
            new ProgressTask(EditProfileActivity.this).execute();


        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                    null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            profileimgezoom.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imagepath = ImageWrite(BitmapFactory.decodeFile(picturePath));
            new ProgressTask(EditProfileActivity.this).execute();

        }
    }

    public String ImageWrite(Bitmap bitmap1) {

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        File file = new File(extStorageDirectory, "slectimage.PNG");

        try {

            outStream = new FileOutputStream(file);
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        String imageInSD = "/sdcard/slectimage.PNG";

        return imageInSD;

    }

    private class ProgressTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private EditProfileActivity activity;

        public ProgressTask(EditProfileActivity editProfileActivity) {
            this.activity = editProfileActivity;
            context = editProfileActivity;
            dialog = new ProgressDialog(context);
        }

        private Context context;

        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Uploading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (success) {

            } else {

            }
        }

        @Override
        protected Boolean doInBackground(final String... args) {
            try {
                // ... processing ...

                Imageuploading();
                return true;
            } catch (Exception e) {
                Log.e("Schedule", "UpdateSchedule failed", e);
                return false;
            }
        }

    }

    protected void Imageuploading() {
        // TODO Auto-generated method stub

        try {

            Log.e("Image Upload", "SIVa");

            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;
            DataInputStream inputStream = null;

            String pathToOurFile = imagepath;


            System.out.println("Before Image Upload" + imagepath);

            String urlServer = Liveurl1 + "image_upload1";

            System.out.println("URL SETVER" + urlServer);
            System.out.println("After Image Upload" + imagepath);
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;

            FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile));

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();
            System.out.println("URL is " + url);
            System.out.println("connection is " + connection);
            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Enable POST method
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();


            System.out.println("image" + serverResponseMessage);

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            DataInputStream inputStream1 = null;
            inputStream1 = new DataInputStream(connection.getInputStream());
            String str = "";
            String Str1_imageurl = "";

            while ((str = inputStream1.readLine()) != null) {
                Log.e("Debug", "Server Response " + str);

                Str1_imageurl = str;
                Log.e("Debug", "Server Response String imageurl" + str);
            }
            inputStream1.close();
            System.out.println("image url" + Str1_imageurl);

            edprofile_userprofilepic = Str1_imageurl.trim();
            System.out.println("Profile Picture Path" + edprofile_userprofilepic);

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void onBackPressed() {
        Intent can = new Intent(getApplicationContext(), ProfileActivity.class);
        can.putExtra("userid", User_id);
        can.putExtra("fbuserproimg", fbuserproimg);
        can.putExtra("whologin", WhoLogin);
        can.putExtra("password", edpcheckpassword);
        System.out.println("Profile Activity Fb Profile imag" + fbuserproimg);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        startActivity(can, bndlanimation);
        finish();
    }


    public void cancel() {
        Intent can = new Intent(getApplicationContext(), ProfileActivity.class);
        can.putExtra("userid", User_id);
        can.putExtra("fbuserproimg", fbuserproimg);
        can.putExtra("whologin", WhoLogin);
        can.putExtra("password", checkpassword);
        System.out.println("Profile Activity Fb Profile imag" + fbuserproimg);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        startActivity(can);
        finish();

    }

    public void done1() {
        System.out.println("Inside the Done Click");
        edprofile_userfname = edpfname.getText().toString();
        edprofile_userlname = edplname.getText().toString();
        edprofile_useremail = edpemail.getText().toString();
        edprofile_pay_useremail = edppayemail.getText().toString();
        edprofile_usermobile = edpmob.getText().toString();
        System.out.println("After Get details");


        //whologin();
        //  done1.setEnabled(false);

        System.out.println("Who login inside the done click" + WhoLogin);
        if (WhoLogin.equals("FaceBook") || WhoLogin.equals("GooglePlus")) {
            System.out.println("Who login inside the done click" + WhoLogin);

            System.out.println("Edit Profile FaceBook SignIN" + WhoLogin);
            edprofile_userpassword = null;
            System.out.println("FB User Password" + edprofile_userpassword);

        } else if (WhoLogin.equals("SignIn")) {
            System.out.println("Who login inside the done click" + WhoLogin);

            System.out.println("SIGNIN Edit Profil Normal SignIN" + WhoLogin);

            if (get_newpassword.equals("null") || get_newpassword == null) {
                edprofile_userpassword = edpcheckpassword;
                System.out.println("Null get new password is" + get_newpassword);
                System.out.println("Null get Profile Password is" + edprofile_userpassword);

            } else {
                edprofile_userpassword = get_newpassword;
                System.out.println("get new password is" + get_newpassword);
                System.out.println("get Profile Password is" + edprofile_userpassword);

            }
        }

        if (edpfname.getText().toString().equals("")) {
            edpfname.setError("Required");

        } else if (edpmob.getText().toString().equals("")) {
            edpmob.setError("Required");
        } else if (edprofile_usermobile.length() < 6 ||edprofile_usermobile.length() > 12) {
            edpmob.setError("Invalid mobile number");
        } else if (edpemail.getText().toString().equals("")) {
            edpemail.setError("Required");

        } else if (!edprofile_useremail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && edprofile_useremail.length() <= 0) {
            edpemail.setError("Invalid Email Id");
        } else if (edprofile_useremail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && edprofile_useremail.length() > 0) {


            done();

        } else {

            Toast toast = Toast.makeText(getApplicationContext(), "Should Enter A Valid E-Mail ID", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }


    }


    public boolean net_connection_check() {
        ConnectionManager cm = new ConnectionManager(this);

        boolean connection = cm.isConnectingToInternet();


        if (!connection) {

            Toast toast = Toast.makeText(getApplicationContext(), "There is no network please connect.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 70);
            toast.show();
        }
        return connection;
    }



    /*........................camera runtime permisstion......................*/
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(android.Manifest.permission.CAMERA, getPackageName());
            if (ContextCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setMessage("Please allow camera permission first")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        makeRequest();
                    }
                });

                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                    final CharSequence[] options = {"Camera", "Gallery", "Cancel"};

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(EditProfileActivity.this);
                    builder.setTitle("Chosse Image");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Camera")) {
                                dialog.dismiss();
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            } else if (options[item].equals("Gallery")) {
                                dialog.dismiss();
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, RESULT_Gallery_IMAGE);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                } else
                    Toast.makeText(this, "Please provide camera permission", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
    }


  /*  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            editBitmap = photo;
           // profileImage.setImageBitmap(photo);
          //  updateProfileImage(editBitmap);
        } else if (requestCode == RESULT_Gallery_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                // profileImage.setImageBitmap(selectedImage);
              //  profileImage.setImageBitmap(getResizedBitmap(rotateImageIfRequired(selectedImage,EditProfileActivity.this,imageUri),300));
               // updateProfileImage(getResizedBitmap(rotateImageIfRequired(selectedImage,EditProfileActivity.this,imageUri),300));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(EditProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }*/


    /*  Image rotate.......................*/
    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.d("orientation: %s", "->>>>>>>>  "+ orientation);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width/ (float) height;
        if (bitmapRatio < 1 && width > maxSize) {

            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else if(height > maxSize){
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /*........................Image Operation................*/




}