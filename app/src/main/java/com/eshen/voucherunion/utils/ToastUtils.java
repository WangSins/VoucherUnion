package com.eshen.voucherunion.utils;

import android.widget.Toast;

import com.eshen.voucherunion.base.BaseApplication;

/**
 * Created by Sin on 2020/5/3
 */
public class ToastUtils {

    private static Toast toast;

    public static void showToast(String tips) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getAppContext(), tips, Toast.LENGTH_SHORT);
        } else {
            toast.setText(tips);
        }
        toast.show();
    }
}
