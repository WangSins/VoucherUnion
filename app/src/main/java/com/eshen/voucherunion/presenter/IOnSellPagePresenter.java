package com.eshen.voucherunion.presenter;

import com.eshen.voucherunion.base.IBasePresenter;
import com.eshen.voucherunion.view.IOnSellPageCallback;

/**
 * Created by Sin on 2020/5/10
 */
public interface IOnSellPagePresenter extends IBasePresenter<IOnSellPageCallback> {

    /**
     * 加载特惠内容
     */
    void getOnSellContent();

    /**
     * 重新加载内容
     */
    void reLoaded();

    /**
     * 加载更多
     */
    void LoaderMore();

}
