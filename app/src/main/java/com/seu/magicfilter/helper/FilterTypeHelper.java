package com.seu.magicfilter.helper;

import com.kidosc.gallery.R;
import com.seu.magicfilter.filter.helper.MagicFilterType;

/**
 * Desc:    滤镜类型的辅助类
 * Email:   frank.xiong@kidosc.com
 * Date:    2017/12/12 14:29
 */

public class FilterTypeHelper {

    public static int FilterType2Name(MagicFilterType filterType) {
        switch (filterType) {
            case NONE:
                return R.string.filter_none;
            case WHITECAT:
                return R.string.filter_whitecat;
            case BLACKCAT:
                return R.string.filter_blackcat;
            case ROMANCE:
                return R.string.filter_romance;
            case SAKURA:
                return R.string.filter_sakura;
            case AMARO:
                return R.string.filter_amaro;
            case BRANNAN:
                return R.string.filter_brannan;
            case BROOKLYN:
                return R.string.filter_brooklyn;
            case EARLYBIRD:
                return R.string.filter_Earlybird;
            case FREUD:
                return R.string.filter_freud;
            case HEFE:
                return R.string.filter_hefe;
            case HUDSON:
                return R.string.filter_hudson;
            case INKWELL:
                return R.string.filter_inkwell;
            case KEVIN:
                return R.string.filter_kevin;
            case LOMO:
                return R.string.filter_lomo;
            case N1977:
                return R.string.filter_n1977;
            case NASHVILLE:
                return R.string.filter_nashville;
            case PIXAR:
                return R.string.filter_pixar;
            case RISE:
                return R.string.filter_rise;
            case SIERRA:
                return R.string.filter_sierra;
            case SUTRO:
                return R.string.filter_sutro;
            case TOASTER2:
                return R.string.filter_toastero;
            case VALENCIA:
                return R.string.filter_valencia;
            case WALDEN:
                return R.string.filter_walden;
            case XPROII:
                return R.string.filter_xproii;
            case ANTIQUE:
                return R.string.filter_antique;
            case CALM:
                return R.string.filter_calm;
            case COOL:
                return R.string.filter_cool;
            case EMERALD:
                return R.string.filter_emerald;
            case EVERGREEN:
                return R.string.filter_evergreen;
            case FAIRYTALE:
                return R.string.filter_fairytale;
            case HEALTHY:
                return R.string.filter_healthy;
            case NOSTALGIA:
                return R.string.filter_nostalgia;
            case TENDER:
                return R.string.filter_tender;
            case SWEETS:
                return R.string.filter_sweets;
            case LATTE:
                return R.string.filter_latte;
            case WARM:
                return R.string.filter_warm;
            case SUNRISE:
                return R.string.filter_sunrise;
            case SUNSET:
                return R.string.filter_sunset;
            case SKINWHITEN:
                return R.string.filter_skinwhiten;
            case CRAYON:
                return R.string.filter_crayon;
            case SKETCH:
                return R.string.filter_sketch;
            default:
                return R.string.filter_none;
        }
    }
}
