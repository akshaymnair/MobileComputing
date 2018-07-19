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
    private int lastXpoint = 0;
    private Random random = new Random();
    public GraphView graph;
    public LineGraphSeries<DataPoint> series;
    private boolean running = false;
    private Runnable graphThread = new Runnable() {
        @Override
        public void run() {
            //add values to the graph similar to the heart beat
            series.appendData(new DataPoint(lastXpoint++,  250*Math.sin(lastXpoint*7)*Math.sin(lastXpoint/2)*Math.cos(3.25*lastXpoint)), true, 1000);
            //lastXpoint += 10;
            runGraph();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graph = findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        //setting the graph x axis and y axis
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-300);
        graph.getViewport().setMaxY(300);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(4);
        graph.getViewport().setMaxX(80);
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        final Button runButton = findViewById(R.id.runButton);
        runButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //if graph is not running add the graph
                if(!running) {
                    graph.addSeries(series);
                    running = true;
                    runGraph();
                }
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
        //if graph is running remove the graph and stop the thread
        if(running) {
            graph.removeAllSeries();
            graphHandler.removeCallbacks(graphThread);
        }
        //set graph status as not running and stop the thread
        running = false;
    }

    private void runGraph() {
        graphHandler.postDelayed(graphThread, 300);
    }
}
