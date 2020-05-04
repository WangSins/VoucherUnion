package com.eshen.voucherunion.presenter;

import com.eshen.voucherunion.base.IBasePresenter;
import com.eshen.voucherunion.view.IHomeCallback;

/**
 * Created by Sin on 2020/5/2
 */
public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    /**
     * 获取搜索商品分类
     */
    void getCategories();

}
