package com.test.time_recorder_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static java.util.Calendar.getInstance;

public class DialogSalaryCalculationFragment extends DialogFragment {

    private SimpleOpenHelper helper;
    private SQLiteDatabase db;
    private Spinner spinner;
    private Button buttonStartDate;
    private Button buttonEndDate;
    private String staffName;
    private String startDate;
    private String endDate;
    private String deadline;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpledateformat = new SimpleDateFormat("E");

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_salary_calculation, null);
        spinner = view.findViewById(R.id.sc_d_spinner);
        createStaffSpinner();
        getNameSpinnerText();

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("PREFS", 0);
        deadline = sharedPreferences.getString("deadline", "5");
        buttonStartDate = view.findViewById(R.id.sc_d_button_start_date);
        buttonEndDate = view.findViewById(R.id.sc_d_button_end_date);
        setStartDate();
        setEndDate();
        buttonStartDate.setOnClickListener(v -> clickStartDate().show());
        buttonEndDate.setOnClickListener(v -> clickEndDate().show());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setTitle(getString(R.string.title_salary_calculation))
                .setView(view)
                .setNegativeButton(getString(R.string.label_cancel), null)
                .setPositiveButton(getString(R.string.label_apply),
                        (dialog, which) -> {
                            // 条件で読み込み(更新)
                            Intent intentSalaryCalculation = new Intent(getActivity(), SalaryCalculationActivity.class);
                            intentSalaryCalculation.putExtra("name", staffName);
                            intentSalaryCalculation.putExtra("startDate", startDate);
                            intentSalaryCalculation.putExtra("endDate", endDate);
                            startActivity(intentSalaryCalculation);
                        })
                .create();
    }

    private void setStartDate() {
        Calendar calendar = getInstance();
        int monthOfYear = calendar.get(Calendar.MONTH);
        int clothingDatePlus;
        switch (deadline) {
            case "5":
            case "10":
            case "15":
            case "20":
            case "25":
                clothingDatePlus = Integer.parseInt(deadline) + 1;
                break;
            default:
                // 月末
                monthOfYear += 1;
                clothingDatePlus = 1;
        }
        startDate = String.format(Locale.JAPAN, "%02d/%02d/%02d", calendar.get(Calendar.YEAR), monthOfYear, clothingDatePlus);
        buttonStartDate.setText(startDate);
    }

    private void setEndDate() {
        Calendar calendar = getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH) + 1;
        int clothingDate;
        switch (deadline) {
            case "5":
            case "10":
            case "15":
            case "20":
            case "25":
                clothingDate = Integer.parseInt(deadline);
                break;
            default:
                // 月末
                clothingDate = calendar.getActualMaximum(Calendar.DATE);
        }
        endDate = String.format(Locale.JAPAN, "%02d/%02d/%02d", year, monthOfYear, clothingDate);
        buttonEndDate.setText(endDate);
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
                    calendar.set(year + 1900, monthOfYear, dayOfMonth-1);
                    Date date = calendar.getTime();
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
                    calendar.set(year + 1900, monthOfYear, dayOfMonth-1);
                    Date date = calendar.getTime();
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
}
