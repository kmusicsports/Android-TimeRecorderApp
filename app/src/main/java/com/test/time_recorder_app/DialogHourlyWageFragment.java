package com.test.time_recorder_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class DialogHourlyWageFragment extends DialogFragment {

    private SimpleOpenHelper helper;
    private SQLiteDatabase db;
    private EditText editText;
    private int hourlyWage;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_hourly_wage, null);

        editText = view.findViewById(R.id.hw_d_edit_text);

        Bundle args = getArguments();
        final String staffName = Objects.requireNonNull(args).getString("Name");

        setWageText(staffName);

        editText.setOnEditorActionListener((text, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hourlyWage  = Integer.parseInt(editText.getText().toString());
                updateData(staffName, hourlyWage);
                return true;
            }
            return false;
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(getString(R.string.title_hourly_wage))
                .setView(view)
                .setNegativeButton(getString(R.string.label_cancel), null)
                .setPositiveButton(getString(R.string.label_save),
                        (dialog, which) -> {
                            hourlyWage  = Integer.parseInt(editText.getText().toString());
                            updateData(staffName, hourlyWage);
                        }
                )
                .create();
    }

    private void setWageText(String staff) {
        if(helper == null){
            helper = new SimpleOpenHelper(getActivity());
        }

        if(db == null){
            db = helper.getReadableDatabase();
        }
        Log.d("debug","**********Cursor");

        Cursor cursor = db.query(
                "stafftable",
                null,
                "staff = ?", new String[]{staff},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            editText.setText(String.valueOf(cursor.getInt(2)));
        }
        // 忘れずに！
        cursor.close();
    }

    private void updateData(String staff, int wage){
        if(helper == null){
            helper = new SimpleOpenHelper(getActivity());
        }

        try (SQLiteDatabase database = helper.getWritableDatabase()){
            ContentValues contentValues = new ContentValues();
            contentValues.put("wage", wage);
            String[] params = {staff};
            database.update("stafftable", contentValues, "staff = ?", params);
            Intent intentStaff = new Intent(getActivity(), StaffActivity.class);
            startActivity(intentStaff);
        }
    }
}
