package com.test.time_recorder_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Objects;

public class DialogEditStaffFragment extends DialogFragment {

    private SimpleOpenHelper helper;
    private SQLiteDatabase db;
    private View view;
    private String staffName;

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_edit_staff, null);

        Bundle getArgs = getArguments();
        staffName = Objects.requireNonNull(getArgs).getString("Name");

        createList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
            .setTitle(staffName)
            .setView(view)
            .create();
    }

    private void createList() {
        ArrayList<String> arrayItems = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, arrayItems);
        ListView list = view.findViewById(R.id.es_d_list);
        arrayItems.add(getString(R.string.label_delete));
        arrayItems.add(getString(R.string.title_change_name));
        arrayItems.add(getString(R.string.title_hourly_wage));
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, v, position, id) -> {
            DialogFragment dialog;
            Bundle setArgs = new Bundle();
            setArgs.putString("Name", staffName);
            switch(position) {
                case 0:
                    // 削除
                    deleteData();
                    break;
                case 1:
                    // 名前変更
                    dialog = new DialogChangeNameFragment();
                    dialog.setArguments(setArgs);
                    dialog.show(Objects.requireNonNull(getFragmentManager()), "dialog_change_name");
                    break;
                case 2:
                    // 時給設定
                    dialog = new DialogHourlyWageFragment();
                    dialog.setArguments(setArgs);
                    dialog.show(Objects.requireNonNull(getFragmentManager()), "dialog_hourly_wage");
                    break;
                default:
                    Toast.makeText(getActivity(), "Unexpected value:" + position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteData() {
        if (helper == null) {
            helper = new SimpleOpenHelper(getActivity());
        }

        if (db == null) {
            db = helper.getWritableDatabase();
        }

        db.delete("stafftable", "staff = ?", new String[]{staffName});
        Intent intentStaff = new Intent(getActivity(), StaffActivity.class);
        startActivity(intentStaff);
    }
}
