package com.eshen.voucherunion.presenter;

import com.eshen.voucherunion.base.IBasePresenter;
import com.eshen.voucherunion.view.ICategoryPagerCallback;

/**
 * Created by Sin on 2020/5/3
 */
public interface ICategoryPagePresenter extends IBasePresenter<ICategoryPagerCallback> {
    /**
     * 根据分类ID去获取对应分类内容
     *
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    void LoaderMore(int categoryId);

    void reload(int categoryId);

}
