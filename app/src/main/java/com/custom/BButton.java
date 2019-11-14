package com.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.driver.hire_me.R;

/**
 * Created by devin on 2017-05-12.
 */

public class BButton extends Button {
    public BButton(Context context) {
        super(context);
    }

    public BButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context,attrs);
    }

    public BButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context,attrs);
    }
    private void applyCustomFont(Context context,AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BTextViewStyle,
                0, 0);
        try {
            String name = a.getString(R.styleable.BTextViewStyle_btextfont);
            Typeface customFont = FontCache.getTypeface(name, context);
            setTypeface(customFont);
        } finally {
            a.recycle();
        }
    }
}
