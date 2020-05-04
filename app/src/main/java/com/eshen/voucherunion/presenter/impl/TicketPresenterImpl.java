package com.eshen.voucherunion.presenter.impl;

import com.eshen.voucherunion.model.Api;
import com.eshen.voucherunion.model.domain.TicketParams;
import com.eshen.voucherunion.model.domain.TicketResult;
import com.eshen.voucherunion.presenter.ITicketPresenter;
import com.eshen.voucherunion.utils.LogUtils;
import com.eshen.voucherunion.utils.RetrofitManager;
import com.eshen.voucherunion.utils.UrlUtils;
import com.eshen.voucherunion.view.ITicketPagerCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sin on 2020/5/4
 */
public class TicketPresenterImpl implements ITicketPresenter {
    private ITicketPagerCallback callback = null;
    private String cover;
    private TicketResult ticketResult;

    enum LoadState {
        LOADING, SUCCESS, ERROR, NONE
    }

    private LoadState currentState = LoadState.NONE;

    @Override
    public void getTicket(String url, String title, String cover) {
        onLoadedTicketLoading();
        LogUtils.d(TicketPresenterImpl.this, "ticket url -->" + url);
        LogUtils.d(TicketPresenterImpl.this, "ticket title -->" + title);
        LogUtils.d(TicketPresenterImpl.this, "ticket cover -->" + cover);
        this.cover = cover;
        String ticketUrl = UrlUtils.getTicketUrl(url);
        //去获取淘口令
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(ticketUrl, title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    //请求成功
                    ticketResult = response.body();
                    LogUtils.d(TicketPresenterImpl.this, ticketResult.toString());
                    //通知UI更新
                    onLoadedTicketSuccess();
                } else {
                    //请求失败
                    onLoadedTicketError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                //失败
                onLoadedTicketError();
            }
        });
    }

    private void onLoadedTicketSuccess() {
        if (callback != null) {
            callback.onTicketLoaded(cover, ticketResult);
        } else {
            currentState = LoadState.SUCCESS;
        }
    }


    private void onLoadedTicketError() {
        if (callback != null) {
            callback.onError();
        } else {
            currentState = LoadState.ERROR;
        }
    }

    private void onLoadedTicketLoading() {
        if (callback != null) {
            callback.onLoading();
        } else {
            currentState = LoadState.LOADING;
        }
    }


    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {
        this.callback = callback;
        if (currentState != LoadState.NONE) {
            //状态已经改变
            //更新UI
            if (currentState == LoadState.SUCCESS) {
                onLoadedTicketSuccess();
            } else if (currentState == LoadState.ERROR) {
                onLoadedTicketError();
            } else if (currentState == LoadState.LOADING) {
                onLoadedTicketLoading();
            }
        }
    }

    @Override
    public void unregisterViewCallback(ITicketPagerCallback callback) {
        this.callback = null;
    }
}
