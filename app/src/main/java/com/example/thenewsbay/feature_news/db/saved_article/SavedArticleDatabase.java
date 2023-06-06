package com.example.thenewsbay.feature_news.db.saved_article;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.thenewsbay.feature_news.models.room.saved_article.Article;
import com.example.thenewsbay.feature_news.models.room.saved_article.Reader;
import com.example.thenewsbay.feature_news.models.room.saved_article.ReaderArticleCrossRef;

import java.util.Date;

@Database(entities = {
        Reader.class,
        Article.class,
//        ReaderWithArticles.class,
//        ArticleWithReaders.class,
        ReaderArticleCrossRef.class
}, version = 2)
public abstract class SavedArticleDatabase extends RoomDatabase {

    private static volatile SavedArticleDatabase instance;

    public abstract SavedArticleDao getSavedArticleDao(); // Room subclasses our abs class

    public static synchronized SavedArticleDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    SavedArticleDatabase.class,
                    "saved_article_db")
                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback) // add test
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private SavedArticleDao savedArticleDao;

        private PopulateDBAsyncTask(SavedArticleDatabase db) {
            savedArticleDao = db.getSavedArticleDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            Reader reader = new Reader("admin@google.com", "admin", "hk","123");
//            savedArticleDao.insertReader(reader);
            return null;
        }
    }

}
