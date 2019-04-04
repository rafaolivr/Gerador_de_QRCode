package com.example.qrcode.api;

import com.example.qrcode.model.RetornoGET;
import com.example.qrcode.model.RetornoPOST;
import com.example.qrcode.model.VisitanteAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BioAPI {

    @GET("person?person_id=35474399843")
    Call<RetornoGET> verificarId(@Header("Authorization") String token);


    @Headers("Content-Type: application/json")
    @POST("enroll?person_id=35474399844")
    Call<RetornoPOST> criarVisitante(@Body VisitanteAPI visitanteAPI,
                                     @Header("Authorization") String token
    );
}