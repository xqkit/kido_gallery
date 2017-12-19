package com.kidosc.gallery.presenter;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Desc:    photo wall presenter
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/21 14:32
 */
public class WallPresenter {

    private static final String TAG = WallPresenter.class.getSimpleName();
    private ArrayList<String> mFilePaths = new ArrayList<>();
    private ArrayList<File> mPhotoFiles = new ArrayList<>();

    private void searchPhotos(File filePath) {
        File[] files = filePath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                //TODO 判断图片方式太过狭隘
                return pathname.isDirectory() || pathname.getPath().endsWith(".png")
                        || pathname.getPath().endsWith(".jpg") || pathname.getPath().endsWith(".mp4");
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
        mFilePaths.clear();
        searchPhotos(file);
        for (File photoFile : mPhotoFiles) {
            mFilePaths.add(photoFile.getAbsolutePath());
        }
        return mFilePaths;
    }

    public ArrayList<String> getMediaPaths(Context context) {
//        getVideoPaths(context);
        mFilePaths.clear();
//        Log.d(TAG, "getMedia");
//        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                , null, MediaStore.Images.Media.MIME_TYPE + "=? or "
//                        + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}
//                , MediaStore.Images.Media.DATE_MODIFIED);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                mFilePaths.add(path);
//            }
//            Log.d(TAG, "mFilePaths .size " + mFilePaths.size());
//            cursor.close();
//        }
        mFilePaths = getImagePaths();
        return mFilePaths;
    }

//    /**
//     * 获取视频地址
//     *
//     * @return 视频地址
//     */
//    public ArrayList<String> getVideoPaths(Context context) {
//        mFilePaths.clear();
//        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//                , null, null, null, null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//                mFilePaths.add(path);
//            }
//            cursor.close();
//        }
//        return mFilePaths;
//    }
}
