package com.example.CustomER;

import android.content.Context;
import android.util.Log;

import com.example.Interfaces.ER;
import com.example.ocvapp.R;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.ml.SVM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomER implements ER {
    private static CustomER instance = null;
    private static int[][] weights;

    private CustomER() {
        weights = new int[][]{
                {0, 1, 1, 1, 1, 0},
                {2, 2, 2, 2, 2, 2},
                {2, 4, 4, 4, 4, 2},
                {1, 1, 2, 2, 1, 1},
                {1, 2, 4, 4, 2, 1},
                {0, 1, 2, 2, 1, 0},
        };
        //TODO: Initialize datamembers
    }

    public static CustomER getInstance() {
        if (instance == null) {
            instance = new CustomER();
        }
        return instance;
    }

    private static double[] get_LBPH_features(float[][] img) {
        // applay LBP
        float[][] lbp = get_LBP(img, 8, 2);
        // regional histograms
        double[] hist = get_LBP_Hist(lbp);
        // standerize
        // standerize
//        double [] mue = new double[] {0.5f, 0.5f, 5.0f }; // read from file
//        double [] std= new double[] {0.5f, 0.5f, 0.70710678f}; // read from file
        for (int i = 0; i < hist.length; i++) {
            if (Constants.stds[i] == 0)
                hist[i] = 0;
            else
                hist[i] = (hist[i] - Constants.mues[i]) / Constants.stds[i];
        }

        return hist;
    }

    private static float[][] get_LBP(float[][] img, int neighbors, int radius) {
        int width, height;
        if (neighbors == 8) {
            height = img.length - 2 * radius;
            width = img[0].length - 2 * radius;
        } else {
            return null;
        }
        float[][] lbp_img = new float[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                lbp_img[i][j] = 0;
            }
        }

        for (int n = 0; n < neighbors; n++) {
            // sample points
            double x = radius * Math.cos(2.0 * Math.PI * n / (double) (neighbors));
            double y = -radius * Math.sin(2.0 * Math.PI * n / (double) (neighbors));
            // relative indices
            int fx = (int) (Math.floor(x));
            int fy = (int) (Math.floor(y));
            int cx = (int) (Math.ceil(x));
            int cy = (int) (Math.ceil(y));
            // fractional part
            double ty = y - fy;
            double tx = x - fx;
            // set interpolation weights
            double w1 = (1 - tx) * (1 - ty);
            double w2 = tx * (1 - ty);
            double w3 = (1 - tx) * ty;
            double w4 = tx * ty;
            // iterate through your data
            for (int i = radius; i < img.length - radius; i++) {
                for (int j = radius; j < img[0].length - radius; j++) {
                    // calculate interpolated value
                    double t = w1 * img[i + fy][j + fx] + w2 * img[i + fy][j + cx] + w3 * img[i + cy][j + fx] + w4 * img[i + cy][j + cx];
                    // doubleing point precision, so check some machine-dependent epsilon
                    int temp = (((t >= img[i][j]) || (Math.abs(t - img[i][j]) < 0))) ? 1 : 0;
                    lbp_img[i - radius][j - radius] += temp << n;
                }
            }
        }
        return lbp_img;
    }

    private static double[] get_LBP_Hist(float[][] lbp_img) {

        int hist_count = 0;
        int[] pins_size = new int[]{1, 64, 64, 0, 64};
        int num_regions = weights.length;
        for (int i = 0; i < num_regions; i++)
            for (int j = 0; j < num_regions; j++)
                hist_count += pins_size[weights[i][j]];

        double[] hist = new double[hist_count];
        int index_to_add = 0;
        for (int i_region = 0; i_region < num_regions; i_region++) {
            for (int j_region = 0; j_region < num_regions; j_region++) {
                int weight = weights[i_region][j_region];
                int pin_size = pins_size[weight];

                if (pin_size == 1) {
                    hist[index_to_add] = 0;
                    index_to_add++;
                } else {
                    // loop over lbp image grid by grid
                    int[] subhist = new int[pin_size];
                    for (int k = 0; k < pin_size; k++)
                        subhist[k] = 0;

                    for (int k = i_region * ((lbp_img.length) / num_regions); k < (i_region + 1) * ((lbp_img.length) / num_regions); k++) {
                        for (int l = j_region * ((lbp_img[0].length) / num_regions); l < (j_region + 1) * ((lbp_img[0].length) / num_regions); l++) {

                            subhist[(int) (pin_size * lbp_img[k][l] / 256)] += 1;
                        }
                    }


                    double sum = ((lbp_img.length) / num_regions) * ((lbp_img.length) / num_regions);
                    for (int k = 0; k < pin_size; k++) {
                        hist[index_to_add] = subhist[k] / sum * (double) weight;
                        index_to_add += 1;
                    }
                }
            }
        }
        return hist;
    }

//    private static Object extractFeatures(int[][] img) {
//        int[][] lbp = get_LBP(img, 8);
//        ArrayList<Integer> hist = get_LBP_Hist(lbp, 6);
//
//        return null;
//    }

//    private static ArrayList<Integer> get_LBP_Hist(int[][] lbp_img, int grid_size){
//        ArrayList<Integer> hist = new ArrayList<>();
//        // loop over lbp image grid by grid
//        for(int i = 0; i < lbp_img.length - grid_size; i+=grid_size){
//            for(int j= 0; j < lbp_img[0].length - grid_size; j+= grid_size){
//
//                // get hist of each grid
//                TreeMap<Integer,Integer> m = new TreeMap<>();
//                for(int k = i; k < i + grid_size;k ++){
//                    for(int l = j; l < j+grid_size; l++){
//
//                            Integer val = m.get(lbp_img[k][l]);
//                            if(val != null) {
//                                m.put(lbp_img[k][l], val + 1);
//                            }else
//                                m.put(lbp_img[k][l],  1);
//                    }
//                }
//                // add new grid hists
//                hist.addAll(m.values());
//            }
//        }
//        return hist;
//    }
//    private static int[][] get_LBP(int[][] img, int neighbors) {
//        int width, height;
//        if (neighbors == 8) {
//            height = img.length - 2;
//            width = img[0].length - 2;
//        } else {
//            return null;
//        }
//        int[][] lbp_img = new int[height][width];
//        char code = 0;
//        for (int i = 1; i < height + 1; i++) {
//            for (int j = 1; j < width + 1; j++) {
//                code |= ((img[i - 1][j - 1] >= img[i][j]) ? 1 : 0) << 7;
//                code |= ((img[i - 1][j] >= img[i][j]) ? 1 : 0) << 6;
//                code |= ((img[i - 1][j + 1] >= img[i][j]) ? 1 : 0) << 5;
//                code |= ((img[i][j + 1] >= img[i][j]) ? 1 : 0) << 4;
//                code |= ((img[i + 1][j + 1] >= img[i][j]) ? 1 : 0) << 3;
//                code |= ((img[i + 1][j] >= img[i][j]) ? 1 : 0) << 2;
//                code |= ((img[i + 1][j - 1] >= img[i][j]) ? 1 : 0) << 1;
//                code |= ((img[i][j - 1] >= img[i][j]) ? 1 : 0);
//                lbp_img[i - 1][j - 1] = code;
//            }
//        }
//        return lbp_img;
//    }

    private static Emotions predictEmotion(Context context, Mat mat)
    {
        InputStream ispro = context.getResources().openRawResource(R.raw.lbpcascade_frontalface);
        File cascadeDirpro = context.getDir("emotion", Context.MODE_PRIVATE);
        File mCascadeFilepro = new File(cascadeDirpro, "emotion_recognition_model.xml");
        FileOutputStream ospro = null;
        try {
            ospro = new FileOutputStream(mCascadeFilepro);
            byte[] bufferpro = new byte[4096];
            int bytesReadpro;
            while ((bytesReadpro = ispro.read(bufferpro)) != -1) {
                ospro.write(bufferpro, 0, bytesReadpro);
            }
            ispro.close();
            ospro.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Load the  classifier
        SVM svm = SVM.load(mCascadeFilepro.getAbsolutePath());
        float pred = svm.predict(mat);
        Log.e("load model ------------------", String.valueOf(pred));
        if (pred == 0.0) {
            return Emotions.NORMAL;
        } else if (pred == 1.0)
            return Emotions.HAPPY;
        else if (pred == 2.0) {
            return Emotions.SURPRISED;
        } else if (pred == 3.0) {
            return Emotions.SAD;
        } else {
            return Emotions.HAPPY;
        }
    }

    public Emotions predict(Context context, float[][] image) {

        double[] features = get_LBPH_features(image);
//        byte[] bytes = new byte[features.length];
//        // convert to byteBuffer
//        for(int i = 0; i< features.length; i++){
//            bytes[i] = (byte)features[i];
//        }
        Mat mat = new Mat(1, features.length, CvType.CV_32F);
        mat.put(0, 0, features);
        return predictEmotion(context, mat);
    }

    public enum Emotions {
        NORMAL,
        HAPPY,
        SAD,
        ANGRY,
        SURPRISED,
    }

}
