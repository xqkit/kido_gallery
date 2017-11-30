package com.kidosc.gallery.adapter;

import android.content.Context;

import com.kidosc.gallery.adapter.base.MultiItemTypeAdapter;
import com.kidosc.gallery.adapter.delegate.WallItemDelegate;

import java.util.List;

/**
 * Desc:    wall adapter
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/28 10:07
 */

public class WallAdapter extends MultiItemTypeAdapter<String>{
    public WallAdapter(Context context, List<String> datas) {
        super(context, datas);
        addItemViewDelegate(new WallItemDelegate());
    }
}
