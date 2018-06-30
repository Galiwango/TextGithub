package com.example.galiwango_john_dev.journalapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.galiwango_john_dev.journalapp.data.JournalContract;
import com.example.galiwango_john_dev.journalapp.data.JournalDbHelper;

public class JournalEntryActivity extends AppCompatActivity {

    private JournalListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private EditText mNewTitleEditText;
    private EditText mNewDetailsEditText;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);
        mNewTitleEditText = this.findViewById(R.id.txtTitle);
        mNewDetailsEditText = this.findViewById(R.id.txtDetails);
        // Create a DB helper (this will create the DB if run for the first time)
        JournalDbHelper dbHelper = new JournalDbHelper(this);
        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers

        mDb = dbHelper.getWritableDatabase();

    }
    /**
     * Adds a new guest to the mDb including the party count and the current timestamp
     *
     * @param title  Guest's name
     * @param details Number in party
     * @return id of new record added
     */
    private long addNewJournal(String title,String details) {
        ContentValues cv = new ContentValues();
        cv.put(JournalContract.JournalEntry.COLUMN_TITLE_NAME, title);
        cv.put(JournalContract.JournalEntry.COLUMN_DETAILS, details);
        return mDb.insert(JournalContract.JournalEntry.TABLE_NAME, null, cv);


    }
    public void addToJournal(View view) {
        if (mNewTitleEditText.getText().length() == 0 ||
                mNewDetailsEditText.getText().length() == 0) {
            return;
        }
        //default party size to 1
        //  int partySize = 1;
        // try {
        //mNewPartyCountEditText inputType="number", so this should always work
        //   partySize = Integer.parseInt(mNewDetailsEditText.getText().toString());
        // } catch (NumberFormatException ex) {
        //    Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
        //  }

        // Add guest info to mDb
        addNewJournal(mNewTitleEditText.getText().toString(),mNewDetailsEditText.getText().toString());
        // addNewJournal(mNewTitleEditText.getText().toString(),partySize);
        // Update the cursor in the adapter to trigger UI to display the new list
       // mAdapter.swapCursor(getAllJournal());

        //clear UI text fields
        mNewDetailsEditText.clearFocus();
        mNewTitleEditText.getText().clear();
        mNewDetailsEditText.getText().clear();


    }


}
