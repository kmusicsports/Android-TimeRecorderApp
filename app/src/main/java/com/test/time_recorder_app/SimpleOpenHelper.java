package com.test.time_recorder_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SimpleOpenHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 1;

    // データーベース名
    private static final String DATABASE_NAME = "Database.db";

    private static final String TABLE_NAME = "recordtable";
    private static final String _ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ACTION = "action";
    private static final String COLUMN_TIME = "time";

    private static final String TABLE_STAFF_NAME = "stafftable";
    private static final String COLUMN_STAFF_NAME = "staff";
    private static final String COLUMN_HOURLY_WAGE = "wage";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_ACTION + " TEXT," +
                    COLUMN_TIME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " + TABLE_STAFF_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_STAFF_NAME + " TEXT," +
                    COLUMN_HOURLY_WAGE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES2 =
            "DROP TABLE IF EXISTS " + TABLE_STAFF_NAME;


    SimpleOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // テーブル作成
        // SQLiteファイルがなければSQLiteファイルが作成される
        // recordtableを作成
        db.execSQL(
                SQL_CREATE_ENTRIES
        );

        // stafftableを作成
        db.execSQL(
                SQL_CREATE_ENTRIES2
        );

        Log.d("debug", "onCreate(SQLiteDatabase db)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // アップデートの判別
        db.execSQL(
                SQL_DELETE_ENTRIES
        );

        db.execSQL(
                SQL_DELETE_ENTRIES2
        );

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}