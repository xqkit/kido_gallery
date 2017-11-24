package com.kidosc.gallery.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

import com.kidosc.gallery.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Desc:    ImageLoader Presenter
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/21 14:36
 */

public class ImageLoaderPresenter {
    private int screenW, screenH;
    /**
     * image cache
     */
    private LruCache<String, Bitmap> imageCache;
    /**
     * new Thread from thread pool
     */
    private ExecutorService executorService = new ThreadPoolExecutor(2, 2
            , 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable);
        }
    });
    private Handler handler = new Handler();

    public ImageLoaderPresenter(int screenH, int screenW) {

        this.screenH = screenH;
        this.screenW = screenW;
        //app's max memory
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //cache size should be 1/16's max memory
        int cacheSize = maxMemory / 16;
        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    private Bitmap loadDrawable(final int smallRate, final String filePath,
                                final ImageCallback callback) {
        //get data from caches
        if (!TextUtils.isEmpty(filePath) && null != imageCache.get(filePath)) {
            return imageCache.get(filePath);
        }
        //get data from sdcard
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, opt);
                //get picture's original height & width
                int picHeight = opt.outHeight;
                int picWidth = opt.outWidth;
                //fail
                if (picHeight == 0 || picWidth == 0) {
                    return;
                }
                //original compression ratio
                opt.inSampleSize = smallRate;
                //Calculate the zoom scale based on the size of the screen and the image size
                if (picWidth > picHeight) {
                    if (picWidth > screenW && screenW != 0) {
                        opt.inSampleSize *= picWidth / screenW;
                    }
                } else {
                    if (picHeight > screenH && screenH != 0) {
                        opt.inSampleSize *= picHeight / screenH;
                    }
                }
                //Regenerated into a pixel, scaled bitmap
                opt.inJustDecodeBounds = false;
                final Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);
                //save map
                imageCache.put(filePath, bmp);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.imageLoaded(bmp);
                    }
                });
            }
        });
        return null;
    }

    /**
     * Read the sd card images asynchronously and compress the image as specified (maximum screen pixels)
     */
    public void loadImage(int smallRate, final String filePath, final ImageView imageView) {
        Bitmap bmp = loadDrawable(smallRate, filePath, new ImageCallback() {
            @Override
            public void imageLoaded(Bitmap bmp) {
                if (imageView.getTag().equals(filePath)) {
                    if (bmp != null) {
                        imageView.setImageBitmap(bmp);
                    } else {
                        imageView.setImageResource(R.drawable.ab);
                    }
                }
            }
        });
        if (bmp != null) {
            if (imageView.getTag().equals(filePath)) {
                imageView.setImageBitmap(bmp);
            }
        } else {
            imageView.setImageResource(R.drawable.ab);
        }
    }

    /**
     * An open callback interface to the outside world
     */
    public interface ImageCallback {
        /**
         * set the resource for the target object
         *
         * @param imageDrawable bitmap
         */
        void imageLoaded(Bitmap imageDrawable);
    }
}
