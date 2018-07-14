package com.mc.cse535.assignment;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Handler graphHandler = new Handler();    // the message queue to communicate to runGraph thread
    private double lastXpoint = 0;
    private Random random = new Random();
    public GraphView graph;
    public LineGraphSeries<DataPoint> series;
    private Runnable graphThread = new Runnable() {
        @Override
        public void run() {
            series.appendData(new DataPoint(lastXpoint, random.nextInt(500)), false, 1000);
            lastXpoint += 10;
            runGraph();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graph = findViewById(R.id.graph);
        series = new LineGraphSeries<>();

        final Button runButton = findViewById(R.id.runButton);
        runButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                graph.addSeries(series);
                runGraph();
            }
        });

        final Button stopButton = findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                stopGraph();
            }
        });
    }

    private void stopGraph() {
        graph.removeAllSeries();
        graphHandler.removeCallbacks(graphThread);
    }

    private void runGraph() {
        graphHandler.postDelayed(graphThread, 500);
    }
}
