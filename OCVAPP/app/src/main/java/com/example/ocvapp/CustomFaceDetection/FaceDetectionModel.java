package com.example.ocvapp.CustomFaceDetection;

import android.content.Context;
import android.util.Log;

import com.example.ocvapp.ml.H0;
import com.example.ocvapp.ml.H1;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FaceDetectionModel {
	private Context mContext;
	private FaceDetectionModel(Context context) {
		mContext = context;
	}
	private static FaceDetectionModel instance = null;
	
	public static FaceDetectionModel getInstance(Context context) {
		if (instance == null) {
			instance = new FaceDetectionModel(context);
		}
		return instance; 
	}

	private static final int width = 32*2;
	private static final int height = 32*2;
	// FloatBoost Prediction
	public boolean predict(int i,int j, int wsize, ArrayList<float[][]> X){
		float[] preds = new float[2];
		//TODO edit according to models exist
		float[][] window1 = ArrayHelpers.getSlice(X.get(0),j,i,wsize,wsize);
		byte[] bytes = new byte[width * height];
		Mat x = ArrayHelpers.arrayToMat(window1);
		Imgproc.resize(x, x, new Size(width, height), 0.5, 0.5);
		// get integral
		x.get(0, 0, bytes);
		double p0 = predict0(ByteBuffer.wrap(bytes));
		if(p0 > 0.5){
			p0 = 1;
		}else{
			p0 = -1;
		}
//		Log.e("p0----------------:", String.valueOf(p0));
//		float[][] window2 = ArrayHelpers.getSlice(X.get(0),j,i,wsize,wsize);
		byte[] bytes2 = new byte[width * height];
		Mat x2 = ArrayHelpers.arrayToMat(window1);// change to window2
		Imgproc.resize(x2, x2, new Size(width, height), 0.5, 0.5);
		x2.get(0, 0, bytes2);
		double p1 = predict1(ByteBuffer.wrap(bytes2));
		if(p1 > 0.5){
			p1 = 1;
		}else{
			p1 = -1;
		}
//		Log.e("p1---------------:", String.valueOf(p1));
//		Log.e("prediction:------------------", String.valueOf((p0 * 2.9055152901005012 + p1)>0));
		return (p0 * 2.9055152901005012 + p1)>0;
	}
	private double predict1(ByteBuffer features){
		try {
			H1 model = H1.newInstance(mContext);

			// Creates inputs for reference.
			TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 1024}, DataType.FLOAT32);
			inputFeature0.loadBuffer(features);

			// Runs model inference and gets result.
			H1.Outputs outputs = model.process(inputFeature0);
			TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
			double prediction = outputFeature0.getFloatArray()[0];
			model.close();
			return prediction;

//			Log.e("H0:-------------",String.valueOf(arr[0]));
			// Releases model resources if no longer used.
		} catch (IOException e) {
			// TODO Handle the exception
		}
		return 0;
	}
	private double predict0(ByteBuffer features){
		try {
			H0 model = H0.newInstance(mContext);

			// Creates inputs for reference.
			TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 1024}, DataType.FLOAT32);
			inputFeature0.loadBuffer(features);

			// Runs model inference and gets result.
			H0.Outputs outputs = model.process(inputFeature0);
			TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
			double prediction = outputFeature0.getFloatArray()[0];
			model.close();
			return prediction;
//			Log.e("H0:-------------",String.valueOf(arr[0]));
			// Releases model resources if no longer used.
		} catch (IOException e) {
			// TODO Handle the exception
		}
		return 0;
	}
}
