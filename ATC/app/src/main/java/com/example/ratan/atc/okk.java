package com.example.ratan.atc;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ratan on 12/13/17.
 */

public class okk {
    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) throws IOException {
        okk example = new okk();
        String response = example.run("http://localhost:8080/chatapp/getmessages.php?user_id=1");
        System.out.println(response);
    }
}
