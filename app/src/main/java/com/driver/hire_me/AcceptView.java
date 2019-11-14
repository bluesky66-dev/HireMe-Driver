package com.driver.hire_me;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by grepixinfotech on 21/07/16.
 */
public class AcceptView {

     private AcceptViewCallBack callback;
     public Context context;
     public  View view;
     public Button accept;
     public Button decline;
     Typeface typeface;
     private TextView tPickuplocation;

     public interface AcceptViewCallBack
     {
         void onAcceptButtonCliked();
         void onDeclineButtonClicked();
     }

     public  void setAcceptViewCallBack(AcceptViewCallBack callback )
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


    public  AcceptView(Context context,View view,Typeface typeface)
    {
        this.context=context;
        this.view=view;
        this.typeface=typeface;
        init();
    }
    private void init()
    {
 //   Typeface typeface=Typeface.createFromAsset(context.getAssets(), Fonts.HELVETICA_NEUE);

    tPickuplocation=(TextView)view.findViewById(R.id.v_pickuploaction);
    tPickuplocation.setTypeface(typeface);
    accept= (Button)view.findViewById(R.id.av_bt_accept);
    accept.setTypeface(typeface);

    accept.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // hide();
            callback.onAcceptButtonCliked();
        }
    });
    decline=(Button)view.findViewById(R.id.av_bt_decline);
    decline.setTypeface(typeface);
    decline.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //  hide();
            callback.onDeclineButtonClicked();

        }
    });
}
 public String setPickupLocation(String location){
      if(location!=null) {
          tPickuplocation.setText(location);
      }
     return location;
 }

}
