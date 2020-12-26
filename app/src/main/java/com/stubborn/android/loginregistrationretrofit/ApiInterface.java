package com.stubborn.android.loginregistrationretrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @Multipart
    @POST("upload.php")
    Call<MyResponse> uploadImage(
            @Part("id") MultipartBody.Part id,
            @Part("name") RequestBody name,
            @Part RequestBody image);
}
/*
Here we should remember that every field(except image) in interface will be of RequestBody type and @Part annotation will be used in place of @Field
*/
