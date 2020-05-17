package com.eshen.voucherunion.base;

import android.app.Application;
import android.content.Context;

import com.vondear.rxtool.RxTool;

/**
 * Created by Sin on 2020/5/3
 */
public class BaseApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getBaseContext();
        RxTool.init(appContext);
    }

    public static Context getAppContext() {
        return appContext;
    }
}
