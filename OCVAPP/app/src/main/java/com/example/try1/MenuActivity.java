package com.example.try1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CustomFD.CustomFD;
import com.example.ML.TellMeEmotions;
import com.example.Statistics.Statistics;
import com.example.ocvapp.R;

public class MenuActivity extends AppCompatActivity {
    private Button testbtn;
    private Button trainbtn;
    private Button glassBtn;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.statistics){
            startActivity(new Intent(MenuActivity.this,Statistics.class));
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        testbtn = (Button) findViewById(R.id.Test);
        trainbtn = (Button) findViewById(R.id.Train);
        glassBtn = findViewById(R.id.glass_btn);
        glassBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, TellMeEmotions.class));
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