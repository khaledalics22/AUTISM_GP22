package com.example.try1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    private Button testbtn;
    private Button trainbtn,profilebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        testbtn=(Button) findViewById(R.id.Test);
        trainbtn=(Button) findViewById(R.id.Train);
        profilebtn=(Button) findViewById(R.id.Profile);

        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(MenuActivity.this, TestChoose.class);
                startActivity(intent);
            }
        });
        trainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(MenuActivity.this, MiddleAcivity.class);
                startActivity(intent);
            }
        });

        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(MenuActivity.this, Profile.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed(){
        final Intent intent;
        intent =  new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
    }
}