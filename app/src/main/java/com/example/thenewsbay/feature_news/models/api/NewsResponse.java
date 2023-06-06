package com.example.thenewsbay.feature_news.models.api;

import com.example.thenewsbay.feature_news.models.room.saved_article.Article;

import java.util.ArrayList;

public class NewsResponse {
    public String status;
    public int totalResults;
    public ArrayList<Article> articles;

    public NewsResponse(String status, int totalResults, ArrayList<Article> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }
}
