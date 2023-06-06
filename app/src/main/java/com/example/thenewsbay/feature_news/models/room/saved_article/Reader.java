package com.example.thenewsbay.feature_news.models.room.saved_article;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "readers")
public class Reader {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String email;
    public String name;
    public String country;
    public String password;

    public Reader(@NonNull String email, String name, String country, String password) {
        this.email = email;
        this.name = name;
        this.country = country;
        this.password = password;
    }
}
