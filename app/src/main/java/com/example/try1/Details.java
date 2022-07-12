package com.example.try1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

public class Details extends AppCompatActivity {
    public ImageView mainimage;
    public TextView text1;
    public ImageView image1;
    public TextView text2;
    public ImageView image2;
    public TextView text3;
    public ImageView image3;
    private TextToSpeech mTTS;
    private String t1,t2,t3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        int imgID=intent.getIntExtra("image",0);
        int type=intent.getIntExtra("type",0);
        mainimage = (ImageView)findViewById(R.id.mainimage);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.rotate);
        mainimage.startAnimation(animation);
        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        image3 = (ImageView)findViewById(R.id.image3);
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2);
        text3 = (TextView)findViewById(R.id.text3);
        mainimage.setImageResource(imgID);
        image1.setImageResource(R.drawable.lovenot);
        image2.setImageResource(R.drawable.nohand);
        image3.setImageResource(R.drawable.notlove);
        /*
         *0 like,not like,want
         *1 like,saw,want
         *2 dirty,wear my dress,like
         *3 i'am ,feel,do you feel
         *4 name,love,miss
         *5 it's,favorite,can yo give me
         *6 it's,draw,favorite
         *7 like,not like,want
         *8 take,like,not like
         *9 it's,want,favourite
         * */

        switch (type){
            case 0:
                t1="أَنَا أُحِبُّ ال"+name;
                t2="أَنَا لا أُحِبُّ ال"+name;
                t3="أنا أريد ال"+name;
                text1.setText("أنا أحب");
                text2.setText("أنا لا أحب");
                text3.setText("أنا أريد");
                image1.setImageResource(R.drawable.lovenot);
                image2.setImageResource(R.drawable.notlove);
                image3.setImageResource(R.drawable.hand);
                break;
            case 1:
                t1="أَنَا أُحِبُّ ال"+name;
                t2="أَنَا أَرَى ال"+name;
                t3="أنا أريد ال"+name;
                text1.setText("أنا أحب");
                text2.setText("أنا أَرَى");
                text3.setText("أنا أريد");
                image1.setImageResource(R.drawable.lovenot);
                image2.setImageResource(R.drawable.see);
                image3.setImageResource(R.drawable.hand);
                break;
            case 2:
                t1="أَنَا أُحِبُّ ال"+name;
                t2="أَنَا لا أُحِبُّ ال"+name;
                t3="أنا أريد أن أَرْتَدِي ال"+name;
                text1.setText("أنا أحب");
                text2.setText("أَنَا لا أُحِبُّ");
                text3.setText("أنا أريد أن أَرْتَدِي");
                image1.setImageResource(R.drawable.lovenot);
                image2.setImageResource(R.drawable.notlove);
                image3.setImageResource(R.drawable.hand);
                break;
            case 3:
                t1="أَنَا أَشْعُرُ بال"+name;
                t2="هل أنت تَشْعُرُ بال"+name;
                t3="أنا أريد أن أَشْعُرُ بال"+name;
                text1.setText("أَنَا أَشْعُرُ ");
                text2.setText("هل أنت تَشْعُرُ ");
                text3.setText("أنا أريد أن أَشْعُرُ ");
                image1.setImageResource(R.drawable.me);
                image2.setImageResource(R.drawable.question);
                image3.setImageResource(R.drawable.hand);
                break;
            case 4:
                t1="يا "+name;
                t2="أَنَا أُحِبُّ "+name;
                t3="أنا أريد أن أَرَى"+name;
                text1.setText(name);
                text2.setText("أَنَا أُحِبُّ");
                text3.setText("أنا أريد أن أَرَى");
                image1.setImageResource(R.drawable.call);
                image2.setImageResource(R.drawable.lovenot);
                image3.setImageResource(R.drawable.hand);
                break;
            case 5:
                t1="هذا "+name;
                t2="أَنَا أُحِبُّ ال"+name;
                t3="أنا لا أُحِبُّ ال"+name;
                text1.setText("هذا");
                text2.setText("أَنَا أُحِبُّ");
                text3.setText("أنا لا أُحِب");
                image1.setImageResource(R.drawable._this);
                image2.setImageResource(R.drawable.lovenot);
                image3.setImageResource(R.drawable.hand);
                break;
            case 6:
                t1="هذا "+name;
                t2="أَنَا أَرْسُمُ ال"+name;
                t3="أنا أُحِبُّ ال"+name;
                text1.setText("هذا");
                text2.setText("أَنَا أَرْسُمُ");
                text3.setText("أنا أُحِبُّ ");
                image1.setImageResource(R.drawable._this);
                image2.setImageResource(R.drawable.me);
                image3.setImageResource(R.drawable.lovenot);
                break;
            case 7:
                t1="أَنَا أُحِبُّ ال"+name;
                t2="أَنَا لا أُحِبُّ ال"+name;
                t3="أنا أريد ال"+name;
                text1.setText("أنا أحب");
                text2.setText("أنا لا أحب");
                text3.setText("أنا أريد");
                image1.setImageResource(R.drawable.lovenot);
                image2.setImageResource(R.drawable.notlove);
                image3.setImageResource(R.drawable.hand);
                break;
            case 8:
                t1="أَنَا أُحِبُّ ال"+name;
                t2="أَنَا لا أُحِبُّ ال"+name;
                t3="أَنَا أريد أن َأَرْكَبُ ال"+name;
                text3.setText("أَنَا أريد أن َأَرْكَب");
                text1.setText("أَنَا أُحِبُّ");
                text2.setText("أَنَا لا أُحِبُّ");
                image1.setImageResource(R.drawable.lovenot);
                image2.setImageResource(R.drawable.notlove);
                image3.setImageResource(R.drawable.hand);
                break;
            case 9:
                t1="هذا "+name;
                t2="أَنَا أُحِبُّ ال"+name;
                t3="أَنَا أريد ال"+name;
                text3.setText("أَنَا أريد");
                text2.setText("أَنَا أُحِبُّ");
                text1.setText("هذا ُّ");
                image1.setImageResource(R.drawable._this);
                image2.setImageResource(R.drawable.lovenot);
                image3.setImageResource(R.drawable.hand);
                break;

             default:
                 t1="أنا أُحِبُّ ال";
                 t2="أنا لا أُحِبُّ ال";
                 t3="أنا أريد ال";
                 text1.setText("أنا أحب");
                 text2.setText("أنا لا أحب");
                 text3.setText("أنا أريد");
                 image1.setImageResource(R.drawable.lovenot);
                 image2.setImageResource(R.drawable.notlove);
                 image3.setImageResource(R.drawable.hand);
                 break;
        }
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        result = mTTS.setLanguage(Locale.forLanguageTag("ar"));
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        mainimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak(name, TextToSpeech.QUEUE_FLUSH, null);
                Animation animation= AnimationUtils.loadAnimation(Details.this,R.anim.blink_anim);
                mainimage.startAnimation(animation);
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak(t1, TextToSpeech.QUEUE_FLUSH, null);
                Animation animation= AnimationUtils.loadAnimation(Details.this,R.anim.blink_anim);
                image1.startAnimation(animation);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak(t2, TextToSpeech.QUEUE_FLUSH, null);
                Animation animation= AnimationUtils.loadAnimation(Details.this,R.anim.blink_anim);
                image2.startAnimation(animation);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak(t3, TextToSpeech.QUEUE_FLUSH, null);
                Animation animation= AnimationUtils.loadAnimation(Details.this,R.anim.blink_anim);
                image3.startAnimation(animation);
            }
        });

    }
}
