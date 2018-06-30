package com.example.galiwango_john_dev.journalapp.user_account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.galiwango_john_dev.journalapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewNoteActivity extends AppCompatActivity {

    private Button btnCreate;
    private EditText etTitle, etDetials;

    private FirebaseAuth fAuth;
    private DatabaseReference fJournalDatabase;

    private String journalID;

    private boolean isExist;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.new_note_menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        try {
            journalID = getIntent().getStringExtra("journalId");

            //Toast.makeText(this, noteID, Toast.LENGTH_SHORT).show();

            if (!journalID.trim().equals("")) {
                isExist = true;
            } else {
                isExist = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        btnCreate = (Button) findViewById(R.id.new_note_btn);
        etTitle = (EditText) findViewById(R.id.new_note_title);
        etDetials = (EditText) findViewById(R.id.new_note_content);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fAuth = FirebaseAuth.getInstance();
        fJournalDatabase = FirebaseDatabase.getInstance().getReference().child("journaltb").child(fAuth.getCurrentUser().getUid());

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = etTitle.getText().toString().trim();
                String detials = etDetials.getText().toString().trim();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(detials)) {
                    createNote(title, detials);
                } else {
                    Snackbar.make(view, "Fill empty fields", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        putData();
    }

    private void putData() {

        if (isExist) {
            fJournalDatabase.child(journalID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("detials")) {
                        String title = dataSnapshot.child("title").getValue().toString();
                        String detials = dataSnapshot.child("detials").getValue().toString();

                        etTitle.setText(title);
                        etDetials.setText(detials);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    private void createNote(String title, String detials) {

        if (fAuth.getCurrentUser() != null) {

            if (isExist) {
                // UPDATE A Journal
                Map<String, Object> updateMap = new HashMap();
                updateMap.put("title", etTitle.getText().toString().trim());
                updateMap.put("detials", etDetials.getText().toString().trim());
                updateMap.put("timestamp", ServerValue.TIMESTAMP);

                fJournalDatabase.child(journalID).updateChildren(updateMap);

                Toast.makeText(this, "Journal Updated", Toast.LENGTH_SHORT).show();
            } else {
                // CREATE A NEW NOTE
                final DatabaseReference newNoteRef = fJournalDatabase.push();

                final Map<String, Object> noteMap = new HashMap();
                noteMap.put("title", title);
                noteMap.put("details", detials);
                noteMap.put("timestamp", ServerValue.TIMESTAMP);

                Thread mainThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(NewNoteActivity.this, "Journal added to database", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NewNoteActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });
                mainThread.start();
            }



        } else {
            Toast.makeText(this, "USERS IS NOT SIGNED IN", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.new_note_delete_btn:
                if (isExist) {
                    deleteNote();
                } else {
                    Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return true;
    }

    private void deleteNote() {

        fJournalDatabase.child(journalID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NewNoteActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    journalID = "no";
                    finish();
                } else {
                    Log.e("NewNoteActivity", task.getException().toString());
                    Toast.makeText(NewNoteActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
