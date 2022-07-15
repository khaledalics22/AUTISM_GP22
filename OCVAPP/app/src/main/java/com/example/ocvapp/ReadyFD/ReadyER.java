package com.example.ocvapp.ReadyFD;

import android.content.Context;
import android.util.Log;

import com.example.Interfaces.ER;
import com.example.ocvapp.CustomER.CustomER;
import com.example.ocvapp.CustomFD.ArrayHelpers;
import com.example.ocvapp.ml.Model;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ReadyER implements ER {
    private static ReadyER instance = null;

    private ReadyER() {
    }

    public static ReadyER getInstance() {
        if (instance == null) {
            instance = new ReadyER();
        }
        return instance;
    }

    public float[] detectEmotion(Context context, ByteBuffer byteBuffer) {
        try {
            Model model = Model.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 64, 64, 3}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] arr = outputFeature0.getFloatArray();
//            Log.e("Emotion", Arrays.toString(outputFeature0.getFloatArray()));
            model.close();
            return arr;
        } catch (IOException e) {
            // TODO Handle the exception
            Log.e("Emotion Detection", "Failed to detect emotion");
        }
        return null;
    }

    //    private String[] classes = new String[]{"anger", "disgust", "Natural", "happy", "fear", "sadness", "surprise"};
    @Override
    public CustomER.Emotions predict(Context context, float[][] image) {
        Mat faceImg = ArrayHelpers.arrayToMat(image);
        Imgproc.resize(faceImg, faceImg, new Size(128, 128));
        Imgproc.cvtColor(faceImg, faceImg, Imgproc.COLOR_RGBA2RGB);

        faceImg.convertTo(faceImg, CvType.CV_8UC4);
//            Log.e("Face Image", String.valueOf(faceImg.channels()));
        byte[] bytes = new byte[faceImg.rows() * faceImg.cols() * faceImg.channels()];
        faceImg.get(0, 0, bytes);
        int idx = getIndexOfLargest(detectEmotion(context, ByteBuffer.wrap(bytes)));
        switch (idx) {
//            case 2:
//                return CustomER.Emotions.NORMAL;
            case 3:
                return CustomER.Emotions.HAPPY;
            case 5:
                return CustomER.Emotions.SAD;
            case 6:
                return CustomER.Emotions.SURPRISED;
            default:
                return CustomER.Emotions.NORMAL;
        }

    }

    public int getIndexOfLargest(float[] array) {
//        Log.e("Output",Arrays.toString(array));
        if (array == null || array.length == 0) return -1; // null or empty

        int largest = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[largest]) largest = i;
        }
        return largest; // position of the first largest found
    }
}
