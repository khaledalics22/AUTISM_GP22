package com.example.Interfaces;

import android.content.Context;

import com.example.CustomER.CustomER;

public interface ER {
    CustomER.Emotions predict(Context context, float[][] image);
}
