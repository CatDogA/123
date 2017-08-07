package com.shanghaigm.dms.model.util;

import android.content.Context;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ScreenUtil {
    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
