package com.example.ocvapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.Objects.Score;
import com.example.ocvapp.RestApis.RestAPI;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class CheckRestApi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_rest_api);
        Score score = new Score();
        Button button = findViewById(R.id.send_request);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score.increaseCorrectHappy();
                score.increaseErrorHappy();
                Log.e("Send Request-------------", "start sending");
                RestAPI.getInstance().getStatistics(CheckRestApi.this);
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission Granted", "Truueeeeeeeeeeeeeeeeee");
        }
        GraphView graphView = findViewById(R.id.idGraphView);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                // on below line we are adding
                // each point on our x and y axis.
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 9),
                new DataPoint(4, 6),
                new DataPoint(5, 3),
                new DataPoint(6, 6),
                new DataPoint(7, 1),
                new DataPoint(8, 2)
        });
        graphView.setTitle("My Graph View");
        graphView.setTitleColor(R.color.purple_200);

        // on below line we are setting
        // our title text size.
        graphView.setTitleTextSize(18);

        // on below line we are adding
        // data series to our graph view.
        graphView.addSeries(series);
    }
}