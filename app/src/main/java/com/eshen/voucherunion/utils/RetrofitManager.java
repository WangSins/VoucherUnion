package com.eshen.voucherunion.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sin on 2020/5/2
 */
public class RetrofitManager {
    private static final RetrofitManager ourInstance = new RetrofitManager();
    private final Retrofit retrofit;

    public static RetrofitManager getInstance() {
        return ourInstance;
    }

    private RetrofitManager() {
        //创建Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
