package com.example.purestock.Fragement;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.purestock.Add_note_activity;
import com.example.purestock.DatabaseHelper;
import com.example.purestock.ListViewAdapter;
import com.example.purestock.MainActivity;
import com.example.purestock.Model.User;
import com.example.purestock.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoteFragment extends Fragment {
    private ImageButton add;
    private DatabaseHelper dbHelper;
    private ArrayList<HashMap<String, SpannableString>> resultEndpointList;
    private ListViewAdapter listviewDisplayAdapter;
    private ListView displayNotes;
    private List<String[]> notes;
    private int currUID;
    private List<String> nid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        dbHelper = new DatabaseHelper(view.getContext());
        add = view.findViewById(R.id.notes_add);
        displayNotes = (ListView) view.findViewById(R.id.notes_display_listview);

        resultEndpointList = new ArrayList<HashMap<String, SpannableString>>();
        User us = new User();
        currUID = us.getUid();
        notes = new ArrayList<String[]>();
        nid = new ArrayList<String>();

        int tmpIDs[] = new int[3];
        tmpIDs[0] = R.id.notes_listview_display_first_column;
        tmpIDs[1] = R.id.notes_listview_display_second_column;

        listviewDisplayAdapter = new ListViewAdapter(((MainActivity) getActivity()), resultEndpointList, R.layout.notes_listview_display, 2, tmpIDs);
        listviewDisplayAdapter.setIdentifyCoumn(-1);
        displayNotes.setAdapter(listviewDisplayAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getContext(), Add_note_activity.class));
                Intent intent = new Intent(getContext(), Add_note_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("noteIndex","-1");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        displayNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Intent intent = new Intent(getContext(), Add_note_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("noteIndex",nid.get(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    private void loadNotes() {
        Cursor notesCursor = dbHelper.getData("SELECT * FROM " + DatabaseHelper.NOTES_TABLE + " WHERE UID=\"" +
                Integer.toString(currUID) + "\"");

        if (notes.size() > 0) {
            notes.clear();
            nid.clear();
        }
        resultEndpointList.clear();

        if (notesCursor != null) {
            if (notesCursor.moveToFirst()) {
                do {
                    String[] values = new String[2];
                    String nID;
                    SpannableString ss1;

                    nID = notesCursor.getString(notesCursor.getColumnIndex("NID"));
                    values[0] = notesCursor.getString(notesCursor.getColumnIndex("NOTE"));
                    values[1] = notesCursor.getString(notesCursor.getColumnIndex("DATE"));

                    if (notes != null)
                        notes.add(values);

                    if (nid != null)
                        nid.add(nID);

                    HashMap<String, SpannableString> hashmap = new HashMap<String, SpannableString>();

                    if (values[0].length() > 20)
                        ss1 = new SpannableString(values[0].substring(0, 20));
                    else
                        ss1 = new SpannableString(values[0]);
                    hashmap.put("0", ss1);

                    ss1 = new SpannableString(values[1]);
                    hashmap.put("1", ss1);

                    resultEndpointList.add(hashmap);
                } while (notesCursor.moveToNext());

                listviewDisplayAdapter.notifyDataSetChanged();
                displayNotes.setAdapter(listviewDisplayAdapter);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadNotes();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar1 = (Toolbar) view.findViewById(R.id.notes_toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar1);
        toolbar1.setTitle("Notes");

        setHasOptionsMenu(true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            loadNotes();
        }
    }
}