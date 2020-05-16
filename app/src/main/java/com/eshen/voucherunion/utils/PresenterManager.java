package com.eshen.voucherunion.utils;

import com.eshen.voucherunion.presenter.ICategoryPagePresenter;
import com.eshen.voucherunion.presenter.IHomePresenter;
import com.eshen.voucherunion.presenter.IOnSellPagePresenter;
import com.eshen.voucherunion.presenter.IRecommendPagePresenter;
import com.eshen.voucherunion.presenter.ISearchPagePresenter;
import com.eshen.voucherunion.presenter.ITicketPagePresenter;
import com.eshen.voucherunion.presenter.impl.CategoryPagePresenterImpl;
import com.eshen.voucherunion.presenter.impl.HomePresenterImpl;
import com.eshen.voucherunion.presenter.impl.OnSellPagePresenterImpl;
import com.eshen.voucherunion.presenter.impl.RecommendPagePresenterImpl;
import com.eshen.voucherunion.presenter.impl.SearchPagePresenterImpl;
import com.eshen.voucherunion.presenter.impl.TicketPagePresenterImpl;

/**
 * Created by Sin on 2020/5/4
 */
public class PresenterManager {
    private static final PresenterManager ourInstance = new PresenterManager();

    private final ICategoryPagePresenter categoryPagePresenter;
    private final IHomePresenter homePresenter;
    private final ITicketPagePresenter ticketPagePresenter;
    private final IRecommendPagePresenter recommendPagePresenter;
    private final IOnSellPagePresenter onSellPagePresenter;
    private final ISearchPagePresenter searchPagePresenter;

    public static PresenterManager getInstance() {
        return ourInstance;
    }

    private PresenterManager() {
        categoryPagePresenter = new CategoryPagePresenterImpl();
        homePresenter = new HomePresenterImpl();
        ticketPagePresenter = new TicketPagePresenterImpl();
        recommendPagePresenter = new RecommendPagePresenterImpl();
        onSellPagePresenter = new OnSellPagePresenterImpl();
        searchPagePresenter = new SearchPagePresenterImpl();
    }

    public ICategoryPagePresenter getCategoryPagePresenter() {
        return categoryPagePresenter;
    }

    public IHomePresenter getHomePresenter() {
        return homePresenter;
    }

    public ITicketPagePresenter getTicketPagePresenter() {
        return ticketPagePresenter;
    }

    public IRecommendPagePresenter getRecommendPagePresenter() {
        return recommendPagePresenter;
    }

    public IOnSellPagePresenter getOnSellPagePresenter() {
        return onSellPagePresenter;
    }

    public ISearchPagePresenter getSearchPagePresenter() {
        return searchPagePresenter;
    }
}
