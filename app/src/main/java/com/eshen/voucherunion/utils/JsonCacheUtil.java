package com.eshen.voucherunion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.eshen.voucherunion.base.BaseApplication;
import com.eshen.voucherunion.model.domain.CacheWithDuration;
import com.google.gson.Gson;

/**
 * Created by Sin on 2020/5/15
 */
public class JsonCacheUtil {

    public static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    private JsonCacheUtil() {
        sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    private static JsonCacheUtil sInstance = null;

    public static JsonCacheUtil getInstance() {
        if (sInstance == null) {
            sInstance = new JsonCacheUtil();
        }
        return sInstance;
    }

    public void saveCache(String key, Object value) {
        saveCache(key, value, -1);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        String valueStr = gson.toJson(value);
        if (duration != -1L) {
            //当前时间
            duration += System.currentTimeMillis();
        }
        //保存一个有数据有时间的内容
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration, valueStr);
        String cacheWithDurationStr = gson.toJson(cacheWithDuration);
        edit.putString(key, cacheWithDurationStr);
        edit.apply();
    }

    public void delCache(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String valueWithDuration = sharedPreferences.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = gson.fromJson(valueWithDuration, CacheWithDuration.class);
        //对时间进行判断
        long duration = cacheWithDuration.getDuration();
        if (duration != -1 && duration - System.currentTimeMillis() <= 0) {
            //过期了
            return null;
        } else {
            //未过期
            String cache = cacheWithDuration.getCache();
            T result = gson.fromJson(cache, clazz);
            return result;
        }
    }
}
