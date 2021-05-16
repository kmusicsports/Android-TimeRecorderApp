package com.test.time_recorder_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import java.util.Locale;
import java.util.Objects;

public class TermsOfUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);

        TermsOfUseActivity.this.setTitle(getString(R.string.title_terms_of_use));

        WebView web = findViewById(R.id.terms_a_web);
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.JAPAN)) {
            // 日本語環境
            web.loadUrl("file:///android_asset/index_ja.html");
        } else {
            // その他の言語環境
            web.loadUrl("file:///android_asset/index.html");
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, AppInformationActivity.class);
        startActivity(intent);

        return super.onSupportNavigateUp();
    }
}