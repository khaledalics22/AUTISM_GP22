package com.example.try1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ocvapp.FaceDetectionActivity;
import com.example.ocvapp.R;

public class MenuActivity extends AppCompatActivity {
    private Button testbtn;
    private Button trainbtn;
    private Button glassBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        testbtn = (Button) findViewById(R.id.Test);
        trainbtn = (Button) findViewById(R.id.Train);
        glassBtn = findViewById(R.id.glass_btn);
        glassBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, FaceDetectionActivity.class));
        });
        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent = new Intent(MenuActivity.this, TestChoose.class);
                startActivity(intent);
            }
        });
        trainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent = new Intent(MenuActivity.this, MiddleAcivity.class);
                startActivity(intent);
            }
        });


    }

}