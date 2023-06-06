package com.example.thenewsbay.feature_news.db.saved_article;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.thenewsbay.feature_news.models.room.saved_article.Article;
import com.example.thenewsbay.feature_news.models.room.saved_article.ArticleWithReaders;
import com.example.thenewsbay.feature_news.models.room.saved_article.Reader;
import com.example.thenewsbay.feature_news.models.room.saved_article.ReaderArticleCrossRef;
import com.example.thenewsbay.feature_news.models.room.saved_article.ReaderWithArticles;

import java.util.List;

@Dao
public interface SavedArticleDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertReader(Reader reader);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(Article article);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReaderArticleCrossRef(ReaderArticleCrossRef crossRef);

    @Transaction
    @Query("SELECT * FROM readers WHERE email = :email and password = :password")
    Reader getReader(String email, String password);

    @Transaction
    @Query("SELECT * FROM readers WHERE email = :email")
    List<ReaderWithArticles> getReaderWithArticles(String email);

    @Transaction
    @Query("SELECT * FROM articles WHERE url = :url")
    List<ArticleWithReaders> getArticleWithReaders(String url);

    @Transaction
    @Query("SELECT * FROM readers")
    List<Reader> getAllReaders();

    @Transaction
    @Query("SELECT * FROM articles")
    List<Article> getAllSavedArticles();

    @Delete
    void deleteSavedArticle(Article article);

    @Delete
    void deleteReaderArticleCrossRdf(ReaderArticleCrossRef crossRef);
}
