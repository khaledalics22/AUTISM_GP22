package com.example.ocvapp.CustomFaceDetection;

import android.content.Context;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

import java.util.ArrayList;

public class FaceDetector {
    private static FaceDetector instance = null;

    private FaceDetector() {
    }

    public static FaceDetector getInstance() {
        if (instance == null) {
            instance = new FaceDetector();
        }
        return instance;
    }

    public MatOfRect detect(Mat mat_img, Context context) {
        ArrayList<float[][]> features = ImageProcessing.prepareImage(mat_img);
        double t1 = System.currentTimeMillis();
        MatOfRect rects = detectFaces(features, context);
        Log.e("Face detection","Faces detection time= "+ ((System.currentTimeMillis()- t1)/1000));
        return rects;
    }

    // detect faces
    // return ArrayList of coordinates for each window size
    public MatOfRect detectFaces(ArrayList<float[][]> X, Context context) {
        ArrayList<Rect> rects = new ArrayList<>();
        double scale = 1.5;
        int wsize = Math.min(X.get(0).length, X.get(0)[0].length);
        Log.e("Features Shape",String.valueOf(X.get(0).length)+", "+ String.valueOf(X.get(0)[0].length));
        for (int i = 0; i < 3; i++) {
            // add rectangles
            rects.addAll(scanImage(X, wsize, wsize / 2, context));
            wsize /= scale;
        }
        MatOfRect faces = new MatOfRect();
        faces.fromList(rects);
        return faces;
    }

    //			Imgproc.cvtColor(img, img, Imgproc.COLOR_RGBA2RGB);

//			img.convertTo(img, CvType.CV_8UC4);
//	//            Log.e("Face Image", String.valueOf(faceImg.channels()));


    // scan over image with certain window size
    // return coordinates of found faces -> {i,j, wsize, wsize}
    public ArrayList<Rect> scanImage(ArrayList<float[][]> X, int wsize, int shift, Context context) {
        ArrayList<Rect> preds = new ArrayList<Rect>();
        FaceDetectionModel model = FaceDetectionModel.getInstance(context);
        for (int i = 0; i < X.get(0).length - wsize + 1; i += shift) {
            for (int j = 0; j < X.get(0)[0].length - wsize + 1; j += shift) {
                if (model.predict(i, j, wsize, X)) {
                    preds.add(new Rect(i, j, wsize, wsize));
                }
            }
        }
        return preds;

    }

}
