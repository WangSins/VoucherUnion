package com.eshen.voucherunion.view;

import com.eshen.voucherunion.base.IBaseCallback;
import com.eshen.voucherunion.model.domain.HomePageContent;

import java.util.List;

/**
 * Created by Sin on 2020/5/3
 */
public interface ICategoryPagerCallback extends IBaseCallback {

    int getCategoryId();

    /**
     * 数据加载回来
     *
     * @param contents
     */
    void onContentLoaded(List<HomePageContent.DataBean> contents);

    /**
     * 加载到更多内容
     *
     * @param contents
     */
    void onLoaderMoreLoaded(List<HomePageContent.DataBean> contents);

    /**
     * 加载更多网络错误
     */
    void onLoaderMoreError();

    /**
     * 加载更多没有更多内容
     */
    void onLoaderMoreEmpty();

    /**
     * 加载到轮播图内容
     *
     * @param contents
     */
    void onLooperListLoaded(List<HomePageContent.DataBean> contents);
}
