package com.example.thenewsbay.feature_news.viewModel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.thenewsbay.feature_news.models.room.saved_article.Article;
import com.example.thenewsbay.feature_news.models.api.NewsResponse;
import com.example.thenewsbay.feature_news.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleViewModel extends AndroidViewModel {
    private ArticleRepository articleRepository;

    private MutableLiveData<List<Article>> articles = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<String> result = new MutableLiveData<>();

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        articleRepository = new ArticleRepository();
    }

    public LiveData<List<Article>> getArticles() {
        return articles;
    }

    public LiveData<String> getResult() {
        return result;
    }

    public void getHeadlinesNews(String countryCode, int pageSize, int pageNumber) {
        articleRepository.getHeadlinesArticles(countryCode, pageSize, pageNumber).enqueue(new Callback<NewsResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (!response.isSuccessful()) {
                    result.setValue("Code: " + response.code());
                    return;
                }
                result.setValue("OK");


                ArrayList<Article> preArticles = (ArrayList<Article>) articles.getValue();
                assert response.body() != null;
                assert preArticles != null;
                preArticles.addAll(response.body().getArticles());

                Set<String> seenIds = new HashSet<>();
                ArrayList<Article> newArticles = (ArrayList<Article>) preArticles
                        .stream()
                        .filter(article -> seenIds.add(article.getUrl()))
                        .collect(Collectors.toList());

//                Log.d("NEW", String.valueOf(newArticles.size()));
//                for (Article article : newArticles) {
//                    Log.d("new", article.toString());
//                }
                articles.setValue(newArticles);
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                result.setValue(t.getMessage());
            }
        });
    }

    public void getCategoryArticles(String countryCode, String category, int pageSize, int pageNumber) {
        articleRepository.getCategoryArticles(countryCode, category, pageSize, pageNumber).enqueue(new Callback<NewsResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (!response.isSuccessful()) {
                    result.setValue("Code: " + response.code());
                    return;
                }
                result.setValue("OK");
                assert response.body() != null;

                ArrayList<Article> preArticles = (ArrayList<Article>) articles.getValue();
                assert response.body() != null;
                assert preArticles != null;
                preArticles.addAll(response.body().getArticles());

                Set<String> seenIds = new HashSet<>();
                ArrayList<Article> newArticles = (ArrayList<Article>) preArticles
                        .stream()
                        .filter(article -> seenIds.add(article.getUrl()))
                        .collect(Collectors.toList());

//                Log.d("NEW", String.valueOf(newArticles.size()));
//                for (Article article : newArticles) {
//                    Log.d("new", article.toString());
//                }
                articles.setValue(newArticles);
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                result.setValue(t.getMessage());
            }
        });
    }

    public void searchForArticles(String searchQuery,int pageNumber) {
        articleRepository.searchForArticles(searchQuery, pageNumber).enqueue(new Callback<NewsResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (!response.isSuccessful()) {
                    result.setValue("Code: " + response.code());
                    return;
                }
                result.setValue("OK");
                assert response.body() != null;

                ArrayList<Article> preArticles = (ArrayList<Article>) articles.getValue();
                assert response.body() != null;
                assert preArticles != null;
                preArticles.addAll(response.body().getArticles());

                Set<String> seenIds = new HashSet<>();
                ArrayList<Article> newArticles = (ArrayList<Article>) preArticles
                        .stream()
                        .filter(article -> seenIds.add(article.getUrl()))
                        .collect(Collectors.toList());

//                Log.d("NEW", String.valueOf(newArticles.size()));
//                for (Article article : newArticles) {
//                    Log.d("new", article.toString());
//                }
                articles.setValue(newArticles);
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                result.setValue(t.getMessage());
            }
        });
    }


}
