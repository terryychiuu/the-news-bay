package com.example.thenewsbay.feature_news.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thenewsbay.R;
import com.example.thenewsbay.feature_news.models.room.saved_article.Article;
import com.example.thenewsbay.feature_news.ui.adapter.ArticleAdapter;
import com.example.thenewsbay.feature_news.viewModel.AccountViewModel;
import com.example.thenewsbay.feature_news.viewModel.ArticleViewModel;
import com.example.thenewsbay.feature_news.viewModel.SavedArticleViewModel;

import java.util.List;

public class BusinessFragment extends Fragment {
    ArticleViewModel mArticleViewModel;
    AccountViewModel accountViewModel;
    SavedArticleViewModel savedArticleViewModel;

    String countryCode;
    String category = "business";
    int pageSize = 20;
    int pageNumber = 1;

    private BroadcastReceiver networkReceiver;
    private AlertDialog networkErrorDialog;

    RecyclerView businessRecyclerView;
    ArticleAdapter articleAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business, null);

        businessRecyclerView = view.findViewById(R.id.recyclerview_business);
        businessRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        articleAdapter = new ArticleAdapter(getContext());
        businessRecyclerView.setAdapter(articleAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        countryCode = accountViewModel.getUserStatic().size()>0 ? accountViewModel.getUserStatic().get(0).country : "us";

        mArticleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        mArticleViewModel.getCategoryArticles(countryCode, category, pageSize, pageNumber);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        articleAdapter.setAccountViewModel(accountViewModel);

        mArticleViewModel.getArticles().observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (articles != null) {
                    articleAdapter.setArticleArrayList(articles);
                }

            }
        });
        articleAdapter.setAccountViewModel(accountViewModel);

        savedArticleViewModel = new ViewModelProvider(this).get(SavedArticleViewModel.class);
        articleAdapter.setSavedArticleViewModel(savedArticleViewModel);

        // network
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                    ConnectivityManager connectivityManager =
                            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        // Dismiss the dialog and refresh the data
                        if (networkErrorDialog != null && networkErrorDialog.isShowing()) {
                            networkErrorDialog.dismiss();
                            networkErrorDialog = null;
                            // Refresh the data
                            if (mArticleViewModel != null) {
                                mArticleViewModel.getCategoryArticles(countryCode, category, pageSize, pageNumber);
                            }
                        }
                    } else {
                        // Show the dialog if not already shown
                        showNetworkErrorDialog();
                    }
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(networkReceiver);
    }

    private void showNetworkErrorDialog() {
        if (networkErrorDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Network Error");
            builder.setMessage("You are not connected to the internet. Please check your network settings.");
            builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", null);
            networkErrorDialog = builder.create();
            networkErrorDialog.show();
        }
    }
}
