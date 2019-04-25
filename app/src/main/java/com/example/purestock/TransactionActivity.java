package com.example.purestock;

import android.app.ActivityManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.purestock.Fragement.ProfileFragment;
import com.example.purestock.Model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    Button submit;
    EditText stock_id, price, numbs, type;

   // Spinner dropdown = findViewById(R.id.spinner1);
   // username
   String[] items = new String[]{"1", "2"};


    com.example.purestock.DatabaseHelper database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_transaction );


        database = new  DatabaseHelper(this);

        submit = findViewById( R.id.submit_transcation );
        //username = findViewById( R.id.Trans_Username );
        stock_id = findViewById( R.id.Trans_stockID );
        price = findViewById( R.id.Trans_price );
        numbs = findViewById( R.id.Trans_num );
       // date = findViewById( R.id.Trans_date );
        type = findViewById( R.id.Trans_type );

//        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
//        ArrayAdapter<CharSequence> adpater = ArrayAdapter.createFromResource( this, R.array.Select, android.R.layout.simple_spinner_item  );
//        adpater.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
//        spinner.setAdapter(adpater);
        //spinner.setOnItemSelectedListener( this );

        final User us = (User) getApplication();
        final String username = us.getUsername();





        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String str_username = username.getText().toString();
                String str_stock_id = stock_id.getText().toString();
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                //String str_date = date.getText().toString();
                String str_price = price.getText().toString();
                String str_num = numbs.getText().toString();
                String str_type  = type.getText().toString();





                /*UserService userService = new UserService.(TransactionActivity.this);
                User str_username = userService.getCurrentUser();
                */
        /*
          UserService uService = new UserService(LoginActivity.this);
                    boolean flag = uService.login(str_username, str_password);
                    if(flag){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(LoginActivity.this, "Fail to login", Toast.LENGTH_LONG).show();
                    }
                }

          */

                //public boolean insertTransaction(String username, String stockID, double price, int numberStocks, boolean type, String date)




                double double_price = Double.parseDouble( str_price );
                int int_num = Integer.parseInt(str_num);
                if( TextUtils.isEmpty( str_stock_id ) || TextUtils.isEmpty( str_price )){
                    Toast.makeText( TransactionActivity.this, "All fileds are required!", Toast.LENGTH_SHORT ).show();
                } else{
                    Boolean insertTrans = database.insertTransaction( "001", str_stock_id, double_price, int_num, str_type, date);


                    if (insertTrans){
                        Toast.makeText(TransactionActivity.this, "Success add transaction", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TransactionActivity.this, ProfileFragment.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(TransactionActivity.this, "Failed to insert transaction data", Toast.LENGTH_LONG).show();
                    }

                }

            }
        } );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition( position ).toString();
        //Toast.makeText( parent.getContext(), text, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

/*DBHelper dbhelper = new DBHelper(this);
SQLiteDatabase db = dbhelper.getWritableDatabase();
Cursor cursor = db.query(Table,
    username,
    isCurrentUser + " = ? ",
    new String []{"true"},
    null, null, null);
cursor.moveToFirst();
String currentUsername = cursor.getString(cursor.getColumnIndex("username"));*/

//Boolean insertUser = database.insertUser(str_username, str_password, str_fullname , str_email);
// public boolean insertTransaction(String username, String stockID, double price, boolean type, String date)