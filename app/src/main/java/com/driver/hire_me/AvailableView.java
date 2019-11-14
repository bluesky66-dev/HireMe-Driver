package com.driver.hire_me;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fonts.Fonts;

/**
 * Created by grepix on 7/25/2016.
 */
public class AvailableView  {

    AvailableViewCallback callback;
    public Context context;
    public View view;
    public EditText editText;
    public Button okButton;
    public TextView tvWhynot;
    public interface AvailableViewCallback{

        void onOkButtonClicked();
    }

    public void setAvailableViewCallback(AvailableViewCallback callback){
        this.callback=callback;
    }
    public void  hide(){
        this.view.setVisibility(View.GONE);
    }
    public void show(){
        this.view.setVisibility(View.VISIBLE);
    }

    public AvailableView(Context context, View view){
        this.context=context;
        this.view=view;
        init();
    }

    private void init(){
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),Fonts.HELVETICA_NEUE);
        tvWhynot= (TextView) view.findViewById(R.id.tv_why_not);
        tvWhynot.setTypeface(typeface);
        okButton= (Button) view.findViewById(R.id.ok_button_id);
        okButton.setTypeface(typeface);
        editText= (EditText) view.findViewById(R.id.et_why_not_value);
        editText.setTypeface(typeface);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                callback.onOkButtonClicked();

            }
        });
    }


}
