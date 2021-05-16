package com.test.time_recorder_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.Objects;

public class SalaryCalculationActivity extends AppCompatActivity {

    private SimpleOpenHelper helper;
    private SQLiteDatabase db;
    private Cursor cursor;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_calculation);

        SalaryCalculationActivity.this.setTitle(getString(R.string.title_sc_mr));

        Intent intentGet = getIntent();
        String staffName = intentGet.getStringExtra("name");
        String startDate = intentGet.getStringExtra("startDate");
        String endDate = intentGet.getStringExtra("endDate");
        String order = "date ASC";

        if(staffName == null || staffName.equals("")) { // フィルターが適用されていなければ
            searchAll(order);
        } else { // 全て指定した場合
            searchByBoth(staffName, startDate, endDate, order);
//            // 表示範囲内で実労働時間を計算し、表の上に表示
            printWorkHours(staffName, startDate, endDate);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_salary_calculation, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DialogFragment dialog;
        switch (item.getItemId()) {
            case R.id.item_add:
                //手動記録ダイアログ起動
                dialog = new DialogManualRecordFragment();
                dialog.show(Objects.requireNonNull(getSupportFragmentManager()), "dialog_manual_record");
                break;
            case R.id.item_calculation:
                dialog = new DialogSalaryCalculationFragment();
                dialog.show(Objects.requireNonNull(getSupportFragmentManager()), "dialog_salary_calculation");
                break;
            case R.id.item_closing_date:
                dialog = new DialogClothingDateFragment();
                dialog.show(Objects.requireNonNull(getSupportFragmentManager()), "dialog_clothing_date");
                break;
            default:
                Intent intentManager = new Intent(this, ManagerActivity.class);
                startActivity(intentManager);
        }
        return true;
    }

    private void searchAll(String order){
        createDataTable(null, null, order);
    }

    private void searchByBoth(String name, String dateOfStart, String dateOfEnd, String order) {
        createDataTable("name = ? and date between ? and ?", new String[]{name, dateOfStart, dateOfEnd}, order);
    }

    private void createDataTable(String selection, String[] params, String order_by) {
        final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
        final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
        if(helper == null){
            helper = new SimpleOpenHelper(this);
        }
        if(db == null){
            db = helper.getReadableDatabase();
        }
        Log.d("debug","**********Cursor");
        cursor = db.query(
                "recordtable",
                null,
                selection, params,
                null,
                null,
                order_by,
                null
        );
        if (cursor.moveToFirst()) {
            TableLayout layout = findViewById(R.id.sc_a_layout);
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
            Toast.makeText(this, getString(R.string.label_no_data), Toast.LENGTH_SHORT).show();
        }
        // 忘れずに！
        cursor.close();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void printWorkHours(String name, String dateOfStart, String dateOfEnd) {
        BigDecimal bigDecimalSum = new BigDecimal("0.0");
        BigDecimal[] stockTimes = new BigDecimal[3];
        BigDecimal bigDecimalInterval;

        if(helper == null){
            helper = new SimpleOpenHelper(this);
        }
        if(db == null){
            db = helper.getReadableDatabase();
        }
        Log.d("debug","**********Cursor");

        cursor = db.query(
                "recordtable",
                null,
                "name = ? and date between ? and ?", new String[]{name, dateOfStart, dateOfEnd},
                null,
                null,
                "date ASC",
                null
        );

        if (cursor.moveToFirst()) {
            for(int i = 0; i < cursor.getCount(); i++) {
                BigDecimal bigDecimalTmp = new BigDecimal(String.valueOf(LocalTime.parse(cursor.getString(4)).toSecondOfDay()));
                String action = cursor.getString(3);
                if(action.equals(getString(R.string.label_start_of_work))){
                    stockTimes[0] = bigDecimalTmp;
                }else if(action.equals(getString(R.string.label_end_of_work))){
                    bigDecimalInterval = bigDecimalTmp.subtract(stockTimes[0]);
                    bigDecimalSum = bigDecimalSum.add(bigDecimalInterval);
                }else if(action.equals(getString(R.string.label_start_of_break))){
                    stockTimes[1] = bigDecimalTmp;
                }else if(action.equals(getString(R.string.label_end_of_break))){
                    bigDecimalInterval = bigDecimalTmp.subtract(stockTimes[1]);
                    bigDecimalSum = bigDecimalSum.subtract(bigDecimalInterval);
                }else if(action.equals(getString(R.string.label_outing))){
                    stockTimes[2] = bigDecimalTmp;
                }else if(action.equals(getString(R.string.label_return_to_office))){
                    bigDecimalInterval = bigDecimalTmp.subtract(stockTimes[2]);
                    bigDecimalSum = bigDecimalSum.add(bigDecimalInterval);
                }else {
                    Toast.makeText(this, "Error(sc_a)", Toast.LENGTH_LONG).show();
                }
                cursor.moveToNext();
            }

//            除算結果を小数第6位で四捨五入し、小数第5位までの値にする
            bigDecimalSum = bigDecimalSum.divide(new BigDecimal("3600"), 5, RoundingMode.HALF_UP);

            BigDecimal bigDecimalHourlyWage = new BigDecimal(String.valueOf(getHourlyWage(name)));
            BigDecimal bigDecimalWage = bigDecimalSum.multiply(bigDecimalHourlyWage).setScale(1, BigDecimal.ROUND_UP);;

            TextView textWorkHours = findViewById(R.id.sc_a_text);
            textWorkHours.setText(getString(R.string.sc_a_label_working_hours) + bigDecimalSum + getString(R.string.sc_a_label_salary) + bigDecimalSum + " × " + bigDecimalHourlyWage + " = " + bigDecimalWage);
        }

        // 忘れずに！
        cursor.close();
    }

    private int getHourlyWage(String staff) {
        int valueHourlyWage = 0;
        if(helper == null){
            helper = new SimpleOpenHelper(this);
        }

        if(db == null){
            db = helper.getReadableDatabase();
        }
        Log.d("debug","**********Cursor");

        cursor = db.query(
                "stafftable",
                null,
                "staff = ?", new String[]{staff},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            valueHourlyWage = cursor.getInt(2);
        }

        return valueHourlyWage;
    }
}