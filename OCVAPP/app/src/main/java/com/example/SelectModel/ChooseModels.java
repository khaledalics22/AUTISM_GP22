package com.example.SelectModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ML.ML;
import com.example.ocvapp.R;
import com.example.try1.MenuActivity;

public class ChooseModels extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_models);
        Button ready = findViewById(R.id.ready_models);
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ML.buildFDModel(ChooseModels.this, ML.ModelType.READY);
                startActivity(new Intent(ChooseModels.this, MenuActivity.class));
            }
        });

        Button custom = findViewById(R.id.custom_models);
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ML.buildFDModel(ChooseModels.this, ML.ModelType.CUSTOM);
                startActivity(new Intent(ChooseModels.this, MenuActivity.class));
            }
        });
    }
}