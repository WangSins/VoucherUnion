package com.eshen.voucherunion.model.domain;

/**
 * Created by Sin on 2020/5/10
 */
public interface IBaseInfo {
    /**
     * 获取商品封面
     *
     * @return
     */
    String getCover();

    /**
     * 获取商品标题
     *
     * @return
     */
    String getTitle();

    /**
     * 获取商品URL
     *
     * @return
     */
    String getUrl();
}
