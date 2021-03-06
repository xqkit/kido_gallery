package com.kidosc.gallery.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Desc:    Utils
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/21 14:46
 */

public class Utils {
    private static int screenW;
    private static int screenH;
    private static float screenDensity;
    private static final String APP_DIR = "kidosc";

    /**
     * Determine if the SD card exists
     *
     * @return true
     */
    public static boolean isSDcardOK() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Get the path to the SD card
     *
     * @return Returns the string of the path
     */
    public static String getSDcardRoot() {
        if (isSDcardOK()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }


    /**
     * Gets the number of occurrences of a string in the string
     *
     * @param res        string
     * @param findString Matched string
     * @return times
     */
    public static int countMatches(String res, String findString) {

        if (TextUtils.isEmpty(res) || TextUtils.isEmpty(findString)) {
            return 0;
        }

        return (res.length() - res.replace(findString, "").length()) / findString.length();
    }

    /**
     * Determine whether the file is a picture
     *
     * @param fileName filepath
     * @return result
     */
    public static boolean isImage(String fileName) {
        //TODO The method of judgment is too narrow to be modified
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }

    /**
     * Initialize screen parameters
     *
     * @param activity activity
     */
    public static void initScreen(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        screenDensity = metric.density;
    }

    /**
     * Before using this method, call first initScreen()
     *
     * @return size
     */
    public static int getScreenW() {
        return screenW;
    }

    /**
     * Before using this method, call first initScreen()
     *
     * @return size
     */
    public static int getScreenH() {
        return screenH;
    }

    /**
     * Before using this method, call first initScreen()
     *
     * @return size
     */
    public static float getScreenDensity() {
        return screenDensity;
    }

    /**
     * From the unit of dp to px (pixel) according to the resolution of the watch
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getScreenDensity() + 0.5f);
    }

    /**
     * From the unit of px to dp (pixel) according to the resolution of the watch
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }

    /**
     * Calculate the distance between two fingers
     */
    public static float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        // Use the Pythagorean theorem to return the distance between two points
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculate the intermediate points between two fingers
     */
    public static PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    /**
     * Gets all the images under the current path
     *
     * @param folderPath folder path
     * @return Image path set
     */
    public static ArrayList<String> getAllImagePathsByFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] allFileNames = folder.list();
        if (allFileNames == null || allFileNames.length == 0) {
            return null;
        }
        ArrayList<String> imageFilePaths = new ArrayList<>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (isImage(allFileNames[i])) {
                imageFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }
        return imageFilePaths;
    }

    /**
     * save bitmap to local
     *
     * @param bm bitmap
     * @return path
     */
    public static String saveBitmapToLocal(Bitmap bm, Context context) {
        String path = null;
        try {
            File file = createTempFile(context);
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
            path = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }

    private static File createTempFile(Context context)
            throws IOException {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "IMG_"
                + System.currentTimeMillis() + ".jpg");
        return file;
    }

    /**
     * Get the current application content directory,
     * and the external storage space is not available to return null
     *
     * @return path
     */
    private static String getAppDirPath(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/" + APP_DIR + "/";
    }

    /**
     * transfer vector drawable to bitmap
     *
     * @param context    context
     * @param drawableId vector drawable's ID
     * @return bitmap
     */
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * get local system version
     *
     * @return version
     */
    public static String getLocalSystemVersion() {
        String version = android.os.Build.VERSION.RELEASE;
        if (version == null) {
            version = "";
        }
        return version;
    }

    /**
     * 获取输出的media文件
     *
     * @return 文件
     */
    public static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        Log.d("frank", "getOutputMediaFile timeStamp :" + timeStamp);
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    /**
     * 获取输出的media文件
     *
     * @return 文件
     */
    public static File getOutputMovie() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "Camera");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        Log.d("frank", "getOutputMediaFile timeStamp :" + timeStamp);
        return new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".mp4");
    }


    /**
     * 将图片路径转换为bitmap
     *
     * @param path 图片路径
     * @param w    图片宽
     * @param h    图片长
     * @return 图片
     */
    public static Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    /**
     * 将时间转换成 00:00 格式
     *
     * @param time 时间
     * @return 结果
     */
    @SuppressLint("DefaultLocale")
    @NonNull
    public static String timeCalculate(long time) {
        long minutesu, secondsu;
        String daysT = "", restT = "";
        minutesu = (Math.round(time) / 60);
        secondsu = Math.round(time) % 60;
        restT = String.format("%02d:%02d", minutesu, secondsu);
        return daysT + restT;
    }
}
