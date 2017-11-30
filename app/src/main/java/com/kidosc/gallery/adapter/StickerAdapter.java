package com.kidosc.gallery.adapter;

import android.content.Context;

import com.kidosc.gallery.adapter.base.MultiItemTypeAdapter;
import com.kidosc.gallery.adapter.delegate.StickerItemDelegate;

import java.util.List;

/**
 * Desc:    sticker adapter
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/27 16:09
 */

public class StickerAdapter extends MultiItemTypeAdapter<Integer> {
    public StickerAdapter(Context context, List<Integer> datas) {
        super(context, datas);
        addItemViewDelegate(new StickerItemDelegate());
    }
}
