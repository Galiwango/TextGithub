package com.example.galiwango_john_dev.journalapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.galiwango_john_dev.journalapp.data.JournalContract;

public class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.JournalViewHolder> {

    // Holds on to the cursor to display the Journal
    private Cursor mCursor;
    private Context mContext;

    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     * @param cursor the db cursor with waitlist data to display
     */
    public JournalListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }
    public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.journal_list_item, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JournalViewHolder holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null

        // Update the view holder with the information needed to display
        String title = mCursor.getString(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_TITLE_NAME));
        String detail = mCursor.getString(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_DETAILS));
        String timestamp= mCursor.getString(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_TIMESTAMP));
        // COMPLETED (6) Retrieve the id from the cursor and
        long id = mCursor.getLong(mCursor.getColumnIndex(JournalContract.JournalEntry._ID));

        // Display the Journal
        holder.titleTextView.setText(title);
        // Display the party count
        holder.detailTextView.setText(detail);
        holder.dateTextView.setText(timestamp);
        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);
    }



    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }


    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */

    class JournalViewHolder extends RecyclerView.ViewHolder {

        // Will display the Journal Title name
        TextView titleTextView;
        // Will display the journal details
        TextView detailTextView;
        // Will display the journal Entry Date
        TextView dateTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link JournalListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public JournalViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            detailTextView = itemView.findViewById(R.id.details_text_view);
            dateTextView= itemView.findViewById(R.id.entry_date);
        }

    }
}
