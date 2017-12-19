package com.kidosc.gallery.global;

import com.seu.magicfilter.filter.helper.MagicFilterType;

/**
 * Desc:    constants
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/11/22 17:19
 */

public class Constants {
    /**
     * 文件路径，主要用于跳转各个activity时，用于携带信息
     */
    public static final String FILE_PATH = "filePath";

    /**
     * 贴纸类型
     */
    public static final String BUNDLE_STICKER = "bundle_sticker";
    /**
     * gallery存储sp
     */
    public static final String FILTERS_SP = "filters_sp";
    /**
     * 当前的滤镜
     */
    public static final String CURRENT_FILTER = "current_filter";
    /**
     * 拍照or录像，用于跳转到camera activity 时，是打开录像还是拍照
     */
    public static final String CAMERA_USE = "camera_use";

    /**
     * 滤镜的所有类型
     */
    public static final MagicFilterType[] FILTER_TYPES = new MagicFilterType[]{
            MagicFilterType.FAIRYTALE,
            MagicFilterType.SUNRISE,
            MagicFilterType.SUNSET,
            MagicFilterType.WHITECAT,
            MagicFilterType.BLACKCAT,
            MagicFilterType.SKINWHITEN,
            MagicFilterType.HEALTHY,
            MagicFilterType.SWEETS,
            MagicFilterType.ROMANCE,
            MagicFilterType.SAKURA,
            MagicFilterType.WARM,
            MagicFilterType.ANTIQUE,
            MagicFilterType.NOSTALGIA,
            MagicFilterType.CALM,
            MagicFilterType.LATTE,
            MagicFilterType.TENDER,
            MagicFilterType.COOL,
            MagicFilterType.EMERALD,
            MagicFilterType.EVERGREEN,
            MagicFilterType.CRAYON,
            MagicFilterType.SKETCH,
            MagicFilterType.AMARO,
            MagicFilterType.BRANNAN,
            MagicFilterType.BROOKLYN,
            MagicFilterType.EARLYBIRD,
            MagicFilterType.FREUD,
            MagicFilterType.HEFE,
            MagicFilterType.HUDSON,
            MagicFilterType.INKWELL,
            MagicFilterType.KEVIN,
            MagicFilterType.LOMO,
            MagicFilterType.N1977,
            MagicFilterType.NASHVILLE,
            MagicFilterType.PIXAR,
            MagicFilterType.RISE,
            MagicFilterType.SIERRA,
            MagicFilterType.SUTRO,
            MagicFilterType.TOASTER2,
            MagicFilterType.VALENCIA,
            MagicFilterType.WALDEN,
            MagicFilterType.XPROII
    };
}