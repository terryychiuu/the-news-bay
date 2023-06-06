package com.example.thenewsbay.feature_news.models.room.saved_article;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.thenewsbay.feature_news.models.converter.DateConverter;

import java.util.Date;

@Entity(tableName = "articles")
public class Article {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String url;;
//    @SerializedName("source")
//    private Source source;
    public String author;
    public String title;
//    @SerializedName("body")
    public String description;
    public String urlToImage;
    @TypeConverters({DateConverter.class})
    public Date publishedAt;

    public String content;

    public Article(@NonNull String url, String author, String title, String description, String urlToImage, Date publishedAt, String content) {
//        this.source = source;
        this.url = url;
        this.author = author;
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }


//    public Source getSource() {
//        return source;
//    }
//
//    public void setSource(Source source) {
//        this.source = source;
//    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return "Article{" +
                ", url='" + url + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
//                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt=" + publishedAt +
//                ", content='" + content + '\'' +
                '}';
    }
}

