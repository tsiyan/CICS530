package com.carethy.model;

import com.jjoe64.graphview.GraphView.GraphViewData;

public class CarethyGraphData {
	private String unit;
	private GraphViewData[] timeSeries;

	public CarethyGraphData(String unit,
			GraphViewData[] timeSeries) {
		this.unit = unit;
		this.timeSeries = timeSeries;
	}

	public GraphViewData[] getTimeSeries() {
		return timeSeries;
	}

	public void setTimeSeries(GraphViewData[] timeSeries) {
		this.timeSeries = timeSeries;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}



}
