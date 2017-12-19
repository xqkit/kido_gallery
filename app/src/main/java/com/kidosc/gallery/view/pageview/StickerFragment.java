package com.kidosc.gallery.view.pageview;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kidosc.gallery.R;
import com.kidosc.gallery.adapter.StickerAdapter;
import com.kidosc.gallery.base.BaseFragment;
import com.kidosc.gallery.global.Constants;

import java.util.ArrayList;

/**
 * Desc:    展示贴纸的fragment
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/22 10:33
 */

public class StickerFragment extends BaseFragment {

    private GridView mGridView;
    private ArrayList<Integer> mList = new ArrayList<>();
    private String mPhotoPath;

    @Override
    protected int getFragmentLayout() {
        return R.layout.sticker_wall;
    }

    @Override
    protected void initView(View view) {
        mGridView = (GridView) view.findViewById(R.id.sticker_wall);
    }

    @Override
    protected void initData() {
        mList.add(R.drawable.sticker_00);
        mList.add(R.drawable.sticker_01);
        mList.add(R.drawable.ic_sticker_02);
        mList.add(R.drawable.ic_sticker_03);
        StickerAdapter stickerAdapter = new StickerAdapter(getActivity(),mList);
        mGridView.setAdapter(stickerAdapter);

        final FragmentManager fragmentManager = getActivity().getFragmentManager();
        final AddStickerFragment addStickerFragment = new AddStickerFragment();
        final Bundle bundle = new Bundle();
        mPhotoPath = ((PhotoActivity) getActivity()).getPhotoPath();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bundle.putInt(Constants.BUNDLE_STICKER, mList.get(position));
                bundle.putString(Constants.FILE_PATH, mPhotoPath);
                addStickerFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fl, addStickerFragment).commit();
            }
        });
    }
}
