package com.example.thenewsbay.feature_news.models.room.saved_article;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ReaderWithArticles {
    @Embedded public Reader reader;
    @Relation(
            parentColumn = "email",
            entityColumn = "url",
            associateBy = @Junction(ReaderArticleCrossRef.class)
    )
    public List<Article> articles;
}
