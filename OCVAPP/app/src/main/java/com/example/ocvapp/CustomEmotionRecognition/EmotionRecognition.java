package com.example.ocvapp.CustomEmotionRecognition;

import android.content.Context;
import android.util.Pair;

import org.opencv.core.Rect;
import org.opencv.ml.SVM;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class EmotionRecognition {
    private static EmotionRecognition instance = null;

    private EmotionRecognition() {

        //TODO: Initialize datamembers
    }

    public static EmotionRecognition getInstance() {
        if (instance == null) {
            instance = new EmotionRecognition();
        }
        return instance;
    }

    private static Object extractFeatures(int[][] img) {
        int[][] lbp = get_LBP(img, 8);
        ArrayList<Integer> hist = get_LBP_Hist(lbp, 6);

        return null;
    }

    private static ArrayList<Integer> get_LBP_Hist(int[][] lbp_img, int grid_size){
        ArrayList<Integer> hist = new ArrayList<>();
        // loop over lbp image grid by grid
        for(int i = 0; i < lbp_img.length - grid_size; i+=grid_size){
            for(int j= 0; j < lbp_img[0].length - grid_size; j+= grid_size){

                // get hist of each grid
                TreeMap<Integer,Integer> m = new TreeMap<>();
                for(int k = i; k < i + grid_size;k ++){
                    for(int l = j; l < j+grid_size; l++){

                            Integer val = m.get(lbp_img[k][l]);
                            if(val != null) {
                                m.put(lbp_img[k][l], val + 1);
                            }else
                                m.put(lbp_img[k][l],  1);
                    }
                }
                // add new grid hists
                hist.addAll(m.values());
            }
        }
        return hist;
    }
    private static int[][] get_LBP(int[][] img, int neighbors) {
        int width, height;
        if (neighbors == 8) {
            height = img.length - 2;
            width = img[0].length - 2;
        } else {
            return null;
        }
        int[][] lbp_img = new int[height][width];
        char code = 0;
        for (int i = 1; i < height + 1; i++) {
            for (int j = 1; j < width + 1; j++) {
                code |= ((img[i - 1][j - 1] >= img[i][j]) ? 1 : 0) << 7;
                code |= ((img[i - 1][j] >= img[i][j]) ? 1 : 0) << 6;
                code |= ((img[i - 1][j + 1] >= img[i][j]) ? 1 : 0) << 5;
                code |= ((img[i][j + 1] >= img[i][j]) ? 1 : 0) << 4;
                code |= ((img[i + 1][j + 1] >= img[i][j]) ? 1 : 0) << 3;
                code |= ((img[i + 1][j] >= img[i][j]) ? 1 : 0) << 2;
                code |= ((img[i + 1][j - 1] >= img[i][j]) ? 1 : 0) << 1;
                code |= ((img[i][j - 1] >= img[i][j]) ? 1 : 0);
                lbp_img[i - 1][j - 1] = code;
            }
        }
        return lbp_img;
    }

    private static Emotions predictEmotion(Context context, ByteBuffer byteBuffer) {
        SVM model = SVM.load("");
//        model.

        return Emotions.HAPPY;
    }

    public Emotions predict(Context context,int[][] image) {

        Object features = extractFeatures(image);
        // convert to byteBuffer
        ByteBuffer byteBuffer = null;


        return predictEmotion(context, byteBuffer);
    }

    public enum Emotions {
        HAPPY,
        SAD,
        ANGRY,
        SURPRISED,
    }

}
