package com.eshen.voucherunion.utils;

import android.content.Context;

/**
 * Created by Sin on 2020/5/3
 */
public class SizeUtils {
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale * 0.5f);
    }
}
