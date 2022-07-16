package com.example.CustomFD;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.Objdetect;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageProcessing {
    private static final int frame_width = 32;
    private static final int frame_height = 32;

    private ImageProcessing() {
    }

    // Calculate Integral Image
    public static float[][] getIntegralImg(float[][] img) {
        float[][] integral_img = new float[img.length + 1][img[0].length + 1];

        for (int i = 1; i < img.length + 1; i++) {
            for (int j = 1; j < img[0].length + 1; j++) {
                integral_img[i][j] = img[i - 1][j - 1] + integral_img[i - 1][j] + integral_img[i][j - 1] - integral_img[i - 1][j - 1];
            }
        }

        return ArrayHelpers.getSlice(integral_img, 1, 1, img[0].length, img.length);
    }

    public static Mat drawRects(ArrayList<ArrayList<int[]>> rects, Mat img) {
        for (ArrayList<int[]> arrayList : rects) {
            for (int[] rect : arrayList) {
                Imgproc.rectangle(img, new Rect(rect[0], rect[1], rect[2], rect[3]), new Scalar(255, 255, 255));
            }
        }

        return img;
    }

    public static MatOfRect integrateRect(MatOfRect faces) {
//			Log.e("Rects Integration----1-----", Arrays.toString(faces.toArray()));
        Objdetect.groupRectangles(faces, new MatOfInt(), 1, 1);
        Log.e("Rects Integration---------", Arrays.toString(faces.toArray()));

        return faces;
    }

    public static ArrayList<float[][]> prepareImage(Mat img_mat) {
        if (img_mat == null) return null;
        //convert to mat
        //resize window
//        TimingLogger timings = new TimingLogger("Face", "prepare Image");
        Mat new_img_mat = new Mat();
        double t1 = System.currentTimeMillis();
        Imgproc.resize(img_mat, new_img_mat, new Size(img_mat.width(), img_mat.height()), 0.5, 0.5);
        double t2 = System.currentTimeMillis() - t1;
//        timings.addSplit("resize time");
        // get integral
        double t3 = System.currentTimeMillis();
        float[][] resized_img = ArrayHelpers.matToArray(new_img_mat);
        float[][] i_img = getIntegralImg(resized_img);
        // extract features
        float[][] img1 = extractHaarFeatures(i_img, HAAR_FILTERS.TWO_H, 3);
        float[][] img2 = extractHaarFeatures(i_img, HAAR_FILTERS.TWO_V, 3);
        float[][] img3 = extractHaarFeatures(i_img, HAAR_FILTERS.THREE, 3);
        float[][] img4 = extractHaarFeatures(i_img, HAAR_FILTERS.FOUR, 3);
        double t4 = System.currentTimeMillis() - t3;
//        timings.addSplit("features extraction");
        int min_h = Math.min(img4.length, Math.min(img3.length, Math.min(img1.length, img2.length)));
        int min_w = Math.min(img4[0].length, Math.min(img3[0].length, Math.min(img1[0].length, img2[0].length)));

        ArrayList<float[][]> list = new ArrayList<>();
        list.add(ArrayHelpers.getSlice(img1, 0, 0, min_w, min_h));
        list.add(ArrayHelpers.getSlice(img2, 0, 0, min_w, min_h));
        list.add(ArrayHelpers.getSlice(img3, 0, 0, min_w, min_h));
        list.add(ArrayHelpers.getSlice(img4, 0, 0, min_w, min_h));
        double t5 = System.currentTimeMillis() - t1;
        Log.e("Feature extraction time", "resize time = " + t2 / 1000 + ",  " +
                "feature extraction time = " + t4 / 1000 + ",   total_time = " + t5 / 1000);
//        timings.dumpToLog();
        return list;
    }

    // Calculate Haar Features
    public static float[][] extractHaarFeatures(float[][] i_img, HAAR_FILTERS FILTER, int rect_size) {
        float[][] f_img;
        rect_size--;
        switch (FILTER) {
            case TWO_H:
                System.out.println("TWO H FILTER");
                f_img = new float[i_img.length - rect_size][i_img[0].length - rect_size * 2];
                for (int j = 0; j < f_img.length; j++) {
                    for (int i = 0; i < f_img[0].length; i++) {
                        f_img[j][i] = i_img[j + rect_size][i + rect_size * 2]
                                + 2 * i_img[j][i + rect_size] + i_img[j + rect_size][i]
                                - i_img[j][i + rect_size * 2] - 2 * i_img[j + rect_size][i + rect_size]
                                - i_img[j][i];
                    }
                }
                return f_img;
            case TWO_V:
                f_img = new float[i_img.length - rect_size * 2][i_img[0].length - rect_size];
                for (int j = 0; j < f_img.length; j++) {
                    for (int i = 0; i < f_img[0].length; i++) {
                        f_img[j][i] = i_img[j + rect_size * 2][i + rect_size] + 2 * i_img[j + rect_size][i] -
                                2 * i_img[j + rect_size][i + rect_size] - i_img[j + rect_size * 2][i] + i_img[j][i + rect_size] -
                                i_img[j][i];
                    }
                }
                return f_img;
            case THREE:
                f_img = new float[i_img.length - rect_size][i_img[0].length - rect_size * 3];
                for (int j = 0; j < f_img.length; j++) {
                    for (int i = 0; i < f_img[0].length; i++) {
                        f_img[j][i] = 2 * i_img[j][i + rect_size] + 2 * i_img[j + rect_size][i + rect_size * 2]
                                + i_img[j + rect_size][i] + i_img[j][i + rect_size * 3] - 2 * i_img[j][i + rect_size * 2]
                                - 2 * i_img[j + rect_size][i + rect_size] - i_img[j][i] - i_img[j + rect_size][i + rect_size * 3];
                    }
                }
                return f_img;
            default:
                f_img = new float[i_img.length - rect_size * 2][i_img[0].length - rect_size * 2];
                for (int j = 0; j < f_img.length; j++) {
                    for (int i = 0; i < f_img[0].length; i++) {
                        f_img[j][i] = -i_img[j][i] + 2 * i_img[j][i + rect_size] - i_img[j][i + rect_size * 2]
                                + 2 * i_img[j + rect_size][i] - 4 * i_img[j + rect_size][i + rect_size] + 2 * i_img[j + rect_size][i + rect_size * 2]
                                - i_img[j + rect_size * 2][i] + 2 * i_img[j + rect_size * 2][i + rect_size] - i_img[j + rect_size * 2][i + rect_size * 2];
                    }
                }
                return f_img;

        }

    }

    public enum HAAR_FILTERS {
        TWO_H,
        TWO_V,
        THREE,
        FOUR,
    }


}
