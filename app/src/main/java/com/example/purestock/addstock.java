package com.example.purestock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addstock extends AppCompatActivity {

    Button submit;
    EditText stock_id, companyname;
    DatabaseHelper database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_addstock );
        //username = findViewById(R.id.username);
        stock_id = findViewById( R.id.StockId );
        companyname = findViewById( R.id.companyname );
        submit = findViewById( R.id.submit_stock);
        database = new  com.example.purestock.DatabaseHelper(this);

        submit.setOnClickListener( new View.OnClickListener() {

            public void onClick(View v) {
                String str_stock_id = stock_id.getText().toString();
                String str_companyname_str = companyname.getText().toString();
                if(TextUtils.isEmpty( str_stock_id ) || TextUtils.isEmpty( str_companyname_str ) ){
                    Toast.makeText( addstock.this, "all filed required", Toast.LENGTH_SHORT ).show();
                } else{
                    Boolean insertStock = database.insertStock(str_stock_id,  str_companyname_str);
                    if(insertStock){
                        Toast.makeText( addstock.this, "Success insert stock data", Toast.LENGTH_SHORT ).show();
                        Intent intent = new Intent(addstock.this, addstock.class);
                        startActivity( intent );
                    } else {
                        Toast.makeText( addstock.this, "Failed to add data", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        } );
    }
}

/*String str_username = username.getText().toString();
                String str_fullname = fullname.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname)
                        || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)){
                    Toast.makeText(RegisterActivity.this,
                            "All fields are required!", Toast.LENGTH_SHORT).show();
                } else if(str_password.length() < 6){
                    Toast.makeText(RegisterActivity.this,
                            "Password must have 6 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean insertUser = database.insertUser(str_username, str_password, str_fullname , str_email);
                    if (insertUser){
                        Toast.makeText(RegisterActivity.this, "Success to insert user data", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        pd.dismiss();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed to insert user data", Toast.LENGTH_LONG).show();
                    }
                }*/