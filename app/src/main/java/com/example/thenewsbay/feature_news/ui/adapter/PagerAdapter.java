package com.example.thenewsbay.feature_news.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.thenewsbay.feature_news.ui.fragment.BusinessFragment;
import com.example.thenewsbay.feature_news.ui.fragment.EntertainmentFragment;
import com.example.thenewsbay.feature_news.ui.fragment.HeadlinesFragment;
import com.example.thenewsbay.feature_news.ui.fragment.HealthFragment;
import com.example.thenewsbay.feature_news.ui.fragment.SportsFragment;
import com.example.thenewsbay.feature_news.ui.fragment.TechnologyFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    int tabCount;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HeadlinesFragment();
            case 1:
                return new TechnologyFragment();
            case 2:
                return new BusinessFragment();
            case 3:
                return new HealthFragment();
            case 4:
                return new SportsFragment();
            case 5:
                return new EntertainmentFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
