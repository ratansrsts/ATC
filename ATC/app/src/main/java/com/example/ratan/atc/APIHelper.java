package com.example.ratan.atc;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ratan on 12/3/2017.
 */


public interface APIHelper {

    @POST("api/v1.5/tr.json/translate")
    Call<TranslatedText> getTranslation(@Query("key") String APIKey,
                                        @Query("text") String textToTranslate,
                                        @Query("lang") String lang);
}
