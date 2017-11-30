package com.kidosc.gallery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.kidosc.gallery.R;
import com.kidosc.gallery.utils.Utils;

/**
 * Desc:    Sticker View
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/21 17:30
 */

public class StickerView extends ImageView {
    private static final String TAG = "StickerView";

    private Bitmap deleteBitmap;
    private Bitmap flipVBitmap;
    private Bitmap topBitmap;
    private Bitmap resizeBitmap;
    private Bitmap mBitmap;
    private Rect dstDelete;
    private Rect dstResize;
    private Rect dstFlipv;
    private Rect dstTop;
    private int deleteBitmapWidth;
    private int deleteBitmapHeight;
    private int resizeBitmapWidth;
    private int resizeBitmapHeight;
    /**
     * horizontal mirror
     */
    private int flipVBitmapWidth;
    private int flipVBitmapHeight;
    /**
     * top
     */
    private int topBitmapWidth;
    private int topBitmapHeight;
    private Paint localPaint;
    private int mScreenWidth, mScreenHeight;
    private static final float BITMAP_SCALE = 0.7f;
    private PointF mid = new PointF();
    private OperationListener operationListener;
    private float lastRotateDegree;

    /**
     * if it's the second finger down
     */
    private boolean isPointerDown = false;
    /**
     * Finger movement distance must exceed this value
     */
    private final float pointerLimitDis = 20f;
    private final float pointerZoomCoeff = 0.09f;
    /**
     * The length of the diagonal
     */
    private float lastLength;
    private boolean isInResize = false;

    private Matrix matrix = new Matrix();
    /**
     * Whether it's inside four lines
     */
    private boolean isInSide;

    private float lastX, lastY;
    /**
     * Is it in edit mode
     */
    private boolean isInEdit = true;

    private float minScale = 0.5f;

    private float maxScale = 1.2f;

    private double halfDiagonalLength;

    private float oringinWidth = 0;

    /**
     * Double refers to the initial distance when zooming
     */
    private float oldDis;

    private final long stickerId;

    private DisplayMetrics dm;

    private boolean isHorizonMirror = false;

    public StickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        stickerId = 0;
        init();
    }

    public StickerView(Context context) {
        super(context);
        stickerId = 0;
        init();
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        stickerId = 0;
        init();
    }

    private void init() {

        dstDelete = new Rect();
        dstResize = new Rect();
        dstFlipv = new Rect();
        dstTop = new Rect();
        localPaint = new Paint();
        localPaint.setColor(getResources().getColor(R.color.red_e73a3d));
        localPaint.setAntiAlias(true);
        localPaint.setDither(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(2.0f);
        dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {

            float[] arrayOfFloat = new float[9];
            matrix.getValues(arrayOfFloat);
            //size
            float f1 = 0.0F * arrayOfFloat[0] + this.mBitmap.getHeight() / 5 * arrayOfFloat[1] + arrayOfFloat[2];
            float f2 = 0.0F * arrayOfFloat[3] + this.mBitmap.getHeight() / 5 * arrayOfFloat[4] + arrayOfFloat[5];
            float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + this.mBitmap.getHeight() / 5 * arrayOfFloat[1] + arrayOfFloat[2];
            float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + this.mBitmap.getHeight() / 5 * arrayOfFloat[4] + arrayOfFloat[5];
            float f5 = (float) (0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() / 1.3 + arrayOfFloat[2]);
            float f6 = (float) (0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() / 1.3 + arrayOfFloat[5]);
            float f7 = (float) (arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() / 1.3 + arrayOfFloat[2]);
            float f8 = (float) (arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() / 1.3 + arrayOfFloat[5]);

            canvas.save();
            canvas.drawBitmap(mBitmap, matrix, null);
            //Delete in the upper right corner
            dstDelete.left = (int) (f3 - deleteBitmapWidth / 2);
            dstDelete.right = (int) (f3 + deleteBitmapWidth / 2);
            dstDelete.top = (int) (f4 - deleteBitmapHeight / 2);
            dstDelete.bottom = (int) (f4 + deleteBitmapHeight / 2);
            //Stretch in the lower right corner
            dstResize.left = (int) (f7 - resizeBitmapWidth / 2);
            dstResize.right = (int) (f7 + resizeBitmapWidth / 2);
            dstResize.top = (int) (f8 - resizeBitmapHeight / 2);
            dstResize.bottom = (int) (f8 + resizeBitmapHeight / 2);
            //The vertical image is in the top left corner
            dstTop.left = (int) (f1 - flipVBitmapWidth / 2);
            dstTop.right = (int) (f1 + flipVBitmapWidth / 2);
            dstTop.top = (int) (f2 - flipVBitmapHeight / 2);
            dstTop.bottom = (int) (f2 + flipVBitmapHeight / 2);
            //The horizontal image is in the lower left corner
            dstFlipv.left = (int) (f5 - topBitmapWidth / 2);
            dstFlipv.right = (int) (f5 + topBitmapWidth / 2);
            dstFlipv.top = (int) (f6 - topBitmapHeight / 2);
            dstFlipv.bottom = (int) (f6 + topBitmapHeight / 2);
            if (isInEdit) {

                canvas.drawLine(f1, f2, f3, f4, localPaint);
                canvas.drawLine(f3, f4, f7, f8, localPaint);
                canvas.drawLine(f5, f6, f7, f8, localPaint);
                canvas.drawLine(f5, f6, f1, f2, localPaint);

                canvas.drawBitmap(deleteBitmap, null, dstDelete, null);
                canvas.drawBitmap(resizeBitmap, null, dstResize, null);
                canvas.drawBitmap(flipVBitmap, null, dstFlipv, null);
                canvas.drawBitmap(topBitmap, null, dstTop, null);
            }

            canvas.restore();
        }
    }

    public void setImageInt(int resId) {
        Bitmap bitmap = Utils.getBitmapFromVectorDrawable(getContext(), resId);
        setBitmap(bitmap);
    }

    public void setBitmap(Bitmap bitmap) {
        matrix.reset();
        mBitmap = bitmap;
        setDiagonalLength();
        initBitmaps();
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        oringinWidth = w;
        float initScale = (minScale + maxScale) / 2;
        matrix.postScale(initScale, initScale, w / 2, h / 2);
        //The Y coordinate is (top operation bar + square diagram) /2
        matrix.postTranslate(mScreenWidth / 2 - w / 2, (mScreenWidth) / 2 - h / 2);
        invalidate();
    }


    private void setDiagonalLength() {
        halfDiagonalLength = Math.hypot(mBitmap.getWidth(), mBitmap.getHeight()) / 2;
    }

    private void initBitmaps() {
        //When the width of the image is larger than the height of the image,
        // the size of the image will be changed by the size of the image and the smallest one is the width of the screen
        if (mBitmap.getWidth() >= mBitmap.getHeight()) {
            float minWidth = mScreenWidth / 8;
            if (mBitmap.getWidth() < minWidth) {
                minScale = 1f;
            } else {
                minScale = 1.0f * minWidth / mBitmap.getWidth();
            }

            if (mBitmap.getWidth() > mScreenWidth) {
                maxScale = 1;
            } else {
                maxScale = 1.0f * mScreenWidth / mBitmap.getWidth();
            }
        } else {
            //When the height of the image is larger than the width of the image,
            // according to the height of the image
            float minHeight = mScreenWidth / 8;
            if (mBitmap.getHeight() < minHeight) {
                minScale = 1f;
            } else {
                minScale = 1.0f * minHeight / mBitmap.getHeight();
            }

            if (mBitmap.getHeight() > mScreenWidth) {
                maxScale = 1;
            } else {
                maxScale = 1.0f * mScreenWidth / mBitmap.getHeight();
            }
        }

        topBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_top_enable);
        deleteBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_delete);
        flipVBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_flip);
        resizeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_resize);

        deleteBitmapWidth = (int) (deleteBitmap.getWidth() * BITMAP_SCALE);
        deleteBitmapHeight = (int) (deleteBitmap.getHeight() * BITMAP_SCALE);

        resizeBitmapWidth = (int) (resizeBitmap.getWidth() * BITMAP_SCALE);
        resizeBitmapHeight = (int) (resizeBitmap.getHeight() * BITMAP_SCALE);

        flipVBitmapWidth = (int) (flipVBitmap.getWidth() * BITMAP_SCALE);
        flipVBitmapHeight = (int) (flipVBitmap.getHeight() * BITMAP_SCALE);

        topBitmapWidth = (int) (topBitmap.getWidth() * BITMAP_SCALE);
        topBitmapHeight = (int) (topBitmap.getHeight() * BITMAP_SCALE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        boolean handled = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isInButton(event, dstDelete)) {
                    if (operationListener != null) {
                        operationListener.onDeleteClick();
                    }
                } else if (isInResize(event)) {
                    isInResize = true;
                    lastRotateDegree = rotationToStartPoint(event);
                    midPointToStartPoint(event);
                    lastLength = diagonalLength(event);
                } else if (isInButton(event, dstFlipv)) {
                    //horizontal mirror
                    PointF localPointF = new PointF();
                    midDiagonalPoint(localPointF);
                    matrix.postScale(-1.0F, 1.0F, localPointF.x, localPointF.y);
                    isHorizonMirror = !isHorizonMirror;
                    invalidate();
                } else if (isInButton(event, dstTop)) {
                    //Placed at the top
                    bringToFront();
                    if (operationListener != null) {
                        operationListener.onTop(this);
                    }
                } else if (isInBitmap(event)) {
                    isInSide = true;
                    lastX = event.getX(0);
                    lastY = event.getY(0);
                } else {
                    handled = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (spacing(event) > pointerLimitDis) {
                    oldDis = spacing(event);
                    isPointerDown = true;
                    midPointToStartPoint(event);
                } else {
                    isPointerDown = false;
                }
                isInSide = false;
                isInResize = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //Double refers to zoom
                if (isPointerDown) {
                    float scale;
                    float disNew = spacing(event);
                    if (disNew == 0 || disNew < pointerLimitDis) {
                        scale = 1;
                    } else {
                        scale = disNew / oldDis;
                        //Zoom is slow
                        scale = (scale - 1) * pointerZoomCoeff + 1;
                    }
                    float scaleTemp = (scale * Math.abs(dstFlipv.left - dstResize.left)) / oringinWidth;
                    boolean isScaleIn = ((scaleTemp <= minScale)) && scale < 1 || (scaleTemp >= maxScale) && scale > 1;
                    if (isScaleIn) {
                        scale = 1;
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    matrix.postScale(scale, scale, mid.x, mid.y);
                    invalidate();
                } else if (isInResize) {

                    matrix.postRotate((rotationToStartPoint(event) - lastRotateDegree) * 2, mid.x, mid.y);
                    lastRotateDegree = rotationToStartPoint(event);

                    float scale = diagonalLength(event) / lastLength;
                    boolean isDiagonalLengthIn = ((diagonalLength(event) / halfDiagonalLength <= minScale)) && scale < 1 ||
                            (diagonalLength(event) / halfDiagonalLength >= maxScale) && scale > 1;
                    if (isDiagonalLengthIn) {
                        scale = 1;
                        if (!isInResize(event)) {
                            isInResize = false;
                        }
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    matrix.postScale(scale, scale, mid.x, mid.y);

                    invalidate();
                } else if (isInSide) {
                    float x = event.getX(0);
                    float y = event.getY(0);
                    //TODO The mobile area should not exceed the screen
                    matrix.postTranslate(x - lastX, y - lastY);
                    lastX = x;
                    lastY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isInResize = false;
                isInSide = false;
                isPointerDown = false;
                break;
            default:
                break;

        }
        if (handled && operationListener != null) {
            operationListener.onEdit(this);
        }
        return handled;
    }

    /**
     * Whether it's inside four lines
     * After the image is rotated, the diamond state cannot be used to determine whether the click
     * area is in the image.
     *
     * @return true
     */
    private boolean isInBitmap(MotionEvent event) {
        float[] arrayOfFloat1 = new float[9];
        this.matrix.getValues(arrayOfFloat1);
        //upper-left
        float f1 = 0.0F * arrayOfFloat1[0] + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f2 = 0.0F * arrayOfFloat1[3] + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        //The top right corner
        float f3 = arrayOfFloat1[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f4 = arrayOfFloat1[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        //The lower left corner
        float f5 = 0.0F * arrayOfFloat1[0] + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f6 = 0.0F * arrayOfFloat1[3] + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];
        //The lower right corner
        float f7 = arrayOfFloat1[0] * this.mBitmap.getWidth() + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f8 = arrayOfFloat1[3] * this.mBitmap.getWidth() + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];

        float[] arrayOfFloat2 = new float[4];
        float[] arrayOfFloat3 = new float[4];
        //Determine the range of the X direction
        //Upper left of the x
        arrayOfFloat2[0] = f1;
        //The upper right of the x
        arrayOfFloat2[1] = f3;
        //The lower right of the x
        arrayOfFloat2[2] = f7;
        //Lower left of the x
        arrayOfFloat2[3] = f5;
        //Determine the range of the Y direction
        //Upper left of the y
        arrayOfFloat3[0] = f2;
        //The upper right of the y
        arrayOfFloat3[1] = f4;
        //The lower right of the y
        arrayOfFloat3[2] = f8;
        //Lower left of the y
        arrayOfFloat3[3] = f6;
        return pointInRect(arrayOfFloat2, arrayOfFloat3, event.getX(0), event.getY(0));
    }

    /**
     * Determines whether the point is inside a rectangle
     *
     * @param xRange x range
     * @param yRange y range
     * @param x      x
     * @param y      y
     * @return true
     */
    private boolean pointInRect(float[] xRange, float[] yRange, float x, float y) {
        //Four sides of the length
        double a1 = Math.hypot(xRange[0] - xRange[1], yRange[0] - yRange[1]);
        double a2 = Math.hypot(xRange[1] - xRange[2], yRange[1] - yRange[2]);
        double a3 = Math.hypot(xRange[3] - xRange[2], yRange[3] - yRange[2]);
        double a4 = Math.hypot(xRange[0] - xRange[3], yRange[0] - yRange[3]);
        //The distance to the four points
        double b1 = Math.hypot(x - xRange[0], y - yRange[0]);
        double b2 = Math.hypot(x - xRange[1], y - yRange[1]);
        double b3 = Math.hypot(x - xRange[2], y - yRange[2]);
        double b4 = Math.hypot(x - xRange[3], y - yRange[3]);

        double u1 = (a1 + b1 + b2) / 2;
        double u2 = (a2 + b2 + b3) / 2;
        double u3 = (a3 + b3 + b4) / 2;
        double u4 = (a4 + b4 + b1) / 2;

        //Rectangular area
        double s = a1 * a2;
        //Helen's formula calculates four triangular areas
        double ss = Math.sqrt(u1 * (u1 - a1) * (u1 - b1) * (u1 - b2))
                + Math.sqrt(u2 * (u2 - a2) * (u2 - b2) * (u2 - b3))
                + Math.sqrt(u3 * (u3 - a3) * (u3 - b3) * (u3 - b4))
                + Math.sqrt(u4 * (u4 - a4) * (u4 - b4) * (u4 - b1));
        return Math.abs(s - ss) < 0.5;
    }

    /**
     * Whether the touch is in a button range
     *
     * @param event motion event
     * @param rect  rect
     * @return true
     */
    private boolean isInButton(MotionEvent event, Rect rect) {
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    /**
     * Whether The touch is in the stretch zone
     *
     * @param event motion event
     * @return true
     */
    private boolean isInResize(MotionEvent event) {
        int left = -20 + this.dstResize.left;
        int top = -20 + this.dstResize.top;
        int right = 20 + this.dstResize.right;
        int bottom = 20 + this.dstResize.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    /**
     * The position of the touch
     * and the midpoint of the upper left corner of the image
     *
     * @param event motion event
     */
    private void midPointToStartPoint(MotionEvent event) {
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + event.getX(0);
        float f4 = f2 + event.getY(0);
        mid.set(f3 / 2, f4 / 2);
    }

    /**
     * Compute the intersection of diagonal lines
     *
     * @param paramPointF point
     */
    private void midDiagonalPoint(PointF paramPointF) {
        float[] arrayOfFloat = new float[9];
        this.matrix.getValues(arrayOfFloat);
        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f5 = f1 + f3;
        float f6 = f2 + f4;
        paramPointF.set(f5 / 2.0F, f6 / 2.0F);
    }

    /**
     * In the sliding rotation process,
     * the deflection Angle is always calculated as the absolute value of the upper left corner
     *
     * @param event motion event
     * @return float
     */
    private float rotationToStartPoint(MotionEvent event) {

        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        double arc = Math.atan2(event.getY(0) - y, event.getX(0) - x);
        return (float) Math.toDegrees(arc);
    }

    /**
     * The distance between the touch point and the center of the rectangle
     *
     * @param event motion event
     * @return float
     */
    private float diagonalLength(MotionEvent event) {
        float diagonalLength = (float) Math.hypot(event.getX(0) - mid.x, event.getY(0) - mid.y);
        return diagonalLength;
    }

    /**
     * Calculate the distance between the two fingers
     */
    private float spacing(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }

    public interface OperationListener {
        /**
         * delete
         */
        void onDeleteClick();

        /**
         * edit
         *
         * @param stickerView sticker view
         */
        void onEdit(StickerView stickerView);

        /**
         * top
         *
         * @param stickerView sticker view
         */
        void onTop(StickerView stickerView);
    }

    public void setOperationListener(OperationListener operationListener) {
        this.operationListener = operationListener;
    }

    public void setInEdit(boolean isInEdit) {
        this.isInEdit = isInEdit;
        invalidate();
    }
}
