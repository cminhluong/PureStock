package com.example.purestock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.purestock.Fragement.ProfileFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Add_note_activity extends AppCompatActivity {

    Button submit;
    EditText text;
    com.example.purestock.DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_note_activity );
        submit = findViewById( R.id.note_submite );
        text = findViewById( R.id.add_content );


       database = new  DatabaseHelper(this);
        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_text = text.getText().toString();
                final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                Boolean insertNote = database.insertNote( str_text, date, "001" );


                if (insertNote){
                    Toast.makeText(Add_note_activity.this, "Success add Note", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Add_note_activity.this, Add_note_activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Add_note_activity.this, "Failed to insert Note", Toast.LENGTH_LONG).show();
                }

            }


        } );
    }
}
