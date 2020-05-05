package com.eshen.voucherunion.presenter;

import com.eshen.voucherunion.base.IBasePresenter;
import com.eshen.voucherunion.model.domain.RecommendPageCategory;
import com.eshen.voucherunion.view.IRecommendPageCallback;

/**
 * Created by Sin on 2020/5/5
 */
public interface IRecommendPagePresenter extends IBasePresenter<IRecommendPageCallback> {
    /**
     * 获取分类
     */
    void getCategories();

    /**
     * 根据分类获取分类内容
     *
     * @param item
     */
    void getContentByCategoryId(RecommendPageCategory.DataBean item);

    /**
     * 重新加载内容
     */
    void reloadCategories();
}
