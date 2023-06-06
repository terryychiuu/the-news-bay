package com.example.thenewsbay.feature_news.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String headlinesChannelId = "headlinesChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel headlinesChannel = new NotificationChannel(
                    headlinesChannelId,
                    "headlinesChannel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            headlinesChannel.setDescription("this is headlinesChannel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(headlinesChannel);
        }
    }
}
