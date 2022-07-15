package com.example.ocvapp.ReadyFD;

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
    //    private Mat grayscaleImage;
//    private boolean frontalFaceExists;
//    private boolean profileFaceExists;
    private static final String TAG = "OCVSample::Activity";
    private static ReadyFD instance = null;
    int method = 4;
    private final Rect detectedFace = null;
    //    private int prev_emotion = -1;
//    private long lastTimeMedia = -1;
//    long lastTimeDetect;
//    private Mat rgbaImage;
    private CascadeClassifier cascadeClassifier;
    private CascadeClassifier cascadeProClassifier;
    //    private Mat faceImg;
    private int absoluteFaceSize;
//    private List<Integer> predicted_list = new ArrayList<>();

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


            ////////////////////////////////
            // Copy the resource into a temp file so OpenCV can load it
//            InputStream isLE = context.getResources().openRawResource(R.raw.haarcascade_lefteye_2splits);
//            File cascadeDirLE = context.getDir("cascadeEL", Context.MODE_PRIVATE);
//            File mCascadeFileLE = new File(cascadeDirLE, "haarcascade_eye_left.xml");
//            FileOutputStream osLE = new FileOutputStream(mCascadeFileLE);
//
//
//            byte[] bufferLE = new byte[4096];
//            int bytesReadLE;
//            while ((bytesReadLE = isLE.read(bufferLE)) != -1) {
//                osLE.write(bufferLE, 0, bytesReadLE);
//            }
//            isLE.close();
//            osLE.close();

//            // Load the cascade classifier
//            CascadeClassifier cascadeLEClassifier = new CascadeClassifier(mCascadeFileLE.getAbsolutePath());
//            cascadeLEClassifier.load(mCascadeFileLE.getAbsolutePath());
//
//            // Copy the resource into a temp file so OpenCV can load it
//            InputStream isRE = context.getResources().openRawResource(R.raw.haarcascade_lefteye_2splits);
//            File cascadeDirRE = context.getDir("cascadeER", Context.MODE_PRIVATE);
//            File mCascadeFileRE = new File(cascadeDirRE, "haarcascade_eye_right.xml");
//            FileOutputStream osRE = new FileOutputStream(mCascadeFileRE);
//
//
//            byte[] bufferRE = new byte[4096];
//            int bytesReadRE;
//            while ((bytesReadRE = isRE.read(bufferRE)) != -1) {
//                osRE.write(bufferRE, 0, bytesReadRE);
//            }
//            isRE.close();
//            osRE.close();
//
//            // Load the cascade classifier
//            CascadeClassifier cascadeREClassifier = new CascadeClassifier(mCascadeFileRE.getAbsolutePath());
//            cascadeREClassifier.load(mCascadeFileRE.getAbsolutePath());
//
//            if (cascadeClassifier.empty() || cascadeREClassifier.empty() || cascadeLEClassifier.empty()) {
//                Log.e(TAG, "Failed to load cascade classifier");
//                cascadeClassifier = null;
//                cascadeREClassifier = null;
//                cascadeLEClassifier = null;
//            } else
//                Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

//            mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

            cascadeDir.delete();
//            mCascadeFileRE.delete();
//            cascadeDirRE.delete();
//            mCascadeFileLE.delete();
//            cascadeDirLE.delete();
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
        // And we are ready to go
//        mOpenCvCameraView.enableView();
    }

    // detect emotion of face stored in faceImg
    // store emotion class in pClass


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
//        Rect[] facesArray = faces.toArray();
//        frontalFaceExists = facesArray.length != 0;
//        for (Rect rect : facesArray) {
//            detectedFace = rect;
//            faceImg = rgbaImage.submat(rect.y, rect.y + rect.height,
//                    rect.x, rect.x + rect.width);
//            break;
//        }
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
//        Rect[] facesArray = faces.toArray();
//        profileFaceExists = facesArray.length != 0;
//        for (Rect rect : facesArray) {
//            detectedFace = rect;
//            faceImg = rgbaImage.submat(rect.y, rect.y + rect.height,
//                    rect.x, rect.x + rect.width);
//
//            //detect eye
////            computeEyeArea(rect);
//            //detect emotion
//            break;
//        }
    }

//    private int most_freq_class(List<Integer> list) {
//        Collections.sort(list);
////        Log.e("Predicted List ---------", list.toString());
//        int mode = 0;
//        int freq_class = 0;
//        int curr_class = 0;
//        int max_mode = 0;
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i) == curr_class) {
//                mode++;
//            } else if (mode > max_mode) {
//                max_mode = mode;
//                mode = 1;
//                curr_class = list.get(i);
//                freq_class = curr_class - 1;
//            } else {
//                curr_class = list.get(i);
//                mode = 1;
//            }
//        }
//        if (mode > max_mode) {
//            freq_class = curr_class;
//        }
////        Log.e("Predicted class ------------", String.valueOf(freq_class));
//        predicted_list.clear();
//        return freq_class;
//    }


}
