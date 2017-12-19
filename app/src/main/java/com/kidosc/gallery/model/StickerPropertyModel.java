package com.kidosc.gallery.model;

import java.io.Serializable;

/**
 * Desc:    StickerPropertyModel
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/21 17:31
 */

public class StickerPropertyModel implements Serializable {
    private static final long serialVersionUID = 3800737478616389410L;

    /**
     * sticker ID
     */
    private long stickerId;
    /**
     * text
     */
    private String text;
    private float xLocation;
    private float yLocation;
    private float degree;
    private float scaling;
    /**
     * Bubble order
     */
    private int order;
    /**
     * Horizontal mirror 1 mirror 2 unmirrored
     */
    private int horizonMirror;
    /**
     * Sticker PNG URL
     */
    private String stickerURL;

    public int getHorizonMirror() {
        return horizonMirror;
    }

    public void setHorizonMirror(int horizonMirror) {
        this.horizonMirror = horizonMirror;
    }

    public String getStickerURL() {
        return stickerURL;
    }

    public void setStickerURL(String stickerURL) {
        this.stickerURL = stickerURL;
    }

    public long getStickerId() {
        return stickerId;
    }

    public void setStickerId(long stickerId) {
        this.stickerId = stickerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getxLocation() {
        return xLocation;
    }

    public void setxLocation(float xLocation) {
        this.xLocation = xLocation;
    }

    public float getyLocation() {
        return yLocation;
    }

    public void setyLocation(float yLocation) {
        this.yLocation = yLocation;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public float getScaling() {
        return scaling;
    }

    public void setScaling(float scaling) {
        this.scaling = scaling;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
