package com.example.thenewsbay.feature_news.repository;

import static com.example.thenewsbay.feature_news.util.Constants.API_KEY;

import com.example.thenewsbay.feature_news.db.api.RetrofitInstance;
import com.example.thenewsbay.feature_news.models.api.NewsResponse;

import retrofit2.Call;

public class ArticleRepository {

    public Call<NewsResponse> getHeadlinesArticles(String countryCode, int pageSize, int pageNumber) {
        return RetrofitInstance.getApi().getHeadlinesArticles(countryCode, pageSize, pageNumber, API_KEY);
    }

    public Call<NewsResponse> getCategoryArticles(String countryCode, String category, int pageSize, int pageNumber) {
        return RetrofitInstance.getApi().getCategoryArticles(countryCode, category, pageSize, pageNumber, API_KEY);
    }

    public Call<NewsResponse> searchForArticles(String searchQuery, int pageNumber) {
        return RetrofitInstance.getApi().searchForArticles(searchQuery, 30, pageNumber, API_KEY);
    }
}
