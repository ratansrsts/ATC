package com.example.ratan.atc;

public class chatlistdataset {
    String username;

    public String getUid() {
        return uid;
    }

    String uid;
    String message;
    String time;

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public chatlistdataset(String username, String message, String time,String uid) {
        this.username = username;
        this.message = message;
        this.time = time;
        this.uid = uid;
    }
}
