//package com.example.ocvapp.ReadyFD;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.SurfaceView;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//
//import androidx.core.content.ContextCompat;
//
//import com.example.ocvapp.R;
//
//import org.opencv.android.BaseLoaderCallback;
//import org.opencv.android.CameraActivity;
//import org.opencv.android.CameraBridgeViewBase;
//import org.opencv.android.LoaderCallbackInterface;
//import org.opencv.android.OpenCVLoader;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfRect;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
//import org.opencv.core.Size;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.objdetect.CascadeClassifier;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.nio.ByteBuffer;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class FaceDetectionActivity {
//
//
//    ImageView imageView;
//    private CameraBridgeViewBase mOpenCvCameraView;
//
//    private MediaPlayer mPlayer;
//
//
//
//    private int pClass;
//
//
////    private Rect eyearea;
////    private Rect eyearea_right;
////    private Rect eyearea_left;
////    private int learn_frames = 0;
////    Mat teplateR;
////    Mat teplateL;
////    private int absoluteEyeSize;
//
//
//
//    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            switch (status) {
//                case LoaderCallbackInterface.SUCCESS: {
//                    Log.i(TAG, "OpenCV loaded successfully");
//                    loadClassifiers();
//                    mOpenCvCameraView.enableView();
////                    mOpenCvCameraView.setOnTouchListener(FaceDetectionActivity.this);
//                }
//                break;
//                default: {
//                    super.onManagerConnected(status);
//                }
//                break;
//            }
//        }
//    };
//
////    public FaceDetectionActivity() {
////        Log.i(TAG, "Instantiated new " + this.getClass());
////    }
//
//
////    /**
////     * Called when the activity is first created.
////     */
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        Log.i(TAG, "called onCreate");
////        super.onCreate(savedInstanceState);
////        requestWindowFeature(Window.FEATURE_NO_TITLE);
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
////
////        setContentView(R.layout.face_detect_surface_view);
////        mOpenCvCameraView = findViewById(R.id.face_detect_camera_view);
////        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
////                == PackageManager.PERMISSION_GRANTED) {
////            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
////            mOpenCvCameraView.setCvCameraViewListener(this);
////        }
////    }
////
////    @Override
////    public void onPause() {
////        super.onPause();
////        if (mOpenCvCameraView != null)
////            mOpenCvCameraView.disableView();
////        if (mPlayer != null) {
////            mPlayer.stop();
////            mPlayer.release();
////            mPlayer = null;
////        }
////    }
////
////    @Override
////    public void onResume() {
////        super.onResume();
////        if (!OpenCVLoader.initDebug()) {
////            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
////            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
////        } else {
////            Log.d(TAG, "OpenCV library found inside package. Using it!");
////            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
////        }
////    }
//
////    @Override
////    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
////        return Collections.singletonList(mOpenCvCameraView);
////    }
//
////    public void onDestroy() {
////        super.onDestroy();
////        if (mOpenCvCameraView != null)
////            mOpenCvCameraView.disableView();
////    }
//
////    public void onCameraViewStarted(int width, int height) {
////        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);
////
////        // The faces will be a 20% of the height of the screen
////        absoluteFaceSize = (int) (height * 0.2);
//////        absoluteEyeSize = (int) (height * 0.1);
////        lastTimeDetect = System.currentTimeMillis();
////        pClass = 4;
////    }
//
//
////    public void onCameraViewStopped() {
////    }
//
//
////    private void playMedia() {
////        if (System.currentTimeMillis() - lastTimeMedia > 3000) {
////            pClass = most_freq_class(predicted_list);
////            Log.e("play music", "playing music");
////            if (mPlayer != null) {
////                mPlayer.stop();
////                mPlayer.release();
////                mPlayer = null;
////            }
////            lastTimeMedia = System.currentTimeMillis();
////            if (pClass != prev_emotion) {
////                prev_emotion = pClass;
////                switch (pClass) {
//////                {"anger", "disgust", "Natural", "happy", "fear", "sadness", "surprise"}
////                    case 0:
////                        mPlayer = MediaPlayer.create(this, R.raw.angery);
////                        mPlayer.start();
////                        break;
////                    case 1:
////                        mPlayer = MediaPlayer.create(this, R.raw.disgusted);
////                        mPlayer.start();
////                        break;
////                    case 2:
////                        break;
////                    case 3:
////                        mPlayer = MediaPlayer.create(this, R.raw.happy);
////                        mPlayer.start();
////                        break;
////                    case 4:
////                        mPlayer = MediaPlayer.create(this, R.raw.scared);
////                        mPlayer.start();
////                        break;
////                    case 5:
////                        mPlayer = MediaPlayer.create(this, R.raw.sad);
////                        mPlayer.start();
////                        break;
////                    case 6:
////                        mPlayer = MediaPlayer.create(this, R.raw.surprised);
////                        mPlayer.start();
////                        break;
////                    default:
////                        break;
////
////                }
////            }
////
////        }
////    }
//
////    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
////        // Create a grayscale image
////        grayscaleImage = inputFrame.gray();
////        rgbaImage = inputFrame.rgba();
////
////        if (System.currentTimeMillis() - lastTimeDetect > 10) {
////            lastTimeDetect = System.currentTimeMillis();
////            detectFrontalFace();
////            detectProfileImage();
////            predicted_list.add(detectEmotion());
////            playMedia();
////        }
////        try {
////            if (profileFaceExists || frontalFaceExists) {
////                if (frontalFaceExists) {
////                    Imgproc.putText(rgbaImage, classes[pClass], detectedFace.tl(), Imgproc.FONT_HERSHEY_COMPLEX, Imgproc.FONT_HERSHEY_PLAIN, new Scalar(255, 0, 0, 255), 2);
////                }
////            }
////        } catch (Exception e) {
////            Log.e("OnCameraFrame", "failed to detect emotion");
////        }
////        // If there are any faces found, draw a rectangle around it
////        return rgbaImage;
////    }
//}
