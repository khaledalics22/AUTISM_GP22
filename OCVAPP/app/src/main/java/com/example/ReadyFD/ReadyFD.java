package com.example.ReadyFD;

import android.content.Context;
import android.util.Log;

import com.example.Interfaces.FD;
import com.example.ocvapp.R;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ReadyFD implements FD {

    private static final String TAG = "OCVSample::Activity";
    private static ReadyFD instance = null;
    int method = 4;
    private final Rect detectedFace = null;

    private CascadeClassifier cascadeClassifier;
    private CascadeClassifier cascadeProClassifier;
    private int absoluteFaceSize;

    private ReadyFD() {
    }

    public static ReadyFD getInstance() {
        if (instance == null) {
            instance = new ReadyFD();
        }
        return instance;
    }

    @Override
    public MatOfRect detect(Mat mat_img, Context context) {
        loadClassifiers(context);
        MatOfRect mat = detectFrontalFace(mat_img);
        mat.push_back(detectProfileImage(mat_img));
        return mat;
    }

    private void loadClassifiers(Context context) {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream ispro = context.getResources().openRawResource(R.raw.lbpcascade_profileface);
            File cascadeDirpro = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFilepro = new File(cascadeDirpro, "lbpcascade_profileface.xml");
            FileOutputStream ospro = new FileOutputStream(mCascadeFilepro);


            byte[] bufferpro = new byte[4096];
            int bytesReadpro;
            while ((bytesReadpro = ispro.read(bufferpro)) != -1) {
                ospro.write(bufferpro, 0, bytesReadpro);
            }
            ispro.close();
            ospro.close();

            // Load the cascade classifier
            cascadeProClassifier = new CascadeClassifier(mCascadeFilepro.getAbsolutePath());


            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = context.getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);


            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());


            cascadeDir.delete();

        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }

    }



    // detect frontal face of image stored in grayscaleImage
    // store detected Rect in detectedFace (Gray)
    // stores cropped face in faceImg (rgba)
    private MatOfRect detectFrontalFace(Mat grayscaleImage) {
        MatOfRect faces = new MatOfRect();

        // Use the classifier to detect faces
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(grayscaleImage.height() * 0.2, grayscaleImage.height() * 0.2), new Size());
        }

        return faces;
    }

    // detect frontal face of image stored in grayscaleImage
    // store detected Rect in detectedFace (Gray)
    // stores cropped face in faceImg (rgba)
    private MatOfRect detectProfileImage(Mat grayscaleImage) {
        MatOfRect faces = new MatOfRect();

        // Use the classifier to detect faces
        if (cascadeProClassifier != null) {
            cascadeProClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        return faces;

    }

}
