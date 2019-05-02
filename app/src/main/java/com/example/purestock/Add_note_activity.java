package com.example.purestock;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.purestock.Fragement.ProfileFragment;
import com.example.purestock.Model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Add_note_activity extends AppCompatActivity {
    private ImageButton delete;
    private Button submit, cancel;
    private EditText text;
    private DatabaseHelper dbHelper;
    private int noteIndexDB;
    private int currUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_note_activity );

        Bundle bundle = getIntent().getExtras();
        Intent intent=getIntent();
        if(intent.hasExtra("noteIndex")) {
            noteIndexDB = Integer.parseInt(intent.getStringExtra("noteIndex"));
        }

        Toolbar toolbar1 = (Toolbar)findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("Note Modifier");

        delete = findViewById(R.id.notes_delete);
        submit = findViewById( R.id.note_submit );
        cancel = findViewById( R.id.note_cancel );
        text = findViewById( R.id.add_content );
        dbHelper = new  DatabaseHelper(this);
        User us = new User();
        currUID = us.getUid();

        if(noteIndexDB != -1)
        {
            loadNote(noteIndexDB);
        }

        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_text = text.getText().toString();
                final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                Boolean insertNote;

                if(noteIndexDB== -1)
                    insertNote  = dbHelper.insertNote( str_text, date, Integer.toString(currUID));
                else
                    insertNote = dbHelper.updateNote(noteIndexDB, str_text, date, Integer.toString(currUID));

                if (insertNote){
                    Toast.makeText(Add_note_activity.this, "Note saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Add_note_activity.this, "Failed to save Note", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        } );

        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noteIndexDB != -1) {
                    Boolean insertNote;
                    insertNote = dbHelper.deleteNote(noteIndexDB);

                    if (insertNote) {
                        Toast.makeText(Add_note_activity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Add_note_activity.this, "Failed to delete Note", Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            }
        });
    }

    private void loadNote(int nid) {
        Cursor noteCursor = dbHelper.getData("SELECT * FROM " + DatabaseHelper.NOTES_TABLE + " WHERE NID=\"" +
                Integer.toString(nid) + "\"");

        if (noteCursor != null) {
            if (noteCursor.moveToFirst()) {
                do {
                    text.setText(noteCursor.getString(noteCursor.getColumnIndex("NOTE")));//, TextView.BufferType.EDITABLE);
                } while (noteCursor.moveToNext());
            }
        }
    }
}
