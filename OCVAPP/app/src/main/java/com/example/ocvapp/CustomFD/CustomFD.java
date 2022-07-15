package com.example.ocvapp.CustomFD;

import android.content.Context;
import android.util.Log;

import com.example.Interfaces.FD;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

import java.util.ArrayList;

public class CustomFD implements FD {
    private static CustomFD instance = null;

    private CustomFD() {
    }

    public static CustomFD getInstance() {
        if (instance == null) {
            instance = new CustomFD();
        }
        return instance;
    }

    public MatOfRect detect(Mat mat_img, Context context) {
        ArrayList<float[][]> features = ImageProcessing.prepareImage(mat_img);
        double t1 = System.currentTimeMillis();
        MatOfRect rects = detectFaces(features, context);
        Log.e("Face detection", "Faces detection time= " + ((System.currentTimeMillis() - t1) / 1000));
        return rects;
    }

    // detect faces
    // return ArrayList of coordinates for each window size
    public MatOfRect detectFaces(ArrayList<float[][]> X, Context context) {
        ArrayList<Rect> rects = new ArrayList<>();
        double scale = 1.5;
        int wsize = Math.min(X.get(0).length, X.get(0)[0].length);
        Log.e("Features Shape", X.get(0).length + ", " + X.get(0)[0].length);
        for (int i = 0; i < 3; i++) {
            // add rectangles
            rects.addAll(scanImage(X, wsize, wsize / 2, context));
            wsize /= scale;
        }
        MatOfRect faces = new MatOfRect();
        faces.fromList(rects);
        return faces;
    }

    public ArrayList<Rect> scanImage(ArrayList<float[][]> X, int wsize, int shift, Context context) {
        ArrayList<Rect> preds = new ArrayList<Rect>();
        for (int i = 0; i < X.get(0).length - wsize + 1; i += shift) {
            for (int j = 0; j < X.get(0)[0].length - wsize + 1; j += shift) {
                if (CustomFDModel.getInstance(context).predict(i, j, wsize, X)) {
                    preds.add(new Rect(j, i, wsize, wsize));
                }
            }
        }
        return preds;

    }

}
