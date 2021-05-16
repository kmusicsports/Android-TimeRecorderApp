package com.test.time_recorder_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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

public class DialogFilterFragment extends DialogFragment {

    private SimpleOpenHelper helper;
    private SQLiteDatabase db;
    private Spinner spinner;
    private Button buttonStartDate;
    private Button buttonEndDate;
    private String staffName;
    private String startDate;
    private String endDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);
        spinner = view.findViewById(R.id.filter_d_spinner);
        createStaffSpinner();
        getNameSpinnerText();

        buttonStartDate = view.findViewById(R.id.filter_d_button_start_date);
        buttonEndDate = view.findViewById(R.id.filter_d_button_end_date);
        buttonStartDate.setOnClickListener(v -> clickStartDate().show());
        buttonEndDate.setOnClickListener(v -> clickEndDate().show());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
            .setTitle(getString(R.string.title_filter))
            .setView(view)
            .setNegativeButton(getString(R.string.label_cancel), null)
            .setPositiveButton(getString(R.string.label_apply),
                    (dialog, which) -> {
                        // 条件で読み込み(更新)
                        Bundle args = new Bundle();
                        args.putString("name", staffName);
                        args.putString("startDate", startDate);
                        args.putString("endDate", endDate);
                        args.putString("order", null);
                        DisplayRecordsFragment fragment = new DisplayRecordsFragment();
                        fragment.setArguments(args);
                        setFragment(fragment);
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
            spinner.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), getString(R.string.label_no_staff), Toast.LENGTH_SHORT).show();
        }
        // 忘れずに！
        cursor.close();
    }

    public void getNameSpinnerText() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private Dialog clickStartDate() {
        // 今日の日付を準備
        final Calendar calendar = getInstance();
        return new DatePickerDialog(Objects.requireNonNull(getActivity()),
                (view1, year, monthOfYear, dayOfMonth) -> {
                    // 選択された日付をテキストボックスに反映
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpledateformat = new SimpleDateFormat("E");
                    Calendar cal = getInstance();
                    cal.set(year + 1900, monthOfYear, dayOfMonth-1);
                    Date date = cal.getTime();
                    String dayOfWeek = simpledateformat.format(date);
                    startDate = String.format(Locale.JAPAN, "%02d/%02d/%02d(%s)", year, monthOfYear + 1, dayOfMonth, dayOfWeek);
                    buttonStartDate.setText(startDate); // monthは0から始まる
                    if (endDate == null) {
                        endDate = startDate;
                        buttonEndDate.setText(endDate);
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    private Dialog clickEndDate() {
        // 今日の日付を準備
        final Calendar calendar = getInstance();
        return new DatePickerDialog(Objects.requireNonNull(getActivity()),
                (view1, year, monthOfYear, dayOfMonth) -> {
                    // 選択された日付をテキストボックスに反映
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpledateformat = new SimpleDateFormat("E");
                    Calendar cal = getInstance();
                    cal.set(year + 1900, monthOfYear, dayOfMonth-1);
                    Date date = cal.getTime();
                    String dayOfWeek = simpledateformat.format(date);
                    endDate = String.format(Locale.JAPAN, "%02d/%02d/%02d(%s)", year, monthOfYear + 1, dayOfMonth, dayOfWeek);
                    buttonEndDate.setText(endDate); // monthは0から始まる
                    if (startDate == null) {
                        startDate = endDate;
                        buttonStartDate.setText(startDate);
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    public void setFragment(Fragment fragment){
        FragmentManager manager = Objects.requireNonNull(getFragmentManager());
        manager.beginTransaction()
                .replace(R.id.main_a_layout, fragment)
                .commit();
    }
}


