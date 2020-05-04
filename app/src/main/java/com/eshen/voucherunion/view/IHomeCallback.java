package com.eshen.voucherunion.view;

import com.eshen.voucherunion.base.IBaseCallback;
import com.eshen.voucherunion.model.domain.Categories;

/**
 * Created by Sin on 2020/5/2
 */
public interface IHomeCallback extends IBaseCallback {

    void onCategoriesLoaded(Categories categories);

}
