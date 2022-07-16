package com.example.Practice;


import android.Manifest;
import android.animation.Animator;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.CustomER.CustomER;
import com.example.CustomFD.ArrayHelpers;
import com.example.CustomFD.ImageProcessing;
import com.example.ML.ML;
import com.example.Objects.Score;
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
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;
import java.util.List;

public class PracticeActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";
    private Mat grayscaleImage;
    private CustomER.Emotions pClass;
    private Rect detectedFace;
    Mat rgbaImage;
    ImageView emotionImg;
    ImageView resultImg;
    TextView emotionText;
    private boolean detectionTaskFinished;
    private CustomER.Emotions currentEmotion;
    private Score score;
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


    public PracticeActivity() {
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

        setContentView(R.layout.activity_practice);
//        imageView = findViewById(R.id.image);
        mOpenCvCameraView = findViewById(R.id.face_detect_camera_view);
        emotionImg = findViewById(R.id.emotion_img);
        emotionText = findViewById(R.id.emotion_text);
        resultImg = findViewById(R.id.result_img);
        score = new Score();
        ImageButton captureImg = findViewById(R.id.capture_img);
        updateEmotionPhoto();
        captureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmotion();
            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mOpenCvCameraView.setCameraIndex(0);//Front Camera
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
        new updateScoreBackground().execute(score);
    }

    private void updateScore(boolean isCorrect) {

        if (isCorrect) {
            switch (currentEmotion) {
                case SURPRISED:
                    score.increaseCorrectSurprised();
                    break;
                case NORMAL:
                    score.increaseCorrectNatural();
                    break;
                case HAPPY:
                    score.increaseCorrectHappy();
                    break;
                case SAD:
                    score.increaseCorrectSad();
                    break;
            }
        } else {
            switch (currentEmotion) {
                case SURPRISED:
                    score.increaseErrorSurprised();
                    break;
                case NORMAL:
                    score.increaseErrorNatural();
                    break;
                case HAPPY:
                    score.increaseErrorHappy();
                    break;
                case SAD:
                    score.increaseErrorSad();
                    break;
            }
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
        lastShownRectTime = System.currentTimeMillis();

    }

    public void onCameraViewStopped() {
    }


    private void checkEmotion() {
        Log.e("Emotion Recognition----------------:", "start emotion Recognition");
        if (grayscaleImage != null)
            new PracticeActivity.FDBackground().execute();

    }

    private double lastShownRectTime;

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        // Create a grayscale image
        if (detectionTaskFinished) {
            rgbaImage = inputFrame.rgba();
            Core.flip(rgbaImage, rgbaImage, 1);
            grayscaleImage = inputFrame.rgba();
            Core.flip(grayscaleImage, grayscaleImage, 1);
        }
        if (detectedFace != null && System.currentTimeMillis() - lastShownRectTime < 1000) {
            Imgproc.rectangle(rgbaImage, detectedFace.tl(), detectedFace.br(), new Scalar(255, 0, 0, 255), 2);
        }

        // If there are any faces found, draw a rectangle around it
        return rgbaImage;
    }

    private String getEmotionText(CustomER.Emotions emotion) {
        switch (emotion) {

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


    private int getEmotionColor(CustomER.Emotions currentEmotion) {
        switch (currentEmotion) {
//            case NORMAL:
//               return "NORMAL";
            case HAPPY:
                return android.R.color.holo_orange_light;
            case SAD:
                return android.R.color.holo_blue_dark;
            case SURPRISED:
                return android.R.color.white;
            default:
                return android.R.color.holo_purple;

        }
    }
    //TODO add list
    private int choice[]={
            R.drawable.angry_,R.drawable.happy,R.drawable.hungry, R.drawable.scared,
            R.drawable.proud,R.drawable.sad,R.drawable.worried, R.drawable.hot,
            R.drawable.exhuasted,R.drawable.cold,R.drawable.cry_, R.drawable.thirsty,
            R.drawable.board,R.drawable.funny,R.drawable.embarrassed, R.drawable.confused,
            R.drawable.excited,R.drawable.exhuasted,R.drawable.disgused, R.drawable.furious,
            R.drawable.guilty,R.drawable.hungry,R.drawable.thirsty, R.drawable.love_,
            R.drawable.hot,R.drawable.cold,R.drawable.pain, R.drawable.proud,
            R.drawable.sick,R.drawable.sorry,R.drawable.sleepy, R.drawable.worried};

    private void updateEmotionPhoto() {
        //TODO change emotion type each time the function is called
        currentEmotion = CustomER.Emotions.HAPPY;
        emotionImg.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.happy, null));
        emotionText.setText(getEmotionText(currentEmotion));
        emotionText.setTextColor(ResourcesCompat.getColor(getResources(), getEmotionColor(currentEmotion), null));
    }

    private void validateEmotion() {
        // check pClass
        updateScore(pClass == currentEmotion);
        if (pClass == currentEmotion) {
            resultImg.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.correct_round, null));
            resultImg.setVisibility(View.VISIBLE);
            resultImg.animate().setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    resultImg.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
        } else {
            resultImg.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.wrong_round, null));
            resultImg.setVisibility(View.VISIBLE);
            resultImg.animate().setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    resultImg.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
        }
        updateEmotionPhoto();
    }

    class updateScoreBackground extends AsyncTask<Score, Void, Void> {

        @Override
        protected Void doInBackground(Score... scores) {
//            assert ML.getFDModel() != null;
            RestAPI.getInstance().setScore(PracticeActivity.this, scores[0]);
            score.clear();
            return null;
        }
    }
    // detect frontal face of image stored in grayscaleImage
// store detected Rect in detectedFace (Gray)
// stores cropped face in faceImg (rgba)

    class FDBackground extends AsyncTask<Mat, Void, MatOfRect> {

        @Override
        protected MatOfRect doInBackground(Mat... mats) {
//            assert ML.getFDModel() != null;
            if (ML.getFDModel() == null) {
//                Log.e("Detect Faces----------------------------:", "failed to get FD model");
                return new MatOfRect();
            }
            return ML.getFDModel().detect(grayscaleImage, PracticeActivity.this);
        }

        @Override
        protected void onPostExecute(MatOfRect faces) {
//            Log.e("Detected Faces ---------------", String.valueOf(faces.size()));

            MatOfRect newFaces = null;
            if (faces.toArray().length > 1)
                newFaces = ImageProcessing.integrateRect(faces);
            else {
                newFaces = faces;
            }
//            Log.e("Integrated Faces ---------------", Arrays.toString(newFaces.get(0, 0)));
            if (newFaces.toArray().length > 0) {
                detectedFace = newFaces.toArray()[0];
                lastShownRectTime = System.currentTimeMillis();
//                EmotionRecognition.Emotions emotion = EmotionRecognition.getInstance().predict(CustomFD.this, detectedFace);
//                Log.e("Emotion detected---------------:", String.valueOf(emotion));
            } else {
                detectedFace = null;
            }
            if (detectedFace != null) {
                new EDBackground().execute();
            } else {
                detectionTaskFinished = true;
                Toast.makeText(PracticeActivity.this, "Please make sure your face is clear", Toast.LENGTH_LONG).show();
            }

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
            return ML.getERModel().predict(PracticeActivity.this,
                    ArrayHelpers.matToArray(face));

        }

        @Override
        protected void onPostExecute(CustomER.Emotions emotions) {

            Log.e("Emotion Detected: ----------------", emotions.toString());
            pClass = emotions;
            detectionTaskFinished = true;
            validateEmotion();
        }

    }
}