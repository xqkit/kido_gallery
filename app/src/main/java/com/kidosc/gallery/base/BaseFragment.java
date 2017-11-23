package com.kidosc.gallery.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Desc:    base fragment
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/22 16:02
 */

public abstract class BaseFragment extends Fragment {


    protected abstract int getFragmentLayout();

    protected abstract void initView(View view);

    protected abstract void initData();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void onBackPressed(){
        getActivity().finish();
    }
}
