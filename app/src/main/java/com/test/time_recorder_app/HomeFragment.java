package com.test.time_recorder_app;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private View view;
    private final Bundle setArgs = new Bundle();
    private SimpleOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.app_name));
        hideAction();
        clickAction(R.id.home_f_button_work);
        clickAction(R.id.home_f_button_break);
        clickAction(R.id.home_f_button_outing);

        createStaffList();

        // Inflate the layout for this fragment
        return view;
    }

    public void createStaffList(){
        final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
        final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
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
            LinearLayout staffListLayout = view.findViewById(R.id.home_f_layout);
            for (int i = 0; i < cursor.getCount(); i++) {
                @SuppressLint("InflateParams") Button button = (Button) getLayoutInflater().inflate(R.layout.button_item, null);
                button.setText(cursor.getString(1));
                cursor.moveToNext();
                staffListLayout.addView(button, new LinearLayout.LayoutParams(MP, WC));
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.label_no_staff), Toast.LENGTH_SHORT).show();
        }
        // 忘れずに！
        cursor.close();
    }

    public void clickAction(int buttonId) {
        Button button = view.findViewById(buttonId);
        button.setOnClickListener(view -> {
            Bundle getArgs = getArguments();
            String staffName = Objects.requireNonNull(getArgs).getString("Name");
            String action =  button.getText().toString();
            String date =  ((TextClock) Objects.requireNonNull(getActivity()).findViewById(R.id.home_f_clock_date)).getText().toString();
            int countOfStart, countOfEnd;
            String actionOfStart="", actionOfEnd = "";

//            switch(action) {
//                case "出退勤":
//                    actionOfStart = getString(R.string.label_start_of_work);
//                    actionOfEnd = getString(R.string.label_end_of_work);
//                    break;
//                case "外出/帰社":
//                    actionOfStart = getString(R.string.label_outing);
//                    actionOfEnd = getString(R.string.label_return_to_office);
//                    break;
//                default:
//                    // 休憩 開始/終了
//                    actionOfStart = getString(R.string.label_start_of_break);
//                    actionOfEnd = getString(R.string.label_end_of_break);
//            }
            if(action.equals(getString(R.string.label_work))){
                actionOfStart = getString(R.string.label_start_of_work);
                actionOfEnd = getString(R.string.label_end_of_work);
            }else if(action.equals(getString(R.string.label_break))){
                actionOfStart = getString(R.string.label_start_of_break);
                actionOfEnd = getString(R.string.label_end_of_break);
            }else if(action.equals(getString(R.string.label_outing_return))) {
                actionOfStart = getString(R.string.label_outing);
                actionOfEnd = getString(R.string.label_return_to_office);
            }else{
                Toast.makeText(getActivity(), "Error(home_f)", Toast.LENGTH_LONG).show();
            }

            countOfStart = search(new String[]{staffName, actionOfStart, date});
            countOfEnd = search(new String[]{staffName, actionOfEnd, date});
            if (countOfStart == countOfEnd) {
                action = actionOfStart;
            } else if (countOfStart - countOfEnd == 1) {
                action = actionOfEnd;
            } else {
                // 強制終了
                getActivity().finish();
            }

            DialogFragment dialog = new DialogRecordFragment();
            setArgs.putString("Name", staffName);
            setArgs.putString("Action", action);
            setArgs.putString("Date", date);
            setArgs.putString("Time", getCurrentTime());
            dialog.setArguments(setArgs);
            dialog.show(Objects.requireNonNull(getFragmentManager()), "dialog_record");
            hideAction();
        });
    }

    private int search(String[] params) {
        int count;
        if(helper == null){
            helper = new SimpleOpenHelper(getActivity());
        }

        if(db == null){
            db = helper.getReadableDatabase();
        }
        Log.d("debug","**********Cursor");

        Cursor cursor = db.query(
                "recordtable",
                null,
                "name = ? and action = ? and date = ?", params,
                null,
                null,
                null,
                null
        );

        count = cursor.getCount();

        // 忘れずに！
        cursor.close();
        return count;
    }

    private String getCurrentTime() {
        final Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return String.format(Locale.JAPAN, "%02d:%02d", hourOfDay, minute);
    }

    public void hideAction() {
        hide(R.id.home_f_button_work);
        hide(R.id.home_f_button_break);
        hide(R.id.home_f_button_outing);
    }

    public void hide(int viewId) {
        (view.findViewById(viewId)).setVisibility(View.INVISIBLE);
    }
}
