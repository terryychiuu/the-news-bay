package com.example.thenewsbay.feature_news.models.room.saved_article;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ArticleWithReaders {
    @Embedded public Article article;
    @Relation(
            parentColumn = "url",
            entityColumn = "email",
            associateBy = @Junction(ReaderArticleCrossRef.class)
    )
    public List<Reader> readers;
}
