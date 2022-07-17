package com.example.Authentication;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
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

import com.example.Modes.ModesActivity;
import com.example.RestApis.User;
import com.example.SelectModel.ChooseModels;
import com.example.ocvapp.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mMediaPlayer;
    Dialog mDialog;
    private Button btlogin,btnsignup;
    private VideoView video;
    private static boolean splashShown = false;
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
        anim.setDuration(300);

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
        try {
            Check_token();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            Log.e("Action Bar", "*******************************");
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setLogo(R.mipmap.ic_logo);
            actionBar.setDisplayUseLogoEnabled(true);
        }
        //show splash screen
        if(!splashShown) {
            showsplash();
            splashShown =true;
        }
        video = findViewById(R.id.videoView);
        btlogin = findViewById(R.id.Login);
        btnsignup=(Button) findViewById(R.id.Signup);

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
                intent = new Intent(MainActivity.this, MainActivityLogin.class);
//                intent = new Intent(MainActivity.this, ChooseModels.class);
//                intent = new Intent(MainActivity.this, MenuActivity.class);


                startActivity(intent);
            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent = new Intent(MainActivity.this, MainActivitySignup.class);
                startActivity(intent);
            }
        });
    }
    String Get_Token_file() throws IOException
    {
        int c;
        String token_file="";
        //open a file
        FileInputStream local_file_in = openFileInput("Token");
        while( (c = local_file_in.read()) != -1)
        {
            token_file = token_file + Character.toString((char)c);
        }
        local_file_in.close();
        return    token_file;
    }

    void Delete_token() throws IOException {
        FileOutputStream local_file_out = openFileOutput("Token", Context.MODE_PRIVATE);
        local_file_out.write(Integer.parseInt(""));
        local_file_out.close();
    }
    void Save_token(String token) throws IOException {
        //create and save a file
        FileOutputStream local_file_out = openFileOutput("Token", Context.MODE_PRIVATE);
        //Write token on a file
        local_file_out.write(token.getBytes());
        local_file_out.close();
    }
    void Check_token() throws IOException {
        String local_token =Get_Token_file();
        if(local_token.equals("Delete")) {
            Log.v("111111111111", "Main Delete");
            User.setType(0);
        }

        else{
            User.setType(1);
            Log.v("22222222222", "you already sign in "+local_token);
            User.setToken(local_token);
            final Intent intent;
            intent = new Intent(MainActivity.this, ChooseModels.class);
            startActivity(intent);
        }
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