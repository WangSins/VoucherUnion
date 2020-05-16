package com.eshen.voucherunion.utils;

import android.content.Context;
import android.content.Intent;

import com.eshen.voucherunion.model.domain.IBaseInfo;
import com.eshen.voucherunion.presenter.ITicketPagePresenter;
import com.eshen.voucherunion.ui.activity.TicketActivity;

/**
 * Created by Sin on 2020/5/10
 */
public class TicketUtil {
    public static void toTicketPage(Context context, IBaseInfo baseInfo) {
        String url = baseInfo.getUrl();
        String title = baseInfo.getTitle();
        String cover = baseInfo.getCover();
        //拿到TickerPresenter去加载数据
        ITicketPagePresenter ticketPagePresenter = PresenterManager.getInstance().getTicketPagePresenter();
        ticketPagePresenter.getTicket(url, title, cover);
        context.startActivity(new Intent(context, TicketActivity.class));
    }
}
