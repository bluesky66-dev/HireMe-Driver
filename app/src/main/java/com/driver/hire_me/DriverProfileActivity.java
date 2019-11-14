package com.driver.hire_me;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.com.driver.webservice.Constants;
import com.com.driver.webservice.SingleObject;
import com.custom.CustomProgressDialog;
import com.grepix.grepixutils.CloudResponse;
import com.grepix.grepixutils.ErrorJsonParsing;
import com.grepix.grepixutils.Validations;
import com.grepix.grepixutils.WebServiceUtil;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class DriverProfileActivity extends AppCompatActivity {


    private static final int RESULT_Gallery_IMAGE = 323;
    private static final int CAMERA_REQUEST = 751;


    CustomProgressDialog progressDialog;
    private static EditText etfname, etlname, etMob, etPassword;
    private static EditText etOldPswd, etNewPswd, etConfirmPswd;
    private static TextView tvDriverName, tvChangePassword,etemail;
    private static LinearLayout linearChangePswd;
    private static ImageView loghide, hrhide;
    // private static ImageView profileimagezoom;
    private static MessageDigest md;
    private static Switch switchChngPswd;
    private Button btnChange, btnBack;
    private static boolean isChangePswd = true;
    Controller controller;
    private static final int RESULT_LOAD_IMAGE = 3;
    boolean isDocUpdate = false;



    int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    private Typeface typeface;

    InterfaceRefreshProfile refreshInterface;

    private SingleObject obj;
    int count = 0;

    URL proimg;
    ImageView profileimagezoom;
    TextView tvUploadDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile_layout);
        progressDialog=new CustomProgressDialog(DriverProfileActivity.this);
        controller = (Controller) getApplicationContext();
        obj = SingleObject.getInstance();
        refreshInterface = controller.getRefreshInterface();
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        bindActivity();

        etemail.setEnabled(false);
        try {
            String driverImg = controller.pref.getDRIVER_PROFILE_IMG_PATH();
            if(driverImg!=null ||
                    !driverImg.equalsIgnoreCase("") ||
                    !driverImg.equalsIgnoreCase("null")) {
                if (driverImg.length() != 0) {
                    Picasso.with(DriverProfileActivity.this).load(driverImg).error(R.drawable.circleuser).into(profileimagezoom);
                } else {
                    profileimagezoom.setBackgroundResource(R.drawable.circleuser);
                }
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {

        }
        //   profileImage();
        boolean profileimage = controller.pref.getPROFILE_IMAGE();
        if (profileimage == true) {
            try {
                controller.pref.setPROFILE_IMAGE(true);
                String fullpath = controller.pref.getDRIVER_PROFILE_IMG_PATH();
                if(fullpath!=null ||
                        !fullpath.equalsIgnoreCase("") ||
                        !fullpath.equalsIgnoreCase("null")) {
                    Picasso.with(DriverProfileActivity.this).load(fullpath).into(profileimagezoom);
                }
            } catch (Exception e) {

            }

        } else {
            profileimagezoom.setBackgroundResource(R.drawable.circleuser);
        }

        profileimagezoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
              //  uploadImage();

            }
        });


        switchChngPswd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (switchChngPswd.isChecked()) {

                    isChangePswd = false;
                    linearChangePswd.setVisibility(View.VISIBLE);

                } else {

                    linearChangePswd.setVisibility(View.GONE);
                    isChangePswd = true;

                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // refreshInterface.onProfileRefresh();
                finish();
            }
        });


        btnChange.setOnClickListener(changProfile);

        tvUploadDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              controller.setDocUpdate(true);
              controller.setProfileDetails(obj);
                Intent intent = new Intent(getApplicationContext(),DocUploadActivity1.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // refreshInterface.onProfileRefresh();
        finish();
    }


    private View.OnClickListener changProfile = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if(Validations.isValidateUpdateProfile(DriverProfileActivity.this,etfname,etlname,etMob,switchChngPswd,etOldPswd,etNewPswd,etConfirmPswd,controller.pref.getPASSWORD())){
                updateProfileApi();
            }

        }
    };

    private void updateProfileApi() {
        progressDialog.showDialog();
        final Controller controller = (Controller) getApplicationContext();
        SingleObject singleObject = SingleObject.getInstance();


        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", controller.pref.getAPI_KEY());
        params.put("driver_id", singleObject.getDriverId());
        params.put("d_fname", etfname.getText().toString());
        params.put("d_lname", etlname.getText().toString());
        params.put("is_send_email", String.valueOf(1));
        params.put("d_phone", etMob.getText().toString());
        if (switchChngPswd.isChecked()) {
            params.put("d_password", etNewPswd.getText().toString());
        }
        System.out.println("Update Profile Params : " + params);
        WebServiceUtil.excuteRequest(this, params, Constants.UPDATE_PROFILE, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
               progressDialog.dismiss();
                if (isUpdate) {
                    String response = data.toString();
                    if (response != null) {
                        if (switchChngPswd.isChecked()) {
                            controller.pref.setPASSWORD(etNewPswd.getText().toString());
                            etNewPswd.setText("");
                            etOldPswd.setText("");
                            etConfirmPswd.setText("");
                        }
                        // driverProfileUpdateParsing(response);
                        SingleObject obj = SingleObject.getInstance();
                        controller.pref.setSIGN_IN_RESPONSE(response);
                        controller.setSignInResponse(response);
                        // String signinResponse=controller.getSignInResponse();
                        obj.driverLoginParseApi(response);
                        Toast.makeText(getApplication(), R.string.Profile_updated_sucessfully, Toast.LENGTH_LONG).show();
                    } else {
                        //   Util.showdialog(getActivity(), "No Network !", "Internet Connection Failed");
                        Toast.makeText(getApplicationContext(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }



    private void bindActivity() {

        linearChangePswd = (LinearLayout) findViewById(R.id.linear_change_pswd);
        tvChangePassword = (TextView) findViewById(R.id.tv_change_pswd);

        tvDriverName = (TextView) findViewById(R.id.tv_driver_id);
        etfname = (EditText) findViewById(R.id.firstname1);
        etfname.setText(obj.getdFname());
        etlname = (EditText) findViewById(R.id.lastname1);
        etlname.setText(obj.getdLname());

        etemail = (TextView) findViewById(R.id.emailadd);
        etemail.setText(obj.getdEmail());
        etemail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(DriverProfileActivity.this, R.string.email_can_not_be_change, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        etMob = (EditText) findViewById(R.id.et_mobile);
        etMob.setText(obj.getdPhone());

        etOldPswd = (EditText) findViewById(R.id.et_old_pswd1);
        etNewPswd = (EditText) findViewById(R.id.et_new_pswd1);
        etConfirmPswd = (EditText) findViewById(R.id.et_confirm_pswd1);

        switchChngPswd = (Switch) findViewById(R.id.switch_chng_pswd);

        btnChange = (Button) findViewById(R.id.btn_change);
        btnBack = (Button) findViewById(R.id.back_btn);

        tvUploadDoc = (TextView) findViewById(R.id.tv_upload_documents);

        profileimagezoom = (ImageView) findViewById(R.id.profileimagezoom);


    }




//TODO 16/June/2017

   /* private void uploadImage() {


        final CharSequence[] items = {getText(R.string.take_photo), getText(R.string.choose_from_library), getText(R.string.cancel)};
        //   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverProfileActivity.this);
        builder.setTitle(R.string.add_photo);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getText(R.string.take_photo))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals(getText(R.string.choose_from_library))) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(Intent.createChooser(intent, getText(R.string.select_file)), SELECT_FILE);
                    } catch (Exception e) {
                        Toast.makeText(DriverProfileActivity.this, R.string.unable_to_fatch_file, Toast.LENGTH_LONG).show();
                    }

                } else if (items[item].equals(getText(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                if (data != null) {
                    onSelectFromGalleryResult(data);
                } else {
                    onSelectFromGalleryResult(data);
                }
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }

    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
       final String bitmap3 = getEncoded64ImageStringFromBitmap(thumbnail);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (bitmap3 != null) {
                profileImageUpload(Constants.D_PROFILE_IMAGE_PATH, bitmap3);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        try {
            if (data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                Bitmap bitmap4 = BitmapFactory.decodeFile(imgDecodableString);

                final String bitmapString4 = getEncoded64ImageStringFromBitmap(bitmap4);

                profileimagezoom.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

                try {
                    if (bitmap4 != null) {
                        profileImageUpload(Constants.D_PROFILE_IMAGE_PATH, bitmapString4);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
            }


        } catch (Exception e) {

        }

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
   */

    /*........................camera runtime permisstion......................*/
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(android.Manifest.permission.CAMERA, getPackageName());
            if (ContextCompat.checkSelfPermission(DriverProfileActivity.this, android.Manifest.permission.CAMERA) !=
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

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DriverProfileActivity.this);
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
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 160);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            profileimagezoom.setImageBitmap(photo);
            profileImageUpload(photo);
        } else if (requestCode == RESULT_Gallery_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                // profileimagezoom.setImageBitmap(selectedImage);
                profileimagezoom.setImageBitmap(getResizedBitmap(rotateImageIfRequired(selectedImage,DriverProfileActivity.this,imageUri),300));
                profileImageUpload(getResizedBitmap(rotateImageIfRequired(selectedImage,DriverProfileActivity.this,imageUri),300));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(DriverProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


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

    /*........................Image Operation.....end...........*/

    private void profileImageUpload(Bitmap bitmapValue) {
        progressDialog.showDialog();
        final String bitmap64 = convert(bitmapValue);
        final String driver_apikey = controller.pref.getAPI_KEY();
        final String driverId = controller.pref.getDRIVER_ID();
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", driver_apikey);
        params.put("driver_id", driverId);
        params.put("image_type", "jpg");
        params.put(Constants.D_PROFILE_IMAGE_PATH, bitmap64);


        WebServiceUtil.excuteRequest(DriverProfileActivity.this, params, Constants.UPDATE_PROFILE, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    progressDialog.dismiss();
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (res.isStatus()) {
                        SingleObject object = SingleObject.getInstance();
                        object.driverUpdateProfileParseApi(response);
                        controller.pref.setDRIVER_PROFILE_IMG_PATH("");
                        String profile_img = object.getD_profile_image_path();
                        String fullpath = Constants.IMAGE_BASE_URL + profile_img;
                        if (profile_img.length() != 0) {
                            controller.pref.setPROFILE_IMAGE(true);
                            controller.pref.setDRIVER_PROFILE_IMG_PATH(fullpath);
                            Picasso.with(DriverProfileActivity.this).load(fullpath).error(R.drawable.circleuser).into(profileimagezoom);
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    progressDialog.dismiss();
                }
            }
        });

    }


    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }







    @Override
    protected void onResume() {
        super.onResume();

        SingleObject obj = SingleObject.getInstance();
        String signinResponse = controller.pref.getSIGN_IN_RESPONSE();
        if (signinResponse != null) {

            obj.driverLoginParseApi(signinResponse);
        }

        bindActivity();

    }
}
