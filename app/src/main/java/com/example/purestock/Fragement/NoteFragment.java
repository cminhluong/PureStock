package com.example.purestock.Fragement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.purestock.Add_note_activity;
import com.example.purestock.Display_note_activity;
import com.example.purestock.R;
import com.example.purestock.historyActivity;

public class NoteFragment extends Fragment {


    Button add;

    com.example.purestock.DatabaseHelper database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_note, container,  false);

        add = view.findViewById( R.id.add_note );

        //database = new com.example.purestock.DatabaseHelper(this);

    add.setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent( getContext(), Add_note_activity.class ));
        }
    } );

        return view;
    }

}
