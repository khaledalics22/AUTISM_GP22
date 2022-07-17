package com.example.try1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Objects.Score;
import com.example.RestApis.RestAPI;
import com.example.ocvapp.R;

import java.util.Random;

public class TestChoose extends AppCompatActivity {
    private TextView twrong;
    private TextView tright;
    private TextView ttest;

    private Test tst=new Test();
    private int numTest=16;
    private int Tests=0;
    private int img=4;
    private int star=0;
    private int wrong=0;
    private int result;
    private ImageView choose0;
    private ImageView choose1;
    private ImageView choose2;
    private ImageView choose3;

    private String currentEmotionString;
    private Score score;
    private void updateScore(boolean isCorrect){
        if(isCorrect) {
            switch (currentEmotionString) {
                case "Happy":
                    score.increaseCorrectHappy();
                    break;
                case "Sad":
                    score.increaseCorrectSad();
                    break;
                case "Normal":
                    score.increaseCorrectNatural();
                    break;
                case "Surprised":
                    score.increaseCorrectSurprised();
                    break;
            }
        }else{
            switch (currentEmotionString) {
                case "Happy":
                    score.increaseErrorHappy();
                    break;
                case "Sad":
                    score.increaseErrorSad();
                    break;
                case "Normal":
                    score.increaseErrorNatural();
                    break;
                case "Surprised":
                    score.increaseErrorSurprised();
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        new SetScoreRecognizeBackground().execute();

    }

    class SetScoreRecognizeBackground extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            RestAPI.getInstance().setScoreRecognize(TestChoose.this, score);
            score.clear();
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_choose);
        twrong = (TextView) findViewById(R.id.wrong);
        tright = (TextView) findViewById(R.id.right);
        ttest = (TextView) findViewById(R.id.testtext);
        choose0 = (ImageView) findViewById(R.id.choose0);
        choose1 = (ImageView) findViewById(R.id.choose1);
        choose2 = (ImageView) findViewById(R.id.choose2);
        choose3 = (ImageView) findViewById(R.id.choose3);
        if(Tests<numTest) {AddAnotherTest();}
        choose0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result==0){
                        star=star+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                        AddAnotherTest();
                }
                else{
                        wrong=wrong+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                }
            }
        });
        choose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result==1){
                        star=star+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                        AddAnotherTest();
                }
                else{
                        wrong=wrong+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                    }
                }

        });
        choose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result==2){
                        star=star+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                        AddAnotherTest();}
                else{
                        wrong=wrong+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                }
            }
        });
        choose3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(result==3){
                        star=star+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                        AddAnotherTest();
                }
                else{
                        wrong=wrong+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                }

            }
        });


    }

    public void AddAnotherTest(){
        Random random=new Random();
        int ind=random.nextInt(numTest-1);
        Log.i("kkkkkkkkkkkkkk", ""+ind);
        String u=tst.getTest()[ind];
        int [] v=tst.getChoice()[ind];
        result=tst.getAnswer()[ind];
        ttest.setText(u);
        choose0.setImageResource(v[0]);
        choose1.setImageResource(v[1]);
        choose2.setImageResource(v[2]);
        choose3.setImageResource(v[3]);
    }
}