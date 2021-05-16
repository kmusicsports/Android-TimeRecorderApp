package com.test.time_recorder_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Objects;

public class StaffActivity extends AppCompatActivity {

    public static ArrayList<String> stock = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StaffMainFragment fragment = new StaffMainFragment();
        setFragment(fragment);
    }

    public void setFragment(Fragment fragment){
        FragmentManager manager = Objects.requireNonNull(getSupportFragmentManager());
        manager.beginTransaction()
                .replace(R.id.staff_layout, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_staff, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item_addStaff:
                DialogFragment dialog = new DialogAddStaffFragment();
                dialog.show(getSupportFragmentManager(), "dialog_add_staff");
                break;
            case R.id.item_delete_multiple:
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.staff_layout);
                if (fragment instanceof StaffMainFragment) {
                    EditStaffFragment fragmentEditStaff = new EditStaffFragment();
                    setFragment(fragmentEditStaff);
                } else {
                    StaffMainFragment fragmentStaffMain = new StaffMainFragment();
                    setFragment(fragmentStaffMain);
                }
                break;
            default:
                Intent intentManager = new Intent(this, ManagerActivity.class);
                startActivity(intentManager);
        }
        return true;
    }

    public void clickCheckbox(View view) {
        String txtCheckbox = ((CheckBox)view).getText().toString();
        if(((CheckBox) view).isChecked()) {
            stock.add(txtCheckbox);
        } else {
            stock.remove(txtCheckbox);
        }
    }
}