package com.driver.hire_me;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fonts.Fonts;

/**
 * Created by grepix on 8/2/2016.
 */
public class ClientPickedUpView {
    public Context context;
    public View view;
    public TextView tvClientPickedup;
    public Button btnYes,btnNo;
    private ClientPickedUpCallback callback;

    public interface ClientPickedUpCallback{
        void onYesButtonClicked();
        void onNoButtonClicked();
    }
    public void   setClientPickedUpCallback(ClientPickedUpCallback callback){
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
    public ClientPickedUpView(Context context, View view){
        this.context=context;
        this.view=view;

        init();
    }
    private void init()
    {
       Typeface typeface=Typeface.createFromAsset(context.getAssets(), Fonts.HELVETICA_NEUE);

        tvClientPickedup= (TextView) view.findViewById(R.id.tv_client_picked_up);
        tvClientPickedup.setTypeface(typeface);
        btnYes= (Button)view.findViewById(R.id.btn_yes);
        btnYes.setTypeface(typeface);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                callback.onYesButtonClicked();
            }
        });
        btnNo=(Button)view.findViewById(R.id.btn_no);
        btnNo.setTypeface(typeface);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                callback.onNoButtonClicked();
            }
        });
    }


}
