package com.driver.hire_me;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adaptor.CarCategory;
import com.adaptor.CustomSpinnerAdapter;
import com.adaptor.GetCars;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.com.driver.webservice.CarActors;
import com.com.driver.webservice.CategoryActors;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.custom.NDSpinner;
import com.fonts.Fonts;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DocUploadActivity extends Activity {

    String[] stateArray;
    private Dialog progressDialog;
    SingleObject singleObject;

    NDSpinner categorylistSpinner;
    Spinner  carlistSpinner;
    String[] category, car;
    String newcar = null;
    String cartype;
    int carSize, catSize;
    String seletedcategory;

    private ArrayList<CategoryActors> categoryResponseList;
    private ArrayList<CarActors> carNameList;
    Controller controller;

    public ArrayList<CarCategory> catArrayList;
    public ArrayList<GetCars> getCarArrayList;
    public ArrayList<GetCars> filterdCarList;


    public Bitmap imgBitmap = null;
    public Bitmap bitmapIs = null;
    private String checkLicencImg;


    EditText addcar;

    public static final String PREFS_NAME = "MyPrefsFile";

    ImageView lic, vote;
    ImageView bitmapImgView;
    Button licb, voteb, cancelb;
    TextView nextb;
    String lics, votes, picturePath;
    String proof_status;
    int count = 0;
    int click = 0;
    private static final int RESULT_LOAD_IMAGE = 3;
    private static final int CAMERA_REQUEST = 1;
    private String imagepath;

    String fbuserproimg, WhoLogin, checkpassword;


    private JSONObject login_jsonobj;
    private String login_status;

    protected static final String TAG = "DocUplaodActivity.class";

    String Liveurl = "";
    String Liveurl1 = "";

    private InputMethodManager mIMEMgr;
    private String api_key;
    private ProgressDialog dialog;
    private Typeface typeface;


    /// new
    private String driverId;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_doc_upload);
        typeface = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_CONDENCE);

        //	getActionBar().hide();


        Intent intent = getIntent();

        driverId = intent.getStringExtra("driver_id");
        api_key = intent.getStringExtra("api_key");


        SingleObject object = SingleObject.getInstance();
        api_key = object.getApiKey();

        controller = (Controller) getApplicationContext();

        bitmapImgView = (ImageView) findViewById(R.id.iv_bitmap_img_id);

        controller.pref.setBITMAP_STRING("");
        controller.pref.setLICENSE_BITMAP_TO_STRING_("");

        getCarArrayList = new ArrayList<GetCars>();

        setCheckLicencImg("dummydata");

/**
 * both below lines should be commented after testing car apis
 * */

        //	String dummyApiKey="7d7ca45960aba02047dab13bfad87d82";

//		String dummyApiKey="f35812868b10f468129a2a753c022072";
//		controller.pref.setAPI_KEY(dummyApiKey);

//        User_id = i.getStringExtra("userid");

//		mIMEMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        licb = (Button) findViewById(R.id.addlic);
        licb.setTypeface(typeface);
        voteb = (Button) findViewById(R.id.addvote);
        voteb.setTypeface(typeface);
        cancelb = (Button) findViewById(R.id.canceldoc);
        nextb = (TextView) findViewById(R.id.nextdoc);
        nextb.setTypeface(typeface);

        lic = (ImageView) findViewById(R.id.licimage);
        vote = (ImageView) findViewById(R.id.voteimage);

        categorylistSpinner = (NDSpinner) findViewById(R.id.categorylist);
        carlistSpinner = (Spinner) findViewById(R.id.carlist);
        addcar = (EditText) findViewById(R.id.addcar);
        addcar.setTypeface(typeface);

        getCarCategoryApi();

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Liveurl = sharedPreferences.getString("liveurl", null);
        Liveurl1 = sharedPreferences.getString("liveurl1", null);

        System.out.println("The Live URL Is " + Liveurl);




        cancelb.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @SuppressLint("NewApi")
            public void onClick(View v) {

                clearApplicationData();
                clearSavedPreferences();
                onBackPressed();

//                Intent canp = new Intent(getApplicationContext(), HomeActivity.class);
//                canp.putExtra("fbuserproimg", fbuserproimg);
//                System.out.println("EditProfile  Activity Fb Profile imag" + fbuserproimg);
//                canp.putExtra("whologin", WhoLogin);
//                canp.putExtra("password", checkpassword);
//                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
//                startActivity(canp, bndlanimation);
//                finish();
            }
        });


        licb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                count = 0;
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                setCheckLicencImg("license");

            }
        });

        voteb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                count = 1;
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
                setCheckLicencImg("idproof");
            }
        });


        //to call upload date api, getBitmap for writeImage method


        nextb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String seletedcategory = controller.getCar_category();
                String car_id = controller.getCar_id();
                if(controller.isDocUpdate()) {

                    if (seletedcategory == null || seletedcategory.length() == 0 || seletedcategory.equals("0")) {
                        Toast.makeText(getApplicationContext(), R.string.please_select_car_category, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (car_id == null || car_id.length() == 0 || car_id.equals("0")) {
                        Toast.makeText(getApplicationContext(), R.string.please_select_your_car_first, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (net_connection_check()) {
                        docSubmitApi();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.network_not_found, Toast.LENGTH_LONG).show();
                    }
                }
                else{


                    if (seletedcategory == null || seletedcategory.length() == 0 || seletedcategory.equals("0")) {
                        Toast.makeText(getApplicationContext(), R.string.please_select_car_category, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (car_id == null || car_id.length() == 0 || car_id.equals("0")) {
                        Toast.makeText(getApplicationContext(), R.string.please_select_your_car_first, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String idproof = controller.pref.getBITMAP_STRING();
                    if (idproof == null || idproof.length() == 0) {
                        Toast.makeText(getApplicationContext(), R.string.please_select_your_Id_proof, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String license = controller.pref.getLICENSE_BITMAP_TO_STRING_();
                    if (license == null || license.length() == 0) {
                        Toast.makeText(getApplicationContext(), R.string.please_select_your_license, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int carid = Integer.parseInt(car_id);
                    if (net_connection_check()) {
                        docSubmitApi();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.network_not_found, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


        if (controller.isDocUpdate()) {
            isfirst=true;
            singleObject = controller.getProfileDetails();
            String licenceUrl = Constants.IMAGE_BASE_URL + singleObject.getD_license_image_path();
            String proofUrl = Constants.IMAGE_BASE_URL + singleObject.getD_rc_image_path();
            String insuranceUrl = Constants.IMAGE_BASE_URL+singleObject.getD_insurance_image_path();
            Picasso.with(DocUploadActivity.this).load(licenceUrl).into(lic);
            Picasso.with(DocUploadActivity.this).load(proofUrl).into(vote);


        }else {
            isfirst=false;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("Request Code+requestCode" + "Result Code" + resultCode + "data" + data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                        null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                System.out.println("The Fild Path is" + picturePath);
                cursor.close();
                if (count == 0) {
                    System.out.println("The count is Zero " + count);

                    lic.setScaleType(ImageView.ScaleType.FIT_XY);
                    //lic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    File file = new File(picturePath);
                    Uri uri = Uri.fromFile(file);
                    System.out.println("Uri data ===1 " + uri);
                    Glide.with(DocUploadActivity.this)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            //.transform(new RoundImageTransform(DocUploadActivity.this))
                            .into(lic);
                    new ProgressTask(DocUploadActivity.this).execute();

                } else {
                    System.out.println("The count is Zero " + count);
                    vote.setScaleType(ImageView.ScaleType.FIT_XY);

                    File file = new File(picturePath);
                    Uri uri = Uri.fromFile(file);

                    System.out.println("Uri data ===2 " + uri);

                    Glide.with(DocUploadActivity.this)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            //.transform(new RoundImageTransform(DocUploadActivity.this))

                            .into(vote);
                    // vote.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    new ProgressTask(DocUploadActivity.this).execute();


                }
            }


        } catch (Exception e) {
            Toast.makeText(DocUploadActivity.this, R.string.invalid_image_formet, Toast.LENGTH_LONG).show();
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


    protected void Imageuploading() {
        // TODO Auto-generated method stub\
        System.out.println("" + "Beffer call progress");


        System.out.println("After call progress");
        try {
            Log.e("Image Upload", "SIVa");

            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;
            DataInputStream inputStream = null;

            String pathToOurFile = imagepath;

            System.out.println("Before Image Upload" + imagepath);

            String urlServer = Liveurl1 + "doc_upload";

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
            System.out.println("URL is3 " + url);
            System.out.println("connection is1 " + connection);
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

            if (count == 0) {
                lics = Str1_imageurl.trim();
                System.out.println("Profile Picture Path" + lics);
            } else {
                votes = Str1_imageurl.trim();
                System.out.println("Profile Picture Path" + votes);
            }


        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    public void login_Progress() {
        Intent i = new Intent(getApplicationContext(), SlideMainActivity.class);
        Toast.makeText(getApplicationContext(), R.string.signup_successful, Toast.LENGTH_LONG).show();
        i.putExtra("whologin", WhoLogin);
        i.putExtra("password", checkpassword);
        startActivity(i);
        finish();
    }

    private void close(String alerttitle, String alertmsg) {

        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(alerttitle);
        builder.setMessage(alertmsg);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearApplicationData();
                clearSavedPreferences();
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(x);


            }
        });


        builder.show();


    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");

                    finish();

                }
            }
        }
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

    private void clearSavedPreferences() {


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();

    }

    private class ProgressTask extends AsyncTask<String, Void, Boolean> {
        //final ProgressDialog dialog=showProgress(DocUploadActivity.this);
        private ProgressDialog dialog;
        private DocUploadActivity activity;

        public ProgressTask(DocUploadActivity activity) {
            this.activity = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }

        private Context context;

        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage(getString(R.string.uploading));
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
                imagepath = ImageWrite(BitmapFactory.decodeFile(picturePath));
                System.out.println("Imagepath is ======  " + imagepath);

                //	Imageuploading();

                //Bitmap bitmap4 = BitmapFactory.decodeFile(picturePath);
                Bitmap imgBitmap = BitmapFactory.decodeFile(picturePath);

                Log.d(TAG, "check bitmap====..... " + imgBitmap);
                System.out.println("check bitmap1==== ");

                if (imgBitmap != null) {

                    //	final ProgressDialog dialog5=	showProgress2(DocUploadActivity.this);

                    System.out.println("check bitmappppp== ");
                    final String bitmap3 = getEncoded64ImageStringFromBitmap(imgBitmap);
                    String checkImgBtn = getCheckLicencImg();
                    Log.d(TAG, "check bitmap..... " + bitmap3);

                    System.out.println("check bitmap.....1 " + bitmap3);

                    if (checkImgBtn.equalsIgnoreCase("license") || checkImgBtn == "license") {
                        System.out.println("License image1115 ");

                        if (!imgBitmap.equals(null) || imgBitmap != null) {
                            controller.pref.setLICENSE_BITMAP_TO_STRING_(bitmap3);
                            System.out.println("License image..... ");
                            System.out.println("check bitmap.....4 " + bitmap3);
                            System.out.println("check bitmap.....7 " + Constants.D_LICENSE_IMAGE_PATH);

                            docUpload(Constants.D_LICENSE_IMAGE_PATH, bitmap3);


                        }

                    } else if (checkImgBtn.equalsIgnoreCase("idproof") || checkImgBtn == "idproof") {
                        System.out.println("License image1116 ");

                        if (!imgBitmap.equals(null) || imgBitmap != null) {
                            controller.pref.setBITMAP_STRING(bitmap3);
                            System.out.println("Id proof image.... ");
                            System.out.println("check bitmap.....5 " + bitmap3);

                            docUpload(Constants.D_RC_IMAGE_PATH, bitmap3);

					/*	Timer buttonTimer = new Timer();
                        buttonTimer.schedule(new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {

										//	voteb.setEnabled(false);
										//	is_required_drawRoute = "Yes";

										docUpload(Constants.D_RC_IMAGE_PATH, bitmap3);
									}
								});
							}
						}, 15000);
*/
                        }


                    } else {
                        System.out.println("License image1117 ");

                    }

			/*} catch (NullPointerException e) {
                e.printStackTrace();
				System.out.println("License image1118 ");

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("License image1119 ");

			}*/


                } else {
                    System.out.println("bitmap is nulll ");

                }


                return true;
            } catch (Exception e) {
                Log.e(TAG, "Schedule" + "UpdateSchedule failed" + e);
                return false;
            }
        }

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

    public ProgressDialog showProgress(Context context) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(R.layout.progress_dialog);
        return dialog;

    }


    private void getCarCategoryApi() {
        final ProgressDialog dialog = showProgress(DocUploadActivity.this);
        final String driver_apikey = controller.pref.getAPI_KEY();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_CAR_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dialog.dismiss();
                        if (response != null) {
                            SingleObject obj = SingleObject.getInstance();
                            categoryResponseList = obj.driverCarCategoriesParseApi(response);
                            controller.setCategoryResponseList(categoryResponseList);

                            category = new String[categoryResponseList.size() + 1];
                            String cat_id[] = new String[categoryResponseList.size() + 1];
                            catSize = categoryResponseList.size();
                            category[0] = "Select category";

                            catArrayList = new ArrayList<CarCategory>();
                            CarCategory catOb = new CarCategory("dummy", "0");
                            catArrayList.add(catOb);

                            for (int j = 1; j < (categoryResponseList.size() + 1); j++) {
                                try {
                                    category[j] = categoryResponseList.get(j - 1).getCat_name();
                                    cat_id[j] = categoryResponseList.get(j - 1).getCategory_id();

                                    CarCategory catObj = new CarCategory(cat_id[j], category[j]);
                                    catArrayList.add(catObj);

                                } catch (Exception e) {

                                }
                            }


                            carCategorySpinner(category);
                            for (int j = 0; j < category.length; j++) {
                                System.out.println("category2 = " + category[j]);
                            }

                        } else {

                            Toast.makeText(getApplicationContext(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getApplicationContext(), R.string.no_internet_available, Toast.LENGTH_SHORT).show();
                        else if (error instanceof ServerError) {
                            String d = new String(error.networkResponse.data);
                            try {
                                JSONObject jso = new JSONObject(d);
                                Toast.makeText(getApplicationContext(), jso.getString("message"), Toast.LENGTH_LONG).show();
                                // signUpFacebook();
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

                return params;

            }

        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DocUploadActivity.this);
        requestQueue.add(stringRequest);

    }


    private ArrayList<GetCars> getCarsNameApi(final String category_id2, final boolean isUpdate,final boolean isCarSelect,final boolean isfirstCall) {
        final Controller controller = (Controller) getApplicationContext();
        //SingleObject singleObject=SingleObject.getInstance();
        final ProgressDialog dialog = showProgress(DocUploadActivity.this);

        final String driver_apikey = controller.pref.getAPI_KEY();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_CAR_NAMES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {
                            Log.d(TAG, "Car name response = " + response);
                            System.out.println("Car name response1 = " + response);
                            SingleObject obj = SingleObject.getInstance();

                            carNameList = obj.carNamesParseApi(response);
                            controller.setCarNameList(carNameList);

                            car = new String[carNameList.size() + 1];
                            String car_id[] = new String[carNameList.size() + 1];
                            String category_id[] = new String[carNameList.size() + 1];
                            car[0] = "Select car ";

                                if (getCarArrayList.size() > 0) {

                                    getCarArrayList.clear();


                            }

                            System.out.println("hellooo  == " + category_id2);

                            //GetCars carOb=new GetCars("0","dummy","0");//for 0th position
                            GetCars carOb = new GetCars("0", "Select car", "0");//for 0th position
                            getCarArrayList.add(carOb);

                            for (int i = 1; i < carNameList.size() + 1; i++) {

                                if ((carNameList.get(i - 1).getCategoryId()).equalsIgnoreCase(category_id2) || carNameList.get(i - 1).getCategoryId() == category_id2) {

                                    try {
                                        System.out.println("hellooo2231  == " + category_id2);
                                        System.out.println("hellooo22345  == " + carNameList.get(i - 1).getCategoryId());

                                        car[i] = carNameList.get(i - 1).getCarName();
                                        car_id[i] = carNameList.get(i - 1).getCarId();
                                        category_id[i] = carNameList.get(i - 1).getCategoryId();

                                        GetCars carObj = new GetCars(car_id[i], car[i], category_id[i]);
                                        getCarArrayList.add(carObj);

                                    } catch (Exception e) {

                                    }

                                }
                            }


                            String carname[] = new String[getCarArrayList.size()];

                            for (int m = 0; m < getCarArrayList.size(); m++) {
                                carname[m] = getCarArrayList.get(m).getCar_name();

                                //	System.out.println("Category size======345  " + carname[m] + "  " + carname.length);

                            }


                            carNameSpinner(carname, isUpdate,isCarSelect,isfirstCall);

                            dialog.dismiss();

                            controller.setCarList(getCarArrayList);

                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        isfirst=false;
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getApplicationContext(), R.string.no_internet_available, Toast.LENGTH_SHORT).show();
                        else if (error instanceof ServerError) {
                            String d = new String(error.networkResponse.data);
                            try {
                                JSONObject jso = new JSONObject(d);
                                Toast.makeText(getApplicationContext(), jso.getString("message"), Toast.LENGTH_LONG).show();
                                // signUpFacebook();
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

                return params;

            }

        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DocUploadActivity.this);
        requestQueue.add(stringRequest);

        return getCarArrayList;
    }

    //code for selected item from spinner
    //  final String city= viewHolder.spinnerCity.getSelectedItem().toString();
    private void carNameSpinner(String car[], final boolean isUpdate,final boolean isCarSelct,final boolean isfirstCall) {

        //String[] stateArray = getResources().getStringArray(R.array.india_states);
        stateArray = car;
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_text, stateArray);
        adapter.notifyDataSetChanged();
        adapter.setNotifyOnChange(true);
        carlistSpinner.setAdapter(adapter);

        if (isfirstCall) {
            try {
                for (int j = 0; j <stateArray.length; j++) {
                    if (singleObject.getCar_name().equals(stateArray[j])) {
                        carlistSpinner.setSelection(j);

                    }
                }
                isfirst=false;
            } catch (Exception e) {
                Log.e(TAG,"Car id null");
            }
        }
        carlistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String carString = parent.getItemAtPosition(position).toString();
                String car_id = getCarArrayList.get(position).getCar_id();

                //	int pos = parent.getSelectedItemPosition();
                if (position > 0) {
                    controller.setCar_id(car_id);
                    controller.setCar_name(carString);
                    System.out.println("car id is3221 = " + carString + "   " + car_id);

                }

//                try {
//                if (singleObject.getCar_id() != null) {
//
//                        for (int j = 0; j < carNameList.size(); j++) {
//                            if (singleObject.getCar_id().equals(carNameList.get(j).getCarId())) {
//                                     if(!isCarSelct){
//                                         carlistSpinner.setSelection(j);
//                                     }
//                            }
//                        }
//
//                } } catch (Exception e) {
//                    Log.e(TAG, "Car id null");
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    boolean isfirst=false;

    private void carCategorySpinner(final String carCategory[]) {

        //String[] stateArray = getResources().getStringArray(R.array.india_states);
        stateArray = carCategory;

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_text, stateArray);
        adapter.notifyDataSetChanged();
        adapter.setNotifyOnChange(true);
        categorylistSpinner.setAdapter(adapter);

        categorylistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String carString = parent.getItemAtPosition(position).toString();

                int tempPos1 = 0;
                tempPos1 = position;

                System.out.println("tempPositon1 == " + tempPos1);
                System.out.println("Positon1 == " + position);
                //	int pos = parent.getSelectedItemPosition();

                System.out.println("car category is322 = " + carString);
                if (position != 0 && catArrayList != null) {
                    try {

                        String categoryid = catArrayList.get(position).getCategory_id();

                        controller.setCar_category(categoryid);
                        int tempPos = 0;
                        tempPos = position;

                        System.out.println("tempPositon == " + tempPos);
                        System.out.println("Positon == " + position);


                       if(!isfirst){
                            getCarsNameApi(categoryid, false,true,false);
                       }


                        //if (position !=0 && position != tempPos ) {
       /*                 if (position != 0) {


                        }*/

                        System.out.println("car category is321 = " + catArrayList.get(position).getCategory_id());
                        System.out.println("car category is333 = " + catArrayList.get(position).getCat_name());
                        System.out.println("car category is42 = " + controller.getCar_category());

                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Car categ excep = " + e);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (controller.isDocUpdate()) {

            try {

                for (int j = 0; j < categoryResponseList.size(); j++) {
                    singleObject = controller.getProfileDetails();
                    if (singleObject.getCategory_id().equals(categoryResponseList.get(j).getCategory_id())) {
                        categorylistSpinner.setSelection(j+1);
                        getCarsNameApi(categoryResponseList.get(j).getCategory_id(), true,false,true);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG,"Category id null");
            }
        }
    }


    private void docSubmitApi() {
        final ProgressDialog dialog = new ProgressDialog(DocUploadActivity.this);
        final SingleObject singleObject = SingleObject.getInstance();
        final String driver_apikey = controller.pref.getAPI_KEY();
        final String driverId = singleObject.getDriverId();
        final String car_id = controller.getCar_id();        //it will set carname and car category itself, we have no need to set cartegory here
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(R.layout.progress_dialog);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        if (response != null) {
                            SingleObject object = SingleObject.getInstance();
                            object.driverUpdateProfileParseApi(response);
                            controller.setProfileDetails(object);
                            Intent intent = new Intent(getApplicationContext(), SlideMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getApplicationContext(), R.string.no_internet_available, Toast.LENGTH_SHORT).show();
                        else if (error instanceof ServerError) {
                            String d = new String(error.networkResponse.data);
                            try {
                                JSONObject jso = new JSONObject(d);
                                Toast.makeText(getApplicationContext(), jso.getString("message"), Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                })

        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", driver_apikey);
                params.put("driver_id", driverId);
                params.put("car_id", car_id);
                params.put("d_is_verified", String.valueOf(1));
//				params.put("d_profile_image_path", idproof);
//				params.put("d_license_image_path", license);
//				params.put("category_id", categoryId);
//				params.put("car_name", carName);
//
                return params;

            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DocUploadActivity.this);
        requestQueue.add(stringRequest);

    }

    private void docUpload(final String bitmapKey, final String bitmapValue) {

        final String driver_apikey = controller.pref.getAPI_KEY();
        final String driverId = controller.pref.getDRIVER_ID();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //	dialog1.dismiss();
                        if (response != null) {
                            SingleObject object = SingleObject.getInstance();
                            //	object.driverUpdateProfileParseApi(response);
                            //		System.out.println("responsssssssss  " + response );

                            //		Log.d(TAG, "next button response = " + response);

                            //SingleObject obj=SingleObject.getInstance();
                            //		Toast.makeText(getApplicationContext(),"documents uploaded " + response, Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(getApplicationContext(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //	dialog1.dismiss();
                        System.out.println("Error =====1");
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getApplicationContext(), R.string.no_internet_available, Toast.LENGTH_SHORT).show();
                        else if (error instanceof ServerError) {
                            String d = new String(error.networkResponse.data);
                            try {
                                JSONObject jso = new JSONObject(d);
                                //		Toast.makeText(getApplicationContext(), jso.getString("message"),Toast.LENGTH_LONG).show();
                                // signUpFacebook();
                                //		System.out.println("Error =====2");
                                //		System.out.println("Error =====" + jso);

                            } catch (JSONException e) {
                                e.printStackTrace();

                                //	System.out.println("Error ====" + e);

                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", driver_apikey);
                params.put("driver_id", driverId);
                params.put(bitmapKey, bitmapValue);

                return params;

            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DocUploadActivity.this);
        requestQueue.add(stringRequest);

    }

	/*private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
		//bitmapImgView.setImageBitmap(bitmap);
		byte[] byteArray = byteArrayOutputStream .toByteArray();
		return Base64.encodeToString(byteArray, Base64.DEFAULT);
	}*/

    public String getEncoded64ImageStringFromBitmap(Bitmap bmp) {
        Bitmap bitmap = scaleDown(bmp, 200, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //	bitmapImgView.setImageBitmap(bitmap);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//		AddImageModel imageModel=new AddImageModel();
//		imageModel.setImageUrl(encodedImage);
//		imageModel.setBitmap(bitmap);
//		detalises.add(imageModel);
//		addImageAdapter.notifyDataSetChanged();

        return encodedImage;

    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    //Base64 to bitmap
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public String getCheckLicencImg() {
        return checkLicencImg;
    }

    public void setCheckLicencImg(String checkLicencImg) {
        this.checkLicencImg = checkLicencImg;
    }

    public Bitmap getBitmapIs() {
        return bitmapIs;
    }

    public void setBitmapIs(Bitmap bitmapIs) {
        this.bitmapIs = bitmapIs;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed() {
        clearApplicationData();
        clearSavedPreferences();
//        Intent canp = new Intent(getApplicationContext(), HomeActivity.class);
////        canp.putExtra("userid", User_id);
//        canp.putExtra("fbuserproimg", fbuserproimg);
//        System.out.println("EditProfile  Activity Fb Profile imag" + fbuserproimg);
//        canp.putExtra("whologin", WhoLogin);
//        canp.putExtra("password", checkpassword);
//        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
//        startActivity(canp, bndlanimation);
        finish();
    }

    public ProgressDialog showProgress2(Context context) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(R.layout.progress_dialog);
        return dialog;

    }
}
