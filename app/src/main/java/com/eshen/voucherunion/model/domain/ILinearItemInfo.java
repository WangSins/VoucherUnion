package com.eshen.voucherunion.model.domain;

/**
 * Created by Sin on 2020/5/16
 */
public interface ILinearItemInfo extends IBaseInfo {
    /**
     * 获取原价
     *
     * @return
     */
    String getFinalPrise();

    /**
     * 获取优惠价格
     *
     * @return
     */
    long getCouponAmount();

    /**
     * 获取销量
     *
     * @return
     */
    long getVolume();
}
