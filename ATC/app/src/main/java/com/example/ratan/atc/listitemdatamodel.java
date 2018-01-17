package com.example.ratan.atc;

/**
 * Created by Ramiz on 12/13/17.
 */

public class listitemdatamodel {
    String message;
    String username;

    public listitemdatamodel(String message, String username, String userid, String reciverid, String fname) {
        this.message = message;
        this.username = username;
        this.fname = fname;
        this.userid = userid;
        this.reciverid = reciverid;
    }

    public String getFname() {
        return fname;
    }

    String fname;

    public listitemdatamodel(String message, String username, String userid, String reciverid) {
        this.message = message;
        this.username = username;
        this.userid = userid;
        this.reciverid = reciverid;
    }

    String userid;

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getUserid() {
        return userid;
    }

    public String getReciverid() {
        return reciverid;
    }

    String reciverid;

}
