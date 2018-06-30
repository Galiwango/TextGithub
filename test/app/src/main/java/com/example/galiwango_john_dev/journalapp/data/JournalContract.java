package com.example.galiwango_john_dev.journalapp.data;

import android.provider.BaseColumns;

public class JournalContract {
    public static final class JournalEntry implements BaseColumns{
        public static final String TABLE_NAME ="journal";
        public static final String COLUMN_TITLE_NAME="titleName";
        public static final String COLUMN_DETAILS="details";
        public static final String COLUMN_TIMESTAMP="timestamp";

    }
}
