package com.example.Authentication;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ocvapp.R;
import com.example.SelectModel.ChooseModels;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mMediaPlayer;
    Dialog mDialog;
    private Button btlogin;
    private VideoView video;

    public void showsplash() {

        mDialog = new Dialog(MainActivity.this,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = LayoutInflater.from(this).inflate(R.layout.activity_splash_screen, null);
        ImageView logoImg = v.findViewById(R.id.logo);
        mDialog.setContentView(v);
        mDialog.setCancelable(true);
        mDialog.show();

        //animate
        Animation anim = new RotateAnimation(-5f, 5f, 150f, 150f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setDuration(700);

// Start animating the image
        logoImg.startAnimation(anim);

// Later.. stop the animation
//        logoImg.setAnimation(null);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                {
                    mDialog.dismiss();
                }
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            Log.e("Action Bar", "*******************************");
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setLogo(R.mipmap.ic_logo);
            actionBar.setDisplayUseLogoEnabled(true);
        }
        //show splash screen
        showsplash();

        video = findViewById(R.id.videoView);
        btlogin = findViewById(R.id.Login);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        video.setVideoURI(uri);
        video.start();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer.setLooping(true);
            }
        });

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent = new Intent(MainActivity.this, ChooseModels.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Capture the current video position and pause the video.
        video.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the video when resuming the Activity
        video.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        if (mMediaPlayer != null)
            mMediaPlayer.release();
        mMediaPlayer = null;
    }
}
//public class MainActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        startActivity(new Intent(this, FaceDetectionActivity.class));
//    }
//}