package com.example.ocvapp.CustomFaceDetection;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ArrayHelpers {
    private ArrayHelpers() {
    }

    public static float[][] matToArray(Mat img) {
        float[][] arr = new float[img.rows()][img.cols()];
        for (int i = 0; i < img.rows(); i++) {
            for (int j = 0; j < img.cols(); j++) {
//				Log.e(" Mat To Array: -------------", (Arrays.toString(img.get(i, j))));
                arr[i][j] = (float) img.get(i, j)[0];
            }
        }
        return arr;
    }

    public static Mat arrayToMat(float[][] img) {
        Mat mat = new Mat(img.length, img[0].length, CvType.CV_8U);
        for (int i = 0; i < img.length; i++) {
            for (int j = 0; j < img[0].length; j++) {
                mat.put(i, j, img[i][j]);
            }
        }
        return mat;
    }

    public static float[][] getSlice(float[][] img, int x, int y, int w, int h) {

        float[][] slice = new float[h][w];
        for (int i = x; i < x + h; i++) {
            for (int j = y; j < y + w; j++) {
                slice[i - x][j - y] = img[i][j];
            }
        }

        return slice;
    }

    public static void printMatrix(float[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }
    }
}
