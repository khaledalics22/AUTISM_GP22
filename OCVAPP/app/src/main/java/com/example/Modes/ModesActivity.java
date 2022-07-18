package com.example.Modes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ML.TellMeEmotions;
import com.example.Practice.PracticeActivity;
import com.example.Profile.ProfileUser;
import com.example.Statistics.Statistics;
import com.example.ocvapp.R;
import com.example.GUI.MiddleAcivity;
import com.example.GUI.TestChoose;

public class ModesActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.statistics){
            startActivity(new Intent(ModesActivity.this,Statistics.class));
        }
        if(item.getItemId() == R.id.profile){
            startActivity(new Intent(ModesActivity.this, ProfileUser.class));
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button testbtn = (Button) findViewById(R.id.Test);
        Button real_time_practice = (Button) findViewById(R.id.real_time_practice);
        Button glassBtn = findViewById(R.id.glass_btn) ;
        Button learn = findViewById(R.id.learn_sentence);

        glassBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, TellMeEmotions.class));
        });
        real_time_practice.setOnClickListener(view -> {
            startActivity(new Intent(this, PracticeActivity.class));
        });
        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent = new Intent(ModesActivity.this, TestChoose.class);
                startActivity(intent);
            }
        });
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent = new Intent(ModesActivity.this, MiddleAcivity.class);
                startActivity(intent);
            }
        });


    }

}