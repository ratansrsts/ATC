package com.example.ratan.atc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;

public class chatactivity extends AppCompatActivity {

    String uid;
    String rid;
    String urla;
    String username;
    EditText edt;
    ArrayList<chatlistdataset> listItems=new ArrayList<chatlistdataset>();
    ListView listView;

    private Spinner spinner1;
    private Spinner spinner2;
    private EditText textToTranslate;
    private ImageButton addToFavourites;
    private ImageButton changeLanguages;
    private TextView translatedText;
    private boolean isFavourite; // if current word is favourite.
    private boolean noTranslate; // do not translate at 1-st text changing. Need when initialize
    // with some text.

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private static chatlistadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);

        Intent intent = getIntent();
         uid = intent.getStringExtra("id");
         rid = intent.getStringExtra("rid");
         username = intent.getStringExtra("fn");
        setTitle(username);
        urla=new getuser().getUrl()+"getmessages.php?user_id="+intent.getStringExtra("id")+"&recive_id="+intent.getStringExtra("rid");;
        listView=(ListView)findViewById(R.id.myList);

        listItems= new ArrayList<>();
        adapter= new chatlistadapter(listItems,getApplicationContext());


        listView.setAdapter(adapter);

        chatactivity.JSONAsyncTask task=new chatactivity.JSONAsyncTask();
        task.execute("",urla,"first");
        setRepeatingAsyncTask();


        Button btn=(Button)findViewById(R.id.do_email_picker);
         edt=(EditText)findViewById(R.id.email_hint);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                chatactivity.sendmessage task=new chatactivity.sendmessage();
                task.execute(translatedText.getText().toString(),"","");


            }
        });


        spinner1 = (Spinner) findViewById(R.id.languages1);
        spinner2 = (Spinner) findViewById(R.id.languages2);
        textToTranslate = edt;
        textToTranslate.setMovementMethod(new ScrollingMovementMethod());
        textToTranslate.setVerticalScrollBarEnabled(true);
        changeLanguages = (ImageButton) findViewById(R.id.changeLanguages);
       // addToFavourites = (ImageButton) rootView.findViewById(R.id.addToFavourites1);
        translatedText = (TextView) findViewById(R.id.translatedText);
        translatedText.setMovementMethod(new ScrollingMovementMethod());
        translatedText.setVerticalScrollBarEnabled(true);

        edt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                translate(edt.getText().toString());
                return false;
            }
        });

        setArgs();

        setSpinners();
        textChangedListener();
        addButtonListener();
    }

    public void setArgs() {
        SharedPreferences sharedPref = getSharedPreferences("default", Context.MODE_PRIVATE);
        String text = sharedPref.getString("textToTranslate", "");
        String translation = sharedPref.getString("translatedText", "");
        int selection1 = sharedPref.getInt("selection1", 0);
        int selection2 = sharedPref.getInt("selection2", 1);
        isFavourite = sharedPref.getBoolean("isFavourite", false);
        if (!text.equals("")) {
            noTranslate = true;
            textToTranslate.setText(text);
            spinner1.setSelection(selection1);
            spinner2.setSelection(selection2);
            translatedText.setText(translation);
            addToFavourites.setVisibility(View.VISIBLE);

            if(isFavourite){
                addToFavourites.setImageResource(R.drawable.selected_favourites_icon);
            } else{
                addToFavourites.setImageResource(R.drawable.default_favourites_icon);
            }
        }
    }

    public void addButtonListener() {

        changeLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sourceLng = spinner1.getSelectedItemPosition();
                int targetLng = spinner2.getSelectedItemPosition();

                spinner1.setSelection(targetLng);
                spinner2.setSelection(sourceLng);

                translate(textToTranslate.getText().toString().trim());
            }
        });

    }

    public void setSpinners() {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        if(Locale.getDefault().getLanguage().equals("en")) {
            Collections.addAll(categories, Languages.getLangsEN());
        } else{
            Collections.addAll(categories, Languages.getLangsRU());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter);
        spinner2.setSelection(1);
    }

    public void textChangedListener() {

        // Translate the text after 500 milliseconds when user ends to typing
        RxTextView.textChanges(textToTranslate).
                filter(charSequence -> charSequence.length() > 0).
                debounce(100, TimeUnit.MILLISECONDS).
                subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        translate(charSequence.toString().trim());
                    }
                });

        RxTextView.textChanges(textToTranslate).
                filter(charSequence -> charSequence.length() == 0).
                subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                });
    }

    private void translate(String text){
        if(noTranslate){
            noTranslate = false;
            return;
        }

        String APIKey = "trnsl.1.1.20170314T200256Z.c558a20c3d6824ff.7" +
                "860377e797dffcf9ce4170e3c21266cbc696f08";
        String language1 = String.valueOf(spinner1.getSelectedItem());
        String language2 = String.valueOf(spinner2.getSelectedItem());

        Retrofit query = new Retrofit.Builder().baseUrl("https://translate.yandex.net/").
                addConverterFactory(GsonConverterFactory.create()).build();
        APIHelper apiHelper = query.create(APIHelper.class);
        Call<TranslatedText> call = apiHelper.getTranslation(APIKey, text,
                langCode(language1) + "-" + langCode(language2));

        call.enqueue(new Callback<TranslatedText>() {
            @Override
            public void onResponse(Call<TranslatedText> call, retrofit2.Response<TranslatedText> response) {
                if(response.isSuccessful()){
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            translatedText.setText(response.body().getText().get(0));

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TranslatedText> call, Throwable t) {}
        });
    }

    public String langCode(String selectedLang) {
        String code = null;

        if(Locale.getDefault().getLanguage().equals("en")) {
            for (int i = 0; i < Languages.getLangsEN().length; i++) {
                if(selectedLang.equals(Languages.getLangsEN()[i])){
                    code = Languages.getLangCodeEN(i);
                }
            }
        } else{
            for (int i = 0; i < Languages.getLangsRU().length; i++) {
                if(selectedLang.equals(Languages.getLangsRU()[i])){
                    code = Languages.getLangCodeRU(i);
                }
            }
        }
        return code;
    }

    class JSONAsyncTask extends AsyncTask<String, String, String> {

        OkHttpClient client = new OkHttpClient();

        String fresult;
        String prevresult;
        String data;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            data=urls[2];
            try {
                Request request = new Request.Builder()
                        .url(urls[1])
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

    class sendmessage extends AsyncTask<String, String, String> {

        OkHttpClient client = new OkHttpClient();

        String fresult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        String data;
        @Override
        protected String doInBackground(String... urls) {




            try {
                Request request = new Request.Builder()
                        .url(new getuser().getUrl()+"sendmessages.php?uname="+rid+"&rid="+uid+"&message="+urls[0])
                        .build();
data=urls[0];
                try (Response response = client.newCall(request).execute())
                {
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
            if(result.equals("suxsex"))
            {
               listItems.add(new chatlistdataset(new getuser().getUsername(chatactivity.this),data,"true",""));

                adapter.notifyDataSetChanged();
                edt.setText("");
                listView.setSelection(adapter.getCount()-1);


            }
            else
            {
                Snackbar snackbar = Snackbar
                        .make(getCurrentFocus(), "Message Sending Failed", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    void onpostexcecute(String result) {

        try {
            JSONObject mainjob=new JSONObject(result);
            JSONArray innerjob=mainjob.getJSONArray("Sucess");
            listItems.clear();
            if(innerjob.length()>adapter.getCount())
            {
                String aa="";
                listItems.clear();

                for (int a = 0; a <= innerjob.length() - 2; a++) {
                  // listItems.add();
                listItems.add(new chatlistdataset(innerjob.getJSONObject(a).getString("username")+"", innerjob.getJSONObject(a).getString("Message")+"","",innerjob.getJSONObject(a).getString("reciver_id")+""));


                    // listItems.add();
                    //  listItems.add(new listitemdatamodel(innerjob.getJSONObject(a).getString("Message"), ""+innerjob.getJSONObject(a).getString("username"),innerjob.getJSONObject(a).getString("usrid")+"",""+innerjob.getJSONObject(a).getString("reciver_id")));

                }}

            adapter.notifyDataSetChanged();
            listView.setSelection(adapter.getCount()-1);
            // fresult.getJSONObject("Sucess");

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
                            chatactivity.JSONAsyncTask task=new chatactivity.JSONAsyncTask();
                            task.execute("",urla,"");
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
