package com.kidosc.gallery.view.pageview;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.kidosc.gallery.R;
import com.kidosc.gallery.base.BaseActivity;
import com.kidosc.gallery.global.Constants;

import java.io.File;

/**
 * Desc:    播放视频activity
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/12/14 18:25
 */

public class VideoActivity extends BaseActivity {
    private static final String TAG = VideoActivity.class.getSimpleName();

    private String mFilePath;

    @Override
    protected void initView() {
        //初始化资源
        mFilePath = getIntent().getStringExtra(Constants.FILE_PATH);
        Log.d(TAG, "filepath = " + mFilePath);
        File file = new File(mFilePath);
        VideoView videoView = (VideoView) findViewById(R.id.vv);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(Uri.fromFile(file));
        videoView.start();
        videoView.requestFocus();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getContentView() {
        return R.layout.video_view;
    }

    @Override
    protected void click(View v) {

    }
}
