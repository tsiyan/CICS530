package com.carethy.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.carethy.R;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class DisplayResultActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_result);
		
		initView();
		graph();
}

	private void initView(){
		//Up navigation 
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	private void graph(){
        GraphView graphView=null;

		String category=getIntent().getStringExtra("category");
		String type=getIntent().getStringExtra("type");

        if (type.equalsIgnoreCase("bar")) {
                graphView = new BarGraphView(this, category);
                ((BarGraphView) graphView).setDrawValuesOnTop(true);
        } else {
                graphView = new LineGraphView(this, category);
                ((LineGraphView) graphView).setDrawDataPoints(true);
                ((LineGraphView) graphView).setDataPointsRadius(15f);
        }
        
        // custom static labels
        graphView.setHorizontalLabels(new String[] {"2 days ago", "yesterday", "today", "tomorrow"});
        graphView.setVerticalLabels(new String[] {"high", "middle", "low"});

        GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
									                        new GraphViewData(1, 2.0d),
									                        new GraphViewData(2, 1.5d),
									                        new GraphViewData(3, 2.5d),
									                        new GraphViewData(4, 1.0d)
									                        });
        graphView.setManualYAxisBounds(2.5d, 0d);
        graphView.addSeries(exampleSeries); 

        LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
        layout.addView(graphView);
	}
}