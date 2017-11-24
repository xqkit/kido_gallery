package com.kidosc.gallery.view;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kidosc.gallery.R;
import com.kidosc.gallery.base.BaseActivity;
import com.kidosc.gallery.global.Constants;

import java.io.File;

/**
 * Desc:    PhotoActivity
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/21 14:48
 */

public class PhotoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PhotoActivity.class.getSimpleName();
    private ImageView mImageView;
    private RelativeLayout mContentViews;
    private RelativeLayout mStickerListView;
    private FragmentManager mFragmentManager;
    private StickerFragment mStickerFragment;
    private String mFilePath;
    private boolean mFlag = true;

    @Override
    protected int getContentView() {
        return R.layout.photo_detail;
    }

    @Override
    protected void initView() {
        mContentViews = findViewById(R.id.ll);
        mImageView = findViewById(R.id.iv1);
        Intent intent = getIntent();
        mFilePath = intent.getStringExtra(Constants.FILE_PATH);
        mImageView.setImageURI(Uri.parse(mFilePath));
        mImageView.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mFragmentManager = getFragmentManager();
        mStickerFragment = new StickerFragment();
    }

    public String getPhotoPath() {
        return mFilePath;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sticker_btn:
                mImageView.setVisibility(View.GONE);
                mContentViews.removeView(mStickerListView);
                mFragmentManager.beginTransaction().replace(R.id.ll, mStickerFragment).commit();
                break;
            case R.id.share_btn:
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.zeusis.csp.activity"
                        , "com.zeusis.csp.activity.MyFamilyListActivity");
                intent.setComponent(componentName);
                startActivity(intent);
                break;
            case R.id.delete_btn:
                File file = new File(mFilePath);
                if (file.delete()) {
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            case R.id.iv1:
                handleStickerView();
                break;
            default:
                break;
        }
    }

    private void handleStickerView() {
        synchronized (this) {
            if (mFlag) {
                addStickerView();
                mFlag = false;
            } else {
                removeStickerView();
                mFlag = true;
            }
        }
    }

    private void addStickerView() {
        //make background black
        mImageView.setImageAlpha(100);
        mImageView.setBackgroundColor(Color.BLACK);
        //init sticker view
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        mStickerListView = layoutInflater.inflate(R.layout.list_detail, null).findViewById(R.id.rl_detail_list);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mContentViews.addView(mStickerListView, layoutParams);
        initListDetailView();
    }

    private void removeStickerView() {
        //make background black
        mImageView.setImageAlpha(255);
        mImageView.setBackgroundColor(Color.TRANSPARENT);
        //remove sticker view
        mContentViews.removeView(mStickerListView);
    }

    private void initListDetailView() {
        View sticker = mStickerListView.findViewById(R.id.sticker_btn);
        View share = mStickerListView.findViewById(R.id.share_btn);
        View delete = mStickerListView.findViewById(R.id.delete_btn);
        sticker.setOnClickListener(this);
        share.setOnClickListener(this);
        delete.setOnClickListener(this);
    }
}
