package com.example.galiwango_john_dev.journalapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.galiwango_john_dev.journalapp.data.JournalContract.JournalEntry;

public class JournalDbHelper extends SQLiteOpenHelper {
    // The database name
    private static final String DATABASE_NAME = "journal.db";
    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 2;

    // Constructor
    public JournalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold Journal data
        final String SQL_CREATE_JOURNAL_TABLE = "CREATE TABLE " + JournalEntry.TABLE_NAME + " (" +
                JournalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                JournalEntry.COLUMN_TITLE_NAME + " TEXT NOT NULL, " +
                JournalEntry.COLUMN_DETAILS + " TEXT NOT NULL, " +
                JournalEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_JOURNAL_TABLE);
    }
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + JournalEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
