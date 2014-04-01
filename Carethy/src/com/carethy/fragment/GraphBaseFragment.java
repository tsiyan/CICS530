package com.carethy.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.carethy.R;
import com.carethy.application.Carethy.BodyData;
import com.carethy.model.CarethyGraphData;
import com.carethy.util.Util;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

/**
 * Abstract Fragment that appears in the "content_frame". ContentFragmentFactory
 * will return concrete subclasses.
 */

public abstract class GraphBaseFragment extends Fragment {
	private LinearLayout linearLayout;
	private LineGraphView graphView;
	protected View rootView;
	private ProgressDialog mProgressDialog = null;
	private ArrayList<CarethyGraphData> values = null;
	protected BodyData mBodyData;

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
			shareData();
			return true;
		case R.id.action_refresh:
			loadData();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void shareData() {
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
					values = Util.fetchData(mBodyData);

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
		int count = 0;
		graphView = new LineGraphView(getActivity(), "");

		for (CarethyGraphData value : values) {
			GraphViewSeries series = new GraphViewSeries(value.getUnit(),
					new GraphViewSeriesStyle(((count == 0) ? Color.rgb(51, 181,
							229) : Color.rgb(229, 99, 51)), 5),
					value.getTimeSeries());

			graphView.addSeries(series);
			count++;
		}

		final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d",
				Locale.CANADA);
		graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
			@Override
			public String formatLabel(double value, boolean isValueX) {
				if (isValueX) {
					Date d = new Date((long) value);
					return dateFormat.format(d);
				}
				return null; // let graphview generate Y-axis label for us
			}
		});

		// set styles
		graphView.getGraphViewStyle().setNumHorizontalLabels(0);
		graphView.getGraphViewStyle().setNumVerticalLabels(0);
//		graphView.setDrawBackground(true);
		graphView.setScalable(true);

		graphView.setShowLegend(true);
		graphView.setLegendAlign(LegendAlign.BOTTOM);
		graphView.getGraphViewStyle().setLegendBorder(20);
		graphView.getGraphViewStyle().setLegendSpacing(30);
		graphView.getGraphViewStyle().setLegendWidth(200);
		graphView.setDrawDataPoints(true);
		graphView.setDataPointsRadius(10f);

		// add GraphView to LinearLayout
		linearLayout = (LinearLayout) rootView.findViewById(R.id.graph);
		linearLayout.removeAllViews();
		linearLayout.addView(graphView);

		

	}
}