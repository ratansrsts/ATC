package com.example.ratan.atc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class login extends AppCompatActivity {


    String uid="1";
    String urla=new getuser().getUrl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        if(!(new getuser().getUid(this).equals("null"))&&!(new getuser().getUid(this).equals("")))
        {

            Intent it=new Intent(login.this,Main2Activity.class);
            startActivity(it);
            finish();
        }


        Button login=(Button) findViewById(R.id.button2);
        Button register=(Button) findViewById(R.id.button3);
        EditText username=(EditText) findViewById(R.id.unamelogin);
        EditText passwd=(EditText) findViewById(R.id.passet);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.JSONAsyncTask  task =new login.JSONAsyncTask();
                task.execute(urla,username.getText().toString(),passwd.getText().toString());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(login.this,register.class);
                startActivity(it);

            }
        });

    }

    class JSONAsyncTask extends AsyncTask<String, String, String> {

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
                        .url(urls[0]+"login.php?uname="+urls[1]+"&pwd="+urls[2])
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

            try {
                JSONObject mainjob=new JSONObject(result);
                JSONArray innerjob=mainjob.getJSONArray("Sucess");

                for(int a=0;a<=innerjob.length()-1;a++)
                {
                    SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("uid",innerjob.getJSONObject(0).getString("usrid"));
                    editor.putString("uname",innerjob.getJSONObject(0).getString("UserName"));
                    editor.commit();
                    Intent it=new Intent(login.this,Main2Activity.class);
                    startActivity(it);
                    finish();
                }


                // fresult.getJSONObject("Sucess");

            } catch (JSONException e) {
                e.printStackTrace();
                TextView tv=(TextView) findViewById(R.id.textView);
                tv.setVisibility(View.VISIBLE);
            }

        }
    }

    }

