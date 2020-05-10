package com.eshen.voucherunion.presenter.impl;

import com.eshen.voucherunion.model.Api;
import com.eshen.voucherunion.model.domain.OnSellContent;
import com.eshen.voucherunion.presenter.IOnSellPagePresenter;
import com.eshen.voucherunion.utils.RetrofitManager;
import com.eshen.voucherunion.utils.UrlUtils;
import com.eshen.voucherunion.view.IOnSellPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sin on 2020/5/10
 */
public class OnSellPagePresenterImpl implements IOnSellPagePresenter {
    private static final int DEFAULT_PAGE = 1;
    private int currentPage = DEFAULT_PAGE;
    private IOnSellPageCallback callback;
    private final Api api;

    public OnSellPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        api = retrofit.create(Api.class);
    }

    @Override
    public void getOnSellContent() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        if (callback != null) {
            callback.onLoading();
        }
        //
        String targetUrl = UrlUtils.getOnSellPageContentUrl(currentPage);
        Call<OnSellContent> task = api.getOnSellPageContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                isLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent onSellContent = response.body();
                    onSuccess(onSellContent);
                } else {
                    onError();
                }

            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onError();
            }
        });
    }

    private void onSuccess(OnSellContent onSellContent) {
        if (callback != null) {
            try {
                if (isEmpty(onSellContent)) {
                    onEmpty();
                } else {
                    callback.onContentLoaded(onSellContent);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onEmpty();
            }
        }
    }

    private void onError() {
        isLoading = false;
        if (callback != null) {
            callback.onError();
        }
    }

    private void onEmpty() {
        if (callback != null) {
            callback.onEmpty();
        }
    }

    @Override
    public void reLoaded() {
        //重新加载
        getOnSellContent();
    }

    /**
     * 当前状态
     */
    private boolean isLoading = false;

    @Override
    public void LoaderMore() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        //加载更多
        currentPage++;
        String targetUrl = UrlUtils.getOnSellPageContentUrl(currentPage);
        Call<OnSellContent> task = api.getOnSellPageContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                isLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent onSellContent = response.body();
                    onMoreLoaded(onSellContent);
                } else {
                    onMoreLoadedError();
                }

            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onMoreLoadedError();
            }
        });
    }

    /**
     * 加载更多的结果，通知UI更新
     *
     * @param onSellContent
     */
    private void onMoreLoaded(OnSellContent onSellContent) {
        if (callback != null) {
            try {
                if (isEmpty(onSellContent)) {
                    currentPage--;
                    callback.onMoreLoadedEmpty();
                } else {
                    callback.onMoreLoaded(onSellContent);
                }
            } catch (Exception e) {
                e.printStackTrace();
                currentPage--;
                callback.onMoreLoadedEmpty();
            }
        }
    }

    private void onMoreLoadedError() {
        isLoading = false;
        currentPage--;
        if (callback != null) {
            callback.onMoreLoadedError();
        }
    }

    private boolean isEmpty(OnSellContent content) {
        int size = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        return size == 0;
    }

    @Override
    public void registerViewCallback(IOnSellPageCallback callback) {
        this.callback = callback;
    }

    @Override
    public void unregisterViewCallback(IOnSellPageCallback callback) {
        this.callback = null;
    }
}
