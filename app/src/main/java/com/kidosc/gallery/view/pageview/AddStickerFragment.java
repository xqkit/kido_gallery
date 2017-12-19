package com.kidosc.gallery.view.pageview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kidosc.gallery.R;
import com.kidosc.gallery.base.BaseFragment;
import com.kidosc.gallery.global.Constants;
import com.kidosc.gallery.utils.Utils;
import com.kidosc.gallery.view.customview.StickerView;

import java.util.ArrayList;

/**
 * Desc:    添加贴纸的fragment
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/22 13:52
 */

public class AddStickerFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = AddStickerFragment.class.getSimpleName();
    private Integer mSticker;
    /**
     * save stickers list
     */
    private ArrayList<View> mViewList;
    /**
     * current edit view
     */
    private StickerView mCurrentView;
    private RelativeLayout mContentRootView;

    @Override
    protected int getFragmentLayout() {
        return R.layout.edit_sticker;
    }

    @Override
    protected void initView(View view) {
        Bundle bundle = getArguments();
        String photoPath = bundle.getString(Constants.FILE_PATH);
        mSticker = bundle.getInt(Constants.BUNDLE_STICKER);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv);
        imageView.setImageURI(Uri.parse(photoPath));

        mContentRootView = (RelativeLayout) view.findViewById(R.id.rl);
        ImageView deleteBtn = (ImageView) view.findViewById(R.id.delete_btn);
        ImageView saveBtn = (ImageView) view.findViewById(R.id.save_btn);

        deleteBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mViewList = new ArrayList<>();
        addStickerView();
    }

    private void addStickerView() {
        final StickerView stickerView = new StickerView(getActivity());
        stickerView.setImageInt(mSticker);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViewList.remove(stickerView);
                mContentRootView.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViewList.indexOf(stickerView);
                if (position == mViewList.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViewList.remove(position);
                mViewList.add(mViewList.size(), stickerTemp);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                , RelativeLayout.LayoutParams.MATCH_PARENT);
        mContentRootView.addView(stickerView, lp);
        mViewList.add(stickerView);
        setCurrentEdit(stickerView);
    }

    /**
     * Set the sticker that is currently in edit mode
     */
    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_btn:
                getActivity().finish();
                break;
            case R.id.save_btn:
                mCurrentView.setInEdit(false);
                generateBitmap();
                break;
            default:
                break;
        }
    }

    private void generateBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mContentRootView.getWidth(), mContentRootView.getHeight()
                , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mContentRootView.draw(canvas);

        String filePath = Utils.saveBitmapToLocal(bitmap, getActivity());
        Log.d(TAG, "filePath = " + filePath);
        Intent intent = new Intent(getActivity(), PhotoActivity.class);
        intent.putExtra(Constants.FILE_PATH, filePath);
        intent.putExtra("sticker_mode", true);
        startActivity(intent);
        getActivity().finish();
    }
}
