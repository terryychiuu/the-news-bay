package com.example.thenewsbay.feature_news.ui;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.thenewsbay.R;
import com.example.thenewsbay.feature_news.models.room.account.User;
import com.example.thenewsbay.feature_news.ui.adapter.PagerAdapter;
import com.example.thenewsbay.feature_news.ui.auth.LoginActivity;
import com.example.thenewsbay.feature_news.ui.auth.RegistrationActivity;
import com.example.thenewsbay.feature_news.ui.ui_nav.NavActivity;
import com.example.thenewsbay.feature_news.ui.ui_profile.ProfileActivity;
import com.example.thenewsbay.feature_news.viewModel.AccountViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final int REQUEST_CODE_LOGIN = 3;
    private static final int REQUEST_CODE_REGISTER = 4;

    private AccountViewModel accountViewModel;
    User user;

    TabLayout tabLayout;
    TabItem mHeadlines, mTechnology, mBusiness, mHealth, mSports, mEntertainment;
    PagerAdapter pagerAdapter;
    Toolbar mToolbar;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // notification
        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
        }

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getUser().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if(users.size() == 1)
                    user = users.get(0);
            }
        });

//        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        mToolbar = findViewById(R.id.news_toolbar);
        setSupportActionBar(mToolbar);
//        mToolbar.showOverflowMenu();

        fab = findViewById(R.id.fab_menu);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, NavActivity.class);
//            intent.putExtra("url", article.getUrl());
            this.startActivity(intent);
        });

        mHeadlines = findViewById(R.id.tab_headlines);
        mTechnology = findViewById(R.id.tab_technology);
        mBusiness = findViewById(R.id.tab_business);
        mHealth = findViewById(R.id.tab_health);
        mSports = findViewById(R.id.tab_sports);
        mEntertainment = findViewById(R.id.tab_entertainment);

        ViewPager viewPager = findViewById(R.id.view_pager_fragment_container);
        tabLayout = findViewById(R.id.tab_layout_include);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 6);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() >= 0 && tab.getPosition() <= 5) {
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, do something
                } else {
                    // Permission not granted, show a message to the user
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {
            if (isLoggedIn()) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(NewsActivity.this, LoginActivity.class);
                intent.putExtra("WantNavTo", "profile");
//                        this.startActivity(intent);
                this.startActivityForResult(intent, REQUEST_CODE_LOGIN);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK ) {
            if (requestCode == REQUEST_CODE_LOGIN) {
                assert data != null;
                if (data.getStringExtra("WantNavTo").equals("profile")) {
                    Intent intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                } else if (data.getStringExtra("WantNavTo").equals("news_for_save")) {

                }
            } else if (requestCode == REQUEST_CODE_REGISTER) {
                Intent intent = new Intent(NewsActivity.this, LoginActivity.class);
                assert data != null;
                if (data.getStringExtra("WantNavTo").equals("profile")) {
                    intent.putExtra("WantNavTo", "profile");
                } else if (data.getStringExtra("WantNavTo").equals("news_for_save")) {
                    intent.putExtra("WantNavTo", "news_for_save");
                }
                this.startActivityForResult(intent, REQUEST_CODE_LOGIN);
            }

        } else if (resultCode == RESULT_FIRST_USER) {
            if (requestCode == REQUEST_CODE_LOGIN) {
                Intent intent = new Intent(NewsActivity.this, RegistrationActivity.class);
                assert data != null;
                if (data.getStringExtra("WantNavTo").equals("profile")) {
                    intent.putExtra("WantNavTo", "profile");
                } else if (data.getStringExtra("WantNavTo").equals("news_for_save")) {
                    intent.putExtra("WantNavTo", "news_for_save");
                }
                this.startActivityForResult(intent, REQUEST_CODE_REGISTER);
            }
        }
    }

    private boolean isLoggedIn() {
        return user != null;
    }
}