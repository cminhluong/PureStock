package com.example.purestock;


import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtilities {
    public String getCurrentDatetime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String datetime = sdf.format(new Date(System.currentTimeMillis()));

        return datetime;
    }
}