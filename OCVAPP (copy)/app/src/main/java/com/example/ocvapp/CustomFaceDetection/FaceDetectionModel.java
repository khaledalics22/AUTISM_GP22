package com.example.ocvapp.CustomFaceDetection;

import android.content.Context;
import android.util.Log;

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

	private static final int width = 32;
	private static final int height = 32;
	// FloatBoost Prediction
	public boolean predict(int i,int j, int wsize, ArrayList<float[][]> X){
		float[] preds = new float[2];
		//TODO edit according to models exist
		float[][] window1 = ArrayHelpers.getSlice(X.get(0),j,i,wsize,wsize);
		byte[] bytes = new byte[1024];
		Mat x = ArrayHelpers.arrayToMat(window1);
		Imgproc.resize(x, x, new Size(width, height), 0.5, 0.5);
		// get integral
		x.get(0, 0, bytes);
		float p0 = predict0(ByteBuffer.wrap(bytes));

//		float[][] window2 = ArrayHelpers.getSlice(X.get(0),j,i,wsize,wsize);
		byte[] bytes2 = new byte[1024];
		Mat x2 = ArrayHelpers.arrayToMat(window1);// change to window2
		Imgproc.resize(x2, x2, new Size(width, height), 0.5, 0.5);
		x2.get(0, 0, bytes2);
		float p1 = predict1(ByteBuffer.wrap(bytes2));

		return true;
	}
	private float predict1(ByteBuffer features){
		try {
			H1 model = H1.newInstance(mContext);

			// Creates inputs for reference.
			TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 1024}, DataType.FLOAT32);
			inputFeature0.loadBuffer(features);

			// Runs model inference and gets result.
			H1.Outputs outputs = model.process(inputFeature0);
			TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
			float[] arr = outputFeature0.getFloatArray();
			Log.e("H0:-------------",arr.toString());
			// Releases model resources if no longer used.
			model.close();
		} catch (IOException e) {
			// TODO Handle the exception
		}
		return 0;
	}
	private float predict0(ByteBuffer features){
		try {
			H0 model = H0.newInstance(mContext);

			// Creates inputs for reference.
			TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 1024}, DataType.FLOAT32);
			inputFeature0.loadBuffer(features);

			// Runs model inference and gets result.
			H0.Outputs outputs = model.process(inputFeature0);
			TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
			float[] arr = outputFeature0.getFloatArray();
			Log.e("H0:-------------",arr.toString());
			// Releases model resources if no longer used.
			model.close();
		} catch (IOException e) {
			// TODO Handle the exception
		}
		return 0;
	}
}
