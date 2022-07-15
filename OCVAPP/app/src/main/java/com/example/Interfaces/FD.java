package com.example.Interfaces;

import android.content.Context;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

public interface FD {

    MatOfRect detect(Mat mat_img, Context context);

}
