package com.stubborn.android.loginregistrationretrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://half-witted-transis.000webhostapp.com/";
    private static Retrofit retrofit;

    public static Retrofit getAppClient(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

        }
        return retrofit;

    }
}