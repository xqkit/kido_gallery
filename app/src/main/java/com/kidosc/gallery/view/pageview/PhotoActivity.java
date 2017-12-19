package com.kidosc.gallery.view.pageview;

import android.app.AlertDialog;
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
 * Desc:    单张照片查看的activity
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/21 14:48
 */

public class PhotoActivity extends BaseActivity {

    private static final String TAG = PhotoActivity.class.getSimpleName();
    private ImageView mImageView;
    private RelativeLayout mContentViews;
    private RelativeLayout mStickerListView;
    private FragmentManager mFragmentManager;
    private StickerFragment mStickerFragment;
    private String mFilePath;
    private boolean mFlag = true;
    private RelativeLayout mDeleteConfirmRl;

    @Override
    protected int getContentView() {
        return R.layout.photo_detail;
    }

    @Override
    protected void initView() {
        mContentViews = (RelativeLayout) findViewById(R.id.ll);
        mImageView = (ImageView) findViewById(R.id.iv1);
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

    private void handleStickerView() {
        synchronized (this) {
            if (mFlag) {
                addStickerView();
                mFlag = false;
            } else {
                removeView(mStickerListView);
                mFlag = true;
            }
        }
    }

    private void addStickerView() {
        //make background black
        mImageView.setImageAlpha(100);
        mImageView.setBackgroundColor(0xD9000000);
        //init sticker view
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        mStickerListView = (RelativeLayout) layoutInflater.inflate(R.layout.list_detail, null).findViewById(R.id.rl_detail_list);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mContentViews.addView(mStickerListView, layoutParams);
        initListDetailView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

    }

    private void addDeleteView() {
        //make background black
        mImageView.setImageAlpha(0);
        mImageView.setBackgroundColor(Color.BLACK);
        mImageView.setEnabled(false);
        //remove sticker view
        mContentViews.removeView(mStickerListView);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        mDeleteConfirmRl = (RelativeLayout) layoutInflater.inflate(R.layout.delete_photo_confirm
                , null).findViewById(R.id.rl_delete_confirm);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mContentViews.addView(mDeleteConfirmRl, layoutParams);
        initDeleteView();
    }

    private void initDeleteView() {
        ImageView noDeleteIv = (ImageView) mDeleteConfirmRl.findViewById(R.id.no_delete);
        ImageView yesDeleteIv = (ImageView) mDeleteConfirmRl.findViewById(R.id.yes_delete);
        noDeleteIv.setOnClickListener(this);
        yesDeleteIv.setOnClickListener(this);
    }

    private void initListDetailView() {
        View sticker = mStickerListView.findViewById(R.id.sticker_tv);
        View share = mStickerListView.findViewById(R.id.share_tv);
        View delete = mStickerListView.findViewById(R.id.delete_tv);
        sticker.setOnClickListener(this);
        share.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    private void removeView(View view) {
        //make background black
        mImageView.setImageAlpha(255);
        mImageView.setBackgroundColor(Color.TRANSPARENT);
        //remove sticker view
        mContentViews.removeView(view);
    }

    @Override
    protected void click(View v) {
        switch (v.getId()) {
            case R.id.sticker_tv:
                mImageView.setVisibility(View.GONE);
                mContentViews.removeView(mStickerListView);
                mFragmentManager.beginTransaction().replace(R.id.fl, mStickerFragment).commit();
                break;
            case R.id.share_tv:
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.zeusis.csp.activity"
                        , "com.zeusis.csp.activity.MyFamilyListActivity");
                intent.setComponent(componentName);
                startActivity(intent);
                break;
            case R.id.delete_tv:
                addDeleteView();
                break;
            case R.id.iv1:
                handleStickerView();
                break;
            case R.id.no_delete:
                mFlag = true;
                mImageView.setEnabled(true);
                removeView(mDeleteConfirmRl);
                break;
            case R.id.yes_delete:
                File file = new File(mFilePath);
                if (file.delete()) {
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            default:
                break;
        }
    }
}
