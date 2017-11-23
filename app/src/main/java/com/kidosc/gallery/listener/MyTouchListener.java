package com.kidosc.gallery.listener;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.kidosc.gallery.utils.Utils;

/**
 * Desc:    TouchListener
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/21 14:49
 */

public class MyTouchListener implements View.OnTouchListener {
    /**
     * The initial state
     */
    private int mode = 0;
    private ImageView mImageView;
    private float startDis;
    private PointF midPoint;
    /**
     * Drag photo mode
     */
    private static final int MODE_DRAG = 1;
    /**
     * Zoom in to zoom out the photo mode
     */
    private static final int MODE_ZOOM = 2;
    /**
     * Used to record the coordinates of the beginning
     */
    private PointF startPoint = new PointF();
    private Matrix matrix = new Matrix();
    private Matrix currentMatrix = new Matrix();
    private static final float S_SCALE = 10f;

    public MyTouchListener(ImageView imageView) {
        this.mImageView = imageView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = MODE_DRAG;
                currentMatrix.set(mImageView.getImageMatrix());
                startPoint.set(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == MODE_DRAG) {
                    // get the distance of the x axis
                    float dx = event.getX() - startPoint.x;
                    // get the distance of the y axis
                    float dy = event.getY() - startPoint.y;
                    // Move the position before it moves
                    matrix.set(currentMatrix);
                    matrix.postTranslate(dx, dy);
                } else if (mode == MODE_ZOOM) {
                    // End of the distance
                    float endDis = Utils.distance(event);
                    // The pixels are greater than 10 when two fingers are joined together
                    if (endDis > S_SCALE) {
                        // Get the scaling factor
                        float scale = endDis / startDis;
                        matrix.set(currentMatrix);
                        matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                break;
            // When there are already contacts on the screen (fingers), there's one touch down on the screen
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = MODE_ZOOM;
                startDis = Utils.distance(event);
                // The pixels are greater than 10 when two fingers are joined together
                if (startDis > S_SCALE) {
                    midPoint = Utils.mid(event);
                    currentMatrix.set(mImageView.getImageMatrix());
                }
                break;
            default:
                break;
        }
        mImageView.setImageMatrix(matrix);
        return true;
    }
}
