package com.example.purestock;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.purestock.Fragement.HomeFragment;
import com.example.purestock.Fragement.NoteFragment;
import com.example.purestock.Fragement.ProfileFragment;
import com.example.purestock.Fragement.SearchFragment;
import com.example.purestock.Fragement.Watch_ListFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation;
    Fragment selectedfragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        Bundle intent = getIntent().getExtras();
        if (intent != null){
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedfragment = new HomeFragment();
                    break;
                case R.id.nav_serach:
                    selectedfragment = new SearchFragment();
                    break;
                case R.id.nav_watch_list:
                    selectedfragment = new Watch_ListFragment();
                    break;
                case R.id.nav_note:
                    selectedfragment = new NoteFragment();
                    break;
                case R.id.nav_profile:
                    selectedfragment = new ProfileFragment();
                    break;
            }
            if (selectedfragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedfragment).commit();
            }

            return true;
        }
    };
}
// readme
/*
    version 01
    What we have now
    1. basic layout for startpage, login, register, main page and fragment for note, post, home, profile, search and watch_list
    2. basic database
    3. register is working right now

    What we have to do
    1. Database don't have login function
    2. missing a lot of function
 */