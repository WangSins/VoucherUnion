package com.eshen.voucherunion.presenter.impl;

import com.eshen.voucherunion.model.Api;
import com.eshen.voucherunion.model.domain.RecommendContent;
import com.eshen.voucherunion.model.domain.RecommendPageCategory;
import com.eshen.voucherunion.presenter.IRecommendPagePresenter;
import com.eshen.voucherunion.utils.LogUtils;
import com.eshen.voucherunion.utils.RetrofitManager;
import com.eshen.voucherunion.utils.UrlUtils;
import com.eshen.voucherunion.view.IRecommendPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sin on 2020/5/5
 */
public class RecommendPagePresenterImpl implements IRecommendPagePresenter {
    private IRecommendPageCallback callback;
    private final Api api;

    public RecommendPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        api = retrofit.create(Api.class);
    }

    @Override
    public void getCategories() {
        if (callback != null) {
            callback.onLoading();
        }
        //
        Call<RecommendPageCategory> task = api.getRecommendPageCategories();
        task.enqueue(new Callback<RecommendPageCategory>() {
            @Override
            public void onResponse(Call<RecommendPageCategory> call, Response<RecommendPageCategory> response) {
                int code = response.code();
                LogUtils.d(RecommendPagePresenterImpl.this, "getRecommendPageCategories code-->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    RecommendPageCategory selectedPageCategory = response.body();
                    //通知UI更新
                    if (callback != null) {
                        callback.onCategoriesLoaded(selectedPageCategory);
                    }
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<RecommendPageCategory> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    private void onLoadedError() {
        if (callback != null) {
            callback.onError();
        }
    }

    @Override
    public void getContentByCategoryId(RecommendPageCategory.DataBean item) {
        String targetUrl = UrlUtils.getRecommendPageContentUrl(item.getFavorites_id());
        Call<RecommendContent> task = api.getRecommendPageContent(targetUrl);
        task.enqueue(new Callback<RecommendContent>() {
            @Override
            public void onResponse(Call<RecommendContent> call, Response<RecommendContent> response) {
                int code = response.code();
                LogUtils.d(RecommendPagePresenterImpl.this, "getRecommendPageContent code-->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    RecommendContent recommendContent = response.body();
                    if (callback != null) {
                        callback.onContentLoaded(recommendContent);
                    }
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<RecommendContent> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    @Override
    public void reloadCategories() {
        getCategories();
    }

    @Override
    public void registerViewCallback(IRecommendPageCallback callback) {
        this.callback = callback;
    }

    @Override
    public void unregisterViewCallback(IRecommendPageCallback callback) {
        this.callback = null;
    }
}
