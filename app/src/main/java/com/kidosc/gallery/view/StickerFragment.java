package com.kidosc.gallery.view;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kidosc.gallery.R;
import com.kidosc.gallery.adapter.WallAdapter;
import com.kidosc.gallery.base.BaseFragment;
import com.kidosc.gallery.global.Constants;
import com.kidosc.gallery.presenter.WallPresenter;

import java.util.ArrayList;

/**
 * Desc:    StickerFragment
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/22 10:33
 */

public class StickerFragment extends BaseFragment {

    private GridView mGridView;
    private ArrayList<String> mList = new ArrayList<>();
    private String mPhotoPath;

    @Override
    protected int getFragmentLayout() {
        return R.layout.sticker_list;
    }

    @Override
    protected void initView(View view) {
        mGridView = view.findViewById(R.id.sticker_wall);
    }

    @Override
    protected void initData() {
        mList = new WallPresenter().getStickerPaths();
        WallAdapter wallAdapter = new WallAdapter(getActivity(), mList);
        mGridView.setAdapter(wallAdapter);

        final FragmentManager fragmentManager = getActivity().getFragmentManager();
        final AddStickerFragment addStickerFragment = new AddStickerFragment();
        final Bundle bundle = new Bundle();
        mPhotoPath = ((PhotoActivity) getActivity()).getPhotoPath();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bundle.putString(Constants.BUNDLE_STICKER, mList.get(position));
                bundle.putString(Constants.FILE_PATH, mPhotoPath);
                addStickerFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.ll, addStickerFragment).commit();
            }
        });
    }
}
