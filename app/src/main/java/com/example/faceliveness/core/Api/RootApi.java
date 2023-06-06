package com.example.faceliveness.core.Api;

import com.example.faceliveness.Models.Request.IdentifyPersonRequest;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RootApi {
    @Headers({"Authorization: Bearer 24d703fdeefadb5c456221e0-ba24-4bd6-83f9-3cf9f86f71d2@auth_iddf57ad713b372a6d"})
    @POST("identify_person")
    Call<ResponseBody> identifyPerson(@Body IdentifyPersonRequest identifyPersonRequest);
}
