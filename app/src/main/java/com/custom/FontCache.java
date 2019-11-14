package com.custom;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by grepixinfotech on 02/01/17.
 */
public class FontCache {
    private  static HashMap<String,Typeface> fontcache= new HashMap<>();
    public  static  Typeface getTypeface(String fontname ,Context  context)
    {
        Typeface typeface=fontcache.get(fontname);
        if(typeface==null)
        {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);

            }catch (Exception e)
            {
                return null;
            }
            fontcache.put(fontname,typeface);
        }
        return  typeface;

    }

}