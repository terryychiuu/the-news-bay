package com.example.thenewsbay.feature_news.ui.ui_nav.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thenewsbay.R;
import com.example.thenewsbay.feature_news.models.room.account.Newspaper;
import com.example.thenewsbay.feature_news.models.room.account.User;
import com.example.thenewsbay.feature_news.ui.adapter.ArticleAdapter;
import com.example.thenewsbay.feature_news.ui.adapter.NewspaperAdapter;
import com.example.thenewsbay.feature_news.util.SavedListUtil;
import com.example.thenewsbay.feature_news.viewModel.AccountViewModel;
import com.example.thenewsbay.feature_news.viewModel.SavedArticleViewModel;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {
    private AccountViewModel accountViewModel;
    private SavedArticleViewModel savedArticleViewModel;

    TextView textView;

    RecyclerView savedRecyclerView;
    NewspaperAdapter newspaperAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        textView = view.findViewById(R.id.tv_saved);

        savedRecyclerView = view.findViewById(R.id.recyclerview_saved);
        savedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        savedRecyclerView.setHasFixedSize(true);

        newspaperAdapter = new NewspaperAdapter(getContext());
        savedRecyclerView.setAdapter(newspaperAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                String savedPageTitle = users.get(0).name + ", " + textView.getText().toString();
                textView.setText(savedPageTitle);
            }
        });

        accountViewModel.getAllNewspapers().observe(getViewLifecycleOwner(), new Observer<List<Newspaper>>() {
            @Override
            public void onChanged(List<Newspaper> newspapers) {
                if (newspapers != null) {
                    SavedListUtil.sortNewspapersByPublishedAt(newspapers);
                    newspaperAdapter.setNewspaperArrayList(newspapers);
                }
            }
        });

        newspaperAdapter.setAccountViewModel(accountViewModel);

        savedArticleViewModel = new ViewModelProvider(this).get(SavedArticleViewModel.class);
        newspaperAdapter.setSavedArticleViewModel(savedArticleViewModel);

    }

}