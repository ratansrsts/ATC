package com.example.ratan.atc;

import android.content.Context;
import android.content.SharedPreferences;

public class getuser {
    String username;

    String Url;


    public String getUrl() {
        return "http://autotranslatechat.000webhostapp.com/";
    }

    public String getUsername(Context cn) {

        String a="null";

        try
        {

            SharedPreferences settings = cn.getSharedPreferences("UserInfo", 0);
            a=settings.getString("uname", "").toString();
        }
        catch(Exception ex)
        {
            a="null";
        }
        return a;


    }

    public String getUid(Context cn) {

        String a="null";

        try
        {

            SharedPreferences settings = cn.getSharedPreferences("UserInfo", 0);
            a=settings.getString("uid", "").toString();
        }
        catch(Exception ex)
        {
            a="null";
        }
        return a;
    }

    String uid;

}
