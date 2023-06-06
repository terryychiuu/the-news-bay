package com.example.thenewsbay.feature_news.db.account;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.thenewsbay.feature_news.models.room.account.Newspaper;
import com.example.thenewsbay.feature_news.models.room.account.User;

@Database(entities = {
        User.class,
        Newspaper.class
}, version = 1)
public abstract class AccountDatabase extends RoomDatabase {
    private static volatile AccountDatabase instance;

    public abstract AccountDao getAccountDao(); // Room subclasses our abs class

    public static synchronized AccountDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AccountDatabase.class,
                            "account_db")
                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback) // add test
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
