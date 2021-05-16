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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogAddStaffFragment extends DialogFragment {

    private SimpleOpenHelper helper;
    private SQLiteDatabase db;
    private String staffName;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_staff, null);
        EditText editText = view.findViewById(R.id.as_d_edit_text);
        editText.setOnEditorActionListener((text, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                staffName = editText.getText().toString();
                insertData(staffName);
                return true;
            }
            return false;
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
            .setTitle(getString(R.string.title_add_new))
            .setMessage(getString(R.string.as_d_label_enter_name))
            .setView(view)
            .setNegativeButton(getString(R.string.label_cancel), null)
            .setPositiveButton(getString(R.string.label_add),
                    (dialog, which) -> {
                        staffName = editText.getText().toString();
                        insertData(staffName);
                    }
            )
            .create();
    }

    private void insertData(String staffName){
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
                "staff = ?", new String[]{staffName},
                null,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()) {
            Toast.makeText(getActivity(), getString(R.string.as_d_label_already), Toast.LENGTH_SHORT).show();
        } else {
            try (SQLiteDatabase database = helper.getWritableDatabase()){
                ContentValues contentValues = new ContentValues();
                contentValues.put("staff", staffName);
                database.insert("stafftable", null, contentValues);
                Intent intentStaff = new Intent(getActivity(), StaffActivity.class);
                startActivity(intentStaff);
            }
        }
        // 忘れずに！
        cursor.close();
    }
}
