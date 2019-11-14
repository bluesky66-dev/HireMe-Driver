package com.driver.hire_me;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TouchActivity extends Activity {

    LinearLayout touch;
    LinearLayout search;
    LinearLayout uber;
    LinearLayout driver;
    TextView touchtext;
    ProgressBar progressbar;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        getActionBar().hide();
        touchtext = (TextView) findViewById(R.id.touchtext);

        touch = (LinearLayout) findViewById(R.id.touch);
//		uber=(LinearLayout)findViewById(R.id.uber);
//		driver=(LinearLayout)findViewById(R.id.driver);
//		search=(LinearLayout)findViewById(R.id.search);


        touch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                touch.setVisibility(View.VISIBLE);

                new CountDownTimer(16000, 1000) {
                    // 500 means, onTick function will be called at every 500 milliseconds

                    @Override
                    public void onTick(long leftTimeInMilliseconds) {
                        long seconds = leftTimeInMilliseconds / 1000;

                        progressbar = (ProgressBar) findViewById(R.id.progresstimer);
                        int progress = (int) (leftTimeInMilliseconds / 160);
                        progressbar.setProgress(progress);
                        touchtext.setText("Touch In: " + seconds + " to accept");

                    }

                    @Override
                    public void onFinish() {
                        progressbar.setProgress(0);
                        touchtext.setText("done!");
                    }
                }.start();
            }


        });

    }


}
