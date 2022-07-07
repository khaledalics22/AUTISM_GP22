package com.example.ocvapp.CustomFaceDetection;
import android.content.Context;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.nio.ByteBuffer;

import java.util.ArrayList;

public class FaceDetector {
	
	public static Mat detect(Mat mat_img, Context context) {
		ArrayList<float[][]>  features = ImageProcessing.prepareImage(mat_img);
		ArrayList<ArrayList<int[]>> rects = detectFaces(features, context);
		return ImageProcessing.drawRects(rects,mat_img);
	}
	
	// detect faces
	// return ArrayList of coordinates for each window size 
	public  static ArrayList<ArrayList<int[]>> detectFaces(ArrayList<float[][]> X, Context context){
		ArrayList<ArrayList<int[]>> rects = new ArrayList<ArrayList<int[]>>();
		double scale =1.5; 
		int wsize = X.get(0).length; 
		if (X.get(0)[0].length < wsize) {
			wsize = X.get(0)[0].length;
		}
		for(int i =0; i< 3;i++) {
			// add rectangles 
			rects.add(scanImage(X,wsize, wsize/2, context));
			wsize /= scale; 
		}
		
		return rects; 
	}

	//			Imgproc.cvtColor(img, img, Imgproc.COLOR_RGBA2RGB);

//			img.convertTo(img, CvType.CV_8UC4);
//	//            Log.e("Face Image", String.valueOf(faceImg.channels()));

	
	// scan over image with certain window size
	// return coordinates of found faces -> {i,j, wsize, wsize}
	public static ArrayList<int[]> scanImage(ArrayList<float[][]> X, int wsize, int shift, Context context){
		ArrayList<int[]> preds = new ArrayList<int[]>();
		FaceDetectionModel model = FaceDetectionModel.getInstance(context);
		for(int i = 0; i<X.get(0).length - wsize +1; i+= shift ) {
			for(int j = 0; j<X.get(0)[0].length - wsize +1; j+= shift ) {
				if (model.predict(i, j, wsize, X)) {
					preds.add(new int[]{i, j, wsize, wsize});
				}
			}
		}
		return preds; 
		
	}

}
