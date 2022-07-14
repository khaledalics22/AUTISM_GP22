package com.example.try1;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface EndPointAPI {
//////////////////////////////sign requests//////////////////////////////////////////
    @POST("/sign-up")
    Call<SignupRequest> signUp(@Body SignUpData data);
    //////////////////////////////login requests//////////////////////////////////////////
    @POST("/login")
    Call<SignupRequest> login(@Body LoginData data);
    //////////////////////////////User requests//////////////////////////////////////////
    @GET("/me")
    Call<userinfo> Get_InfoUser(@Header("Authorization") String token);
}
