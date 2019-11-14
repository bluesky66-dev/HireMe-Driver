package com.driver.hire_me;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adaptor.CustomRiderAdapter;
import com.adaptor.TripModel;
import com.android.volley.VolleyError;
import com.com.driver.webservice.Constants;
import com.custom.CustomProgressDialog;
import com.fonts.Fonts;
import com.grepix.grepixutils.CloudResponse;
import com.grepix.grepixutils.ErrorJsonParsing;
import com.grepix.grepixutils.WebServiceUtil;
import com.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Myreview extends Activity implements XListView.IXListViewListener {
    private static final String TAG = Myreview.class.getSimpleName();
    private CustomRiderAdapter adapter;
    private TextView tv, tvTripHistory;
    Button recancel;
    private Controller controller;
    private ArrayList<TripModel> tripHistoryList;
    private Typeface typeface;
    private XListView mListView;
    private Handler mHandler;
    private Typeface typeface1;
    private boolean isHasMoreData;
    private int offSet = 0;
    private int limit = 10;
    private CustomProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreview);
        progressDialog=new CustomProgressDialog(Myreview.this);
        getActionBar().hide();
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        controller = (Controller) getApplicationContext();
        typeface = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_CONDENCE);
        typeface1 = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_CONDENCE);

        tripHistoryList = new ArrayList<>();

        mListView = (XListView) findViewById(R.id.xListView);
        mListView.setPullLoadEnable(true);


        tv = (TextView) findViewById(R.id.listempty);
        tv.setTypeface(typeface);
        tvTripHistory = (TextView) findViewById(R.id.textView14);
        tvTripHistory.setTypeface(typeface1);

        recancel = (Button) findViewById(R.id.recancel);

        recancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                finish();

            }
        });

        if (net_connection_check()) {
            adapter = new CustomRiderAdapter(Myreview.this, tripHistoryList, typeface);
            mListView.setXListViewListener(Myreview.this);
            mListView.setAdapter(adapter);
            callwebservice(offSet, false);
        } else {
            Toast.makeText(Myreview.this, R.string.no_internet_available, Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void callwebservice(final int nextoffset, final boolean isLoadMore) {

        if (!isLoadMore) {
          progressDialog.showDialog();
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", controller.pref.getAPI_KEY());
        params.put("driver_id", controller.pref.getDRIVER_ID());
        params.put("is_request", "0");
        params.put("next_offset", String.valueOf(nextoffset));
        params.put("limit", String.valueOf(limit));
        WebServiceUtil.excuteRequest(Myreview.this, params, Constants.DRIVER_TRIP_HISTORY, new WebServiceUtil.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                progressDialog.dismiss();
                if (isUpdate) {
                    String response = data.toString();
                    ErrorJsonParsing parser = new ErrorJsonParsing();
                    CloudResponse res = parser.getCloudResponse("" + response);
                    if (res.isStatus()) {
                        handleHistryResponse(response);
                    } else {
                        Toast.makeText(getApplicationContext(), res.getError(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.internet_error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
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

    private void handleHistryResponse(String s) {
        try {
            JSONObject jsonRootObject = new JSONObject(s);
            JSONArray response = jsonRootObject.getJSONArray("response");
            isHasMoreData = response.length() == limit;
            for (int i = 0; i < response.length(); i++) {
                JSONObject childObject = response.getJSONObject(i);
                TripModel tripModel = TripModel.parseJson(childObject.toString());
                tripHistoryList.add(tripModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetInvalidated();
    }


    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("");
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        if (isHasMoreData) {
            offSet += limit;
            callwebservice(offSet, true);
        }
    }




}
