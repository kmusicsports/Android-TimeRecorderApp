package com.test.time_recorder_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // load the password
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", 0);
        String password = sharedPreferences.getString("password", "");

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (password.equals("")) {
                // if there is no password
                Intent intentCreatePassword = new Intent(SplashActivity.this, CreatePasswordActivity.class);
                startActivity(intentCreatePassword);
            } else {
                // if there is a password
                Intent intentMain = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intentMain);
            }
        }, 2000);
    }
}