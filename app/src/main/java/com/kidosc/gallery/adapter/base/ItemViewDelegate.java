package com.kidosc.gallery.adapter.base;

/**
 * Desc:    item view delegate
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/27 15:43
 */

public interface ItemViewDelegate<T> {
    /**
     * get item view layoutID
     *
     * @return layout ID
     */
    int getItemViewLayoutId();

    /**
     * convert holder
     *
     * @param holder   holder
     * @param t        t
     * @param position position
     */
    void convert(ViewHolder holder, T t, int position);

    /**
     * is for this view type ?
     *
     * @param item     item
     * @param position position
     * @return true means show on this item
     */
    boolean isForViewType(T item, int position);
}
