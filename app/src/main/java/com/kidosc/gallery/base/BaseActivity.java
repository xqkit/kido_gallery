package com.kidosc.gallery.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Desc:    base activity
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/22 13:52
 */

public abstract class BaseActivity extends Activity {

    protected abstract void initView();
    protected abstract void initData();
    protected abstract int getContentView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

}
