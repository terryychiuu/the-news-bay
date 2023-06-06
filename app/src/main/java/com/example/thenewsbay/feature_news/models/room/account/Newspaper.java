package com.example.thenewsbay.feature_news.models.room.account;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.thenewsbay.feature_news.models.converter.DateConverter;

import java.util.Date;

@Entity(tableName = "newspapers")
public class Newspaper {
    @PrimaryKey
    @NonNull
    public String url;
    public String author;
    public String title;
    public String description;
    public String urlToImage;
    @TypeConverters({DateConverter.class})
    public Date publishedAt;

    public String content;

    public Newspaper(@NonNull String url, String author, String title, String description, String urlToImage, Date publishedAt, String content) {
//        this.source = source;
        this.url = url;
        this.author = author;
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public String getContent() {
        return content;
    }
}
