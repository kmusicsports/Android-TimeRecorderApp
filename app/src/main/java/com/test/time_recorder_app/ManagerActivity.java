package com.test.time_recorder_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        ManagerActivity.this.setTitle(getString(R.string.title_manager_menu));

        // ListViewに表示するリスト項目をArrayListで準備する
        ArrayList arrayItems = new ArrayList<>();
        arrayItems.add(getString(R.string.title_staff_main));
        arrayItems.add(getString(R.string.title_sc_mr));
        arrayItems.add(getString(R.string.title_change_password));
        // リスト項目とListViewを対応付けるArrayAdapterを用意する
        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayItems);
        // ListViewにArrayAdapterを設定する
        ListView list = findViewById(R.id.manager_a_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    // 従業員管理
                    Intent intentStaff = new Intent(ManagerActivity.this, StaffActivity.class);
                    startActivity(intentStaff);
                    break;
                case 1:
                    // 給料計算・手動記録
                    Intent intentSalaryCalculation = new Intent(ManagerActivity.this, SalaryCalculationActivity.class);
                    startActivity(intentSalaryCalculation);
                    break;
                case 2:
                    // パスワード変更
                    DialogFragment dialogChangePassword = new DialogChangePasswordFragment();
                    dialogChangePassword.show(getSupportFragmentManager(), "dialog_change_password");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);

        return super.onSupportNavigateUp();
    }
}
