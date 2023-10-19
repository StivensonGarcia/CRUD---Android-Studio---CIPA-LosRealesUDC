package com.example.crudreales;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public TaskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TASKS_TABLE = "CREATE TABLE " +
                TaskContract.TaskEntry.TABLE_NAME + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_COMPLETED + " INTEGER NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_YEAR + " INTEGER NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_MONTH + " INTEGER NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_DAY + " INTEGER NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_HOUR + " INTEGER NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_MINUTE + " INTEGER NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_DAYS + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
