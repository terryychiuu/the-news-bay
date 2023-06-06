package com.example.thenewsbay.feature_news.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.thenewsbay.feature_news.models.room.account.Newspaper;
import com.example.thenewsbay.feature_news.models.room.account.User;
import com.example.thenewsbay.feature_news.repository.AccountRepository;

import java.util.List;

public class AccountViewModel extends AndroidViewModel{
    private AccountRepository accountRepository;

    private LiveData<List<User>> user;
    private LiveData<List<Newspaper>> allNewspapers;

    public AccountViewModel(@NonNull Application application) {
        super(application);

        accountRepository = new AccountRepository(application);

        user = accountRepository.getUser();
        allNewspapers = accountRepository.getAllNewspapers();
    }

    public void insertUser(User user) {
        accountRepository.insertUser(user);
    }

    public void insertNewspaper(Newspaper newspaper) {
        accountRepository.insertNewspaper(newspaper);
    }

    public void insertAllNewspapers(List<Newspaper> newspapers) {
        accountRepository.insertAllNewspapers(newspapers);
    }

    public List<User> getUserStatic() {
        return accountRepository.getUserStatic();
    }

    public LiveData<List<User>> getUser() {
        return user;
    }

    public LiveData<List<Newspaper>> getAllNewspapers() {
        return allNewspapers;
    }

    public List<Newspaper> getAllNewspapersStatic() {
        return accountRepository.getAllNewspapersStatic();
    }

    public void deleteUser() {
        accountRepository.deleteUser();
    }

    public void deleteNewspaper(Newspaper newspaper) {
        accountRepository.deleteNewspaper(newspaper);
    }

    public void deleteAllNewspapers() {
        accountRepository.deleteAllNewspapers();
    }

}
