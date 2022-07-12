package com.example.try1;

import com.google.gson.annotations.SerializedName;

public class SignupRequest {
    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
