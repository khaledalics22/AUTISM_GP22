package com.example.CustomEyeContact;

import android.os.AsyncTask;

import com.example.RestApis.RestAPI;

import org.opencv.core.Rect;
import org.opencv.core.Size;

public class EyeContact {

    private static EyeContact instance = null;
    private EyeContact(){}
    public static EyeContact getInstance() {
        if(instance == null){
            instance = new EyeContact();
        }
        return instance;
    }

    public double getScore(Rect rect, Size frame_size){
        double x = (rect.x + rect.width/2.0);
        double y = (rect.y + rect.height/2.0);
        double d =  Math.sqrt(Math.pow(x-frame_size.width / 2.0, 2) + Math.pow(y-frame_size.height / 2.0, 2));
        double score = Math.abs(frame_size.width - d)/frame_size.width;
        score = Math.round(score * 10)/10.0;
        return score;
//        double  score  = (x/(frame_size.width - x)) * (y/(frame_size.height-y));
//        Log.e("Eye Score --------------------: ", String.valueOf();
    }



}
