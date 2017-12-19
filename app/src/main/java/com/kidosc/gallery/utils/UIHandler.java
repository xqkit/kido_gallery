package com.kidosc.gallery.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Desc:    专门用于更新UI的handler，不会hold对象
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/12/12 17:07
 */

public class UIHandler<T> extends Handler {

    private final WeakReference<T> mWeakReference;
    private final HandleMsgListener mListener;

    public UIHandler(T t, HandleMsgListener<T> listener) {
        mWeakReference = new WeakReference<T>(t);
        mListener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        mListener.handleMessage(mWeakReference.get(), msg);
    }

    public interface HandleMsgListener<T> {
        /**
         * 包装过的handleMessage，不会有内存泄露的危险
         *
         * @param holder Handler绑定的对象
         * @param msg    handleMessage传递的参数
         */
        void handleMessage(T holder, Message msg);
    }
}
