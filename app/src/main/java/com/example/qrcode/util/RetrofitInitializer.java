package com.example.qrcode.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInitializer {

    public static Retrofit retrofit;
    public static OkHttpClient.Builder httpClient =  new OkHttpClient.Builder();

    public static Retrofit getRetrofitInstance(){

        if(retrofit == null){

            retrofit = new Retrofit.Builder()
                    //.baseUrl("http://everis.morphodemos.com/service/") //GET
                    .baseUrl("http://everis.morphodemos.com/service/mbss/") //POST
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
