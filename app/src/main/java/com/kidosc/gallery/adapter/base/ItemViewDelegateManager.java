package com.kidosc.gallery.adapter.base;

import android.util.Log;
import android.util.SparseArray;

/**
 * Desc:    manage item view delegate
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/27 15:46
 */
public class ItemViewDelegateManager<T> {
    private static final String TAG = ItemViewDelegateManager.class.getSimpleName();
    private SparseArray<ItemViewDelegate<T>> mDelegates = new SparseArray<>();

    int getItemViewDelegateCount() {
        return mDelegates.size();
    }

    void addDelegate(ItemViewDelegate<T> itemViewDelegate) {
        int delegateType = mDelegates.size();
        if (itemViewDelegate != null) {
            mDelegates.put(delegateType, itemViewDelegate);
        }
    }

    public void addDelegate(int viewType, ItemViewDelegate<T> delegate) {
        if (mDelegates.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "An ItemViewDelegate is already registered for the viewType = "
                            + viewType
                            + ". Already registered ItemViewDelegate is "
                            + mDelegates.get(viewType));
        }
        mDelegates.put(viewType, delegate);
    }

    protected void removeDelegate(ItemViewDelegate<T> delegate) {
        if (delegate == null) {
            throw new NullPointerException("ItemViewDelegate is null");
        }
        int indexToRemove = mDelegates.indexOfValue(delegate);

        if (indexToRemove >= 0) {
            mDelegates.removeAt(indexToRemove);
        }
    }

    protected void removeDelegate(int itemType) {
        int indexToRemove = mDelegates.indexOfKey(itemType);
        if (indexToRemove >= 0) {
            mDelegates.removeAt(indexToRemove);
        }
    }

    int getItemViewType(T item, int position) {
        int delegatesCount = mDelegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            try {
                ItemViewDelegate<T> delegate = mDelegates.valueAt(i);
                if (delegate.isForViewType(item, position)) {
                    return mDelegates.keyAt(i);
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "No ItemViewDelegate added that matches position=" + position + " in data source");
            }
        }
        return -1;
    }

    void convert(ViewHolder holder, T item, int position) {
        int delegatesCount = mDelegates.size();
        for (int i = 0; i < delegatesCount; i++) {
            try {

                ItemViewDelegate<T> delegate = mDelegates.valueAt(i);
                if (delegate.isForViewType(item, position)) {
                    delegate.convert(holder, item, position);
                    return;
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "No ItemViewDelegate added that matches position=" + position + " in data source");
            }
        }
    }


    public int getItemViewLayoutId(int viewType) {
        return mDelegates.get(viewType).getItemViewLayoutId();
    }

    public int getItemViewType(ItemViewDelegate itemViewDelegate) {
        return mDelegates.indexOfValue(itemViewDelegate);
    }

    ItemViewDelegate getItemViewDelegate(T item, int position) {
        int delegatesCount = mDelegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            try {
                ItemViewDelegate<T> delegate = mDelegates.valueAt(i);
                if (delegate.isForViewType(item, position)) {
                    return delegate;
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "No ItemViewDelegate added that matches position=" + position + " in data source");
            }
        }
        return null;
    }

    public int getItemViewLayoutId(T item, int position) {
        return getItemViewDelegate(item, position).getItemViewLayoutId();
    }
}
