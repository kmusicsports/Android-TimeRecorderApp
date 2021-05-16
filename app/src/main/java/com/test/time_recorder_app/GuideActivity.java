package com.test.time_recorder_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Objects;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentGet = getIntent();
        int guideId = intentGet.getIntExtra("layoutId", R.layout.activity_guide0);
        setContentView(guideId);

        switch(guideId) {
            case R.layout.activity_guide0:
                GuideActivity.this.setTitle(getString(R.string.title_guide0));
                break;
            case R.layout.activity_guide1:
                GuideActivity.this.setTitle(getString(R.string.title_guide1));
                break;
            case R.layout.activity_guide2:
                GuideActivity.this.setTitle(getString(R.string.title_guide2));
                break;
            case R.layout.activity_guide3:
                GuideActivity.this.setTitle(getString(R.string.title_guide3));
                break;
            default:
                Toast.makeText(this, "Unexpected value:" + guideId, Toast.LENGTH_SHORT).show();
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intentHelp = new Intent(this, HelpActivity.class);
        startActivity(intentHelp);

        return super.onSupportNavigateUp();
    }
}