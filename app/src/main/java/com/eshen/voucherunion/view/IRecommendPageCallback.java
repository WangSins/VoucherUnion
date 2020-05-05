package com.eshen.voucherunion.view;

import com.eshen.voucherunion.base.IBaseCallback;
import com.eshen.voucherunion.model.domain.RecommendContent;
import com.eshen.voucherunion.model.domain.RecommendPageCategory;

/**
 * Created by Sin on 2020/5/5
 */
public interface IRecommendPageCallback extends IBaseCallback {
    /**
     * 分类内容
     *
     * @param categories
     */
    void onCategoriesLoaded(RecommendPageCategory categories);

    /**
     * 内容
     *
     * @param content
     */
    void onContentLoaded(RecommendContent content);
}
