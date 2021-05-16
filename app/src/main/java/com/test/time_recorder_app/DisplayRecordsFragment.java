package com.test.time_recorder_app;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class DisplayRecordsFragment extends Fragment {

    private View view;
    private SimpleOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_display_records, container, false);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.title_display_records));

        //オプションメニュー利用フラグを立てる
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        String staffName = Objects.requireNonNull(args).getString("name");
        String startDate = args.getString("startDate");
        String endDate = args.getString("endDate");
        String order = args.getString("order");
        String defaultOrder = "date ASC";
        if (order != null) {
            Toast.makeText(getActivity(), getString(R.string.dr_f_label_filter_disabled), Toast.LENGTH_SHORT).show();
        } else {
            order = defaultOrder;
        }

        if(staffName == null && startDate == null && endDate == null) { // フィルターが適用されていなければ
            searchAll(order);
        } else if(staffName.equals("") && (startDate == null || endDate == null)) { // 何も指定せずに適用した場合
            Toast.makeText(getActivity(), getString(R.string.dr_f_label_not_correct), Toast.LENGTH_SHORT).show();
            searchAll(order);
        } else if(startDate == null || endDate == null) { // 名前のみ指定された場合
            searchByName(staffName, order);
        } else if(staffName.equals("") && startDate != null && endDate != null) { // 日付のみ指定された場合
            searchByDate(startDate, endDate, order);
        } else { // 全て指定した場合
            searchByBoth(staffName, startDate, endDate, order);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.option_menu_display_records, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DialogFragment dialog;
        switch(item.getItemId()) {
            case R.id.item_filter:
                dialog = new DialogFilterFragment();
                dialog.show(Objects.requireNonNull(getFragmentManager()), "dialog_filter");
                break;
            case R.id.item_sort:
                dialog = new DialogSortFragment();
                dialog.show(Objects.requireNonNull(getFragmentManager()), "dialog_sort");
                break;
            case R.id.item_export:
                dialog = new DialogExportFragment();
                dialog.show(Objects.requireNonNull(getFragmentManager()), "dialog_export");
                break;
            default:
                Toast.makeText(getActivity(), "Unexpected value:" + item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void searchAll(String order){
        createDataTable(null, null, order);
    }

    private void searchByName(String name, String order) {
        createDataTable("name = ?", new String[]{name}, order);
    }


    private void searchByDate(String dateOfStart, String dateOfEnd, String order) {
        createDataTable("date between ? and ?", new String[]{dateOfStart, dateOfEnd}, order);
    }

    private void searchByBoth(String name, String dateOfStart, String dateOfEnd, String order) {
        createDataTable("name = ? and date between ? and ?", new String[]{name, dateOfStart, dateOfEnd}, order);
    }

    private void createDataTable(String selection, String[] params, String order_by) {
        final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
        final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
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
                selection, params,
                null,
                null,
                order_by,
                null
        );
        if (cursor.moveToFirst()) {
            TableLayout layout = view.findViewById(R.id.dr_f_layout);
            for(int i = 0; i < cursor.getCount(); i++){
                @SuppressLint("InflateParams") TableRow tableRow = (TableRow)getLayoutInflater().inflate(R.layout.table_row, null);
                TextView textDate = tableRow.findViewById(R.id.text_row1);
                textDate.setText(cursor.getString(1));
                TextView textName = tableRow.findViewById(R.id.text_row2);
                textName.setText(cursor.getString(2));
                TextView textAction = tableRow.findViewById(R.id.text_row3);
                textAction.setText(cursor.getString(3));
                TextView textTime = tableRow.findViewById(R.id.text_row4);
                textTime.setText(cursor.getString(4));
                cursor.moveToNext();
                layout.addView(tableRow, new TableLayout.LayoutParams(MP, WC));
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.label_no_data), Toast.LENGTH_SHORT).show();
        }
        // 忘れずに！
        cursor.close();
    }

}