package com.carethy.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.application.Carethy.BodyData;
import com.carethy.model.CarethyGraphData;
import com.carethy.util.Util;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

/**
 * Abstract Fragment that appears in the "content_frame". ContentFragmentFactory
 * will return concrete subclasses.
 */

public abstract class GraphBaseFragment extends Fragment {
	public LinearLayout lineLinearLayout;
	public LinearLayout barLinearLayout;
	public LinearLayout summary;
	private LineGraphView mLineGraphView;
	private BarGraphView mBarGraphView;
	private TextView statsAvg;
	private TextView statsHigh;
	private TextView statsLow;

	private GraphViewSeriesStyle mGraphViewSeriesStyle;

	protected View rootView;
	private ProgressDialog mProgressDialog = null;
	protected BodyData mBodyData;
	protected HashMap<BodyData, ArrayList<CarethyGraphData>> map = new HashMap<BodyData, ArrayList<CarethyGraphData>>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_graph, container, false);

		// add refresh icon to the action bar
		setHasOptionsMenu(true);

		loadData();

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		// add refresh button
		inflater.inflate(R.menu.fragment_graph_actions, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_share:
			shareData(summary);
			return true;
		case R.id.action_refresh:
			loadData();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void shareData(LinearLayout linearLayout) {
		linearLayout.setDrawingCacheEnabled(true);// enable cache
		linearLayout.buildDrawingCache(true);

		Bitmap imageData = Bitmap.createBitmap(linearLayout.getDrawingCache());

		linearLayout.setDrawingCacheEnabled(false); // clear cache

		String fileName = Util.getTimestamp() + ".png";
		if (Util.saveImage(imageData, fileName)) {
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setType("image/*");
			String imagePath = Environment.getExternalStorageDirectory()
					+ File.separator + "Carethy" + File.separator + fileName;
			File imageFileToShare = new File(imagePath);
			Uri uri = Uri.fromFile(imageFileToShare);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(intent, "Share to..."));
		}
	}

	public void loadData() {
		AsyncTask<Void, Integer, Void> task = new AsyncTask<Void, Integer, Void>() {
			@Override
			protected void onPreExecute() {
				mProgressDialog = ProgressDialog.show(getActivity(),
						"Loading data...", "Please be patient.", true);
			}

			@Override
			protected Void doInBackground(Void... arg0) {
				try {
					Thread.sleep(500);
					map.put(mBodyData, Util.fetchData(mBodyData));

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				initView();

				mProgressDialog.dismiss();
			}
		};
		task.execute((Void[]) null);
	}

	public void initView() {
		summary = (LinearLayout) rootView.findViewById(R.id.summary);

		// Line Graph
		lineLinearLayout = (LinearLayout) rootView
				.findViewById(R.id.graph_line);
		graph(mLineGraphView, lineLinearLayout, mBodyData, R.id.graph_line,
				"line", false);

		// Stats
		String number = Double.toString(map.get(mBodyData).get(0).getLow());
		if (map.get(mBodyData).size() > 1) {
			number += "\n"
					+ Double.toString(map.get(mBodyData).get(1).getLow());
		}
		SpannableString ss = new SpannableString(number + "\n Low");
		ss.setSpan(new RelativeSizeSpan(2f), 0, number.length(), 0);
		ss.setSpan(new ForegroundColorSpan(Color.RED), 0, number.length(), 0);
		statsLow = (TextView) rootView.findViewById(R.id.stats_low);
		statsLow.setText(ss);

		number = Double.toString(map.get(mBodyData).get(0).getAvg());
		if (map.get(mBodyData).size() > 1) {
			number += "\n"
					+ Double.toString(map.get(mBodyData).get(1).getAvg());
		}
		ss = new SpannableString(number + "\n Average");
		ss.setSpan(new RelativeSizeSpan(2f), 0, number.length(), 0);
		ss.setSpan(new ForegroundColorSpan(Color.RED), 0, number.length(), 0);
		statsAvg = (TextView) rootView.findViewById(R.id.stats_avg);
		statsAvg.setText(ss);

		number = Double.toString(map.get(mBodyData).get(0).getHigh());
		if (map.get(mBodyData).size() > 1) {
			number += "\n"
					+ Double.toString(map.get(mBodyData).get(1).getHigh());
		}
		ss = new SpannableString(number + "\n High");
		ss.setSpan(new RelativeSizeSpan(2f), 0, number.length(), 0);
		ss.setSpan(new ForegroundColorSpan(Color.RED), 0, number.length(), 0);
		statsHigh = (TextView) rootView.findViewById(R.id.stats_high);
		statsHigh.setText(ss);

		// Bar Graph
		barLinearLayout = (LinearLayout) rootView.findViewById(R.id.graph_bar);
		graph(mBarGraphView, barLinearLayout, mBodyData, R.id.graph_bar, "bar",
				false);
	}

	public void graph(GraphView graphView, LinearLayout linearLayout,
			BodyData mBodyData, int id, String type, boolean isTrends) {

		if (type.equals("line")) {
			graphView = new LineGraphView(getActivity(), mBodyData.name()
					.toUpperCase());

			for (final CarethyGraphData value : map.get(mBodyData)) {
				mGraphViewSeriesStyle = new GraphViewSeriesStyle(
						value.getUnit() == "Diastolic" ? Color.rgb(229, 99, 51)
								: Color.rgb(51, 181, 229), 5);

				GraphViewData[] array = new GraphViewData[value.getTimeSeries().length];
				for (int i = 0; i < array.length; i++) {
					array[i] = value.getTimeSeries()[i];
				}

				// reverse input since date was reversed
				for (int i = 0; i < array.length / 2; i++) {
					GraphViewData tmp = array[i];
					array[i] = array[array.length - 1 - i];
					array[array.length - 1 - i] = tmp;
				}

				GraphViewSeries series = new GraphViewSeries(value.getUnit(),
						mGraphViewSeriesStyle, array);

				graphView.addSeries(series);

			}
		} else {
			graphView = new BarGraphView(getActivity(), mBodyData.name()
					.toUpperCase());

			for (final CarethyGraphData value : map.get(mBodyData)) {

				mGraphViewSeriesStyle = new GraphViewSeriesStyle(
						value.getUnit() == "Diastolic" ? Color.rgb(229, 99, 51)
								: Color.rgb(51, 181, 229), 5);

				GraphViewData[] array = new GraphViewData[value.getTimeSeries().length];
				for (int i = 0; i < array.length; i++) {
					array[i] = value.getTimeSeries()[i];
				}

				// reverse input since date was reversed
				for (int i = 0; i < array.length / 2; i++) {
					GraphViewData tmp = array[i];
					array[i] = array[array.length - 1 - i];
					array[array.length - 1 - i] = tmp;
				}

				// calculate the diff from prev day
				GraphViewData[] newArray = new GraphViewData[array.length];
				for (int i = 0; i < newArray.length; i++) {
					if (i == 0) {
						newArray[i] = new GraphViewData(array[i].valueX, 0);
					} else {
						newArray[i] = new GraphViewData(array[i].valueX,
								array[i].valueY - array[i - 1].valueY);
					}
				}

				GraphViewSeries series = new GraphViewSeries(value.getUnit(),
						mGraphViewSeriesStyle, newArray);

				graphView.addSeries(series);

			}

		}

		graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
			@Override
			public String formatLabel(double value, boolean isValueX) {
				if (isValueX) {
					Date d = new Date((long) value);

					return new SimpleDateFormat("MMMM dd", Locale.CANADA)
							.format(d).toString();
				}
				return null;
			}
		});

		// set styles
		graphView.getGraphViewStyle().setNumHorizontalLabels(0);
		graphView.getGraphViewStyle().setNumVerticalLabels(0);
		graphView.setScalable(true);
		graphView.setShowLegend(true);
		graphView.setLegendAlign(LegendAlign.BOTTOM);
		graphView.getGraphViewStyle().setLegendBorder(20);
		graphView.getGraphViewStyle().setLegendSpacing(30);
		graphView.getGraphViewStyle().setLegendWidth(200);

		if (type.equals("line")) {
			if (mBodyData.equals(BodyData.activities)) {
				graphView.setManualYAxisBounds(60, 10);
			}

			if (mBodyData.equals(BodyData.sleep)) {
				graphView.setManualYAxisBounds(490, 280);
			}

			if (mBodyData.equals(BodyData.heartBeats)) {
				graphView.setManualYAxisBounds(95, 60);
			}

			if (mBodyData.equals(BodyData.bloodPressures)) {
				graphView.setManualYAxisBounds(175, 75);
			}
		}

		if (isTrends) {
			graphView.getGraphViewStyle().setVerticalLabelsColor(Color.WHITE);
			graphView.getGraphViewStyle().setVerticalLabelsWidth(1);
			graphView.getGraphViewStyle().setGridColor(Color.WHITE);
		} else {
			if (type.equals("line")) {
				((LineGraphView) graphView).setDrawDataPoints(true);
				((LineGraphView) graphView).setDataPointsRadius(10f);
			} else {
				graphView.getGraphViewStyle().setVerticalLabelsWidth(100);

			}
		}

		// add GraphView to LinearLayout
		linearLayout.removeAllViews();
		linearLayout.addView(graphView);

	}
}