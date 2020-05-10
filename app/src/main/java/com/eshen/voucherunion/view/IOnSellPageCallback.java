package com.eshen.voucherunion.view;

import com.eshen.voucherunion.base.IBaseCallback;
import com.eshen.voucherunion.model.domain.OnSellContent;

/**
 * Created by Sin on 2020/5/10
 */
public interface IOnSellPageCallback extends IBaseCallback {

    /**
     * 特惠内容
     *
     * @param result
     */
    void onContentLoaded(OnSellContent result);

    /**
     * 加载更多的结果
     *
     * @param moreResult
     */
    void onMoreLoaded(OnSellContent moreResult);

    /**
     * 加载更多失败
     */
    void onMoreLoadedError();

    /**
     * 加载更多，没有更多内容
     */
    void onMoreLoadedEmpty();
}
