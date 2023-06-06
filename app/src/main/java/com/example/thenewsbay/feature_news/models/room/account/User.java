package com.example.thenewsbay.feature_news.models.room.account;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String email;
    public String name;
    public String country;
    public String password;

    public User(@NonNull String email, String name, String country, String password) {
        this.email = email;
        this.name = name;
        this.country = country;
        this.password = password;
    }
}
