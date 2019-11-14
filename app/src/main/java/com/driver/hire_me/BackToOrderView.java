package com.driver.hire_me;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fonts.Fonts;

/**
 * Created by grepix on 7/25/2016.
 */
public class BackToOrderView {
    BackToOrderViewCallback callback;
    Context context;
    View view;
    public static EditText editText;
    public Button okButton;
    public TextView tvWhynot, tvBacktoOrder;
    public ImageButton ibBack, ibNext;
    public static LinearLayout linearbackNext;
    public static RelativeLayout relativeLayout;
    public interface BackToOrderViewCallback{
        void onOkButtonClicked();
        void onBackButtonClicked();
        void onNextButtonClicked();

    }
    public void setBackToOrderCallback(BackToOrderViewCallback callback){
        this.callback=callback;

    }
    public void hide(){
        this.view.setVisibility(View.GONE);
    }
    public void show(){
        this.view.setVisibility(View.VISIBLE);
    }

    public BackToOrderView(Context context, View view){
        this.context=context;
        this.view=view;

        init();
    }

    private void init(){
        Typeface typeface=Typeface.createFromAsset(context.getAssets(), Fonts.MYRIADPRO_REGULAR);
        linearbackNext = (LinearLayout) view.findViewById(R.id.linear_horizontal);
        relativeLayout= (RelativeLayout) view.findViewById(R.id.back_to_relative);

        tvWhynot= (TextView) view.findViewById(R.id.tv_why_not1);
        tvWhynot.setTypeface(typeface);
        okButton= (Button) view.findViewById(R.id.ok_button_id1);
        okButton.setTypeface(typeface);
        editText= (EditText) view.findViewById(R.id.et_why_not_value1);
        editText.setTypeface(typeface);
        tvBacktoOrder= (TextView) view.findViewById(R.id.tv_back_to_orders);
        tvBacktoOrder.setTypeface(typeface);
        ibBack= (ImageButton) view.findViewById(R.id.ib_back_arrow);
        ibNext= (ImageButton) view.findViewById(R.id.ib_next_arrow);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  hide();
                callback.onOkButtonClicked();

            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                callback.onBackButtonClicked();
            }
        });
        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                callback.onNextButtonClicked();
            }
        });
    }
}
