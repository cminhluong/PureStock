package com.example.purestock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserService {

    private DatabaseHelper dbHelper;

    public UserService(Context context){

        dbHelper = new DatabaseHelper(context);

    }
    public boolean login(int uid, String username,String password){

        SQLiteDatabase sdb= dbHelper.getReadableDatabase();

        String sql="select * from user where uid=? and username=? and password=?";

        Cursor cursor=sdb.rawQuery(sql, new String[]{String.valueOf( uid ), username,password});

        if(cursor.moveToFirst()==true){

            cursor.close();

            return true;

        }

        return false;

    }
}
