package com.test.time_recorder_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class DialogRecordFragment extends DialogFragment {

    private SimpleOpenHelper helper;
    private SQLiteDatabase db;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_record, null);
        Bundle args = getArguments();
        final String recordDate = Objects.requireNonNull(args).getString("Date");
        final String staffName = args.getString("Name");
        final String recordAction = args.getString("Action");
        final String recordTime = args.getString("Time");

        TextView textAction = view.findViewById(R.id.record_d_text_action);
        TextView textTime = view.findViewById(R.id.record_d_text_time);
        TextView textName = view.findViewById(R.id.record_d_text_name);
        textAction.setText(recordAction);
        textTime.setText(recordTime);
        textName.setText(staffName);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton(getString(R.string.label_cancel),
                        (dialog, which) -> Toast.makeText(getActivity(),getString(R.string.label_record_cancel), Toast.LENGTH_SHORT).show()
                )
                .setPositiveButton(getString(R.string.label_save),
                        (dialog, which) -> {
                            // 記録に打刻
                            insertData(recordDate, staffName, recordAction, recordTime);
                        }
                )
                .create();
    }

    private void insertData(String date, String name, String action, String time){
        if(helper == null){
            helper = new SimpleOpenHelper(getActivity());
        }

        if(db == null){
            db = helper.getWritableDatabase();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("name", name);
        contentValues.put("action", action);
        contentValues.put("time", time);

        db.insert("recordtable", null, contentValues);
        Toast.makeText(getActivity(),getString(R.string.label_recorded), Toast.LENGTH_SHORT).show();
    }
}
