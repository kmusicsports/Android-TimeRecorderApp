package com.test.time_recorder_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class DialogChangeNameFragment extends DialogFragment {

    private SimpleOpenHelper helper;
    private String staffName;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_name, null);

        Bundle args = getArguments();
        String oldName = Objects.requireNonNull(args).getString("Name");

        EditText editText = view.findViewById(R.id.cn_d_edit_text);
        editText.setText(oldName);
        editText.setOnEditorActionListener((text, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                staffName = editText.getText().toString();
                updateData(oldName, staffName);
                return true;
            }
            return false;
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(getString(R.string.title_change_name))
                .setView(view)
                .setNegativeButton(getString(R.string.label_cancel), null)
                .setPositiveButton(getString(R.string.label_change),
                        (dialog, which) -> {
                            staffName = editText.getText().toString();
                            updateData(oldName, staffName);
                        }
                )
                .create();
    }

    private void updateData(String previousName, String staffName){
        if(helper == null){
            helper = new SimpleOpenHelper(getActivity());
        }

        try (SQLiteDatabase database = helper.getWritableDatabase()){
            ContentValues contentValues = new ContentValues();
            contentValues.put("staff", staffName);
            String[] params = {previousName};
            database.update("stafftable", contentValues, "staff = ?", params);
            Intent intentStaff = new Intent(getActivity(), StaffActivity.class);
            startActivity(intentStaff);
        }
    }
}
