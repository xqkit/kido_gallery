package com.kidosc.gallery.global;

import android.app.Application;

/**
 * Desc:    Gallery Application
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/24 18:15
 */

public class GalleryApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        CustomCrash.getInstance().setCustonCrashInfo();
    }
}
