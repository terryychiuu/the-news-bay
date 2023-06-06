package com.example.thenewsbay.feature_news.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.thenewsbay.feature_news.db.saved_article.SavedArticleDao;
import com.example.thenewsbay.feature_news.db.saved_article.SavedArticleDatabase;
import com.example.thenewsbay.feature_news.models.room.saved_article.Article;
import com.example.thenewsbay.feature_news.models.room.saved_article.Reader;
import com.example.thenewsbay.feature_news.models.room.saved_article.ReaderArticleCrossRef;
import com.example.thenewsbay.feature_news.models.room.saved_article.ReaderWithArticles;

import java.util.List;

public class SavedArticleRepository {
    private SavedArticleDao savedArticleDao;

    public SavedArticleRepository(Application application) {
        SavedArticleDatabase savedArticleDatabase = SavedArticleDatabase.getInstance(application);
        savedArticleDao = savedArticleDatabase.getSavedArticleDao();
    }

    public boolean insertReader(Reader reader) {
        try {
            savedArticleDao.insertReader(reader);
//            new InsertReaderAsyncTask(savedArticleDao).execute(reader);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void insertArticle(Article article) {
        savedArticleDao.insertArticle(article);
//        new InsertArticleAsyncTask(savedArticleDao).execute(article);
    }

    public void insertReaderArticleCrossRdf(ReaderArticleCrossRef crossRef) {
        savedArticleDao.insertReaderArticleCrossRef(crossRef);
    }

    public Reader getReader(String email, String password) {
        return savedArticleDao.getReader(email, password);
    }

    public List<Reader> getAllReader() {
        return savedArticleDao.getAllReaders();
    }

    // livedata auto do AsyncTask for you
    public List<Article> getAllSavedArticles() {
        return savedArticleDao.getAllSavedArticles();
    }

    public List<ReaderWithArticles> getReaderWithArticles(String email) {
        return savedArticleDao.getReaderWithArticles(email);
    }

    public void deleteSavedArticle(Article article) {
        savedArticleDao.deleteSavedArticle(article);
//        new DeleteArticleAsyncTask(savedArticleDao).execute(article);
    }

    public void deleteReaderArticleCrossRdf(ReaderArticleCrossRef crossRef) {
        savedArticleDao.deleteReaderArticleCrossRdf(crossRef);
    }


    /**
     * AsyncTask
     */
    private static class InsertReaderAsyncTask extends AsyncTask<Reader, Void, Void> {
        private SavedArticleDao savedArticleDao;

        private InsertReaderAsyncTask(SavedArticleDao savedArticleDao) {
            this.savedArticleDao = savedArticleDao;
        }

        @Override
        protected Void doInBackground(Reader... readers) {
            savedArticleDao.insertReader(readers[0]);
            return null;
        }
    }

    private static class InsertArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private SavedArticleDao savedArticleDao;

        private InsertArticleAsyncTask(SavedArticleDao savedArticleDao) {
            this.savedArticleDao = savedArticleDao;
        }
        @Override
        protected Void doInBackground(Article... articles) {
            savedArticleDao.insertArticle(articles[0]);
            return null;
        }
    }

    private static class DeleteArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private SavedArticleDao savedArticleDao;

        private DeleteArticleAsyncTask(SavedArticleDao savedArticleDao) {
            this.savedArticleDao = savedArticleDao;
        }
        @Override
        protected Void doInBackground(Article... articles) {
            savedArticleDao.deleteSavedArticle(articles[0]);
            return null;
        }
    }
}
