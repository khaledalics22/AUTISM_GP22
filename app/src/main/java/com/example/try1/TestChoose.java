package com.example.try1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class TestChoose extends AppCompatActivity {
    private TextView twrong;
    private TextView tright;
    private TextView ttest;

    private Test tst=new Test();
    private int numTest=32;
    private int Tests=0;
    private int img=4;
    private int star=0;
    private int wrong=0;
    private int result;
    private ImageView choose0;
    private ImageView choose1;
    private ImageView choose2;
    private ImageView choose3;

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
        if(Tests>=numTest){
            Toast.makeText( TestChoose.this, "Good you finish the test ^^ ", Toast.LENGTH_SHORT).show();
        }
        if(Tests<numTest) {AddAnotherTest();}
        choose0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(result==0){
                            if(Tests<numTest) {
                                star=star+1;
                                tright.setText(Integer.toString(star));
                                twrong.setText( Integer.toString(wrong));
                                AddAnotherTest();}
                            else{Toast.makeText( TestChoose.this, "Good you finish the test ^^ ", Toast.LENGTH_SHORT).show(); }

                        }
                        else{
                            if(Tests<numTest) {
                                wrong=wrong+1;
                                tright.setText(Integer.toString(star));
                                twrong.setText( Integer.toString(wrong));
                            }
                            else{Toast.makeText( TestChoose.this, "Good you finish the test ^^ ", Toast.LENGTH_SHORT).show(); }
                        }
                    }
                });
        choose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result==1){
                    if(Tests<numTest) {
                        star=star+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                        AddAnotherTest();}
                    else{Toast.makeText( TestChoose.this, "Good you finish the test ^^ ", Toast.LENGTH_SHORT).show(); }

                }
                else{
                    if(Tests<numTest) {
                        wrong=wrong+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                    }
                    else{Toast.makeText( TestChoose.this, "Good you finish the test ^^ ", Toast.LENGTH_SHORT).show(); }
                }
            }
        });
        choose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result==2){
                    if(Tests<numTest) {
                        star=star+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                        AddAnotherTest();}
                    else{Toast.makeText( TestChoose.this, "Good you finish the test ^^ ", Toast.LENGTH_SHORT).show(); }

                }
                else{
                    if(Tests<numTest) {
                        wrong=wrong+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                    }
                    else{Toast.makeText( TestChoose.this, "Good you finish the test ^^ ", Toast.LENGTH_SHORT).show(); }
                }
            }
        });
        choose3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(result==3){

                    if(Tests<numTest) {
                        star=star+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                        AddAnotherTest();}
                    else{Toast.makeText( TestChoose.this, "Good you finish the test ^^ ", Toast.LENGTH_SHORT).show(); }

                }
                else{

                    if(Tests<numTest) {
                        wrong=wrong+1;
                        tright.setText(Integer.toString(star));
                        twrong.setText( Integer.toString(wrong));
                    }
                    else{Toast.makeText( TestChoose.this, "Good you finish the test ^^ ", Toast.LENGTH_SHORT).show(); }

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
//        Tests=Tests+1;
    }
}