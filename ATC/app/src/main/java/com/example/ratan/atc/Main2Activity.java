package com.example.ratan.atc;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Main2Activity extends AppCompatActivity {

    int Length;
    getuser gt=new getuser();
    String uid;
    String urla=new getuser().getUrl()+"getmessages.php?user_id=";
    ArrayList<listitemdatamodel> listItems=new ArrayList<listitemdatamodel>();
    ListView listView;
    TextView errtv;
    Dialog dialog;
    Timer timer;
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private static listitemadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle(new getuser().getUsername(this));
        uid=gt.getUid(this);
        listView=(ListView)findViewById(R.id.listviews);

        listItems= new ArrayList<>();

        bottomNavigationListener();
        adapter= new listitemadapter(listItems,getApplicationContext());


        listView.setAdapter(adapter);

        JSONAsyncTask task=new JSONAsyncTask();
        task.execute(uid,urla,"first");
        // new JSONAsyncTask.execute("","","");

       //timer = new Timer();
      // timer.schedule(new WaitTimer(), 0, 5000);
        FloatingActionButton fl=(FloatingActionButton) findViewById(R.id.floatingActionButton2);
        setRepeatingAsyncTask();

        // add button listener
        fl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                  dialog = new Dialog(Main2Activity.this);
                dialog.setContentView(R.layout.newmessagemodallayout);
                dialog.setTitle("Add Friend.");

                // set the custom dialog components - text, image and button
                EditText text = (EditText) dialog.findViewById(R.id.editText);
                 errtv = (TextView) dialog.findViewById(R.id.textView2);



                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonCancel);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        checkusername task=new checkusername();
                        task.execute(new getuser().getUrl()+"getusers.php?Username="+text.getText(),"","");


                    }
                });

                dialogButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

    public void bottomNavigationListener() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_translate:
                                logout();
                                break;
                            case R.id.action_favourites:
                               // changeToListView("Favourites.db");
                                break;

                        }
                        return true;
                    }
                });
    }

    void logout()
    {
        SharedPreferences sharedPref= getSharedPreferences("UserInfo",0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();      //its clear all data.
        editor.commit();  //Don't forgot to commit  SharedPreferences.
        Intent it=new Intent(this,login.class);
        startActivity(it);
        finish();

    }


    String prevresult;


    class JSONAsyncTask extends AsyncTask<String, String, String> {

        OkHttpClient client = new OkHttpClient();

        String fresult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        String data;
        @Override
        protected String doInBackground(String... urls) {


           data=urls[2];

            try {
                Request request = new Request.Builder()
                        .url(urls[1]+urls[0])
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    fresult = response.body().string();
                }
                //return fresult.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fresult.toString();
        }

        @Override
        protected void onPostExecute(String result) {


            if(data.equals("first"))
            {
                onpostexcecute(result);
            }

            if(!result.equals(prevresult)) {
                onpostexcecute(result);
            }
            prevresult = result;



        }


    }


    class checkusername extends AsyncTask<String, String, String> {

        OkHttpClient client = new OkHttpClient();

        String fresult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {


            String data;

            try {
                Request request = new Request.Builder()
                        .url(urls[0])
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    fresult = response.body().string();
                }
                //return fresult.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fresult.toString();
        }

        @Override
        protected void onPostExecute(String result) {
          if (!fresult.equals("{\"Sucess\":[]}")) {
                errtv.setVisibility(View.INVISIBLE);
                try {
                    JSONObject mainjob = new JSONObject(result);
                    JSONArray innerjob = mainjob.getJSONArray("Sucess");

                    for (int a = 0; a <= innerjob.length() - 1; a++) {

                        if(innerjob.getJSONObject(a).getString("usrid").equals(new getuser().getUid(Main2Activity.this)))
                        {

                            errtv.setText("Username invalid.");
                            errtv.setVisibility(View.VISIBLE);

                        }
                        else {
                            Intent i = new Intent(Main2Activity.this, chatactivity.class);
                            i.putExtra("rid", innerjob.getJSONObject(a).getString("usrid"));

                             i.putExtra("id", new getuser().getUid(Main2Activity.this));
                            i.putExtra("fn", innerjob.getJSONObject(a).getString("Fullname"));

                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            dialog.dismiss();
                        }
                      //  finish();

                       // listItems.add(new listitemdatamodel(innerjob.getJSONObject(a).getString("Message"), "" + innerjob.getJSONObject(a).getString("username"), innerjob.getJSONObject(a).getString("usrid") + "", "" + innerjob.getJSONObject(a).getString("reciver_id")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                errtv.setText("User not found.");
                errtv.setVisibility(View.VISIBLE);
            }
        }
    }



    void onpostexcecute(String result)
    {

        try {
            JSONObject mainjob=new JSONObject(result);
            JSONArray innerjob=mainjob.getJSONArray("Sucess");
            if(innerjob.length()>adapter.getCount())
            {
                String aa="";
                listItems.clear();

                for (int a = 0; a <= innerjob.length() - 2; a++) {
                    aa+="|UserID:"+innerjob.getJSONObject(a).getString("usrid")+"| |Reciverid:"+innerjob.getJSONObject(a).getString("reciver_id")+"|";
                    if(!aa.contains("|UserID:"+innerjob.getJSONObject(a).getString("usrid")+"|")||!aa.contains("|Reciverid:"+innerjob.getJSONObject(a).getString("reciver_id")+"|")||!aa.contains("|UserID:"+innerjob.getJSONObject(a).getString("reciver_id")+"|")||!aa.contains("|Reciverid:"+innerjob.getJSONObject(a).getString("usrid")+"|")) {

                        listItems.add(new listitemdatamodel(innerjob.getJSONObject(a).getString("Message") + "", "" + innerjob.getJSONObject(a).getString("username"), innerjob.getJSONObject(a).getString("usrid") + "", "" + innerjob.getJSONObject(a).getString("reciver_id"), "" + innerjob.getJSONObject(a).getString("Fullname")));

                    }

                    // listItems.add();
                    //  listItems.add(new listitemdatamodel(innerjob.getJSONObject(a).getString("Message"), ""+innerjob.getJSONObject(a).getString("username"),innerjob.getJSONObject(a).getString("usrid")+"",""+innerjob.getJSONObject(a).getString("reciver_id")));

                }


                adapter.notifyDataSetChanged();
                // fresult.getJSONObject("Sucess");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void setRepeatingAsyncTask() {

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            JSONAsyncTask task=new JSONAsyncTask();
                            task.execute(uid,urla,"");
                        } catch (Exception e) {
                            // error, do something
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 5000);  // interval of one minute

    }
}
