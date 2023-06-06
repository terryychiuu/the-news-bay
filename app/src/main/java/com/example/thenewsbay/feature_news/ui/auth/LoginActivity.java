package com.example.thenewsbay.feature_news.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thenewsbay.R;
import com.example.thenewsbay.feature_news.models.alert.AlertTopic;
import com.example.thenewsbay.feature_news.models.room.account.Newspaper;
import com.example.thenewsbay.feature_news.models.room.account.User;
import com.example.thenewsbay.feature_news.models.room.saved_article.Reader;
import com.example.thenewsbay.feature_news.models.room.saved_article.ReaderWithArticles;
import com.example.thenewsbay.feature_news.viewModel.AccountViewModel;
import com.example.thenewsbay.feature_news.viewModel.SavedArticleViewModel;
import com.example.thenewsbay.feature_news.worker.ApiWorker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    AccountViewModel accountViewModel;
    SavedArticleViewModel savedArticleViewModel;
    Reader reader;

    List<String> topics = new ArrayList<>(Arrays.asList("Headlines", "Technology", "Business", "Health", "Sports", "Entertainment"));
    List<Boolean> isOn = new ArrayList<>(Arrays.asList(false, false, false, false, false, false));
    ArrayList<AlertTopic> alertTopics;

    EditText editTextEmail, editTextPassword;
    Button btnLogin;
    TextView textViewRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        alertTopics = loadAlert();
        if (alertTopics == null) {
            alertTopics = new ArrayList<>();
            for(int i=0; i<topics.size(); i++){
                AlertTopic alertTopic = new AlertTopic(topics.get(i), isOn.get(i));
                alertTopics.add(alertTopic);
            }
            saveAlert(alertTopics);
        }

        savedArticleViewModel = new ViewModelProvider(LoginActivity.this).get(SavedArticleViewModel.class);

        editTextEmail = findViewById(R.id.et_login_email);
        editTextPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        textViewRegistration = findViewById(R.id.tv_goto_registration);

        Intent intent = getIntent();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

//                accountViewModel = new ViewModelProvider(RegistrationActivity.this).get(AccountViewModel.class);
//                User user = new User(reader.email, reader.name, reader.country, reader.password);
//                accountViewModel.insertUser(user);

                Reader reader = savedArticleViewModel.getReader(email, password);
                LoginActivity.this.reader = reader;

                if (reader != null) {
                    Toasty.success(v.getContext(), "Login successfully!", Toast.LENGTH_SHORT).show();

                    User user = new User(reader.email, reader.name, reader.country, reader.password);
                    accountViewModel = new ViewModelProvider(LoginActivity.this).get(AccountViewModel.class);
                    accountViewModel.insertUser(user);

                    List<ReaderWithArticles> readerWithArticlesList = savedArticleViewModel.getReaderWithArticles(reader.email);
                    Optional<ReaderWithArticles> optionalReaderWithArticles = readerWithArticlesList.stream()
                            .filter(readerWithArticle -> readerWithArticle.reader.email.equals(reader.email))
                            .findFirst();
                    if (optionalReaderWithArticles.isPresent()) {
                        ReaderWithArticles readerWithArticles = optionalReaderWithArticles.get();
                        List<Newspaper> newspapers = readerWithArticles.articles.stream()
                                        .map(article -> new Newspaper(article.url, article.author, article.title, article.description, article.urlToImage, article.publishedAt, article.content))
                                        .collect(Collectors.toList());
                        accountViewModel.insertAllNewspapers(newspapers);
                    } else {
                        Toasty.error(v.getContext(), "Something wrong in db!", Toast.LENGTH_SHORT).show();
                    }

                    // Cancel the previously enqueued worker and recreate
                    WorkManager workManager = WorkManager.getInstance(LoginActivity.this);
                    workManager.cancelAllWork();

                    PeriodicWorkRequest worker;
                    int i = 0;
                    for (AlertTopic topic : alertTopics) {
                        if (!topic.getOn()) continue;
                        worker = new PeriodicWorkRequest.Builder(ApiWorker.class, 15+i, TimeUnit.MINUTES)
                                .setInputData(new Data.Builder()
                                        .putString("task", topic.getTopic())
                                        .putString("countryCode", reader.country)
                                        .build())
                                .addTag(topic.getTopic())
                                .build();
                        workManager.enqueue(worker);
                        i++;
                    }


                    Intent intent = getIntent();
                    String WantNavTo = intent.getStringExtra("WantNavTo");
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("WantNavTo", WantNavTo);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }else{
                    Toasty.error(v.getContext(), "Unregistered user, or incorrect!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        textViewRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String WantNavTo = intent.getStringExtra("WantNavTo");
                Intent resultIntent = new Intent();
                resultIntent.putExtra("WantNavTo", WantNavTo);
                setResult(RESULT_FIRST_USER, resultIntent);
                finish();
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