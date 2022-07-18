package com.example.ML;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.CustomER.CustomER;
import com.example.CustomEyeContact.EyeContact;
import com.example.CustomFD.ArrayHelpers;
import com.example.CustomFD.ImageProcessing;
import com.example.RestApis.RestAPI;
import com.example.ocvapp.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
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
    private double eyeDetectionScore;


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
            mOpenCvCameraView.setCameraIndex(0);//BACK Camera
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
        eyeDetectionScore = 1;

    }

    public void onCameraViewStopped() {
    }
    private CustomER.Emotions prev_emotion = CustomER.Emotions.NORMAL;

    private void playMedia() {
//        if (System.currentTimeMillis() - lastTimeMedia > 3000) {
        if(pClass == prev_emotion){
            return;
        }
        prev_emotion = pClass;

        Log.e("play music", "playing music");
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        switch (pClass) {

//            case NORMAL:
//                mPlayer = MediaPlayer.create(this, R.raw.happy);
//                mPlayer.start();
//                break;
            case HAPPY:
                mPlayer = MediaPlayer.create(this, R.raw.happy);
                mPlayer.start();
                break;

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
        Core.rotate(rgbaImage,rgbaImage,Core.ROTATE_90_CLOCKWISE);

//        Mat mat = Imgproc.getRotationMatrix2D(new Point(rgbaImage.width()/2.0, rgbaImage.height()/2.0), 90, 1);
//        Imgproc.warpAffine(rgbaImage, rgbaImage, mat, Imgproc.INTER_LINEAR);

        if (System.currentTimeMillis() - lastTimeDetect > 1000 && detectionTaskFinished) {
            grayscaleImage = inputFrame.gray();
            Core.rotate(grayscaleImage,grayscaleImage,Core.ROTATE_90_CLOCKWISE);

            lastTimeDetect = System.currentTimeMillis();
            Log.e("Face Detection----------------:", "start face detection");
            detectionTaskFinished = false;
            if (grayscaleImage != null)
                new FDBackground().execute(grayscaleImage);
        }
        try {
            if (detectedFace != null) {
                Imgproc.rectangle(rgbaImage, detectedFace.tl(), detectedFace.br(), new Scalar(0, 0, 255, 255), 2);
                Imgproc.putText(rgbaImage,getEmotionText(),detectedFace.tl(),Imgproc.FONT_HERSHEY_DUPLEX,1, getEmotionColor());
                if(arrowPoint1!=null && eyeDetectionScore >= 0.6 && eyeDetectionScore < 0.8) {
                    Imgproc.arrowedLine(rgbaImage,arrowPoint2, arrowPoint1,new Scalar(0, 255 , 0, 255),2);
                    Toast.makeText(TellMeEmotions.this, "Average Eye Contact\nMove in the green arrow direction", Toast.LENGTH_LONG).show();
                }
                else if(arrowPoint1!=null && eyeDetectionScore < 0.6){
                    Imgproc.arrowedLine(rgbaImage,arrowPoint2, arrowPoint1,new Scalar(255, 0 , 0, 255),2);
                    Toast.makeText(TellMeEmotions.this, "Very Poor Eye Contact\nMove in the red arrow direction", Toast.LENGTH_LONG).show();

                }
            }
        } catch (Exception e) {
            Log.e("OnCameraFrame", "failed to detect face");
        }
        // If there are any faces found, draw a rectangle around it
        return rgbaImage;
    }

    private String getEmotionText(){
        switch (pClass) {

//            case NORMAL:
//               return "NORMAL";
            case HAPPY:
                return "HAPPY";
            case SAD:
                return "SAD";
            case SURPRISED:
                return "SURPRISED";
            default:
                return "NORMAL";

        }
    }

    private Scalar getEmotionColor(){
        switch (pClass) {
//            case NORMAL:
//               return "NORMAL";
            case HAPPY:
                return new Scalar(255, 255, 0,255);
            case SAD:
                return new Scalar(0,0,255,255);
            case SURPRISED:
                return new Scalar(255,255,255,255);
            default:
                return new Scalar(255, 192, 203,255);

        }
    }

    // detect frontal face of image stored in grayscaleImage
    // store detected Rect in detectedFace (Gray)
    // stores cropped face in faceImg (rgba)
    private static double lastEyeContact = System.currentTimeMillis();

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
//            if (faces.toArray().length > 1)
//                newFaces = ImageProcessing.integrateRect(faces);
//            else {
                newFaces = faces;
//            }
            Log.e("Integrated Faces ---------------", Arrays.toString(newFaces.get(0, 0)));
            if (newFaces.toArray().length > 0) {
                detectedFace = getLargestFace(newFaces.toArray());
                updateArrow(detectedFace, grayscaleImage.size());
                if (System.currentTimeMillis() - lastEyeContact > 10000) {
                    // send score in background
                    eyeDetectionScore = EyeContact.getInstance().getScore(detectedFace, grayscaleImage.size());
                    new ECUpdateBackground().execute(
                            eyeDetectionScore);
                    lastEyeContact = System.currentTimeMillis();
                }
//                EmotionRecognition.Emotions emotion = EmotionRecognition.getInstance().predict(CustomFD.this, detectedFace);
//                Log.e("Emotion detected---------------:", String.valueOf(emotion));
            }
            if (detectedFace != null)
                new EDBackground().execute();
            else{
                grayscaleImage = null;
                detectionTaskFinished = true;
                Toast.makeText(TellMeEmotions.this,"Please look at the person",Toast.LENGTH_SHORT).show();
            }
        }

        private Rect getLargestFace(Rect[] array) {
            int largest = 0;
            int max_width  = 0;
            for(int i =0; i< array.length; i++){
                if(array[i].width > max_width){
                    largest = i;
                    max_width = array[i].width;
                }
            }
            return array[largest];
        }

    }
    private  Point arrowPoint1;
    private Point arrowPoint2;

    private void updateArrow(Rect rect, Size size) {
        arrowPoint1 = new Point();
        arrowPoint1.x = rect.x + rect.width / 2.0;
        arrowPoint1.y = rect.y + rect.height/2.0;
        arrowPoint2 = new Point();
        arrowPoint2.x = size.width/2;
        arrowPoint2.y = size.height/2;
    }

    class ECUpdateBackground extends AsyncTask<Double, Void, Void> {


        @Override
        protected Void doInBackground(Double... doubles) {
            Log.e("EYE Score *****************", "start sending eye score");
            RestAPI.getInstance().addEyeContactScore(TellMeEmotions.this, doubles[0]);
            return null;
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