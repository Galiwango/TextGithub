package com.example.galiwango_john_dev.journalapp;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import com.example.galiwango_john_dev.journalapp.data.JournalContract;
import com.example.galiwango_john_dev.journalapp.data.JournalDbHelper;

public class MainActivity extends AppCompatActivity {
    private JournalListAdapter mAdapter;
    private SQLiteDatabase mDb;
   // private EditText mNewTitleEditText;
   // private EditText mNewDetailsEditText;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView journalRecyclerView;
        // Set local attributes to corresponding views
        journalRecyclerView = this.findViewById(R.id.all_journal_list_view);
      //  mNewTitleEditText = this.findViewById(R.id.title_txt);
       // mNewDetailsEditText = this.findViewById(R.id.detials_txt);
        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        journalRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a DB helper (this will create the DB if run for the first time)
        JournalDbHelper dbHelper = new JournalDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers

        mDb = dbHelper.getWritableDatabase();

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllJournal();
        // Create an adapter for that cursor to display the data
        mAdapter = new JournalListAdapter(this, cursor);

        // Link the adapter to the RecyclerView
        journalRecyclerView.setAdapter(mAdapter);

        //..............................Delete Code Here..................................................
        // COMPLETED (3) Create a new ItemTouchHelper with a SimpleCallback that handles both LEFT and RIGHT swipe directions
        // Create an item touch helper to handle swiping items off the list
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // COMPLETED (4) Override onMove and simply return false inside
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            // COMPLETED (5) Override onSwiped
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                //get the id of the item being swiped
                long id = (long) viewHolder.itemView.getTag();
                // COMPLETED (9) call removeGuest and pass through that id
                //remove from DB
                removeJournal(id);
                // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                //update the list
                mAdapter.swapCursor(getAllJournal());
            }

            //COMPLETED (11) attach the ItemTouchHelper to the journalRecyclerView
        }).attachToRecyclerView(journalRecyclerView);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //    .setAction("Action", null).show();
                Intent intent= new Intent(MainActivity.this,JournalEntryActivity.class);
                startActivity(intent);


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the cursor in the adapter to trigger UI to display the new list
        mAdapter.swapCursor(getAllJournal());
    }

    /**
         * Query the mDb and get all data from the Journal table
         *
         * @return Cursor containing the list of guests
         */
        private Cursor getAllJournal() {
            return mDb.query(
                    JournalContract.JournalEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    JournalContract.JournalEntry.COLUMN_TIMESTAMP
            );
        }

    // COMPLETED (1) Create a new function called removeJournal that takes long id as input and returns a boolean
    /**
     * Removes the record with the specified id
     *
     * @param id the DB id to be removed
     * @return True: if removed successfully, False: if failed
     */
    private boolean removeJournal(long id) {
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that JournalEntry._ID equals id
        return mDb.delete(JournalContract.JournalEntry.TABLE_NAME, JournalContract.JournalEntry._ID + "=" + id, null) > 0;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
