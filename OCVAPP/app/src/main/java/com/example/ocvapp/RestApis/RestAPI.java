package com.example.ocvapp.RestApis;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.Objects.Score;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class RestAPI {

    private static final String BASE_URL = "https://f973-156-196-59-109.ngrok.io/";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MmQwNmQ3ZTZlMDQxYjJiNWMyN2NlYjMiLCJpYXQiOjE2NTc4MjY2ODYsImV4cCI6MjQyOTAwNDUzMTM1NjYxOTAwfQ.TCC_s_-3qLVDcuDsoZdKEHDbnTRMPbEACFiLXjnzf6M";
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
                Log.e("On Response method-----------", response);
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
                Log.e("On Response Score -----------", response);
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
            protected Map<String, String> getParams() throws AuthFailureError {
               /* Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String strDate= formatter.format(date);*/
                String date = android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString();
                Map<String, String> params = new Hashtable<String, String>();
                params.put("date", "25-10-2020");
                params.put("correctSad", String.valueOf(score.getCorrect_sad()));
                params.put("correctHappy", String.valueOf(score.getCorrect_happy()));
                params.put("correctSurprise", String.valueOf(score.getCorrect_surprised()));
                params.put("correctNatural", String.valueOf(score.getCorrect_natural()));
                params.put("errorSad", String.valueOf(score.getError_sad()));
                params.put("errorHappy", String.valueOf(score.getError_happy()));
                params.put("errorSurprise", String.valueOf(score.getError_surprised()));
                params.put("errorNatural", String.valueOf(score.getError_natural()));
                /*params.put("copyrightsType",copyRightsType);
                params.put("releaseDate",strDate);*/
                return params;
            }
        };


        Singleton.getInstance(context).getRequestQueue().add(stringRequest);
        return result;

    }

    public ArrayList<String> getStatistics(Context context) {
        final ArrayList<String> result = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + "me/doing" + "?all=true&emotion=all"
                , response -> {
            try {
                Log.e("On Response Score -----------", response);
                JSONObject jsonObject = new JSONObject(response);
                Log.e("On Response Score -----------", jsonObject.toString());

//                result.add(response);
            } catch (Exception e) {
                Log.e("On Response Score -----------", "error parsing");

                e.fillInStackTrace();
            }
        }, error -> Log.e("Error-Scores-----------", "" + error)) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }

//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//               /* Date date = new Date();
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                String strDate= formatter.format(date);*/
//
//                String date = android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString();
//                Map<String, String> params = new Hashtable<String, String>();
//                params.put("all", "true");
//                params.put("emotion", "all");
//                /*params.put("copyrightsType",copyRightsType);
//                params.put("releaseDate",strDate);*/
//                return params;
//            }
        };


        Singleton.getInstance(context).getRequestQueue().add(stringRequest);
        return result;

    }

    public ArrayList<String> getAllUsers(Context context) {
        final ArrayList<String> result = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + "users"
                , response -> {
            try {

                Log.e("On Response Score -----------", response);

//                result.add(response);
            } catch (Exception e) {
                Log.e("On Response Score -----------", "error parsing");

                e.fillInStackTrace();
            }
        }, error -> Log.e("Error-Scores-----------", "" + error)) {

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


}
