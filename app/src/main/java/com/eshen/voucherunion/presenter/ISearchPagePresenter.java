package com.eshen.voucherunion.presenter;

import com.eshen.voucherunion.base.IBasePresenter;
import com.eshen.voucherunion.view.ISearchPageCallback;

/**
 * Created by Sin on 2020/5/15
 */
public interface ISearchPagePresenter extends IBasePresenter<ISearchPageCallback> {
    /**
     * 获取搜索历史
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     * 发起搜索
     *
     * @param keyword
     */
    void doSearch(String keyword);

    /**
     * 重新搜索
     */
    void reSearch();

    /**
     * 获取更多搜索结果
     */
    void loaderMore();

    /**
     * 获取搜索推荐词
     */
    void getRecommendWords();
}
