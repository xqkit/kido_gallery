package com.kidosc.gallery.view;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kidosc.gallery.R;
import com.kidosc.gallery.adapter.WallAdapter;
import com.kidosc.gallery.base.BaseActivity;
import com.kidosc.gallery.global.Constants;
import com.kidosc.gallery.presenter.WallPresenter;
import com.kidosc.gallery.utils.Utils;

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

    @Override
    protected void initView() {
        Log.d(TAG, "onCreate()");
        GridView mPhotoWall = findViewById(R.id.photo_wall_grid);
        mWallPresenter = new WallPresenter();
        mList.addAll(mWallPresenter.getImagePaths());
        mWallAdapter = new WallAdapter(this, mList);
        mPhotoWall.setAdapter(mWallAdapter);
        //init screen parameter
        Utils.initScreen(this);
        mPhotoWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(MainActivity.this, PhotoActivity.class);
                it.putExtra(Constants.FILE_PATH, mList.get(position));
                startActivity(it);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.photo_wall;
    }

    @Override
    protected void initData() {
        Log.d(TAG, "onResume()");
        if (mList != null) {
            mList.clear();
            mList.addAll(mWallPresenter.getImagePaths());
            mWallAdapter.refreshList(mList);
            mWallAdapter.notifyDataSetChanged();
        }
    }
}
