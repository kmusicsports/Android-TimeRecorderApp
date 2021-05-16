package com.test.time_recorder_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AppInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_information);

        AppInformationActivity.this.setTitle(getString(R.string.title_app_information));

        createList();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        return super.onSupportNavigateUp();
    }

    private void createList() {
        String[] titles = new String[]{getString(R.string.title_version), getString(R.string.title_license), getString(R.string.title_terms_of_use)};
        String[] comments = new String[]{BuildConfig.VERSION_NAME, null, null, null};
        ArrayList<HashMap<String, String>> arrayItems = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            HashMap<String, String> mapItems = new HashMap<>();
            mapItems.put("Title", titles[i]);
            mapItems.put("Comment", comments[i]);
            arrayItems.add(mapItems);
        }
        final SimpleAdapter adapter = new SimpleAdapter(this, arrayItems, R.layout.list_item, new String[] {"Title", "Comment"}, new int[] {R.id.text_title, R.id.text_comment});
        ListView list = findViewById(R.id.ai_a_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    // バージョン
                    break;
                case 1:
                    // ライセンス
                    Intent ossLicensesMenuIntent = new Intent(AppInformationActivity.this, OssLicensesMenuActivity.class);
                    ossLicensesMenuIntent.putExtra("title", getString(R.string.title_license));
                    startActivity(ossLicensesMenuIntent);
                    break;
                case 2:
                    // 利用規約
                    Intent termsOfUseIntent = new Intent(AppInformationActivity.this, TermsOfUseActivity.class);
                    startActivity(termsOfUseIntent);
                    break;
                default:
                    Toast.makeText(AppInformationActivity.this, String.valueOf(position), Toast.LENGTH_LONG).show();
            }
        });

    }
}
