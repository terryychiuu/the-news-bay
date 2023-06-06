package com.example.thenewsbay.feature_news.ui.ui_nav;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;

import com.example.thenewsbay.R;
import com.example.thenewsbay.feature_news.models.room.account.User;
import com.example.thenewsbay.feature_news.models.room.saved_article.Reader;
import com.example.thenewsbay.feature_news.ui.NewsActivity;
import com.example.thenewsbay.feature_news.ui.auth.LoginActivity;
import com.example.thenewsbay.feature_news.ui.auth.RegistrationActivity;
import com.example.thenewsbay.feature_news.ui.ui_nav.fragment.AlertFragment;
import com.example.thenewsbay.feature_news.ui.ui_nav.fragment.SavedFragment;
import com.example.thenewsbay.feature_news.ui.ui_nav.fragment.SearchFragment;
import com.example.thenewsbay.feature_news.viewModel.AccountViewModel;
import com.example.thenewsbay.feature_news.viewModel.SavedArticleViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class NavActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOGIN = 1;
    private static final int REQUEST_CODE_REGISTER = 2;

//    private BroadcastReceiver networkReceiver;
//    private AlertDialog networkErrorDialog;

    private AccountViewModel accountViewModel;
    private SavedArticleViewModel savedArticleViewModel;

    User user;
    Reader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        // network
//        networkReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//                    ConnectivityManager connectivityManager =
//                            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//                    if (networkInfo == null || !networkInfo.isConnected()) {
//                        // Show a message to the user that they are not connected to the internet
//                        showNetworkErrorDialog();
//                    } else {
//                        // Dismiss the dialog and refresh the data
//                        if (networkErrorDialog != null && networkErrorDialog.isShowing()) {
//                            networkErrorDialog.dismiss();
//                            networkErrorDialog = null;
//                            // Refresh the data
//                        }
//                    }
//                }
//            }
//        };

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getUser().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if(users.size() == 1)
                    user = users.get(0);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment, new SearchFragment()).commit();

        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_saved:
                    if (isLoggedIn())
                        selectedFragment = new SavedFragment();
                    else {
                        Intent intent = new Intent(NavActivity.this, LoginActivity.class);
                        intent.putExtra("WantNavTo", "saved");
//                        this.startActivity(intent);
                        this.startActivityForResult(intent, REQUEST_CODE_LOGIN);
                    }
                    break;
                case R.id.nav_alert:
                    selectedFragment = new AlertFragment();
//                    if (isLoggedIn())
//                        selectedFragment = new AlertFragment();
//                    else {
//                        Intent intent = new Intent(NavActivity.this, LoginActivity.class);
//                        intent.putExtra("WantNavTo", "alert");
////                        this.startActivity(intent);
//                        this.startActivityForResult(intent, REQUEST_CODE_LOGIN);
//                    }
                    break;
                case R.id.nav_back:
                    Intent intent = new Intent(NavActivity.this, NewsActivity.class);
//                    intent.putExtra("url", article.getUrl());
                    this.startActivity(intent);
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_fragment, selectedFragment)
                        .commit();
                return true;
            }
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK ) {
            if (requestCode == REQUEST_CODE_LOGIN) {
                Fragment selectedFragment = null;
                assert data != null;
                if (data.getStringExtra("WantNavTo").equals("saved")) {
                    selectedFragment = new SavedFragment();
                } else if (data.getStringExtra("WantNavTo").equals("alert")) {
                    selectedFragment = new AlertFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_fragment, selectedFragment)
                            .commit();
                }

            } else if (requestCode == REQUEST_CODE_REGISTER) {
                Intent intent = new Intent(NavActivity.this, LoginActivity.class);
                if (data.getStringExtra("WantNavTo").equals("saved")) {
                    intent.putExtra("WantNavTo", "saved");
                } else if (data.getStringExtra("WantNavTo").equals("alert")) {
                    intent.putExtra("WantNavTo", "alert");
                }
                this.startActivityForResult(intent, REQUEST_CODE_LOGIN);
            }

        } else if (resultCode == RESULT_FIRST_USER) {
            if (requestCode == REQUEST_CODE_LOGIN) {
                Intent intent = new Intent(NavActivity.this, RegistrationActivity.class);
                if (data.getStringExtra("WantNavTo").equals("saved")) {
                    intent.putExtra("WantNavTo", "saved");
                } else if (data.getStringExtra("WantNavTo").equals("alert")) {
                    intent.putExtra("WantNavTo", "alert");
                }
                this.startActivityForResult(intent, REQUEST_CODE_REGISTER);
            }
        }
    }

    private boolean isLoggedIn() {
        return user != null;
    }

//    private void showNetworkErrorDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Network Error");
//        builder.setMessage("You are not connected to the internet. Please check your network settings.");
//        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                startActivity(intent);
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//        builder.show();
//        networkErrorDialog = builder.show();
//    }
}