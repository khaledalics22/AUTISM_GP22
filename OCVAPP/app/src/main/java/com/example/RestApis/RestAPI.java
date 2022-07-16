package com.example.RestApis;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.Objects.Score;
import com.example.Statistics.Statistics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class RestAPI {


    private static final String BASE_URL = "https://53f6-102-42-182-216.ngrok.io/";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MmQyMDQ4MWUzNTdmODBkMGM0N2MxYzEiLCJpYXQiOjE2NTc5MzA4ODEsImV4cCI6MjQyOTAwNDUzMTM1NzY2MDgwfQ.RVZh5sNY3ieAUb5OT1eHCo0jJOsIpa5PG7Y9DJdKuIQ";
    private static RestAPI instance;

    private RestAPI() {
    }

    public static RestAPI getInstance() {
        if (instance == null) {
            instance = new RestAPI();
        }
        return instance;
    }

    public ArrayList<String> getMyProfile(Context context) {
        final ArrayList<String> result = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + "me"
                , response -> {
            try {
                JSONObject object = new JSONObject(response);
                Log.e("On Response profile-----------", response);
                result.add(response);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }, error -> Log.e("Error-Profile-----------", "" + error.getMessage())) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }
        };


        Singleton.getInstance(context).getRequestQueue().add(stringRequest);
        return result;

    }
    public ArrayList<String> setScore(Context context, Score score) {
        final ArrayList<String> result = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, BASE_URL + "me/doing"
                , response -> {
            try {
//                JSONObject object = new JSONObject(response);
                Log.e("On Response set Score -----------", "success");
//                result.add(response);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }, error -> Log.e("Error-Scores-----------", "" + error)) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                String date = android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString();
                Map<String, String> params = new Hashtable<String, String>();
                params.put("date", date);
                params.put("correctSad", String.valueOf(score.getCorrect_sad()));
                params.put("correctHappy", String.valueOf(score.getCorrect_happy()));
                params.put("correctSurprise", String.valueOf(score.getCorrect_surprised()));
                params.put("correctNatural", String.valueOf(score.getCorrect_natural()));
                params.put("errorSad", String.valueOf(score.getError_sad()));
                params.put("errorHappy", String.valueOf(score.getError_happy()));
                params.put("errorSurprise", String.valueOf(score.getError_surprised()));
                params.put("errorNatural", String.valueOf(score.getError_natural()));
                return params;
            }
        };


        Singleton.getInstance(context).getRequestQueue().add(stringRequest);
        return result;

    }

    public void getStatistics(Context context, Statistics.OnResponseSuccess responseCallBack) {
        final ArrayList<String> result = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + "me/doing" + "?all=true&emotion=all"
                , response -> {

                Log.e("On Response statistics -----------", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.e("On Response statistics -----------", "success");
                responseCallBack.getStatisticsSuccess(jsonObject);
            } catch (JSONException e) {
                Log.e("On Response statistics -----------", "conversion failed");
//                e.printStackTrace();
            }


        }, error -> Log.e("Error-statistics-----------", "" + error)) {

            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }

        };
        Singleton.getInstance(context).getRequestQueue().add(stringRequest);

    }

    public ArrayList<String> getAllUsers(Context context) {
        final ArrayList<String> result = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + "users"
                , response -> {
            try {
                Log.e("On Response users -----------", response);
            } catch (Exception e) {
                Log.e("On Response users -----------", "error parsing");

                e.fillInStackTrace();
            }
        }, error -> Log.e("Error-users-----------", "" + error)) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }

        };


        Singleton.getInstance(context).getRequestQueue().add(stringRequest);
        return result;

    }


    public void addEyeContactScore(Context context, Double score) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, BASE_URL + "me/eye_contact"
                , response -> {
            try {
                Log.e("On Response Eye Score -----------", "success");
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }, error -> Log.e("Error-Eye Scores-----------", "" + error)) {

            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }

            @Override
            protected Map<String, String> getParams()  {

                String date = android.text.format.DateFormat.format("dd-MM-yyyy",
                        new java.util.Date()).toString();
                Map<String, String> params = new Hashtable<String, String>();
                params.put("date", date);
                params.put("score", String.valueOf(score));
                return params;
            }
        };
        Singleton.getInstance(context).getRequestQueue().add(stringRequest);
    }

    public void getEyeContactScores(Context context, Statistics.OnResponseSuccess onResponseSuccess) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + "me/eye_contact"
                , response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.e("On Response Eye Score -----------", "success");
                onResponseSuccess.getEyeContactScoresSuccess(jsonObject);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }, error -> Log.e("Error-Eye Scores-----------", "" + error)) {

            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }

//            @Override
//            protected Map<String, String> getParams()  {
//
//                Map<String, String> params = new Hashtable<String, String>();
//                params.put("all", "true");
//                params.put("emotion", "all");
//                return params;
//            }
        };
        Singleton.getInstance(context).getRequestQueue().add(stringRequest);
    }
}
