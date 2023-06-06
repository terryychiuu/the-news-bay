package com.example.thenewsbay.feature_news.ui.ui_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thenewsbay.R;
import com.example.thenewsbay.feature_news.models.alert.AlertTopic;
import com.example.thenewsbay.feature_news.models.room.account.User;
import com.example.thenewsbay.feature_news.ui.NewsActivity;
import com.example.thenewsbay.feature_news.viewModel.AccountViewModel;
import com.example.thenewsbay.feature_news.worker.ApiWorker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {
    AccountViewModel accountViewModel;

    List<String> topics = new ArrayList<>(Arrays.asList("Headlines", "Technology", "Business", "Health", "Sports", "Entertainment"));
    List<Boolean> isOn = new ArrayList<>(Arrays.asList(false, false, false, false, false, false));
    ArrayList<AlertTopic> alertTopics;

    ImageView iv_back, iv_image_profile;
    TextView tv_name;
    TextInputEditText ti_name, ti_email, ti_country;
    Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        alertTopics = loadAlert();
        if (alertTopics == null) {
            alertTopics = new ArrayList<>();
            for(int i=0; i<topics.size(); i++){
                AlertTopic alertTopic = new AlertTopic(topics.get(i), isOn.get(i));
                alertTopics.add(alertTopic);
            }
            saveAlert(alertTopics);
        }

        iv_back = findViewById(R.id.iv_back);
        iv_image_profile = findViewById(R.id.iv_image_profile);
        tv_name = findViewById(R.id.tv_name);
        ti_name = findViewById(R.id.ti_name);
        ti_email = findViewById(R.id.ti_email);
        ti_country = findViewById(R.id.ti_country);
        btn_logout = findViewById(R.id.btn_logout);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getUser().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if(users.size() == 1) {
                    User user = users.get(0);
                    tv_name.setText(user.name);
                    ti_name.setText(user.name);
                    ti_email.setText(user.email);
                    ti_country.setText(user.country);
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, NewsActivity.class);
                ProfileActivity.this.startActivity(intent);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountViewModel.deleteUser();
                accountViewModel.deleteAllNewspapers();

                // Cancel the previously enqueued worker and recreate
                WorkManager workManager = WorkManager.getInstance(ProfileActivity.this);
                workManager.cancelAllWork();

                PeriodicWorkRequest worker;
                int i = 0;
                for (AlertTopic topic : alertTopics) {
                    if (!topic.getOn()) continue;
                    worker = new PeriodicWorkRequest.Builder(ApiWorker.class, 15+i, TimeUnit.MINUTES)
                            .setInputData(new Data.Builder()
                                    .putString("task", topic.getTopic())
                                    .putString("countryCode", "us")
                                    .build())
                            .addTag(topic.getTopic())
                            .build();
                    workManager.enqueue(worker);
                    i++;
                }


                Toasty.success(view.getContext(), "Logout!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ProfileActivity.this, NewsActivity.class);
                ProfileActivity.this.startActivity(intent);
            }
        });
    }

    public void saveAlert(ArrayList<AlertTopic> alertTopics) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("alert", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alertTopics);
        editor.putString("alertTopics", json);
        editor.apply();
    };

    public ArrayList<AlertTopic> loadAlert() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("alert", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("alertTopics", null);
        Type type = new TypeToken<ArrayList<AlertTopic>>(){}.getType();
        return gson.fromJson(json, type);
    }
}