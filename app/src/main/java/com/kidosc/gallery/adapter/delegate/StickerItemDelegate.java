package com.kidosc.gallery.adapter.delegate;

import com.kidosc.gallery.R;
import com.kidosc.gallery.adapter.base.ItemViewDelegate;
import com.kidosc.gallery.adapter.base.ViewHolder;

/**
 * Desc:    Sticker item delegate
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/27 16:10
 */

public class StickerItemDelegate implements ItemViewDelegate<Integer> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.sticker_wall_item;
    }

    @Override
    public void convert(ViewHolder holder, Integer s, int position) {
        holder.setImageResource(R.id.sticker_wall_item_photo, s);
    }

    @Override
    public boolean isForViewType(Integer item, int position) {
        return true;
    }
}
