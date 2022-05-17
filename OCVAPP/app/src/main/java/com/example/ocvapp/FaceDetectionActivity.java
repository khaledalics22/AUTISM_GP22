package com.example.ocvapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FaceDetectionActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";


    ImageView imageView;
    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean frontalFaceExists;
    private boolean profileFaceExists;
    private MediaPlayer mPlayer;
    private int prev_emotion = -1;
    private long lastTimeMedia = -1;
    private Mat grayscaleImage;
    private int absoluteFaceSize;
    long lastTimeDetect;
    private Mat rgbaImage;
    private Mat faceImg;
    private String[] classes = new String[]{"anger", "disgust", "Natural", "happy", "fear", "sadness", "surprise"};
    private int pClass;
    private Rect detectedFace = null;
    private List<Integer> predicted_list = new ArrayList<>();

//    private Rect eyearea;
//    private Rect eyearea_right;
//    private Rect eyearea_left;
//    private int learn_frames = 0;
//    Mat teplateR;
//    Mat teplateL;
//    private int absoluteEyeSize;


    int method = 4;

    private CascadeClassifier cascadeClassifier;
    private CascadeClassifier cascadeLEClassifier;
    private CascadeClassifier cascadeREClassifier;
    private CascadeClassifier cascadeProClassifier;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    loadClassifiers();
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

    public FaceDetectionActivity() {
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
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);

        // The faces will be a 20% of the height of the screen
        absoluteFaceSize = (int) (height * 0.2);
//        absoluteEyeSize = (int) (height * 0.1);
        lastTimeDetect = System.currentTimeMillis();
        pClass = 4;
    }


    public void onCameraViewStopped() {
    }

    private void loadClassifiers() {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream ispro = getResources().openRawResource(R.raw.lbpcascade_profileface);
            File cascadeDirpro = getDir("cascade", Context.MODE_PRIVATE);
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
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
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
            InputStream isLE = getResources().openRawResource(R.raw.haarcascade_lefteye_2splits);
            File cascadeDirLE = getDir("cascadeEL", Context.MODE_PRIVATE);
            File mCascadeFileLE = new File(cascadeDirLE, "haarcascade_eye_left.xml");
            FileOutputStream osLE = new FileOutputStream(mCascadeFileLE);


            byte[] bufferLE = new byte[4096];
            int bytesReadLE;
            while ((bytesReadLE = isLE.read(bufferLE)) != -1) {
                osLE.write(bufferLE, 0, bytesReadLE);
            }
            isLE.close();
            osLE.close();

            // Load the cascade classifier
            cascadeLEClassifier = new CascadeClassifier(mCascadeFileLE.getAbsolutePath());
            cascadeLEClassifier.load(mCascadeFileLE.getAbsolutePath());

            // Copy the resource into a temp file so OpenCV can load it
            InputStream isRE = getResources().openRawResource(R.raw.haarcascade_lefteye_2splits);
            File cascadeDirRE = getDir("cascadeER", Context.MODE_PRIVATE);
            File mCascadeFileRE = new File(cascadeDirRE, "haarcascade_eye_right.xml");
            FileOutputStream osRE = new FileOutputStream(mCascadeFileRE);


            byte[] bufferRE = new byte[4096];
            int bytesReadRE;
            while ((bytesReadRE = isRE.read(bufferRE)) != -1) {
                osRE.write(bufferRE, 0, bytesReadRE);
            }
            isRE.close();
            osRE.close();

            // Load the cascade classifier
            cascadeREClassifier = new CascadeClassifier(mCascadeFileRE.getAbsolutePath());
            cascadeREClassifier.load(mCascadeFileRE.getAbsolutePath());

            if (cascadeClassifier.empty() || cascadeREClassifier.empty() || cascadeLEClassifier.empty()) {
                Log.e(TAG, "Failed to load cascade classifier");
                cascadeClassifier = null;
                cascadeREClassifier = null;
                cascadeLEClassifier = null;
            } else
                Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

//            mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

            cascadeDir.delete();
            mCascadeFileRE.delete();
            cascadeDirRE.delete();
            mCascadeFileLE.delete();
            cascadeDirLE.delete();
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
        // And we are ready to go
        mOpenCvCameraView.enableView();
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


    // detect frontal face of image stored in grayscaleImage
    // store detected Rect in detectedFace (Gray)
    // stores cropped face in faceImg (rgba)
    private void detectFrontalFace() {
        MatOfRect faces = new MatOfRect();

        // Use the classifier to detect faces
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }
        Rect[] facesArray = faces.toArray();
        frontalFaceExists = facesArray.length != 0;
        for (Rect rect : facesArray) {
            detectedFace = rect;
            faceImg = rgbaImage.submat(rect.y, rect.y + rect.height,
                    rect.x, rect.x + rect.width);
            break;
        }
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

        if (System.currentTimeMillis() - lastTimeDetect > 10) {
            lastTimeDetect = System.currentTimeMillis();
            detectFrontalFace();
            detectProfileImage();
            predicted_list.add(detectEmotion());
            playMedia();
        }
        try {
            if (profileFaceExists || frontalFaceExists) {
                if (frontalFaceExists) {
                    Imgproc.putText(rgbaImage, classes[pClass], detectedFace.tl(), Imgproc.FONT_HERSHEY_COMPLEX, Imgproc.FONT_HERSHEY_PLAIN, new Scalar(255, 0, 0, 255), 2);
                }
//            Imgproc.rectangle(rgbaImage, detectedFace.tl(), detectedFace.br(), new Scalar(0, 0, 255, 255), 2);
//            Imgproc.rectangle(rgbaImage,eyearea_left.tl(),eyearea_left.br() , new Scalar(255,0, 0, 255), 2);
//            Imgproc.rectangle(rgbaImage,eyearea_right.tl(),eyearea_right.br() , new Scalar(255, 0, 0, 255), 2);
//                detectedFace = null;
            }
        } catch (Exception e) {
            Log.e("OnCameraFrame", "failed to detect emotion");
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

//    public ByteBuffer Mat2Bytes(Mat mat) {
//        byte[] b = new byte[mat.channels() * mat.cols() * mat.rows()];
//        mat.get(0, 0, b);
//        return ByteBuffer.wrap(b);
//    }


//        private static final int TM_SQDIFF = 0;
//        private static final int TM_SQDIFF_NORMED = 1;
//        private static final int TM_CCOEFF = 2;
//        private static final int TM_CCOEFF_NORMED = 3;
//        private static final int TM_CCORR = 4;
//        private static final int TM_CCORR_NORMED = 5;
//
//         public Mat get_template(CascadeClassifier clasificator, Rect area, int size) {
//            Mat template = new Mat();
//            Mat mROI = grayscaleImage.submat(area);
//            MatOfRect eyes = new MatOfRect();
//            Point iris = new Point();
//            Rect eye_template = new Rect();
//            clasificator.detectMultiScale(mROI, eyes, 1.15, 2,
//                    Objdetect.CASCADE_FIND_BIGGEST_OBJECT
//                            | Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30),
//                    new Size());
//
//            Rect[] eyesArray = eyes.toArray();
//
//             for (Rect rect : eyesArray) {
//                 Log.e("get templete", "Eyes array");
//                 Rect e = rect;
//                 e.x = area.x + e.x;
//                 e.y = area.y + e.y;
//                 Rect eye_only_rectangle = new Rect((int) e.tl().x,
//                         (int) (e.tl().y + e.height * 0.4), (int) e.width,
//                         (int) (e.height * 0.6));
//                 mROI = grayscaleImage.submat(eye_only_rectangle);
//                 Mat vyrez = rgbaImage.submat(eye_only_rectangle);
//
//
//                 Core.MinMaxLocResult mmG = Core.minMaxLoc(mROI);
//
//                 Imgproc.circle(vyrez, mmG.minLoc, 2, new Scalar(255, 255, 255, 255), 1);
//                 iris.x = mmG.minLoc.x + eye_only_rectangle.x;
//                 iris.y = mmG.minLoc.y + eye_only_rectangle.y;
//                 eye_template = new Rect((int) iris.x - size / 2, (int) iris.y
//                         - size / 2, size, size);
//                 Imgproc.rectangle(rgbaImage, eye_template.tl(), eye_template.br(),
//                         new Scalar(255, 0, 0, 255), 2);
//                 template = (grayscaleImage.submat(eye_template)).clone();
//                 return template;
//             }
//            return template;
//        }
//        public void match_eye(Rect area, Mat mTemplate, int type) {
//            try {
//                Point matchLoc;
//                Mat mROI = grayscaleImage.submat(area);
//                int result_cols = mROI.cols() - mTemplate.cols() + 1;
//                int result_rows = mROI.rows() - mTemplate.rows() + 1;
//
//                // Check for bad template size
//                if (mTemplate.cols() == 0 || mTemplate.rows() == 0) {
//                    return;
//                }
//                Mat mResult = new Mat(result_cols, result_rows, CvType.CV_8U);
//
//                switch (type) {
//                    case TM_SQDIFF:
//                        Imgproc.matchTemplate(mROI, mTemplate, mResult, Imgproc.TM_SQDIFF);
//                        break;
//                    case TM_SQDIFF_NORMED:
//                        Imgproc.matchTemplate(mROI, mTemplate, mResult,
//                                Imgproc.TM_SQDIFF_NORMED);
//                        break;
//                    case TM_CCOEFF:
//                        Imgproc.matchTemplate(mROI, mTemplate, mResult, Imgproc.TM_CCOEFF);
//                        break;
//                    case TM_CCOEFF_NORMED:
//                        Imgproc.matchTemplate(mROI, mTemplate, mResult,
//                                Imgproc.TM_CCOEFF_NORMED);
//                        break;
//                    case TM_CCORR:
//                        Imgproc.matchTemplate(mROI, mTemplate, mResult, Imgproc.TM_CCORR);
//                        break;
//                    case TM_CCORR_NORMED:
//                        Imgproc.matchTemplate(mROI, mTemplate, mResult,
//                                Imgproc.TM_CCORR_NORMED);
//                        break;
//                }
//
//                Core.MinMaxLocResult mmres = Core.minMaxLoc(mResult);
//                // there is difference in matching methods - best match is max/min value
//                if (type == TM_SQDIFF || type == TM_SQDIFF_NORMED) {
//                    matchLoc = mmres.minLoc;
//                } else {
//                    matchLoc = mmres.maxLoc;
//                }
//
//                Point matchLoc_tx = new Point(matchLoc.x + area.x, matchLoc.y + area.y);
//                Point matchLoc_ty = new Point(matchLoc.x + mTemplate.cols() + area.x, matchLoc.y + mTemplate.rows() + area.y);
//                Imgproc.rectangle(rgbaImage, matchLoc_tx, matchLoc_ty, new Scalar(255, 255, 0, 255));
//                Rect rec = new Rect(matchLoc_tx, matchLoc_ty);
//
//            } catch (Exception exc) {
//
//            }
//        }
//
}
//    private Scalar convertScalarHsv2Rgba(Scalar hsvColor) {
//        Mat pointMatRgba = new Mat();
//        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
//        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);
//
//        return new Scalar(pointMatRgba.get(0, 0));
//    }

//    public boolean onTouch(View v, MotionEvent event) {
//        int cols = mRgba.cols();
//        int rows = mRgba.rows();
//
//        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
//        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;
//
//        int x = (int) event.getX() - xOffset;
//        int y = (int) event.getY() - yOffset;
//
//        Log.i(TAG, "Touch image coordinates: (" + x + ", " + y + ")");
//
//        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;
//
//        Rect touchedRect = new Rect();
//
//        touchedRect.x = (x > 4) ? x - 4 : 0;
//        touchedRect.y = (y > 4) ? y - 4 : 0;
//
//        touchedRect.width = (x + 4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
//        touchedRect.height = (y + 4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;
//
//        Mat touchedRegionRgba = mRgba.submat(touchedRect);
//
//        Mat touchedRegionHsv = new Mat();
//        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);
//
//        // Calculate average color of touched region
//        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
//        int pointCount = touchedRect.width * touchedRect.height;
//        for (int i = 0; i < mBlobColorHsv.val.length; i++)
//            mBlobColorHsv.val[i] /= pointCount;
//
//        mBlobColorRgba = convertScalarHsv2Rgba(mBlobColorHsv);
//
//        Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
//                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");
//
//        mDetector.setHsvColor(mBlobColorHsv);
//
//        Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);
//
//        mIsColorSelected = true;
//
//        touchedRegionRgba.release();
//        touchedRegionHsv.release();
//
//        return false; // don't need subsequent touch events
//    }

//    private void computeEyeArea(Rect faceGray){
//        eyearea = new Rect(faceGray.x +faceGray.width/8,(int)(faceGray.y + (faceGray.height/4.5)),faceGray.width - 2*faceGray.width/8,(int)( faceGray.height/3.0));
//// split it
//        eyearea_right = new Rect(faceGray.x +faceGray.width/16,(int)(faceGray.y + (faceGray.height/4.5)),(faceGray.width - 2*faceGray.width/16)/2,(int)( faceGray.height/3.0));
//        eyearea_left = new Rect(faceGray.x +faceGray.width/16 +(faceGray.width - 2*faceGray.width/16)/2,(int)(faceGray.y + (faceGray.height/4.5)),(faceGray.width - 2*faceGray.width/16)/2,(int)( faceGray.height/3.0));
//// draw the area - mGray is working grayscale mat, if you want to see area in rgb preview, change mGray to mRgba
//        if (learn_frames < 10) {
//            teplateR = get_template(cascadeREClassifier, eyearea_right, 24);
//            teplateL = get_template(cascadeLEClassifier, eyearea_left, 24);
//            learn_frames++;
//        }  else {
//            match_eye(eyearea_right, teplateR, method);
//            match_eye(eyearea_left, teplateL, method);
//        }
//    }