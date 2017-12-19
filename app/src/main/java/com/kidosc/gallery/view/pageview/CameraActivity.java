package com.kidosc.gallery.view.pageview;

import android.content.Intent;
import android.graphics.Point;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kidosc.gallery.R;
import com.kidosc.gallery.base.BaseActivity;
import com.kidosc.gallery.global.Constants;
import com.kidosc.gallery.presenter.ImageLoaderPresenter;
import com.kidosc.gallery.utils.SPUtils;
import com.kidosc.gallery.utils.UIHandler;
import com.kidosc.gallery.utils.Utils;
import com.seu.magicfilter.MagicEngine;
import com.seu.magicfilter.helper.FilterTypeHelper;
import com.seu.magicfilter.helper.SavePictureTask;
import com.seu.magicfilter.widget.MagicCameraView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Desc:    拍照的activity
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/12/11 14:18
 */

public class CameraActivity extends BaseActivity implements UIHandler.HandleMsgListener<CameraActivity> {

    private static final String TAG = CameraActivity.class.getSimpleName();
    private static final int SMALL_RATE = 4;
    private static final int UPDATE_PHOTO = 0;
    private MagicEngine mMagicEngine;
    private ImageView mPhotoIv;
    private ImageLoaderPresenter mLoader;
    private File mPhotoFile;
    private String mFilePath;
    private UIHandler mHandler;
    private boolean isRecording = false;
    private TextView mTitleTv;
    private Timer mTimer;
    private volatile int mTime = 0;
    private int mFilterType = 0;
    private int mCameraUse = 0;
    private ScheduledTask mTask;
    private ExecutorService mExecutorService;

    @Override
    protected void initView() {
        //获取传过来的照片路径
        mFilePath = getIntent().getStringExtra(Constants.FILE_PATH);
        mCameraUse = getIntent().getIntExtra(Constants.CAMERA_USE, 0);
        Log.d(TAG, "mFilePath = " + mFilePath);
        //初始化资源
        mHandler = new UIHandler<CameraActivity>(this, this);
        SPUtils.initASpName(Constants.FILTERS_SP);
        mExecutorService = new ThreadPoolExecutor(2, 2, 0
                , TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r);
            }
        });
//        mLoader = new ImageLoaderPresenter(Utils.getScreenH(), Utils.getScreenW());
        //初始化滤镜的engine
        MagicEngine.Builder builder = new MagicEngine.Builder();
        mMagicEngine = builder.build((MagicCameraView) findViewById(R.id.magic_camera_view));
        //初始化magic view
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        MagicCameraView cameraView = (MagicCameraView) findViewById(R.id.magic_camera_view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cameraView.getLayoutParams();
        params.width = screenSize.x;
        params.height = screenSize.x * 4 / 3;
        cameraView.setLayoutParams(params);
        //find view id
        mTitleTv = (TextView) findViewById(R.id.title_iv);
        mPhotoIv = (ImageView) findViewById(R.id.btn_gallery);
        findViewById(R.id.btn_filter).setOnClickListener(this);
        findViewById(R.id.btn_shutter).setOnClickListener(this);
        mPhotoIv.setOnClickListener(this);
        if (mFilePath == null) {
            //从没有拍过照片
            return;
        }
        File file = new File(mFilePath);
        Glide.with(this).load(file).fitCenter().into(mPhotoIv);
//        mPhotoIv.setTag(mFilePath);
//        mLoader.loadImage(SMALL_RATE, mFilePath, mPhotoIv);
    }

    @Override
    protected void initData() {
        //获取滤镜类型
        mFilterType = SPUtils.getInt(this, Constants.CURRENT_FILTER);
    }

    @Override
    protected int getContentView() {
        return R.layout.camera_page;
    }

    @Override
    protected void click(View v) {
        switch (v.getId()) {
            case R.id.btn_shutter:
                if (mCameraUse == 0) {
                    //拍照
                    takePhoto();
                } else {
                    //录像
                    takeVideo();
                }
                break;
            case R.id.btn_gallery:
                //单张照片查看
                Intent intent = new Intent(this, PhotoActivity.class);
                intent.putExtra(Constants.FILE_PATH, mFilePath);
                startActivity(intent);
                Toast.makeText(this, "正在查看照片", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_filter:
                //切换滤镜
                mMagicEngine.setFilter(Constants.FILTER_TYPES[mFilterType]);
                mTitleTv.setText(FilterTypeHelper.FilterType2Name(Constants.FILTER_TYPES[mFilterType]));
                mFilterType++;
                Toast.makeText(this, "已切换滤镜", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 录像
     */
    private void takeVideo() {
        if (isRecording) {
            Toast.makeText(this, "停止录像", Toast.LENGTH_SHORT).show();
            mMagicEngine.stopRecord();
            if (mFilterType == 0) {
                mTitleTv.setText(R.string.filter_none);
            } else {
                mTitleTv.setText(FilterTypeHelper.FilterType2Name(Constants.FILTER_TYPES[mFilterType]));
            }
            timerStop();
        } else {
            Toast.makeText(this, "正在录像", Toast.LENGTH_SHORT).show();
            mMagicEngine.startRecord();
            timerStart();
        }
        isRecording = !isRecording;
    }

    class ScheduledTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTime++;
                    mTitleTv.setText(Utils.timeCalculate(mTime));
                }
            });
        }
    }

    private void timerStart() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTask == null) {
            mTask = new ScheduledTask();
        }
        mTimer.schedule(mTask, 1000, 1000);
    }

    private void timerStop() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTime = 0;
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        mPhotoFile = Utils.getOutputMediaFile();
        mMagicEngine.savePicture(mPhotoFile, new SavePictureTask.OnPictureSaveListener() {
            @Override
            public void onSaved(String result) {
                Log.d(TAG, "onSaved result = " + result);
                //设置图片
                mHandler.sendEmptyMessage(UPDATE_PHOTO);
            }
        });
    }

    @Override
    public void handleMessage(CameraActivity holder, Message msg) {
        switch (msg.what) {
            case UPDATE_PHOTO:
                Glide.with(this).load(mPhotoFile).fitCenter().into(mPhotoIv);
                Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
