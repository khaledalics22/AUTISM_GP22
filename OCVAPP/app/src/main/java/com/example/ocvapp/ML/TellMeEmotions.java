package com.example.ocvapp.ML;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.example.ocvapp.CustomER.CustomER;
import com.example.ocvapp.CustomFD.ArrayHelpers;
import com.example.ocvapp.CustomFD.ImageProcessing;
import com.example.ocvapp.R;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TellMeEmotions extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";
    private final String[] classes = new String[]{"anger", "disgust", "Natural", "happy", "fear", "sadness", "surprise"};
    long lastTimeDetect;
    private CameraBridgeViewBase mOpenCvCameraView;
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
    private MediaPlayer mPlayer;
    private Mat grayscaleImage;
    private CustomER.Emotions pClass;
    private Rect detectedFace;
    //    private final List<Integer> predicted_list = new ArrayList<>();
    private boolean detectionTaskFinished;


    public TellMeEmotions() {
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
//        imageView = findViewById(R.id.image);
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
        Log.e("Activity: ----------", "pause activity");
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
            Log.e(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.e(TAG, "OpenCV library found inside package. Using it!");
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
        detectedFace = null;
        detectionTaskFinished = true;

        // The faces will be a 20% of the height of the screen
//        absoluteFaceSize = (int) (height * 0.2);
//        absoluteEyeSize = (int) (height * 0.1);
//        lastTimeDetect = System.currentTimeMillis();
//        pClass = 4;
    }

    public void onCameraViewStopped() {
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

    private void playMedia() {
//        if (System.currentTimeMillis() - lastTimeMedia > 3000) {
        Log.e("play music", "playing music");
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
//            lastTimeMedia = System.currentTimeMillis();
//            if (pClass != prev_emotion) {
//            prev_emotion = pClass;
        switch (pClass) {
//                {"anger", "disgust", "Natural", "happy", "fear", "sadness", "surprise"}
//                case 0:
//                    mPlayer = MediaPlayer.create(this, R.raw.angery);
//                    mPlayer.start();
//                    break;
//                case 1:
//                    mPlayer = MediaPlayer.create(this, R.raw.disgusted);
//                    mPlayer.start();
//                    break;
            case NORMAL:
                mPlayer = MediaPlayer.create(this, R.raw.happy);
                mPlayer.start();
                break;
            case HAPPY:
                mPlayer = MediaPlayer.create(this, R.raw.happy);
                mPlayer.start();
                break;
//                case 4:
//                    mPlayer = MediaPlayer.create(this, R.raw.scared);
//                    mPlayer.start();
//                    break;
            case SAD:
                mPlayer = MediaPlayer.create(this, R.raw.sad);
                mPlayer.start();
                break;
            case SURPRISED:
                mPlayer = MediaPlayer.create(this, R.raw.surprised);
                mPlayer.start();
                break;
            default:
                break;

        }
//        }

//        }
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        // Create a grayscale image
        Mat rgbaImage = inputFrame.rgba();
        if (System.currentTimeMillis() - lastTimeDetect > 10 && detectionTaskFinished) {
            grayscaleImage = inputFrame.gray();
            lastTimeDetect = System.currentTimeMillis();
            Log.e("Face Detection----------------:", "start face detection");
            detectionTaskFinished = false;
            if (grayscaleImage != null)
                new FDBackground().execute(grayscaleImage);
        }
        try {
            if (detectedFace != null) {
                Imgproc.rectangle(rgbaImage, detectedFace.tl(), detectedFace.br(), new Scalar(255, 0, 0, 255), 2);
            }
        } catch (Exception e) {
            Log.e("OnCameraFrame", "failed to detect face");
        }
        // If there are any faces found, draw a rectangle around it
        return rgbaImage;
    }

    // detect frontal face of image stored in grayscaleImage
    // store detected Rect in detectedFace (Gray)
    // stores cropped face in faceImg (rgba)
    class FDBackground extends AsyncTask<Mat, Void, MatOfRect> {

        @Override
        protected MatOfRect doInBackground(Mat... mats) {
//            assert ML.getFDModel() != null;
            if (ML.getFDModel() == null) {
                Log.e("Detect Faces----------------------------:", "failed to get FD model");
                return new MatOfRect();
            }
            return ML.getFDModel().detect(grayscaleImage, TellMeEmotions.this);
        }

        @Override
        protected void onPostExecute(MatOfRect faces) {
            Log.e("Detected Faces ---------------", String.valueOf(faces.size()));

            MatOfRect newFaces = null;
            if (faces.toArray().length > 1)
                newFaces = ImageProcessing.integrateRect(faces);
            else {
                newFaces = faces;
            }
            Log.e("Integrated Faces ---------------", Arrays.toString(newFaces.get(0, 0)));
            if (newFaces.toArray().length > 0) {
                detectedFace = newFaces.toArray()[0];
//                EmotionRecognition.Emotions emotion = EmotionRecognition.getInstance().predict(CustomFD.this, detectedFace);
//                Log.e("Emotion detected---------------:", String.valueOf(emotion));
            }
            if (detectedFace != null)
                new EDBackground().execute();
            else
                detectionTaskFinished = true;
        }

    }

    class EDBackground extends AsyncTask<Void, Void, CustomER.Emotions> {


        @Override
        protected CustomER.Emotions doInBackground(Void... voids) {
            Mat face = new Mat(grayscaleImage, detectedFace);

            Imgproc.resize(face, face, new Size(48, 48), 0.5, 0.5);
//            assert ML.getERModel() != null;
            if (ML.getERModel() == null) {
                Log.e("Detect emotions----------------------------:", "failed to get ER model");
                return CustomER.Emotions.NORMAL;
            }
            return ML.getERModel().predict(TellMeEmotions.this,
                    ArrayHelpers.matToArray(face));

        }

        @Override
        protected void onPostExecute(CustomER.Emotions emotions) {

            Log.e("Emotion Detected: ----------------", emotions.toString());
            pClass = emotions;
            detectionTaskFinished = true;

            playMedia();
        }
    }

}