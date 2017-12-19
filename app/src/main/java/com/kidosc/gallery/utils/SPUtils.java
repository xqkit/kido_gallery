package com.kidosc.gallery.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Desc:    SharePreference 的工具类
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/12/12 14:31
 */

public class SPUtils {
    private static final String TAG = SPUtils.class.getSimpleName();
    private static String DEFAULT_SP_NAME = "gallery_sp";

    private static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 初始化标记的SP区域
     *
     * @param name 区域名字
     */
    public static void initASpName(String name) {
        if (name != null) {
            Log.i(TAG, "initASpName: " + name);
            DEFAULT_SP_NAME = name;
        }
    }

    public static String getDefaultSPName() {
        return DEFAULT_SP_NAME;
    }

    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        return edit.commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, defValue);
    }

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        return edit.commit();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, defValue);
    }

    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        return edit.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static boolean remove(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(key);
        return edit.commit();
    }
}
