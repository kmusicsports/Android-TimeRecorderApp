package com.test.time_recorder_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static java.util.Calendar.getInstance;

public class DialogManualRecordFragment extends DialogFragment {

    private SimpleOpenHelper helper;
    private SQLiteDatabase db;
    private Spinner spinnerName;
    private Spinner spinnerAction;
    private Button buttonDate;
    private Button buttonTime;
    private String staffName;
    private String recordAction;
    private String recordDate;
    private String recordTime;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_manual_record, null);
        spinnerName = view.findViewById(R.id.mr_d_spinner_name);
        createStaffSpinner();
        spinnerAction = view.findViewById(R.id.mr_d_spinner_action);
        getNameSpinnerText();
        getActionSpinnerText();

        buttonDate = view.findViewById(R.id.mr_d_button_date);
        buttonTime = view.findViewById(R.id.mr_d_button_time);
        buttonDate.setOnClickListener(v -> clickDate().show());
        buttonTime.setOnClickListener(v -> clickTime().show());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setTitle(getString(R.string.title_manual_record))
                .setView(view)
                .setNegativeButton(getString(R.string.label_cancel),
                        (dialog, which) -> Toast.makeText(getActivity(), getString(R.string.label_record_cancel), Toast.LENGTH_SHORT).show())
                .setPositiveButton(getString(R.string.label_save),
                        (dialog, which) -> {
                            // 記録に打刻
                            saveManualRecord();
                        })
                .create();
    }

    public void createStaffSpinner(){
        if (helper == null) {
            helper = new SimpleOpenHelper(getActivity());
        }
        if (db == null) {
            db = helper.getReadableDatabase();
        }
        Log.d("debug", "**********Cursor");
        Cursor cursor = db.query(
                "stafftable",
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            ArrayList<String> arrayItems = new ArrayList<>();
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_dropdown_item, arrayItems);
            arrayItems.add("");
            for (int i = 0; i < cursor.getCount(); i++) {
                arrayItems.add(cursor.getString(1));
                cursor.moveToNext();
            }
            spinnerName.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), getString(R.string.label_no_staff), Toast.LENGTH_SHORT).show();
        }
        // 忘れずに！
        cursor.close();
    }

    public void getNameSpinnerText() {
        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                staffName = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getActionSpinnerText() {
        spinnerAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                recordAction = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private Dialog clickDate() {
        // 今日の日付を準備
        final Calendar calendar = Calendar.getInstance();
        return new DatePickerDialog(Objects.requireNonNull(getActivity()),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // 選択された日付をテキストボックスに反映
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpledateformat = new SimpleDateFormat("E");
                    Calendar cal = getInstance();
                    cal.set(year + 1900, monthOfYear, dayOfMonth-1);
                    Date date = cal.getTime();
                    String dayOfWeek = simpledateformat.format(date);
                    recordDate = String.format(Locale.JAPAN, "%02d/%02d/%02d(%s)", year, monthOfYear + 1, dayOfMonth, dayOfWeek);
                    buttonDate.setText(recordDate); // monthは0から始まる
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    private Dialog clickTime() {
        // 今日の日付を準備
        final Calendar calendar = Calendar.getInstance();
        return new TimePickerDialog(getActivity(),
                (view, hourOfDay, minute) -> {
                    // 選択された時刻をテキストボックスに反映
                    recordTime = String.format(Locale.JAPAN, "%02d:%02d", hourOfDay, minute);
                    buttonTime.setText(recordTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        );
    }

    private void saveManualRecord() {
        if(staffName.equals("")||recordAction.equals("")||recordDate == null||recordTime == null) {
            Toast.makeText(getActivity(),getString(R.string.mr_d_label_all_requirements), Toast.LENGTH_SHORT).show();
        } else {
            insertData(recordDate, staffName, recordAction, recordTime);
            Toast.makeText(getActivity(), getString(R.string.label_recorded), Toast.LENGTH_SHORT).show();
        }

        Intent intentSalaryCalculation = new Intent(getActivity(), SalaryCalculationActivity.class);
        intentSalaryCalculation.putExtra("name", (String) null);
        intentSalaryCalculation.putExtra("startDate", (String) null);
        intentSalaryCalculation.putExtra("endDate", (String) null);
        startActivity(intentSalaryCalculation);
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
    }

    public void setFragment(Fragment fragment){
        FragmentManager manager = Objects.requireNonNull(getFragmentManager());
        manager.beginTransaction()
                .replace(R.id.main_a_layout, fragment)
                .commit();
    }
}
