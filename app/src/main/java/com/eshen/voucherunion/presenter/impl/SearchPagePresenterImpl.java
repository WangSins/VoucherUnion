package com.eshen.voucherunion.presenter.impl;

import com.eshen.voucherunion.model.Api;
import com.eshen.voucherunion.model.domain.Histories;
import com.eshen.voucherunion.model.domain.SearchRecommend;
import com.eshen.voucherunion.model.domain.SearchResult;
import com.eshen.voucherunion.presenter.ISearchPagePresenter;
import com.eshen.voucherunion.utils.JsonCacheUtil;
import com.eshen.voucherunion.utils.RetrofitManager;
import com.eshen.voucherunion.view.ISearchPageCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sin on 2020/5/15
 */
public class SearchPagePresenterImpl implements ISearchPagePresenter {

    private final Api api;
    private ISearchPageCallback callback;
    private static final int DEFAULT_PAGE = 0;
    private int currentPage = DEFAULT_PAGE;
    private String currentKeyWord;
    public static final String KEY_HISTORIES = "key_histories";
    public static final int DEFAULT_HISTORIES_SIZE = 10;
    private int historiesMaxSize = DEFAULT_HISTORIES_SIZE;
    private final JsonCacheUtil jsonCacheUtil;

    public SearchPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        api = retrofit.create(Api.class);
        jsonCacheUtil = JsonCacheUtil.getInstance();
    }

    @Override
    public void getHistories() {
        Histories histories = jsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if (callback != null) {
            callback.onHistoriesLoaded(histories);
        }
    }

    @Override
    public void delHistories() {
        jsonCacheUtil.delCache(KEY_HISTORIES);
        if (callback != null) {
            callback.onHistoriesDeleted();
        }
    }

    /**
     * 添加历史记录
     *
     * @param history
     */
    private void saveHistories(String history) {
        Histories histories = jsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        //如果已经有了，干掉再添加
        List<String> historiesList = null;
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        //去重完成
        //处理没有数据的情况
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }
        histories.setHistories(historiesList);
        //对个数进行限制
        if (historiesList.size() > historiesMaxSize) {
            historiesList = historiesList.subList(0, historiesMaxSize);
        }
        //添加记录
        historiesList.add(history);
        //保存记录
        jsonCacheUtil.saveCache(KEY_HISTORIES, histories);
    }

    @Override
    public void doSearch(String keyword) {
        if (currentKeyWord == null || !currentKeyWord.contains(keyword)) {
            this.currentKeyWord = keyword;
            this.saveHistories(keyword);
        }
        if (callback != null) {
            callback.onLoading();
        }
        //
        Call<SearchResult> task = api.doSearch(currentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchResult searchResult = response.body();
                    handleSearchResult(searchResult);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onError();
            }
        });
    }

    private void onError() {
        if (callback != null) {
            callback.onError();
        }
    }

    private void handleSearchResult(SearchResult searchResult) {
        if (callback != null) {
            if (isResultEmpty(searchResult)) {
                callback.onEmpty();
            } else {
                callback.onSearchSuccess(searchResult);
            }
        }
    }

    private boolean isResultEmpty(SearchResult searchResult) {
        try {
            return searchResult == null || searchResult.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void reSearch() {
        if (currentKeyWord == null) {
            if (callback != null) {
                callback.onEmpty();
            }
        } else {
            //可以重新搜索
            doSearch(currentKeyWord);
        }
    }

    @Override
    public void loaderMore() {
        currentPage++;
        if (currentKeyWord == null) {
            if (callback != null) {
                callback.onEmpty();
            }
        } else {
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = api.doSearch(currentPage, currentKeyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchResult searchResult = response.body();
                    handleMoreSearchResult(searchResult);
                } else {
                    onLoaderMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onLoaderMoreError();
            }
        });
    }

    private void handleMoreSearchResult(SearchResult searchResult) {
        if (callback != null) {
            if (isResultEmpty(searchResult)) {
                callback.onMoreLoaderEmpty();
            } else {
                callback.onMoreLoaded(searchResult);
            }
        }
    }

    private void onLoaderMoreError() {
        currentPage--;
        if (callback != null) {
            callback.onMoreLoaderError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = api.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchRecommend searchRecommend = response.body();
                    if (callback != null) {
                        callback.onRecommendWordLoaded(searchRecommend.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchPageCallback callback) {
        this.callback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchPageCallback callback) {
        this.callback = null;
    }
}
