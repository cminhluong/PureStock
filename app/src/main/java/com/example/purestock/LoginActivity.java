package com.example.purestock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.purestock.Model.User;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    TextView txt_signup;
    static int uid;
    public static int uids;
    com.example.purestock.DatabaseHelper database;
    //FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        txt_signup = findViewById(R.id.txt_signup);
        //final User us = (User) getApplication();

       // auth = FirebaseAuth.getInstance();
        database = new  com.example.purestock.DatabaseHelper(this);
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                String str_username = username.getText().toString();
                String str_password = password.getText().toString();

                if(TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_password)){
                    Toast.makeText(LoginActivity.this, "All fileds are required!", Toast.LENGTH_SHORT).show();
                } else {
//                    auth.signInWithEmailAndPassword(str_email, str_password)
//                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if(task.isSuccessful()){
//                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
//                                                .child(auth.getCurrentUser().getUid());
//
//                                        reference.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                pd.dismiss();
//                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                pd.dismiss();
//                                            }
//                                        });
//                                    } else {
//                                        pd.dismiss();
//                                        Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
//                                    }
//                                }

//                            });


                    SQLiteDatabase sdb= database.getReadableDatabase();
                    UserService uService = new UserService(LoginActivity.this);
                    boolean flag = uService.login(str_username, str_password);
                    if(flag){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        //Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_LONG).show();

                       // String sql="select uid from user where username=" + str_username;
                        //String args[] = {"uid" + "=?"};

                       // rawQuery("SELECT id, name FROM people WHERE name = ? AND id = ?", new String[] {"David", "2"}
                        Cursor cursor=sdb.rawQuery("select uid from user where username= ?", new String[]{str_username});

                        while (cursor.moveToNext()) {
                            uids = cursor.getInt( cursor.getColumnIndex( "UID" ) );
                            //String useid = Integer.toString( uids );
                            User us = new User(uids);
                               us.setUsername( str_username );
                               us.setUid( uids );
                            //Intent inte = new Intent(LoginActivity.this,TransactionActivity.class);

                            //inte.putExtra("UID", uids);



                            Toast.makeText(LoginActivity.this, "useid" + uids, Toast.LENGTH_LONG).show();
                           // startActivity(inte);
                            //Toast.makeText(LoginActivity.this, "uid" + us.getUid(), Toast.LENGTH_LONG).show();
                        }
//                        if(cursor !=  null && cursor.moveToFirst()){
//                            do {
//                                uid = cursor.getInt(cursor.getColumnIndex("UID"));
//                                User us = new User(uid,str_username,str_password  );
//                                us.setUsername( str_username );
//                                us.setUid( uid );
//                            }while(cursor.moveToNext());
//                        }
//                        while(cursor.moveToNext()){
//
//
//
//                             Toast.makeText(LoginActivity.this, "uid" + uid, Toast.LENGTH_LONG).show();
//
//
//                        }

//                        us.setUsername(str_username);
//                        us.setUid(uid);
                        cursor.close();
                    }else{
                        Toast.makeText(LoginActivity.this, "Fail to login", Toast.LENGTH_LONG).show();
                    }
                }



            }
        });
    }

}