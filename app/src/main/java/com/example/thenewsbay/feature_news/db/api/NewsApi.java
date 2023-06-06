package com.example.thenewsbay.feature_news.db.api;

import com.example.thenewsbay.feature_news.models.api.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("v2/top-headlines")
    Call<NewsResponse> getHeadlinesArticles(
            @Query("country") String countryCode,
            @Query("pageSize") int pageSize,
            @Query("page") int pageNumber,
            @Query("apiKey") String apiKey
    );

    @GET("v2/top-headlines")
    Call<NewsResponse> getCategoryArticles(
            @Query("country") String countryCode,
            @Query("category") String category,
            @Query("pageSize") int pageSize,
            @Query("page") int pageNumber,
            @Query("apiKey") String apiKey
    );

    @GET("v2/everything")
    Call<NewsResponse> searchForArticles(
            @Query("q") String searchQuery,
            @Query("pageSize") int pageSize,
            @Query("page") int pageNumber,
            @Query("apiKey") String apiKey
    );
}
