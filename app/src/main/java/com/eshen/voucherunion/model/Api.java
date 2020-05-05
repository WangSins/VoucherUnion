package com.eshen.voucherunion.model;

import com.eshen.voucherunion.model.domain.Categories;
import com.eshen.voucherunion.model.domain.HomePagerContent;
import com.eshen.voucherunion.model.domain.RecommendContent;
import com.eshen.voucherunion.model.domain.RecommendPageCategory;
import com.eshen.voucherunion.model.domain.TicketParams;
import com.eshen.voucherunion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Sin on 2020/5/2
 */
public interface Api {
    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<RecommendPageCategory> getRecommendPageCategories();

    @GET()
    Call<RecommendContent> getRecommendPageContent(@Url String url);
}
