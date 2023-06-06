package com.example.faceliveness.Services;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInterceptor {
    private static final String BASE_URL = "https://nayanai.cloudstrats.com:8080/api/kiosk/";

    public static Retrofit getService() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory
                        .create(gson)).build();
    }

}
