package com.example.Statistics;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Objects.Score;
import com.example.RestApis.RestAPI;
import com.example.ocvapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Statistics extends AppCompatActivity {


    public interface OnResponseSuccess {
        void getStatisticsSuccess(JSONObject jsonObject);

        void getEyeContactScoresSuccess(JSONObject jsonObject);

    }

    GraphView graphViewStatistics;
    GraphView graphViewEyeContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Statistics");
        }
        graphViewStatistics = findViewById(R.id.graph_view_statistics);
        graphViewStatistics.setTitle("Emotion Recognizing Development");
        graphViewStatistics.setTitleColor(R.color.black);
        graphViewStatistics.setTitleTextSize(58);
        new StatisticsBackground().execute();

        // eye contact
        graphViewEyeContact = findViewById(R.id.graph_view_eye_contact);
        graphViewEyeContact.setTitle("Eye Contact Development");
        graphViewEyeContact.setTitleColor(R.color.black);
        graphViewEyeContact.setTitleTextSize(58);
        new EyeContactBackground().execute();

    }

    private void updateGraphStatistics(DataPoint[] points) {
        graphViewStatistics.addSeries(new LineGraphSeries<DataPoint>(points));
    }

    private void updateGraphEyeContact(DataPoint[] points) {
        graphViewEyeContact.addSeries(new LineGraphSeries<DataPoint>(points));

    }

    class EyeContactBackground extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            RestAPI.getInstance().getEyeContactScores(Statistics.this, new OnResponseSuccess() {
                @Override
                public void getStatisticsSuccess(JSONObject jsonObject) {

                }

                @Override
                public void getEyeContactScoresSuccess(JSONObject jsonObject) {
                    try {
                        JSONArray array = jsonObject.getJSONArray("results");
                        DataPoint[] points = new DataPoint[array.length()];
                        Log.e("Eye Contact Activity **************", array.toString());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = (JSONObject) array.get(i);

                            int test_num = object.getInt("test_number");
                            double s = object.getDouble("score");
                            points[i] = (new DataPoint(test_num, s));
                        }
                        updateGraphEyeContact(points);

                    } catch (JSONException e) {
//                        e.printStackTrace();
                        Log.e("Eye Contact Activity **************", "Conversion failed");

                    }
                }
            });
            return null;
        }

    }

    class StatisticsBackground extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            RestAPI.getInstance().getStatistics(Statistics.this, new OnResponseSuccess() {
                @Override
                public void getStatisticsSuccess(JSONObject jsonObject) {
                    try {
                        JSONArray array = jsonObject.getJSONArray("results");
                        DataPoint[] points = new DataPoint[array.length()];
                        Log.e("Statistics Activity **************", array.toString());

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = (JSONObject) array.get(i);
                            int test_num = object.getInt("test_number");
                            double s = object.getDouble("score");
                            points[i] = (new DataPoint(test_num, s));
                        }
                        updateGraphStatistics(points);

                    } catch (JSONException e) {
//                        e.printStackTrace();
                        Log.e("Statistics Activity **************", "Conversion failed");

                    }
                }

                @Override
                public void getEyeContactScoresSuccess(JSONObject jsonObject) {

                }
            });
            return null;
        }


    }
}