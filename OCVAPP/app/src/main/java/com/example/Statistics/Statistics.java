package com.example.Statistics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.Objects.Score;
import com.example.RestApis.RestAPI;
import com.example.ocvapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;

public class Statistics extends AppCompatActivity {



    public interface OnResponseSuccess{
        void getStatisticsSuccess(JSONObject jsonObject);
    }
    GraphView graphView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!= null){
            actionBar.setTitle("Statistics");
        }
        graphView = findViewById(R.id.idGraphView);
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
        graphView.setTitle("Eye Contact Development");
        graphView.setTitleColor(R.color.black);
        graphView.setTitleTextSize(58);
        graphView.addSeries(series);
        new StatisticsBackground().execute();
    }
    class StatisticsBackground extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Score score = new Score();
            score.increaseCorrectSad();;
            score.increaseErrorHappy();
            RestAPI.getInstance().setScore(Statistics.this,score);
            RestAPI.getInstance().getStatistics(Statistics.this, new OnResponseSuccess() {
                @Override
                public void getStatisticsSuccess(JSONObject jsonObject) {
                    Log.e("Statistics **************", jsonObject.toString());
                }
            });
             return null;
        }



    }
}