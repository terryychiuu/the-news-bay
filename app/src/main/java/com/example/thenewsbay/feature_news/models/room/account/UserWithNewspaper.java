package com.example.thenewsbay.feature_news.models.room.account;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithNewspaper {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "email",
            entityColumn = "url"
    )
    public List<Newspaper> newspapers;
}
