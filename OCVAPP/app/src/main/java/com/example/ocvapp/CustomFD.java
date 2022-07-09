package com.example.ocvapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.ocvapp.CustomEmotionRecognition.EmotionRecognition;
import com.example.ocvapp.CustomFaceDetection.FaceDetector;
import com.example.ocvapp.CustomFaceDetection.ImageProcessing;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomFD extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";


    ImageView imageView;
    long lastTimeDetect;
    int method = 4;
    FDBackground asyncTask;
    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean faceExists;
    private boolean profileFaceExists;
    private MediaPlayer mPlayer;
    private int prev_emotion = -1;
    private long lastTimeMedia = -1;
    private Mat grayscaleImage;
    private int absoluteFaceSize;
    private Mat rgbaImage;
    private Mat faceImg;
    private final String[] classes = new String[]{"anger", "disgust", "Natural", "happy", "fear", "sadness", "surprise"};
    private int pClass;
    private Rect detectedFace = null;
//    private Rect eyearea;
//    private Rect eyearea_right;
//    private Rect eyearea_left;
//    private int learn_frames = 0;
//    Mat teplateR;
//    Mat teplateL;
//    private int absoluteEyeSize;
    private final List<Integer> predicted_list = new ArrayList<>();
    private boolean detectionTaskFinished = true;
    private CascadeClassifier cascadeClassifier;
    private CascadeClassifier cascadeLEClassifier;
    private CascadeClassifier cascadeREClassifier;
    private CascadeClassifier cascadeProClassifier;
    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
//                    mOpenCvCameraView.setOnTouchListener(FaceDetectionActivity.this);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };


    public CustomFD() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.face_detect_surface_view);
        imageView = findViewById(R.id.image);
        mOpenCvCameraView = findViewById(R.id.face_detect_camera_view);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        grayscaleImage = new Mat(height, width, CvType.CV_8U);

        // The faces will be a 20% of the height of the screen
//        absoluteFaceSize = (int) (height * 0.2);
//        absoluteEyeSize = (int) (height * 0.1);
        lastTimeDetect = System.currentTimeMillis();
        pClass = 4;
    }

    public void onCameraViewStopped() {
    }

    // detect emotion of face stored in faceImg
    // store emotion class in pClass
    private int detectEmotion() {
        if (faceImg == null) return 2;
        Imgproc.resize(faceImg, faceImg, new Size(128, 128));
        Imgproc.cvtColor(faceImg, faceImg, Imgproc.COLOR_RGBA2RGB);

        faceImg.convertTo(faceImg, CvType.CV_8UC4);
//            Log.e("Face Image", String.valueOf(faceImg.channels()));
        byte[] bytes = new byte[faceImg.rows() * faceImg.cols() * faceImg.channels()];
        faceImg.get(0, 0, bytes);

        return getIndexOfLargest(EmotionDetection.
                detectEmotion(this, ByteBuffer.wrap(bytes)));
//        Log.e("Emotion", classes[pClass]);
    }

    private void detectFaces(Mat grayscaleImage) {
//        if(asyncTask!=null){
////                    asyncTask.cancel(true);
//        }
        asyncTask = new FDBackground();
        asyncTask.execute(grayscaleImage);

//        Rect[] facesArray = faces.toArray();
//        faceExists = faces.size() != 0;
//        for (Rect rect : facesArray) {
//            detectedFace = rect;
//            faceImg = rgbaImage.submat(rect.y, rect.y + rect.height,
//                    rect.x, rect.x + rect.width);
//            break;
//        }
    }

    // detect frontal face of image stored in grayscaleImage
    // store detected Rect in detectedFace (Gray)
    // stores cropped face in faceImg (rgba)
    private void detectProfileImage() {
        MatOfRect faces = new MatOfRect();

        // Use the classifier to detect faces
        if (cascadeProClassifier != null) {
            cascadeProClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        Rect[] facesArray = faces.toArray();
        profileFaceExists = facesArray.length != 0;
        for (Rect rect : facesArray) {
            detectedFace = rect;
            faceImg = rgbaImage.submat(rect.y, rect.y + rect.height,
                    rect.x, rect.x + rect.width);

            //detect eye
//            computeEyeArea(rect);
            //detect emotion
            break;
        }
    }

    private int most_freq_class(List<Integer> list) {
        Collections.sort(list);
//        Log.e("Predicted List ---------", list.toString());
        int mode = 0;
        int freq_class = 0;
        int curr_class = 0;
        int max_mode = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == curr_class) {
                mode++;
            } else if (mode > max_mode) {
                max_mode = mode;
                mode = 1;
                curr_class = list.get(i);
                freq_class = curr_class - 1;
            } else {
                curr_class = list.get(i);
                mode = 1;
            }
        }
        if (mode > max_mode) {
            freq_class = curr_class;
        }
//        Log.e("Predicted class ------------", String.valueOf(freq_class));
        predicted_list.clear();
        return freq_class;
    }

    private void playMedia() {
        if (System.currentTimeMillis() - lastTimeMedia > 3000) {
            pClass = most_freq_class(predicted_list);
            Log.e("play music", "playing music");
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
            lastTimeMedia = System.currentTimeMillis();
            if (pClass != prev_emotion) {
                prev_emotion = pClass;
                switch (pClass) {
//                {"anger", "disgust", "Natural", "happy", "fear", "sadness", "surprise"}
                    case 0:
                        mPlayer = MediaPlayer.create(this, R.raw.angery);
                        mPlayer.start();
                        break;
                    case 1:
                        mPlayer = MediaPlayer.create(this, R.raw.disgusted);
                        mPlayer.start();
                        break;
                    case 2:
                        break;
                    case 3:
                        mPlayer = MediaPlayer.create(this, R.raw.happy);
                        mPlayer.start();
                        break;
                    case 4:
                        mPlayer = MediaPlayer.create(this, R.raw.scared);
                        mPlayer.start();
                        break;
                    case 5:
                        mPlayer = MediaPlayer.create(this, R.raw.sad);
                        mPlayer.start();
                        break;
                    case 6:
                        mPlayer = MediaPlayer.create(this, R.raw.surprised);
                        mPlayer.start();
                        break;
                    default:
                        break;

                }
            }

        }
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        // Create a grayscale image
        grayscaleImage = inputFrame.gray();
        rgbaImage = inputFrame.rgba();

        if (System.currentTimeMillis() - lastTimeDetect > 10 && detectionTaskFinished) {
            lastTimeDetect = System.currentTimeMillis();
            detectionTaskFinished = false;
            detectFaces(grayscaleImage);
//            detectProfileImage();
//            predicted_list.add(detectEmotion());
//            playMedia();
        }
        try {
//            if (faceExists ) {
//                    Imgproc.putText(rgbaImage, classes[pClass], detectedFace.tl(), Imgproc.FONT_HERSHEY_COMPLEX, Imgproc.FONT_HERSHEY_PLAIN, new Scalar(255, 0, 0, 255), 2);
//                }
            if (detectedFace != null)
                Imgproc.rectangle(rgbaImage, detectedFace.tl(), detectedFace.br(), new Scalar(0, 0, 255, 255), 2);
//           } Imgproc.rectangle(rgbaImage,eyearea_left.tl(),eyearea_left.br() , new Scalar(255,0, 0, 255), 2);
//            Imgproc.rectangle(rgbaImage,eyearea_right.tl(),eyearea_right.br() , new Scalar(255, 0, 0, 255), 2);
//                detectedFace = null;
//            }
        } catch (Exception e) {
            Log.e("OnCameraFrame", "failed to detect face");
        }
        // If there are any faces found, draw a rectangle around it
        return rgbaImage;
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

    // detect frontal face of image stored in grayscaleImage
    // store detected Rect in detectedFace (Gray)
    // stores cropped face in faceImg (rgba)
    class FDBackground extends AsyncTask<Mat, Void, MatOfRect> {

        @Override
        protected MatOfRect doInBackground(Mat... mats) {
            return FaceDetector.getInstance().detect(grayscaleImage, CustomFD.this);
        }

        @Override
        protected void onPostExecute(MatOfRect faces) {
            Log.e("Detected Faces ---------------", String.valueOf(faces.size()));
            MatOfRect newFaces = ImageProcessing.integrateRect(faces);
            Log.e("Integrated Faces ---------------", Arrays.toString(newFaces.get(0, 0)));
            if (newFaces.toArray().length > 0) {
                detectedFace = newFaces.toArray()[0];
//                EmotionRecognition.Emotions emotion = EmotionRecognition.getInstance().predict(CustomFD.this, detectedFace);
//                Log.e("Emotion detected---------------:", String.valueOf(emotion));
            }
            detectionTaskFinished = true;
        }

    }

}