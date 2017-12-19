package com.kidosc.gallery.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kidosc.gallery.global.ActivityManager;

/**
 * Desc:    base activity
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/22 13:52
 */

public abstract class BaseActivity extends Activity implements View.OnClickListener {

    private ActivityManager mActivityManager;

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 获取资源文件
     *
     * @return 布局文件ID
     */
    protected abstract int getContentView();

    /**
     * view点击事件
     *
     * @param v v
     */
    protected abstract void click(View v);

    /**
     * 拍照
     */
    protected int PHOTO = 0;
    /**
     * 录像
     */
    protected int VIDEO = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initView();
        mActivityManager = ActivityManager.getInstance();
        mActivityManager.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        click(v);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mActivityManager.removeActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityManager.removeActivity(this);
    }
}
