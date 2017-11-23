package com.kidosc.gallery.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
}
