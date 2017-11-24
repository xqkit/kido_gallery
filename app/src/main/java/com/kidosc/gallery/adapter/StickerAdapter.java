package com.kidosc.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kidosc.gallery.R;

import java.util.ArrayList;

/**
 * Desc:    Sticker wall adapter
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/23 19:06
 */

public class StickerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> imagePathList = null;

    public StickerAdapter(Context context, ArrayList<Integer> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;
    }

    @Override
    public int getCount() {
        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Integer drawableID = (Integer) getItem(position);
        final StickerAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sticker_wall_item, null);
            holder = new StickerAdapter.ViewHolder();
            holder.imageView = convertView.findViewById(R.id.sticker_wall_item_photo);
            convertView.setTag(holder);
        } else {
            holder = (StickerAdapter.ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(drawableID);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }
}
