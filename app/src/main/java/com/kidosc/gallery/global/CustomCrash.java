package com.kidosc.gallery.global;

import android.os.Environment;

import com.kidosc.gallery.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Desc:    collect crash to save local file
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/24 18:16
 */

public class CustomCrash implements Thread.UncaughtExceptionHandler {

    /**
     * 崩溃日志SD卡保存路径
     */
    private static final String CRASH_SAVE_SD_PATH = "sdcard/crash_cache/";

    private CustomCrash() {
    }

    private static CustomCrash instance;

    static CustomCrash getInstance() {
        if (instance == null) {
            synchronized (CustomCrash.class) {
                if (instance == null) {
                    instance = new CustomCrash();
                }
            }
        }
        return instance;
    }

    /**
     * 设置异常类
     */
    void setCustonCrashInfo() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        saveToSD(e);
    }

    private void saveToSD(Throwable ex) {
        String fileName = null;
        StringBuilder sBuffer = new StringBuilder();
        // 添加异常信息
        sBuffer.append(getExceptionInfo(ex));
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file1 = new File(CRASH_SAVE_SD_PATH);
            if (!file1.exists()) {
                file1.mkdir();
            }
            fileName = file1.toString() + File.separator + System.currentTimeMillis() + ".log";
            File file2 = new File(fileName);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file2);
                fos.write(sBuffer.toString().getBytes());
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取并且转化异常信息
     * 同时可以进行投递相关的设备，用户信息
     *
     * @param ex throwable
     * @return 异常信息的字符串形式
     */
    private String getExceptionInfo(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("---------Crash Log Begin---------\n");
        //可以进行相关设备信息投递
        stringBuffer.append("SystemVersion:").append(Utils.getLocalSystemVersion()).append("\n");
        stringBuffer.append(sw.toString()).append("\n");
        stringBuffer.append("---------Crash Log End---------\n");
        return stringBuffer.toString();
    }
}
