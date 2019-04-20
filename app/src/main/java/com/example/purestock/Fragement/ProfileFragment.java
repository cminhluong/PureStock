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

import com.example.purestock.MainActivity;
import com.example.purestock.R;
import com.example.purestock.TransactionActivity;

public class ProfileFragment extends Fragment {

    Button trans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate( R.layout.fragment_profile, container,  false);
        trans = view.findViewById( R.id.transaction );

        trans.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getContext(), TransactionActivity.class ) );
            }
        } );


        return view ;





    }


}
