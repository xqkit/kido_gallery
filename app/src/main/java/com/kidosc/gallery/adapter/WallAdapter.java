package com.kidosc.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kidosc.gallery.presenter.ImageLoaderPresenter;
import com.kidosc.gallery.utils.Utils;
import com.kidosc.gallery.R;

import java.util.ArrayList;

/**
 * Desc:    Wall adapter
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/21 14:35
 */

public class WallAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagePathList = null;
    private ImageLoaderPresenter mLoader;

    public void refreshList(ArrayList<String> imagePathList){
        this.imagePathList = imagePathList;
        notifyDataSetChanged();
    }


    public WallAdapter(Context context, ArrayList<String> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;
        mLoader = new ImageLoaderPresenter(Utils.getScreenW(), Utils.getScreenH());
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
        String filePath = (String) getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_wall_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.photo_wall_item_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setTag(filePath);
        mLoader.loadImage(4, filePath, holder.imageView);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }
}
