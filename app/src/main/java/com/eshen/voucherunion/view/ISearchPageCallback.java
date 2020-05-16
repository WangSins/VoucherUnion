package com.eshen.voucherunion.view;

import com.eshen.voucherunion.base.IBaseCallback;
import com.eshen.voucherunion.model.domain.Histories;
import com.eshen.voucherunion.model.domain.SearchRecommend;
import com.eshen.voucherunion.model.domain.SearchResult;

import java.util.List;

/**
 * Created by Sin on 2020/5/15
 */
public interface ISearchPageCallback extends IBaseCallback {

    /**
     * 搜索历史结果
     *
     * @param histories
     */
    void onHistoriesLoaded(Histories histories);

    /**
     * 历史记录删除完成
     */
    void onHistoriesDeleted();

    /**
     * 搜索结果，成功
     *
     * @param result
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载到了更多内容
     *
     * @param result
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更多时，网络出错
     */
    void onMoreLoaderError();

    /**
     * 没有更多内容
     */
    void onMoreLoaderEmpty();

    /**
     * 获取到关键词
     *
     * @param recommendWord
     */
    void onRecommendWordLoaded(List<SearchRecommend.DataBean> recommendWord);
}
