package com.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.com.driver.webservice.Constants;

/**
 * Created by grepixinfotech on 09/11/17.
 */

public class PreferencesUtils {
    public static final long RECORDING_TRACK_ID_DEFAULT = -1L;
    public static final boolean ALLOW_ACCESS_DEFAULT = false;
    public static final boolean RECORDING_TRACK_PAUSED_DEFAULT = true;

    /**
     * Gets a boolean preference value.
     *
     * @param context      the context
     * @param keyId        the key id
     * @param defaultValue the default value
     */
    public static boolean getBoolean(Context context, int keyId, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.SETTINGS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(getKey(context, keyId), defaultValue);
    }



    public static long getLong(Context context, int recording_track_id_key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.SETTINGS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(getKey(context, recording_track_id_key), 0);
    }

    public static void setBoolean(Context context, int keyId, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.SETTINGS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getKey(context, keyId), value);
        editor.commit();
    }
    /**
     * Gets a preference key
     *
     * @param context the context
     * @param keyId the key id
     */
    public static String getKey(Context context, int keyId) {
        return context.getString(keyId);
    }


    /**
     * Sets a long preference value.
     *
     * @param context the context
     * @param keyId the key id
     * @param value the value
     */

    public static void setLong(Context context, int keyId, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.SETTINGS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(getKey(context, keyId), value);
        editor.commit();
    }

    public static void setFloat(Context context, int keyId, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.SETTINGS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(getKey(context, keyId), value);
        editor.commit();
    }
    public static float getFloat(Context context, int recording_track_id_key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.SETTINGS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(getKey(context, recording_track_id_key), 0);
    }
}

