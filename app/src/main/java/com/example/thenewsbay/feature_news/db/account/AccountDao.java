package com.example.thenewsbay.feature_news.db.account;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.thenewsbay.feature_news.models.room.account.Newspaper;
import com.example.thenewsbay.feature_news.models.room.account.User;
import com.example.thenewsbay.feature_news.models.room.account.UserWithNewspaper;

import java.util.List;

@Dao
public interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewspaper(Newspaper newspaper);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllNewspapers(List<Newspaper> newspapers);

    @Transaction
    @Query("SELECT * FROM user")
    List<User> getUserStatic();

    @Transaction
    @Query("SELECT * FROM user")
    LiveData<List<User>> getUser(); // size = 1

    @Transaction
    @Query("SELECT * FROM newspapers")
    LiveData<List<Newspaper>> getAllNewspapers();

    @Transaction
    @Query("SELECT * FROM newspapers")
    List<Newspaper> getAllNewspapersStatic();

    @Transaction
    @Query("SELECT * FROM user")
    LiveData<List<UserWithNewspaper>> getUserWithNewspapers();    // 1 to many

    @Query("DELETE FROM user")
    void deleteUser();  // size = 1

    @Delete
    void deleteNewspaper(Newspaper newspaper);

    @Query("DELETE FROM newspapers")
    void deleteAllNewspapers();
}
