package com.driver.hire_me;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by grepix on 12/26/2016.
 */
public class AddressLocationView {

    public View view;
    public Context context;
    public AddressViewCallback callback;

    private TextView tvLocTitle, tvLocAddress;
    private ImageView ivSearchBtn;
    private Typeface typeface;

    public AddressLocationView(Context context,View view,Typeface typeface){
        this.view=view;
        this.context=context;
        this.typeface=typeface;

        init();

    }

    public void setLocationTitle(CharSequence text) {
        if (text !=null){
            tvLocTitle.setText(text);
        }

    }

    public interface AddressViewCallback{
        void setLocationTitle();
        void setLocationAdres();
        void onSearchButtonClicked();
    }

    public void onSearchButtonClicked(){

    }

    public void setAddressViewCallback(AddressViewCallback callback){

        this.callback=callback;

    }
    public void hide(){

        this.view.setVisibility(View.GONE);

    }
    public void show(){

        this.view.setVisibility(View.VISIBLE);

    }

    private void init() {
        tvLocAddress= (TextView) view.findViewById(R.id.tv_location_address);
        tvLocAddress.setTypeface(typeface);
        tvLocTitle= (TextView) view.findViewById(R.id.tv_location_title);
        tvLocTitle.setTypeface(typeface);
        ivSearchBtn= (ImageView) view.findViewById(R.id.iv_search_btn);

        ivSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hide();
//                callback.onSearchButtonClicked();
            }
        });

    }

    public String setLocationAddress(String location){
        if (location !=null){

            tvLocAddress.setText(location);
        }
        return location;
    }
    public String setLocationTitle(String title){
        if (title !=null){
            tvLocTitle.setText(title);
        }

        return title;
    }

}
