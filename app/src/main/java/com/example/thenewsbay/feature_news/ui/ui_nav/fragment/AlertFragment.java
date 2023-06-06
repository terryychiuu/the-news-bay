package com.example.thenewsbay.feature_news.ui.ui_nav.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.thenewsbay.R;
import com.example.thenewsbay.feature_news.models.alert.AlertTopic;
import com.example.thenewsbay.feature_news.ui.adapter.AlertAdapter;
import com.example.thenewsbay.feature_news.viewModel.AccountViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.thenewsbay.feature_news.worker.ApiWorker;

public class AlertFragment extends Fragment {
    private static final String TAG = "apiWorker";

    AccountViewModel accountViewModel;
    String countryCode;

    List<String> topics = new ArrayList<>(Arrays.asList("Headlines", "Technology", "Business", "Health", "Sports", "Entertainment"));
    List<Boolean> isOn = new ArrayList<>(Arrays.asList(false, false, false, false, false, false));
    ArrayList<AlertTopic> alertTopics;

    RecyclerView recyclerView;
    AlertAdapter alertAdapter;

    private WorkManager workManager;
    private List<WorkRequest> workRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false);

        alertTopics = loadAlert();
        if (alertTopics == null) {
            alertTopics = new ArrayList<>();
            for(int i=0; i<topics.size(); i++){
                AlertTopic alertTopic = new AlertTopic(topics.get(i), isOn.get(i));
                alertTopics.add(alertTopic);
            }
            saveAlert(alertTopics);
        }


        recyclerView = view.findViewById(R.id.recyclerview_alert);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        alertAdapter = new AlertAdapter(getContext(), alertTopics);
        recyclerView.setAdapter(alertAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        countryCode = accountViewModel.getUserStatic().size()>0 ? accountViewModel.getUserStatic().get(0).country : "us";

        workManager = WorkManager.getInstance(requireContext());
        alertAdapter.setWorkManager(workManager);

        workRequest = new ArrayList<>();
        for(int i=0; i<alertTopics.size(); i++) {
            workRequest.add(i, new PeriodicWorkRequest.Builder(ApiWorker.class, 15, TimeUnit.MINUTES)
                    .setInputData(new Data.Builder()
                            .putString("task", alertTopics.get(i).getTopic())
                            .putString("countryCode", countryCode)
                            .build())
                    .addTag(alertTopics.get(i).getTopic())
                    .build());
        }
        alertAdapter.setWorkRequest(workRequest);
    }

    public void saveAlert(ArrayList<AlertTopic> alertTopics) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("alert", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alertTopics);
        editor.putString("alertTopics", json);
        editor.apply();
    };

    public ArrayList<AlertTopic> loadAlert() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("alert", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("alertTopics", null);
        Type type = new TypeToken<ArrayList<AlertTopic>>(){}.getType();
        return gson.fromJson(json, type);
    }
}