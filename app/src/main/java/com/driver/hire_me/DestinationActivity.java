package com.driver.hire_me;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fonts.Fonts;

/**
 * Created by grepixinfotech on 21/07/16.
 */
public class DestinationActivity {
    private DestinationActivityCallBack callback;
    public Context context;
    public  View view;
    public Button YES;
    public Button NO;
    private TextView tclientreached;

    public interface DestinationActivityCallBack {
        void onYESButtonCliked();
        void onNOButtonClicked();
    }
    public  void setDestinationActivityCallBack(DestinationActivityCallBack callback )
    {
        this.callback=callback;
    }



    public  DestinationActivity(Context context,View view)
    {
        this.context=context;
        this.view=view;
        init();
    }
    private void init()
    {
        Typeface typeface=Typeface.createFromAsset(context.getAssets(), Fonts.HELVETICA_NEUE);

        YES= (Button)view.findViewById(R.id.ad_bt_yes);
        YES.setTypeface(typeface);
        YES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  hide();
                callback.onYESButtonCliked();
            }
        });
        NO=(Button)view.findViewById(R.id.ad_bt_no);
        NO.setTypeface(typeface);
        NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // hide();
                callback.onNOButtonClicked();
            }
        });
    }
    public  void hide()
    {
        this.view. setVisibility(View.GONE);
    }
    public void show()
    {
        this.view.setVisibility(View.VISIBLE);
    }


}
