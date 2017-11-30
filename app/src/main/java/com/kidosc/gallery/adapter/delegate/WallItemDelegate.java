package com.kidosc.gallery.adapter.delegate;

import com.kidosc.gallery.R;
import com.kidosc.gallery.adapter.base.ItemViewDelegate;
import com.kidosc.gallery.adapter.base.ViewHolder;

/**
 * Desc:    photo wall delegate
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/28 10:07
 */

public class WallItemDelegate implements ItemViewDelegate<String> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.photo_wall_item;
    }

    @Override
    public void convert(ViewHolder holder, String s, int position) {
        holder.setImageFilePath(R.id.photo_wall_item_photo, s);
    }

    @Override
    public boolean isForViewType(String item, int position) {
        return true;
    }
}
