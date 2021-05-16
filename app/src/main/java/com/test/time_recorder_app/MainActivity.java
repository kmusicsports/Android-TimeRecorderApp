package com.test.time_recorder_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final Bundle args = new Bundle();
    private HomeFragment fragmentHome;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentHome = new HomeFragment();
        setFragment(fragmentHome);
        BottomNavigationView bottomNavigation = findViewById(R.id.main_a_button_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    setFragment(fragmentHome);
                    return true;
                case R.id.action_display_records:
                    args.putString("name", null);
                    args.putString("startDate", null);
                    args.putString("endDate", null);
                    args.putString("order", null);
                    DisplayRecordsFragment fragmentDisplayRecords = new DisplayRecordsFragment();
                    fragmentDisplayRecords.setArguments(args);
                    setFragment(fragmentDisplayRecords);
                    return true;
                case R.id.action_settings:
                    SettingFragment fragmentSetting = new SettingFragment();
                    setFragment(fragmentSetting);
                    return true;
            }
            return false;
        });
    }

    public void setFragment(Fragment fragment){
        FragmentManager manager = Objects.requireNonNull(getSupportFragmentManager());
        manager.beginTransaction()
                .replace(R.id.main_a_layout, fragment)
                .commit();
    }

    public void visibleId(int ids) {
        (findViewById(ids)).setVisibility(View.VISIBLE);
    }

    public void clickName(View view) {
        visibleId(R.id.home_f_button_work);
        visibleId(R.id.home_f_button_break);
        visibleId(R.id.home_f_button_outing);
        String name =  ((Button)view).getText().toString();
        args.putString("Name", name);
        fragmentHome.setArguments(args);
    }
}