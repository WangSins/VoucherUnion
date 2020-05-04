package com.eshen.voucherunion.base;

/**
 * Created by Sin on 2020/5/3
 */
public interface IBasePresenter<T> {
    /**
     * 注册UI通知接口
     *
     * @param callback
     */
    void registerViewCallback(T callback);

    /**
     * 取消注册注册UI通知接口
     *
     * @param callback
     */
    void unregisterViewCallback(T callback);
}
