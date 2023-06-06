package com.example.thenewsbay.feature_news.worker;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import static com.example.thenewsbay.feature_news.util.Constants.API_KEY;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.thenewsbay.R;
import com.example.thenewsbay.feature_news.app.App;
import com.example.thenewsbay.feature_news.db.api.RetrofitInstance;
import com.example.thenewsbay.feature_news.models.api.NewsResponse;
import com.example.thenewsbay.feature_news.models.room.saved_article.Article;
import com.example.thenewsbay.feature_news.ui.NewsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiWorker extends Worker {
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_CODE = 0;

    private static final String TAG = "ApiWorker";
    //"Headlines", "Technology", "Business", "Health", "Sports", "Entertainment"
    Context context;
    WorkerParameters workerParams;

    List<Article> articleArrayList = new ArrayList<>();

    public ApiWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.workerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        String task = getInputData().getString("task");
        String countryCode = getInputData().getString("countryCode");
        if (task == null) return Result.success();

        switch (task) {
            case "Headlines":
                test();
                getHeadlines(countryCode);
                break;
            case "Technology":
                test();
                getCategoryArticles(countryCode, "technology");
                break;
            case "Business":
                test();
                getCategoryArticles(countryCode, "business");
                break;
            case "Health":
                test();
                getCategoryArticles(countryCode, "health");
                break;
            case "Sports":
                test();
                getCategoryArticles(countryCode, "sports");
                break;
            case "Entertainment":
                test();
                getCategoryArticles(countryCode, "entertainment");
                break;
        }
        return Result.success();
    }

    private void sendNotification(String topic, String title) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, build and send the notification

            Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(getApplicationContext(), App.headlinesChannelId)
                    .setSmallIcon(R.drawable.ic_news)
                    .setContentTitle("The New Bay - " + topic)
                    .setContentText(title)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(pendingIntent)
                    .build();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.notify(NOTIFICATION_ID, notification);
        } else {
            // Permission is not granted, request the permission
//            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
        }
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.d("Worker","work cancel");
    }

    private void test() {
        Log.d("Worker","working");
    }

    private void getHeadlines(String countryCode) {
        RetrofitInstance.getApi().getHeadlinesArticles(countryCode, 1, 1, API_KEY).enqueue(new Callback<NewsResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getArticles() != null) {
                        articleArrayList.addAll(response.body().getArticles());
                        sendNotification("headlines", articleArrayList.get(0).getTitle());
                    }

                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {

            }
        });
    }

    private void getCategoryArticles(String countryCode, String category) {
        RetrofitInstance.getApi().getCategoryArticles(countryCode, category, 1, 1, API_KEY).enqueue(new Callback<NewsResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("FKKK" +response.body() );
                    if (response.body() != null && response.body().getArticles() != null) {
                        articleArrayList.addAll(response.body().getArticles());
                        sendNotification(category, articleArrayList.get(0).getTitle());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
