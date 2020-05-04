package com.eshen.voucherunion.presenter;

import com.eshen.voucherunion.base.IBasePresenter;
import com.eshen.voucherunion.view.ITicketPagerCallback;

/**
 * Created by Sin on 2020/5/4
 */
public interface ITicketPresenter extends IBasePresenter<ITicketPagerCallback> {

    /**
     * 生成淘口令
     *
     * @param url
     * @param title
     * @param cover
     */
    void getTicket(String url, String title, String cover);
}
