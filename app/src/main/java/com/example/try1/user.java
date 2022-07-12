package com.example.try1;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Holds current user data
 */
public class user {
    private static String Firstname = null;
    private static String Lastname = null;
    private static String email = null;
    private static String password = null;
    private static String    userName = null;
    private static String    gander = null;
    private static String    brithDay = null;
    private static String Token=null;
    public user() {
    }

    public static String getToken() {
        return Token;
    }

    public static void setToken(String token) {
        Token = token;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        user.userName = userName;
    }

    public static String getGander() {
        return gander;
    }

    public static void setGander(String gander) {
        user.gander = gander;
    }

    public static String getBrithDay() {
        return brithDay;
    }

    public static void setBrithDay(String brithDay) {
        user.brithDay = brithDay;
    }

    public static String getFirstname() {
        return Firstname;
    }

    public static void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public static String getLastname() {
        return Lastname;
    }

    public static void setLastname(String lastname) {
        Lastname = lastname;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        user.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        user.password = password;
    }

    }


