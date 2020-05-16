package com.eshen.voucherunion.presenter.impl;

import com.eshen.voucherunion.model.Api;
import com.eshen.voucherunion.model.domain.HomePageContent;
import com.eshen.voucherunion.presenter.ICategoryPagePresenter;
import com.eshen.voucherunion.utils.RetrofitManager;
import com.eshen.voucherunion.utils.UrlUtils;
import com.eshen.voucherunion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sin on 2020/5/3
 */
public class CategoryPagePresenterImpl implements ICategoryPagePresenter {

    private Map<Integer, Integer> pagesInfo = new HashMap<>();

    public static final int DEFAULT_PAGE = 1;
    private Integer currentPage;

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
        //根据分类ID去加载内容
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, targetPage);
        }
        Call<HomePageContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePageContent>() {
            @Override
            public void onResponse(Call<HomePageContent> call, Response<HomePageContent> response) {
                //数据结果
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePageContent pagerContent = response.body();
                    //把数据通知UI更新
                    handleHomePageContent(pagerContent, categoryId);
                } else {
                    //请求失败
                    handleNetWorkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePageContent> call, Throwable t) {
                //加载失败的结果
                handleNetWorkError(categoryId);
            }
        });
    }

    private Call<HomePageContent> createTask(int categoryId, Integer targetPage) {
        String homePagerUrl = UrlUtils.CreateHomePagerUrl(categoryId, targetPage);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        return api.getHomePagerContent(homePagerUrl);
    }

    private void handleNetWorkError(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }
    }

    private void handleHomePageContent(HomePageContent pagerContent, int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pagerContent == null || pagerContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    List<HomePageContent.DataBean> data = pagerContent.getData();
                    List<HomePageContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void LoaderMore(int categoryId) {
        //加载更多数据
        //拿到当前页码
        currentPage = pagesInfo.get(categoryId);
        if (currentPage == null) {
            currentPage = 1;
        }
        //页码增加
        currentPage++;
        //加载数据
        Call<HomePageContent> task = createTask(categoryId, currentPage);
        //处理数据结果
        task.enqueue(new Callback<HomePageContent>() {
            @Override
            public void onResponse(Call<HomePageContent> call, Response<HomePageContent> response) {
                //结果
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePageContent result = response.body();
                    handleLoaderMoreResult(result, categoryId);

                } else {
                    handleLoaderMoreError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePageContent> call, Throwable t) {
                handleLoaderMoreError(categoryId);
            }
        });

    }

    private void handleLoaderMoreResult(HomePageContent result, int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result == null || result.getData().size() == 0) {
                    callback.onLoaderMoreEmpty();
                } else {
                    callback.onLoaderMoreLoaded(result.getData());
                }
            }
        }
    }

    private void handleLoaderMoreError(int categoryId) {
        currentPage--;
        pagesInfo.put(categoryId, currentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoaderMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryId) {

    }

    private ArrayList<ICategoryPagerCallback> callbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }
}
