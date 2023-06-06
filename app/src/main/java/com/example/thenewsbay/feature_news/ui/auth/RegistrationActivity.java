package com.example.thenewsbay.feature_news.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thenewsbay.R;
import com.example.thenewsbay.feature_news.models.room.saved_article.Reader;
import com.example.thenewsbay.feature_news.viewModel.AccountViewModel;
import com.example.thenewsbay.feature_news.viewModel.SavedArticleViewModel;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {
    AccountViewModel accountViewModel;
    SavedArticleViewModel savedArticleViewModel;

    Map<String, String> codeCountryMap = new HashMap<>();
    Map<String, String> countryCodeMap = new HashMap<>();

    EditText editTextName, editTextEmail, editTextPassword;
    Button buttonRegister;

    Spinner spinnerCountry;
    String code;

    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        codeCountryMap.put("us", "United State");
        codeCountryMap.put("hk", "Hong Kong");
        codeCountryMap.put("jp", "Japan");
        codeCountryMap.put("in", "India");
        codeCountryMap.put("au", "Australia");
        for (Map.Entry<String, String> entry : codeCountryMap.entrySet()) {
            countryCodeMap.put(entry.getValue(), entry.getKey());
        }

        editTextName = findViewById(R.id.et_reg_name);
        editTextEmail = findViewById(R.id.et_reg_email);
        editTextPassword = findViewById(R.id.et_reg_password);

        spinnerCountry = findViewById(R.id.spinner_reg_country);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setSelection(0);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                code = countryCodeMap.get(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                code = "United State";
            }
        });

        buttonRegister = findViewById(R.id.btn_register);

        textViewLogin = findViewById(R.id.tv_goto_login);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (name.equals("") || email.equals("") || password.equals(""))
                    Toasty.warning(view.getContext(), "Please do not leave blank.", Toast.LENGTH_SHORT).show();
                else {
                    Reader reader = new Reader(email, name, code, password);
                    if (!isRegisterSuccessful(reader)) {
                        Toasty.error(view.getContext(), "Email has already been used, please use another email.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.success(view.getContext(), "Account created! You can try to login.", Toast.LENGTH_SHORT).show();

                        Intent intent = getIntent();
                        String WantNavTo = intent.getStringExtra("WantNavTo");
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("WantNavTo", WantNavTo);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                }
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String WantNavTo = intent.getStringExtra("WantNavTo");
                Intent resultIntent = new Intent();
                resultIntent.putExtra("WantNavTo", WantNavTo);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private boolean isRegisterSuccessful(Reader reader) {
        boolean savedArticleDbSuccess;

        savedArticleViewModel = new ViewModelProvider(RegistrationActivity.this).get(SavedArticleViewModel.class);
        savedArticleDbSuccess = savedArticleViewModel.insertReader(reader);

        return savedArticleDbSuccess;
    }
}