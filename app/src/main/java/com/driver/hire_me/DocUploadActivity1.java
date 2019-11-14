package com.driver.hire_me;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adaptor.CarCategory;
import com.adaptor.CarDetails;
import com.adaptor.CustomSpinnerAdapter;
import com.adaptor.GetCars;
import com.adaptor.IgnorCaseComprator;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.com.driver.webservice.CarActors;
import com.com.driver.webservice.CategoryActors;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.custom.CustomProgressDialog;
import com.custom.NDSpinner;
import com.fonts.Fonts;
import com.grepix.grepixutils.CloudResponse;
import com.grepix.grepixutils.ErrorJsonParsing;
import com.grepix.grepixutils.Utils;
import com.grepix.grepixutils.WebServiceUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class DocUploadActivity1 extends Activity {


    String[] stateArray;
    SingleObject singleObject;

    NDSpinner categorylistSpinner, carlistSpinner, carmodelSpinner, yearSpinner;
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
    Button licb, voteb, cancelb, insurance;
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

    protected static final String TAG = "DocUplaodActivity1.class";

    String Liveurl = "";
    String Liveurl1 = "";

    private InputMethodManager mIMEMgr;
    private String api_key;
    private Typeface typeface;


    // new
    private String driverId;
    private EditText regNumber;

    private String getCarResponce;
    private ImageView idUpload, licenseUpload, insuranceUpload;
    private String carYearString = "";
    private String carModelString = "";
    private String carMakeString = "";
    private CarDetails carDetail;

    private CustomProgressDialog progressDialog;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_doc_upload1);

        progressDialog=new CustomProgressDialog(DocUploadActivity1.this);

        typeface = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_CONDENCE);

        //	getActionBar().hide();


        final SingleObject object = SingleObject.getInstance();
        api_key = object.getApiKey();

        controller = (Controller) getApplicationContext();

        bitmapImgView = (ImageView) findViewById(R.id.iv_bitmap_img_id);

        controller.pref.setBITMAP_STRING("");
        controller.pref.setLICENSE_BITMAP_TO_STRING_("");

        getCarArrayList = new ArrayList<GetCars>();

        setCheckLicencImg("dummydata");



        licb = (Button) findViewById(R.id.add_license);
        licb.setTypeface(typeface);
        voteb = (Button) findViewById(R.id.add_id_proof);
        voteb.setTypeface(typeface);
        insurance = (Button) findViewById(R.id.addinsurance);
        insurance.setTypeface(typeface);
        cancelb = (Button) findViewById(R.id.canceldoc);
        nextb = (TextView) findViewById(R.id.nextdoc);
        nextb.setTypeface(typeface);

        lic = (ImageView) findViewById(R.id.licimage);
        vote = (ImageView) findViewById(R.id.voteimage);
        idUpload = (ImageView) findViewById(R.id.id_upload);
        licenseUpload = (ImageView) findViewById(R.id.license_upload);
        insuranceUpload = (ImageView) findViewById(R.id.insurance_upload);

        categorylistSpinner = (NDSpinner) findViewById(R.id.categorylist);
        carlistSpinner = (NDSpinner) findViewById(R.id.carlist);
        carmodelSpinner = (NDSpinner) findViewById(R.id.select_a_model);
        yearSpinner = (NDSpinner) findViewById(R.id.select_a_year);
        addcar = (EditText) findViewById(R.id.addcar);
        regNumber = (EditText) findViewById(R.id.ca_reg_no);

        regNumber.setTypeface(typeface);
        addcar.setTypeface(typeface);

        getCarsNameApi();




        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        cancelb.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @SuppressLint("NewApi")
            public void onClick(View v) {

                clearApplicationData();
                clearSavedPreferences();

                if(controller.isDocUpdate()){
                    finish();
                }else{
                    Intent intent= new Intent(DocUploadActivity1.this,RegisterActivity.class);
                    startActivity(intent);
                }

            }
        });


        licb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try{
                    count = 0;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    setCheckLicencImg("license");
                    //  licenseUpload.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Toast.makeText(DocUploadActivity1.this, R.string.invalid_image_formet, Toast.LENGTH_LONG).show();
                }


            }
        });

        voteb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try{
                    count = 0;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    setCheckLicencImg("idproof");
                    //  licenseUpload.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Toast.makeText(DocUploadActivity1.this, R.string.invalid_image_formet, Toast.LENGTH_LONG).show();
                }
            }
        });

        insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    count = 0;
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    setCheckLicencImg("insurance");
                    //  licenseUpload.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Toast.makeText(DocUploadActivity1.this, R.string.invalid_image_formet, Toast.LENGTH_LONG).show();
                }
            }
        });


        //to call upload date api, getBitmap for writeImage method


        nextb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
               try {
                   String reg_no = regNumber.getText().toString();
                   seletedcategory = String.valueOf(getCraCateoryID(categorylistSpinner.getSelectedItem().toString()));

                   if (Utils.net_connection_check(DocUploadActivity1.this)) {
                       if(controller.isDocUpdate()) {
                          /* if(!carMakeString.equalsIgnoreCase(singleObject.getCarName())
                                   || !carModelString.equalsIgnoreCase(singleObject.getCarModel())
                                   || !carYearString.equalsIgnoreCase(singleObject.getCarYear())
                                   || !seletedcategory.equalsIgnoreCase(singleObject.getCategory_id()))
                           {*/
                               if (reg_no.equalsIgnoreCase("")) {
                                   Toast.makeText(DocUploadActivity1.this, "Please enter your Licence no.", Toast.LENGTH_SHORT).show();
                               }/*else if(singleObject.getCarRegNo().equalsIgnoreCase(reg_no)){
                                   Toast.makeText(DocUploadActivity1.this, "Please change your Licence no.", Toast.LENGTH_SHORT).show();
                               }*/
                               else{
                                   docSubmitApi(carYearString, carMakeString, carModelString, reg_no, seletedcategory);
                               }
                           /*} else {
                               if (reg_no.equalsIgnoreCase("")) {
                                   Toast.makeText(DocUploadActivity1.this, "Please enter your Licence no.", Toast.LENGTH_SHORT).show();
                               }else{
                                   docSubmitApi(carYearString, carMakeString, carModelString, reg_no, seletedcategory);
                               }
                           }*/
                       }else{
                           if(reg_no.equalsIgnoreCase("")){
                               Toast.makeText(DocUploadActivity1.this, "Please enter License no.", Toast.LENGTH_SHORT).show();
                           }else{
                               docSubmitApi(carYearString, carMakeString, carModelString, reg_no, seletedcategory);
                           }
                       }
                   } else {
                       Toast.makeText(getApplicationContext(), R.string.network_not_found, Toast.LENGTH_LONG).show();
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        });




        if (controller.isDocUpdate()) {
            isfirst = true;
            singleObject = controller.getProfileDetails();

            String licenceUrl = Constants.IMAGE_BASE_URL + singleObject.getD_license_image_path();
            String proofUrl = Constants.IMAGE_BASE_URL + singleObject.getD_rc_image_path();
            if (singleObject.getD_license_image_path()==null||singleObject.getD_license_image_path().equals("")||singleObject.getD_license_image_path().equals("null")){
            }else{
                licenseUpload.setVisibility(View.VISIBLE);
            }
            if (singleObject.getD_rc_image_path()==null||singleObject.getD_rc_image_path().equals("")||singleObject.getD_rc_image_path().equals("null")){
            }else{
                idUpload.setVisibility(View.VISIBLE);
            }

            if (singleObject.getD_insurance_image_path()==null||singleObject.getD_insurance_image_path().equals("")||singleObject.getD_insurance_image_path().equals("null")){

            }else{
                insuranceUpload.setVisibility(View.VISIBLE);
            }
                if(!singleObject.getCarRegNo().equalsIgnoreCase("null")) {
                    regNumber.setText(singleObject.getCarRegNo());
                }else{
                    regNumber.setHint("Enter licence number");
                }
           /* if(licenceUrl!=null
                    || !licenceUrl.equalsIgnoreCase("")
                    || !licenceUrl.equalsIgnoreCase("null")
                    || proofUrl!=null
                    || !proofUrl.equalsIgnoreCase("")
                    || !proofUrl.equalsIgnoreCase("null")) {
                Picasso.with(DocUploadActivity1.this).load(licenceUrl).into(lic);
                Picasso.with(DocUploadActivity1.this).load(proofUrl).into(vote);
            }
*/
        } else {
            isfirst = false;
        }
    }

    int progressCount=0;


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                        null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                progressDialog.showDialog();
                if (count == 0) {
                    lic.setScaleType(ImageView.ScaleType.FIT_XY);
                    File file = new File(picturePath);
                    Uri uri = Uri.fromFile(file);
                    Glide.with(DocUploadActivity1.this)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(lic);

                    new ProgressTask(DocUploadActivity1.this).execute();

                } else {
                    vote.setScaleType(ImageView.ScaleType.FIT_XY);

                    File file = new File(picturePath);
                    Uri uri = Uri.fromFile(file);

                    Glide.with(DocUploadActivity1.this)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(vote);
                    new ProgressTask(DocUploadActivity1.this).execute();


                }
            }


        } catch (Exception e) {
            Toast.makeText(DocUploadActivity1.this, R.string.invalid_image_formet, Toast.LENGTH_LONG).show();
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
        private DocUploadActivity1 activity;


        public ProgressTask(DocUploadActivity1 activity) {
            this.activity = activity;
            context = activity;
        }

        private Context context;

        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (success) {

            } else {

            }
        }

        @Override
        protected Boolean doInBackground(final String... args) {
            try {
                imagepath = ImageWrite(BitmapFactory.decodeFile(picturePath));
                Bitmap imgBitmap = BitmapFactory.decodeFile(picturePath);


                if (imgBitmap != null) {
                    final String bitmap3 = getEncoded64ImageStringFromBitmap(imgBitmap);
                    String checkImgBtn = getCheckLicencImg();
                    if (checkImgBtn.equalsIgnoreCase("license") || checkImgBtn == "license") {
                        if (!imgBitmap.equals(null) || imgBitmap != null) {
                            controller.pref.setLICENSE_BITMAP_TO_STRING_(bitmap3);
                            docUpload(Constants.D_LICENSE_IMAGE_PATH, bitmap3);
                        }

                    } else if (checkImgBtn.equalsIgnoreCase("idproof") || checkImgBtn == "idproof") {
                        if (!imgBitmap.equals(null) || imgBitmap != null) {
                            controller.pref.setBITMAP_STRING(bitmap3);
                            docUpload(Constants.D_RC_IMAGE_PATH, bitmap3);
                        }



                    } else if (checkImgBtn.equalsIgnoreCase("insurance") || checkImgBtn == "insurance") {
                        if (!imgBitmap.equals(null) || imgBitmap != null) {
                            controller.pref.setBITMAP_STRING(bitmap3);
                            docUpload(Constants.D_INSURANCE_IMAGE_PATH, bitmap3);
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }

    }





    private void getCarCategoryApi() {
        final String driver_apikey = controller.pref.getAPI_KEY();
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
        WebServiceUtil.excuteRequest(DocUploadActivity1.this, params, Constants.GET_CAR_CATEGORY, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                   progressDialog.dismiss();
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (res.isStatus()) {

                        CategoryActors actors = new CategoryActors();
                        actors.setCategory_id("4");
                        actors.setCat_name("Bike");
                        actors.setCat_desc("bike");
                        actors.setCat_base_price("10");
                        actors.setCat_fare_per_km("1");
                        actors.setCat_fare_per_min("1");
                        actors.setCat_max_size("2");
                        actors.setCat_is_fixed_price("1");
                        actors.setCat_prime_time_percentage("10");
                        actors.setCat_status("1");
                        actors.setCat_created("2018-01-22 00:00:00");
                        actors.setCat_modified("2018-01-22 00:00:00");

                        SingleObject obj = SingleObject.getInstance();
                        categoryResponseList = obj.driverCarCategoriesParseApi(response);

                        categoryResponseList.add(actors);

                        Collections.sort(categoryResponseList, new Comparator<CategoryActors>() {
                            @Override
                            public int compare(CategoryActors t0, CategoryActors t1) {
                                return t0.getCategory_id().compareTo(t1.getCategory_id());
                            }
                        });

                        controller.setCategoryResponseList(categoryResponseList);

                        category = new String[categoryResponseList.size()];
                        String cat_id[] = new String[categoryResponseList.size()];
                        catSize = categoryResponseList.size();
                       // category[0] = "Select a category";

                        catArrayList = new ArrayList<CarCategory>();
                        CarCategory catOb = new CarCategory("dummy", "0");
                        catArrayList.add(catOb);

                        for (int j = 0; j < (categoryResponseList.size()); j++) {
                            try {
                                category[j] = categoryResponseList.get(j).getCat_name();
                                cat_id[j] = categoryResponseList.get(j).getCategory_id();

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
                        Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    progressDialog.dismiss();
                }
            }
        });
    }


    private void getCarsNameApi() {
       progressDialog.showDialog();
        final String driver_apikey = controller.pref.getAPI_KEY();
        final ArrayList<String> carnamelist = new ArrayList<>();
        count = 0;
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
        WebServiceUtil.excuteRequest(DocUploadActivity1.this, params, Constants.CAR_DETAIL, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    getCarCategoryApi();
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (data!=null) {
                        try {

                            getCarResponce = response;
                            JSONObject object = new JSONObject(response);
                            Iterator iterator = object.keys();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();

                                carnamelist.add(key);
                            }
                            IgnorCaseComprator icc = new IgnorCaseComprator();
                            java.util.Collections.sort(carnamelist, icc);
                          //  carnamelist.add(0, "Select a Make");
                            carNameSpinner(carnamelist);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                }else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    //code for selected item from spinner

    ArrayList<String> carModel = new ArrayList<>();
    ArrayList<String> carYears = new ArrayList<>();
    HashMap<String, String> carDetailsList = new HashMap<>();

    private void carNameSpinner(ArrayList<String> carnamelist) {

        final String[] stateArray = carnamelist.toArray(new String[carnamelist.size()]);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_text, stateArray);
        adapter.notifyDataSetChanged();
        adapter.setNotifyOnChange(true);
        carlistSpinner.setAdapter(adapter);


        carlistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       progressDialog.showDialog();

                if (parent.getCount() > 0) {

                    carMakeString = parent.getItemAtPosition(position).toString();

                    try {
                        JSONObject object = new JSONObject(getCarResponce);
                        JSONArray jsonArray = object.getJSONArray(stateArray[position]);
                        if (carModel.size() > 0) {
                            carModel.clear();
                        }
                        if (carYears.size()>0){
                            carYears.clear();
                        }
                        if (carDetailsList.size() > 0) {
                            carDetailsList.clear();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            CarDetails carDetails = new CarDetails();
                            String carId = object1.getString("id");
                            String carMake = object1.getString("make");
                            carDetails.setMake(carMake);

                            String carMod = object1.getString("model");
                            String carY = object1.getString("year");

                            carModel.add(carMod);

                            carDetailsList.put(carMod + ":" + carY, carId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    carMakeString = "";
                }


                int thisYear = Calendar.getInstance().get(Calendar.YEAR);
                int last50Year = Calendar.getInstance().get(Calendar.YEAR) - 50;
                for (int i = thisYear; i >= last50Year; i--) {
                    carYears.add(Integer.toString(i));
                }
               // carYears.add(0, "Select a Year");
                carYearSpinner(carYears);

                HashSet<String> hashSet1 = new HashSet<String>();
                hashSet1.addAll(carModel);
                carModel.clear();
                carModel.addAll(hashSet1);
                IgnorCaseComprator icc = new IgnorCaseComprator();
                java.util.Collections.sort(carModel, icc);
              //  carModel.add(0, "Select a Model");
                carModelSpinner(carModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      //  carlistSpinner.setSelection(0);

        if (controller.isDocUpdate()) {
            try {

                for (int j = 0; j < carnamelist.size(); j++) {

                    if (singleObject.getCarName().equals(carnamelist.get(j))) {
                        carlistSpinner.setSelection(j);
                    }
                }
            } catch (Exception e) {
            }
        }

    }


    private void carYearSpinner(ArrayList<String> carYear) {
        final String[] stateArray = carYear.toArray(new String[carYear.size()]);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_text, stateArray);
        adapter.notifyDataSetChanged();
        adapter.setNotifyOnChange(true);
        yearSpinner.setAdapter(adapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position >= 0) {
                    carYearString = (String) parent.getItemAtPosition(position);
                } else {
                    carYearString = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

     //   yearSpinner.setSelection(0);

        if (controller.isDocUpdate()) {
            try {

                for (int j = 0; j < carYear.size(); j++) {

                    if (singleObject.getCarYear().equals(carYear.get(j))) {
                        yearSpinner.setSelection(j);
                    }
                }
            } catch (Exception e) {
            }
        }

    }


    private void carModelSpinner(ArrayList<String> carModel) {
        final String[] stateArray = carModel.toArray(new String[carModel.size()]);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_text, stateArray);
        adapter.notifyDataSetChanged();
        adapter.setNotifyOnChange(true);
        carmodelSpinner.setAdapter(adapter);
        progressDialog.dismiss();
        carmodelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position >= 0) {
                    carModelString = (String) parent.getItemAtPosition(position);

                } else {
                    carModelString = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      //  carmodelSpinner.setSelection(0);

        if (controller.isDocUpdate()) {
            try {

                for (int j = 0; j < carModel.size(); j++) {

                    if (singleObject.getCarModel().equals(carModel.get(j))) {
                        carmodelSpinner.setSelection(j);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    boolean isfirst = false;

    private void carCategorySpinner(final String carCategory[]) {

        stateArray = carCategory;

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_text, stateArray);
        adapter.notifyDataSetChanged();
        adapter.setNotifyOnChange(true);
        categorylistSpinner.setAdapter(adapter);

        categorylistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0 && catArrayList != null) {
                    try {
                        seletedcategory = catArrayList.get(position).getCategory_id();
                        controller.setCar_category(seletedcategory);

                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                } else {
                    seletedcategory = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

     //   categorylistSpinner.setSelection(0);

        if (controller.isDocUpdate()) {

            try {

                for (int j = 0; j < categoryResponseList.size(); j++) {
                    singleObject = controller.getProfileDetails();
                    if (singleObject.getCategory_id().equals(categoryResponseList.get(j).getCategory_id())) {
                        categorylistSpinner.setSelection(j );
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public int getCraCateoryID(String selectedCarName){
        int i = 1;
        if(selectedCarName.equalsIgnoreCase("Hatchback")){
            i=1;
        }else if(selectedCarName.equalsIgnoreCase("Sedan")){
            i=2;
        }else if(selectedCarName.equalsIgnoreCase("SUV")){
               i=3;
        }else if(selectedCarName.equalsIgnoreCase("Bike")){
            i=4;
        }
        return i;
    }


    private void docSubmitApi(final String year, final String carMakeString, final String carModelString, final String reg_no, final String seletedcategory) {

        progressDialog.showDialog();

        final SingleObject singleObject = SingleObject.getInstance();
        final String driver_apikey = controller.pref.getAPI_KEY();
        final String driverId = singleObject.getDriverId();
        Map<String, String> params = new HashMap<String, String>();
            params.put("api_key", driver_apikey);
            params.put("driver_id", driverId);
            params.put("category_id", seletedcategory);
            params.put("car_model", year);
            params.put("car_reg_no", reg_no);
            params.put("car_make", carMakeString);
            params.put("car_name", carModelString);
            params.put("d_is_verified", String.valueOf(true));
           System.out.print(""+params);
        WebServiceUtil.excuteRequest(DocUploadActivity1.this, params, Constants.UPDATE_PROFILE, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (res.isStatus()) {
                        SingleObject object = SingleObject.getInstance();
                        object.driverUpdateProfileParseApi(response);
                        controller.setProfileDetails(object);
                        controller.setSignInResponse(response);
                        controller.pref.setSIGN_IN_RESPONSE(response);
                        if(controller.isDocUpdate()){
                            progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), SlideMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finishAffinity();
                        }else{
                          //  UpdateIsVerified();
                            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        progressDialog.dismiss();
                       // Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                     //   Toast.makeText(getApplicationContext(), "Error.....", Toast.LENGTH_LONG).show();

                    }
                }else{
                    progressDialog.dismiss();
                }
            }
        });

    }

 /*   private void UpdateIsVerified() {
        progressDialog.showDialog();

        final SingleObject singleObject = SingleObject.getInstance();
        final String driver_apikey = controller.pref.getAPI_KEY();
        final String driverId = singleObject.getDriverId();
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
        params.put("driver_id", driverId);
        params.put("d_is_verified", "1");
        System.out.print(""+params);
        WebServiceUtil.excuteRequest(DocUploadActivity1.this, params, Constants.UPDATE_PROFILE, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    progressDialog.dismiss();
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (res.isStatus()) {

                            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                            startActivity(intent);
                            finish();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    progressDialog.dismiss();
                }
            }
        });
    }*/


    private void docUpload(final String bitmapKey, final String bitmapValue) {
        final String driver_apikey = controller.pref.getAPI_KEY();
        final String driverId = controller.pref.getDRIVER_ID();
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
        params.put("driver_id", driverId);
        params.put(bitmapKey, bitmapValue);
        WebServiceUtil.excuteRequest(DocUploadActivity1.this, params, Constants.UPDATE_PROFILE, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                progressDialog.dismiss();
                if (isUpdate) {
                    String response = data.toString();
                    controller.setSignInResponse(response);
                    controller.pref.setSIGN_IN_RESPONSE(response);
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (res.isStatus()) {
                        switch (bitmapKey){
                            case Constants.D_LICENSE_IMAGE_PATH:
                                licenseUpload.setVisibility(View.VISIBLE);
                                break;
                            case Constants.D_RC_IMAGE_PATH:
                                idUpload.setVisibility(View.VISIBLE);
                                break;
                            case Constants.D_INSURANCE_IMAGE_PATH:
                                insuranceUpload.setVisibility(View.VISIBLE);
                                break;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                }else{
                }
            }
        });

    }


    public String getEncoded64ImageStringFromBitmap(Bitmap bmp) {
        Bitmap bitmap = scaleDown(bmp, 200, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
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

    public String getCheckLicencImg() {
        return checkLicencImg;
    }

    public void setCheckLicencImg(String checkLicencImg) {
        this.checkLicencImg = checkLicencImg;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed() {
       /* clearApplicationData();
        clearSavedPreferences();*/
        finish();
    }

}


