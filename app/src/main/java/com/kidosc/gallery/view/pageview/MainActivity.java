package com.kidosc.gallery.view.pageview;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.kidosc.gallery.R;
import com.kidosc.gallery.adapter.WallAdapter;
import com.kidosc.gallery.base.BaseActivity;
import com.kidosc.gallery.global.Constants;
import com.kidosc.gallery.presenter.WallPresenter;
import com.kidosc.gallery.utils.Utils;
import com.kidosc.gallery.view.pullzoom.PullZoomView;

import java.util.ArrayList;

/**
 * Desc:    main app's entrance
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/21 14:32
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<String> mList = new ArrayList<>();
    private WallPresenter mWallPresenter;
    private WallAdapter mWallAdapter;
    private GridView mPhotoWall;
    private RelativeLayout mEmptyView;
    private ArrayList<String> mVideoPaths = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.photo_wall;
    }

    @Override
    protected void initView() {
        Log.d(TAG, "onCreate()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }
        mPhotoWall = (GridView) findViewById(R.id.photo_wall_grid);
        mEmptyView = (RelativeLayout) findViewById(R.id.empty_view);
        findViewById(R.id.take_video_btn).setOnClickListener(this);
        findViewById(R.id.take_photo_btn).setOnClickListener(this);
        float sensitive = 1.5f;
        int zoomTime = 500;
        boolean isParallax = true;
        boolean isZoomEnable = false;
        PullZoomView pzv = (PullZoomView) findViewById(R.id.pzv);
        pzv.setIsParallax(isParallax);
        pzv.setIsZoomEnable(isZoomEnable);
        pzv.setSensitive(sensitive);
        pzv.setZoomTime(zoomTime);
        mWallPresenter = new WallPresenter();
        mList.addAll(mWallPresenter.getMediaPaths(this));
        if (mList.size() == 0) {
            showEmptyView();
        }
        mWallAdapter = new WallAdapter(this, mList);
        mPhotoWall.setAdapter(mWallAdapter);
        //init screen parameter
        Utils.initScreen(this);
        mPhotoWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class clazz = null;
                if (mList.get(position).endsWith("mp4")) {
                    clazz = VideoActivity.class;
                } else {
                    clazz = PhotoActivity.class;
                }
                Intent it = new Intent(MainActivity.this, clazz);
                it.putExtra(Constants.FILE_PATH, mList.get(position));
                startActivity(it);
            }
        });
    }

    private void showEmptyView() {
        mPhotoWall.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void removeEmptyView() {
        if (mPhotoWall.getVisibility() != View.VISIBLE && mEmptyView.getVisibility() != View.GONE) {
            mPhotoWall.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        Log.d(TAG, "onResume()");
        refreshDatas();
    }

    @Override
    protected void click(View v) {
        switch (v.getId()) {
            case R.id.take_photo_btn:
                Intent intent = new Intent(this, CameraActivity.class);
                if (mList.size() > 0) {
                    intent.putExtra(Constants.FILE_PATH, mList.get(0));
                }
                intent.putExtra(Constants.CAMERA_USE, PHOTO);
                startActivity(intent);
                break;
            case R.id.take_video_btn:
                Intent intent2 = new Intent(this, CameraActivity.class);
                if (mList.size() > 0) {
                    intent2.putExtra(Constants.FILE_PATH, mList.get(0));
                }
                intent2.putExtra(Constants.CAMERA_USE, VIDEO);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    private void refreshDatas() {
        mList.clear();
        mList.addAll(mWallPresenter.getMediaPaths(this));
        if (mList.size() > 0) {
            removeEmptyView();
            mWallAdapter.refreshList(mList);
            mWallAdapter.notifyDataSetChanged();
        } else {
            showEmptyView();
        }
    }
}
