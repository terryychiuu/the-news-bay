package com.example.thenewsbay.feature_news.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.thenewsbay.R;
import com.example.thenewsbay.feature_news.models.alert.AlertTopic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {

    Context context;
//    List<String> topics;
//    List<Boolean> isOn;
    List<AlertTopic> alertTopics;

    private WorkManager workManager;
    private List<WorkRequest> workRequest;

    public AlertAdapter(Context context,@NonNull List<AlertTopic> alertTopics) {
        this.context = context;
        this.alertTopics = alertTopics;
    }

    public static class AlertViewHolder extends RecyclerView.ViewHolder {
        SwitchCompat s;

        public AlertViewHolder(@NonNull View itemView) {
            super(itemView);

            s = itemView.findViewById(R.id.switch_alert);
        }
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_alert, parent, false);
        return new AlertAdapter.AlertViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        holder.s.setText(alertTopics.get(position).getTopic());
        holder.s.setChecked(alertTopics.get(position).getOn());

        final int i = position;
        holder.s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                alertTopics.get(i).setOn(compoundButton.isChecked());
                saveAlert((ArrayList<AlertTopic>) alertTopics);
                if (compoundButton.isChecked()) {
                    workManager.enqueue(workRequest.get(i));
                    Toasty.info(context, alertTopics.get(i).getTopic() + " notification is set! ", Toast.LENGTH_SHORT).show();
//                    Log.d("Worker",workRequest.get(i).getId().toString());
                }
                else {
//                    workManager.cancelAllWork();
                    workManager.cancelAllWorkByTag(alertTopics.get(i).getTopic());

//                    workManager.cancelWorkById(workRequest.get(i).getId());
                    Toasty.info(context, alertTopics.get(i).getTopic() + " notification is cancel! ", Toast.LENGTH_SHORT).show();
//                    Log.d("Worker",workRequest.get(i).getId().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alertTopics.size();
    }

    public void saveAlert(ArrayList<AlertTopic> alertTopics) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("alert", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alertTopics);
        editor.putString("alertTopics", json);
        editor.apply();
    };

    public ArrayList<AlertTopic> loadAlert() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("alert", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("alertTopics", null);
        Type type = new TypeToken<ArrayList<AlertTopic>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void setWorkManager(WorkManager workManager) {
        this.workManager = workManager;
    }

    public void setWorkRequest(List<WorkRequest> workRequest) {
        this.workRequest = workRequest;
    }
}
