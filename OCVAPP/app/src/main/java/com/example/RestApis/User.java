package com.example.RestApis;

/**
 * Holds current user data
 */
public class User {
    private static String Firstname = null;
    private static String Lastname = null;
    private static String email = null;
    private static String password = null;
    private static String    userName = null;
    private static String    gander = null;
    private static String    brithDay = null;
    private static String Token=null;

    public static int getType() {
        return type;
    }

    public static void setType(int type) {
        User.type = type;
    }

    private static int type=0;

    public User() {
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
        User.userName = userName;
    }

    public static String getGander() {
        return gander;
    }

    public static void setGander(String gander) {
        User.gander = gander;
    }

    public static String getBrithDay() {
        return brithDay;
    }

    public static void setBrithDay(String brithDay) {
        User.brithDay = brithDay;
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
        User.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    }


