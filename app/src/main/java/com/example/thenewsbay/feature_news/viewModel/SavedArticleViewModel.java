package com.example.thenewsbay.feature_news.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.thenewsbay.feature_news.models.room.saved_article.Article;
import com.example.thenewsbay.feature_news.models.room.saved_article.Reader;
import com.example.thenewsbay.feature_news.models.room.saved_article.ReaderArticleCrossRef;
import com.example.thenewsbay.feature_news.models.room.saved_article.ReaderWithArticles;
import com.example.thenewsbay.feature_news.repository.SavedArticleRepository;

import java.util.List;

public class SavedArticleViewModel extends AndroidViewModel {
    private SavedArticleRepository savedArticleRepository;

    public SavedArticleViewModel(@NonNull Application application) {
        super(application);

        savedArticleRepository = new SavedArticleRepository(application);
    }

    public boolean insertReader(Reader reader) {
        return savedArticleRepository.insertReader(reader);
    }

    public void insertArticle(Article article) {
        savedArticleRepository.insertArticle(article);
    }

    public void insertReaderArticleCrossRdf(String email, String url) {
        ReaderArticleCrossRef crossRef = new ReaderArticleCrossRef(email, url);
        savedArticleRepository.insertReaderArticleCrossRdf(crossRef);
    }

    public Reader getReader(String email, String password) {
        return savedArticleRepository.getReader(email, password);
    }

//    public LiveData<List<Reader>> getAllReader() {
//        return savedArticleRepository.getAllReader();
//    }

    public List<Article> getAllSavedArticles() {
        return savedArticleRepository.getAllSavedArticles();
    }

    public List<ReaderWithArticles> getReaderWithArticles(String email) {
        return savedArticleRepository.getReaderWithArticles(email);
    }

    public void deleteSavedArticle(Article article) {
        savedArticleRepository.deleteSavedArticle(article);
    }

    public void deleteReaderArticleCrossRdf(String email, String url) {
        ReaderArticleCrossRef crossRef = new ReaderArticleCrossRef(email, url);
        savedArticleRepository.deleteReaderArticleCrossRdf(crossRef);
    }
}
