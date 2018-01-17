package com.example.ratan.atc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class register extends AppCompatActivity {

    String uid="1";
    String urla=new getuser().getUrl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Signup");




        Button register=(Button) findViewById(R.id.button3);
        EditText username=(EditText) findViewById(R.id.uname);
        EditText full=(EditText) findViewById(R.id.fname);
        EditText passwd=(EditText) findViewById(R.id.passet);
        EditText passwd2=(EditText) findViewById(R.id.passet3);
        TextView tv=(TextView) findViewById(R.id.textView);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.JSONAsyncTask  task =new register.JSONAsyncTask();

                String p1=passwd.getText().toString();
                String p2=passwd2.getText().toString();


                if (p1.equals("")||p2.equals("")||full.getText().toString().equals("")||username.getText().toString().equals(""))
                {
                    tv.setText("Please fill all feilds");


                tv.setVisibility(View.VISIBLE);
                }
                else{
                    if(p1.length()<8)
                    {
                        tv.setText("Password length must be minimum 8 chars");
                        tv.setVisibility(View.VISIBLE);
                    }
                   else if(!p1.equals(p2))
                   {
                       tv.setText("Passwords do not match");
                       tv.setVisibility(View.VISIBLE);

                   }
                   else{
                    task.execute(urla+"adduser.php?uname="+username.getText().toString()+"&pwd="+passwd.getText().toString()+"&fname="+full.getText().toString(),"","");
                }
                }}
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
            if(fresult.equals("sucess:"))
            {
                Toast toast = Toast.makeText(register.this, "Register Sucessfull", Toast.LENGTH_LONG);
                toast.show();
//                Intent it=new Intent(register.this,login.class);
//                startActivity(it);
                finish();
            }
            else if (fresult.contains("err:"))
            {
                TextView tv=(TextView) findViewById(R.id.textView);
                tv.setText(fresult.replace("err:",""));
                tv.setVisibility(View.VISIBLE);
            }

        }
    }

}
