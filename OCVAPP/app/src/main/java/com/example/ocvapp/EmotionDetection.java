package com.example.ocvapp;

import android.content.Context;
import android.util.Log;

import com.example.ocvapp.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class EmotionDetection {

    public static float[] detectEmotion(Context context, ByteBuffer byteBuffer) {
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
}
