package com.example.purestock.Model;


import android.app.Application;

public class User extends Application {

    private String username;
    private String password;
    public static int uid;
    /*private String fullname;
    private String email;*/

    //, String fullname, String email
    public User(int uid, String username, String password){
        this.username = username;
        this.password = password;
        this.uid = uid;
        /*this.fullname = fullname;
        this.email = email;*/
    }

    public User(){

    }
    public User(int uid){
        this.uid = uid;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public static int getUid(){return uid;}
    public void setUid(int uid) {this.uid = uid;}

   /* public String getFullname(){
        return fullname;
    }
    public void setFullname(String fullname){
        this.fullname = fullname;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }*/
}
