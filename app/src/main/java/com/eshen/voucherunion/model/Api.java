package com.eshen.voucherunion.model;

import com.eshen.voucherunion.model.domain.Categories;
import com.eshen.voucherunion.model.domain.HomePagerContent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Sin on 2020/5/2
 */
public interface Api {
    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);
}
