package com.kidosc.gallery.presenter;

import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc:    photo wall presenter
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/21 14:32
 */
public class WallPresenter {

    private List<File> mPhotoFiles = new ArrayList<>();
    private ArrayList<String> mImagePaths = new ArrayList<>();
    private ArrayList<String> mStickerPaths = new ArrayList<>();

    private void searchPhotos(File filePath) {
        File[] files = filePath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getPath().endsWith(".png")
                        || pathname.getPath().endsWith(".jpg");
            }
        });
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchPhotos(file);
                } else {
                    mPhotoFiles.add(file);
                }
            }
        }
    }

    public ArrayList<String> getImagePaths() {
        File file = Environment.getExternalStorageDirectory();
        mPhotoFiles.clear();
        mImagePaths.clear();
        searchPhotos(file);
        for (File photoFile : mPhotoFiles) {
            mImagePaths.add(photoFile.getAbsolutePath());
        }
        return mImagePaths;
    }

    public ArrayList<String> getStickerPaths() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sticker";
        File file = new File(path);
        mPhotoFiles.clear();
        mStickerPaths.clear();
        searchPhotos(file);
        for (File photoFile : mPhotoFiles) {
            mStickerPaths.add(photoFile.getAbsolutePath());
        }
        return mStickerPaths;
    }
}
