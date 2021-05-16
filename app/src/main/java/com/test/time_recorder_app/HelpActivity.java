package com.test.time_recorder_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        HelpActivity.this.setTitle(getString(R.string.title_help));

        createGuideList();
        createQuestionList();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);

        return super.onSupportNavigateUp();
    }

    private void createGuideList() {
        ArrayList arrayItems = new ArrayList<>();
        arrayItems.add(getString(R.string.title_guide0));
        arrayItems.add(getString(R.string.title_guide1));
        arrayItems.add(getString(R.string.title_guide2));
        arrayItems.add(getString(R.string.title_guide3));

        // リスト項目とListViewを対応付けるArrayAdapterを用意する
        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayItems);
        // ListViewにArrayAdapterを設定する
        ListView list = findViewById(R.id.h_a_list_guide);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intentGuide0 = new Intent(HelpActivity.this, GuideActivity.class);
            switch (position) {
                case 0:
                    // 従業員を登録する
                    intentGuide0.putExtra("layoutId", R.layout.activity_guide0);
                    break;
                case 1:
                    // 従業員の操作
                    intentGuide0.putExtra("layoutId", R.layout.activity_guide1);
                    break;
                case 2:
                    // 実労働時間(正味就業時間)を表示する
                    intentGuide0.putExtra("layoutId", R.layout.activity_guide2);
                    break;
                case 3:
                    // 従業員の時給を設定する
                    intentGuide0.putExtra("layoutId", R.layout.activity_guide3);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
            startActivity(intentGuide0);
        });
    }

    private void createQuestionList() {
        ArrayList arrayItems = new ArrayList<>();
        arrayItems.add(getString(R.string.title_question0));

        // リスト項目とListViewを対応付けるArrayAdapterを用意する
        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayItems);
        // ListViewにArrayAdapterを設定する
        ListView list = findViewById(R.id.h_a_list_question);
        list.setAdapter(adapter);
    }

}
