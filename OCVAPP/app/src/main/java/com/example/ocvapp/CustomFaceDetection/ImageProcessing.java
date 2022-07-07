package com.example.ocvapp.CustomFaceDetection;

import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opencv.objdetect.Objdetect;
public class ImageProcessing {
	public enum HAAR_FILTERS{
		TWO_H,
		TWO_V,
		THREE, 
		FOUR, 
	}
	private static final int frame_width  = 32;
	private static final int frame_height = 32;


	// Calculate Integral Image
		public static float[][] getIntegralImg(float[][] img){
			float [][] integral_img = new float[img.length+1][img[0].length+1];

			for(int i = 1; i< img.length+1; i++) {
				for(int j =1; j < img[0].length+1; j++) {
					integral_img[i][j] = img[i-1][j-1]+integral_img[i-1][j]+integral_img[i][j-1]-integral_img[i-1][j-1];
				}
			}
	
			return ArrayHelpers.getSlice(integral_img, 1, 1, img[0].length,img.length);
		}
		public static Mat drawRects(ArrayList<ArrayList<int[]>> rects, Mat img){
			for(ArrayList<int[]> arrayList : rects){
				for(int[] rect : arrayList){
					Imgproc.rectangle(img,new Rect(rect[0], rect[1],  rect[2], rect[3]),new Scalar(255,255,255));
				}
			}

			return img;
		}
		public static MatOfRect integrateRect(MatOfRect faces){
			Log.e("Rects Integration----1-----", Arrays.toString(faces.toArray()));
			Objdetect.groupRectangles(faces, new MatOfInt(),1,1);
			Log.e("Rects Integration-----2----", Arrays.toString(faces.toArray()));

			return faces;
		}

		public static ArrayList<float[][]> prepareImage(Mat img_mat){
			if (img_mat == null) return null;
			//convert to mat
			//resize window
			Imgproc.resize(img_mat, img_mat, new Size(img_mat.rows(), img_mat.height()), 0.5, 0.5);
			// get integral
			float[][] resized_img = ArrayHelpers.matToArray(img_mat);
			float[][] i_img = getIntegralImg(resized_img);
			// extract features
			float[][]img1 = extractHaarFeatures(i_img, HAAR_FILTERS.TWO_H,3);
			float[][]img2 = extractHaarFeatures(i_img, HAAR_FILTERS.TWO_V,3);
			float[][]img3 = extractHaarFeatures(i_img, HAAR_FILTERS.THREE,3);
			float[][]img4 = extractHaarFeatures(i_img, HAAR_FILTERS.FOUR,3);

			int min_h =Math.min(img4.length,Math.min(img3.length, Math.min(img1.length, img2.length)));
			int min_w =Math.min(img4[0].length,Math.min(img3[0].length, Math.min(img1[0].length, img2[0].length)));

			ArrayList<float[][]> list = new ArrayList<>();
			list.add(ArrayHelpers.getSlice(img1,0,0,min_w, min_h));
			list.add(ArrayHelpers.getSlice(img2,0,0,min_w, min_h));
			list.add(ArrayHelpers.getSlice(img3,0,0,min_w, min_h));
			list.add(ArrayHelpers.getSlice(img4,0,0,min_w, min_h));
			return list;
		}
		
	// Calculate Haar Features
		public static float[][] extractHaarFeatures(float[][] i_img, HAAR_FILTERS FILTER, int rect_size){
			float[][] f_img;
			rect_size--; 
			switch(FILTER){
			case TWO_H: 
				System.out.println("TWO H FILTER");
				f_img = new float[i_img.length-rect_size][i_img[0].length-rect_size*2];
				for(int j =0 ; j < f_img.length; j++) {
					for (int i = 0; i < f_img[0].length; i++) {
						f_img[j][i] = i_img[j+rect_size][i+rect_size*2]  
								+ 2 * i_img[j][ i+rect_size] + i_img[j+rect_size][i] 
								- i_img[j][i+rect_size*2] - 2 * i_img[j+rect_size][i+rect_size] 
								- i_img[j][i];
					}
				}
				return f_img; 
			case TWO_V:
				f_img = new float[i_img.length-rect_size*2][i_img[0].length-rect_size];
				for(int j =0 ; j < f_img.length; j++) {
					for (int i = 0; i < f_img[0].length; i++) {
						f_img[j][i] = i_img[j+rect_size*2][i+rect_size]  + 2 * i_img[j+rect_size][i] - 
					              2 * i_img[j+rect_size][i+rect_size] - i_img[j+rect_size*2][i] + i_img[j][i+rect_size] -
					               i_img[j][i];
					}
				}
				return f_img;  
			case THREE: 
				f_img = new float[i_img.length-rect_size][i_img[0].length-rect_size*3];
				for(int j =0 ; j < f_img.length; j++) {
					for (int i = 0; i < f_img[0].length; i++) {
						f_img[j][i] = 2 * i_img[j][i+rect_size] + 2 * i_img[j+rect_size][i+rect_size*2]
						          + i_img[j+rect_size][i] + i_img[j][i+rect_size*3] - 2 * i_img[j][i+rect_size*2]
						          - 2 * i_img[j+rect_size][i+rect_size] - i_img[j][i] - i_img[j+rect_size][i+rect_size*3];
					}
				}
				return f_img; 
			default: 
				f_img = new float[i_img.length-rect_size*2][i_img[0].length-rect_size*2];
				for(int j =0 ; j < f_img.length; j++) {
					for (int i = 0; i < f_img[0].length; i++) {
						f_img[j][i] = - i_img[j][i] + 2 * i_img[j][i+rect_size]  - i_img[j][i+rect_size*2]
						          + 2 *  i_img[j+rect_size][i] - 4 * i_img[j+rect_size][i+rect_size]  + 2* i_img[j+rect_size][i+rect_size*2]
						          -  i_img[j+rect_size*2][i] + 2 * i_img[j+rect_size*2][i+rect_size]  - i_img[j+rect_size*2][i+rect_size*2];
					}
				}
				return f_img;  
			
			}

		}
		
		
}
