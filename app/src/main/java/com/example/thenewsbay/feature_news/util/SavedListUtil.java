package com.example.thenewsbay.feature_news.util;

import com.example.thenewsbay.feature_news.models.room.account.Newspaper;

import java.util.Comparator;
import java.util.List;

public class SavedListUtil {
    public static void sortNewspapersByPublishedAt(List<Newspaper> newspapers) {
        Comparator<Newspaper> byPublishedAt = Comparator.comparing(Newspaper::getPublishedAt);
        newspapers.sort(byPublishedAt);
    }
}
