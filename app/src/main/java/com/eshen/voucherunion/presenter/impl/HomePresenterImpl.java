package com.eshen.voucherunion.presenter.impl;

import com.eshen.voucherunion.model.Api;
import com.eshen.voucherunion.model.domain.Categories;
import com.eshen.voucherunion.presenter.IHomePresenter;
import com.eshen.voucherunion.utils.LogUtils;
import com.eshen.voucherunion.utils.RetrofitManager;
import com.eshen.voucherunion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sin on 2020/5/2
 */
public class HomePresenterImpl implements IHomePresenter {
    private IHomeCallback callback = null;

    public HomePresenterImpl() {
        super();
    }

    @Override
    public void getCategories() {
        if (callback != null) {
            callback.onLoading();
        }
        //加载分类数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //数据结果
                int code = response.code();
                LogUtils.d(HomePresenterImpl.this, "code-->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //请求成功
                    Categories categories = response.body();
                    if (callback != null) {
                        if (categories == null || categories.getData().size() == 0) {
                            callback.onEmpty();
                        } else {
                            LogUtils.d(HomePresenterImpl.this, categories.toString());
                            callback.onCategoriesLoaded(categories);
                        }
                    }
                } else {
                    //请求失败
                    LogUtils.d(HomePresenterImpl.this, "请求失败...");
                    if (callback != null) {
                        callback.onError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //加载失败的结果
                LogUtils.d(HomePresenterImpl.this, "请求错误...");
                if (callback != null) {
                    callback.onError();
                }
            }
        });
    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        this.callback = callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallback callback) {
        this.callback = null;
    }
}
