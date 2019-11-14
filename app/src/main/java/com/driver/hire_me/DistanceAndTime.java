package com.driver.hire_me;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fonts.Fonts;

/**
 * Created by grepix on 8/12/2016.
 */
public class DistanceAndTime {
    private DistanceViewCallBack callback;
    public Context context;
    public  View view;
    public Button accept;
    public Button decline;
    String distance, approxTime, cuTime;

    private TextView tvDistanceKey,tvDistanceValue, tvTimeKey, tvTimeValue,tvCuTimeValue, tvCuTimeKey, tvAta;

    public  DistanceAndTime(Context context,View view,String distance, String approxTime, String cuTime)
    {
        this.context=context;
        this.view=view;

        this.distance=distance;
        this.approxTime=approxTime;
        this.cuTime=cuTime;

        init();

    }
    public interface DistanceViewCallBack
    {
//        void setDistanceValue();
//        void setTimeValue();
//        void setCurrentTime();

    }

    public  void setDistanceAndTime(DistanceViewCallBack callback )
    {
        this.callback=callback;
    }
    public  void hide()
    {
        this.view. setVisibility(View.GONE);
    }
    public void show()
    {
        this.view.setVisibility(View.VISIBLE);
    }


    private void init()
    {
        Typeface typeface=Typeface.createFromAsset(context.getAssets(), Fonts.MYRIADPRO_REGULAR);

        tvAta= (TextView) view.findViewById(R.id.tv_eta);
        tvAta.setTypeface(typeface);
        tvDistanceKey= (TextView) view.findViewById(R.id.tv_distance_key);
        tvDistanceKey.setTypeface(typeface);
        tvDistanceValue= (TextView) view.findViewById(R.id.tv_distance_value);
        tvDistanceValue.setTypeface(typeface);
        tvDistanceValue.setText(distance);
        tvTimeKey= (TextView) view.findViewById(R.id.tv_time_key);
        tvTimeKey.setTypeface(typeface);
        tvTimeValue= (TextView) view.findViewById(R.id.tv_time_value);
        tvTimeValue.setTypeface(typeface);
        tvTimeValue.setText(approxTime);
        tvCuTimeKey= (TextView) view.findViewById(R.id.tv_am_pm);
        tvCuTimeKey.setTypeface(typeface);
        tvCuTimeValue= (TextView) view.findViewById(R.id.tv_cu_time_value);
        tvCuTimeValue.setText(cuTime);
        tvCuTimeValue.setTypeface(typeface);


       /* tvDistanceValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                callback.setDistanceValue();
            }
        });

        tvTimeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                callback.setTimeValue();
            }
        });
        tvCuTimeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                callback.setCurrentTime();
            }
        });*/
    }
    public void setDistanceTimeData(String distance, String approxTime, String cuTime){
        tvDistanceValue.setText(distance);
        tvTimeValue.setText(approxTime);
        tvCuTimeValue.setText(cuTime);

    }

}
