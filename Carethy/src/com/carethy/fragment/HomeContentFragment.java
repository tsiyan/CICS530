package com.carethy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.carethy.R;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;

public class HomeContentFragment extends AbstractContentFragment {
	private LinearLayout linearLayout;
	private BarGraphView graphView;
	private double[] values=null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_graph, container,
				false);

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			values=bundle.getDoubleArray("values");
			if(values!=null){
				initView(rootView);
			}
		}

		return rootView;
	}

	public void initView(View rootView) {
		GraphViewData[] graphViewData = new GraphViewData[values.length];
		for (int i = 0; i < graphViewData.length; i++) {
			GraphViewData v = new GraphViewData(i, values[i]);//TODO for now just random data
			graphViewData[i] = v;
		}
		GraphViewSeries series = new GraphViewSeries(graphViewData);

		// GraphView
		graphView = new BarGraphView(getActivity(), "Mock data");
		graphView.addSeries(series);

		// set styles
		graphView.getGraphViewStyle().setNumHorizontalLabels(10);
		graphView.getGraphViewStyle().setNumVerticalLabels(10);

		graphView.setViewPort(1, 10);
		graphView.setScalable(true);

		graphView.setLegendAlign(LegendAlign.BOTTOM);
		graphView.getGraphViewStyle().setLegendBorder(20);
		graphView.getGraphViewStyle().setLegendSpacing(30);
		graphView.getGraphViewStyle().setLegendWidth(200);

		// Create GraphView
		linearLayout = (LinearLayout) rootView.findViewById(R.id.graph);
		linearLayout.addView(graphView);

	}
}
