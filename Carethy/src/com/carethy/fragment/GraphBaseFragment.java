package com.carethy.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.carethy.R;
import com.carethy.util.Util;
import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

/**
 * Abstract Fragment that appears in the "content_frame". ContentFragmentFactory
 * will return concrete subclasses.
 */
public abstract class GraphBaseFragment extends Fragment {
	private LinearLayout linearLayout;
	private LineGraphView graphView;
	private Button captureButton;
	private View rootView;
	private ProgressDialog mProgressDialog = null;
	private double[] values;

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
		super.onOptionsItemSelected(item);
		loadData();
		return true;
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
					values = Util.fetchData();

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
		GraphViewData[] graphViewData = new GraphViewData[values.length];
		for (int i = 0; i < graphViewData.length; i++) {
			GraphViewData v = new GraphViewData(i, values[i]);
			graphViewData[i] = v;
		}
		GraphViewSeries series = new GraphViewSeries(graphViewData);

		// GraphView
		graphView = new LineGraphView(getActivity(), "");
		graphView.addSeries(series);

		// set styles
		graphView.getGraphViewStyle().setNumHorizontalLabels(0);
		graphView.getGraphViewStyle().setNumVerticalLabels(0);
		graphView.setDrawBackground(true);
		graphView.setBackgroundColor(Color.rgb(153, 204, 153));
		graphView.setViewPort(1, 10);
		graphView.setScalable(true);

		graphView.setLegendAlign(LegendAlign.BOTTOM);
		graphView.getGraphViewStyle().setLegendBorder(20);
		graphView.getGraphViewStyle().setLegendSpacing(30);
		graphView.getGraphViewStyle().setLegendWidth(200);

		// Create GraphView
		linearLayout = (LinearLayout) rootView.findViewById(R.id.graph);
		linearLayout.removeAllViews();
		linearLayout.addView(graphView);

		// Create HoloGraph
		Line l = new Line();
		l.setColor(Color.parseColor("#FFBB33"));

		for (int i = 0; i < values.length; i++) {
			LinePoint p = new LinePoint();
			p.setX(i);
			p.setY(values[i] > 0 ? values[i] : -3 * values[i]);
			l.addPoint(p);
		}

		LineGraph li = (LineGraph) rootView.findViewById(R.id.holograph);
		li.removeAllLines();
		li.addLine(l);
		li.setRangeY(0, 5);
		li.setLineToFill(0);

		// Capture button
		captureButton = (Button) rootView.findViewById(R.id.capture);
		captureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				linearLayout.setDrawingCacheEnabled(true);// enable cache
				linearLayout.buildDrawingCache(true);

				Bitmap imageData = Bitmap.createBitmap(linearLayout
						.getDrawingCache());

				linearLayout.setDrawingCacheEnabled(false); // clear cache

				String fileName = Util.getTimestamp() + ".png";
				if (Util.saveImage(imageData, fileName)) {
					Toast.makeText(getActivity(), "Saved in Carethy folder.",
							Toast.LENGTH_SHORT).show();
				}
			}

		});
	}

}