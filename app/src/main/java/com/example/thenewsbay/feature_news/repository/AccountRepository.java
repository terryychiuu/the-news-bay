package com.example.thenewsbay.feature_news.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.thenewsbay.feature_news.db.account.AccountDao;
import com.example.thenewsbay.feature_news.db.account.AccountDatabase;
import com.example.thenewsbay.feature_news.models.room.account.Newspaper;
import com.example.thenewsbay.feature_news.models.room.account.User;

import java.util.List;

public class AccountRepository {
    private AccountDao accountDao;

    private LiveData<List<User>> user;
    private LiveData<List<Newspaper>> allNewspapers;

    public AccountRepository(Application application) {
        AccountDatabase accountDatabase = AccountDatabase.getInstance(application);
        accountDao = accountDatabase.getAccountDao();
        user = accountDao.getUser();
        allNewspapers = accountDao.getAllNewspapers();
    }

    public void insertUser(User user) {
        accountDao.insertUser(user);
    }

    public void insertNewspaper(Newspaper newspaper) {
        accountDao.insertNewspaper(newspaper);
    }

    public void insertAllNewspapers(List<Newspaper> newspapers) {
        accountDao.insertAllNewspapers(newspapers);
    }

    public List<User> getUserStatic() {
        return accountDao.getUserStatic();
    }

    public LiveData<List<User>> getUser() {
        return user;
    }

    public LiveData<List<Newspaper>> getAllNewspapers() {
        return allNewspapers;
    }

    public List<Newspaper> getAllNewspapersStatic() {
        return accountDao.getAllNewspapersStatic();
    }

    public void deleteUser() {
        accountDao.deleteUser();
    }

    public void deleteNewspaper(Newspaper newspaper) {
        accountDao.deleteNewspaper(newspaper);
    }

    public void deleteAllNewspapers() {
        accountDao.deleteAllNewspapers();
    }

}
