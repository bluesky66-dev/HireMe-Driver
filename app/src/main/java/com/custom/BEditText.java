package com.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.driver.hire_me.R;


/**
 * Created by grepixinfotech on 02/01/17.
 */
public class BEditText extends EditText {
    public BEditText(Context context) {
        super(context);
    }

    public BEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context,attrs);
    }

    public BEditText(Context context, AttributeSet attrs, int defStyle) {
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
