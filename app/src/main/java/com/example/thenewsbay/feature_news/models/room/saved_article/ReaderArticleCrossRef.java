package com.example.thenewsbay.feature_news.models.room.saved_article;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"email", "url"})
public class ReaderArticleCrossRef {
    @NonNull
    public String email;
    @NonNull
    public String url;

    public ReaderArticleCrossRef(@NonNull String email, @NonNull String url) {
        this.email = email;
        this.url = url;
    }
}
