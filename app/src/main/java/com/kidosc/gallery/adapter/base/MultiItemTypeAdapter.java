package com.kidosc.gallery.adapter.base;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Desc:    common item type adapter
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/27 15:48
 */

public class MultiItemTypeAdapter<T> extends BaseAdapter {

    private Context mContext;
    private List<T> mDatas;
    private ItemViewDelegateManager mItemViewDelegateManager;

    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager<>();
    }

    public void refreshList(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    protected void addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
    }

    private boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    @Override
    public int getViewTypeCount() {
        if (useItemViewDelegateManager()) {
            return mItemViewDelegateManager.getItemViewDelegateCount();
        }
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (useItemViewDelegateManager()) {
            return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("XQK", "getView position = " + position);
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(mDatas.get(position), position);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder viewHolder;
        if (convertView == null) {
            View itemView = LayoutInflater.from(mContext).inflate(layoutId, parent,
                    false);
            viewHolder = new ViewHolder(mContext, itemView, parent, position);
            viewHolder.mLayoutId = layoutId;
            onViewHolderCreated(viewHolder, viewHolder.getConvertView());
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
        }
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    private void convert(ViewHolder viewHolder, T item, int position) {
        mItemViewDelegateManager.convert(viewHolder, item, position);
    }

    private void onViewHolderCreated(ViewHolder holder, View itemView) {
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
