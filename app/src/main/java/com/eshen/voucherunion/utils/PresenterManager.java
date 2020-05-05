package com.eshen.voucherunion.utils;

import com.eshen.voucherunion.presenter.ICategoryPagerPresenter;
import com.eshen.voucherunion.presenter.IHomePresenter;
import com.eshen.voucherunion.presenter.IRecommendPagePresenter;
import com.eshen.voucherunion.presenter.ITicketPresenter;
import com.eshen.voucherunion.presenter.impl.CategoryPagerPresenterImpl;
import com.eshen.voucherunion.presenter.impl.HomePresenterImpl;
import com.eshen.voucherunion.presenter.impl.RecommendPagePresenterImpl;
import com.eshen.voucherunion.presenter.impl.TicketPresenterImpl;

/**
 * Created by Sin on 2020/5/4
 */
public class PresenterManager {
    private static final PresenterManager ourInstance = new PresenterManager();

    private final ICategoryPagerPresenter categoryPagerPresenter;
    private final IHomePresenter homePresenter;
    private final ITicketPresenter ticketPresenter;
    private final IRecommendPagePresenter recommendPagePresenter;

    public static PresenterManager getInstance() {
        return ourInstance;
    }

    private PresenterManager() {
        categoryPagerPresenter = new CategoryPagerPresenterImpl();
        homePresenter = new HomePresenterImpl();
        ticketPresenter = new TicketPresenterImpl();
        recommendPagePresenter = new RecommendPagePresenterImpl();
    }

    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return categoryPagerPresenter;
    }

    public IHomePresenter getHomePresenter() {
        return homePresenter;
    }

    public ITicketPresenter getTicketPresenter() {
        return ticketPresenter;
    }

    public IRecommendPagePresenter getRecommendPagePresenter() {
        return recommendPagePresenter;
    }
}
