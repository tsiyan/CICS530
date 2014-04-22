package com.carethy.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.carethy.R;
import com.carethy.application.Carethy.BodyData;
import com.carethy.util.Util;
import com.jjoe64.graphview.LineGraphView;

public class TrendsFragment extends GraphBaseFragment {
	private LinearLayout trends;

	private LineGraphView graphView_activities;
	private LinearLayout linearLayout_activities;

	private LineGraphView graphView_sleep;
	private LinearLayout linearLayout_sleep;

	private LineGraphView graphView_heartBeats;
	private LinearLayout linearLayout_heartBeats;

	private LineGraphView graphView_bloodPressures;
	private LinearLayout linearLayout_bloodPressures;

	protected View rootView;
	private ProgressDialog mProgressDialog = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_trends, container, false);
		// add refresh icon to the action bar
		setHasOptionsMenu(true);

		loadData();

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// add refresh button
		inflater.inflate(R.menu.fragment_trends_actions, menu);
	}

	@Override
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
					map.put(BodyData.activities,
							Util.fetchData(BodyData.activities));
					map.put(BodyData.sleep, Util.fetchData(BodyData.sleep));
					map.put(BodyData.heartBeats,
							Util.fetchData(BodyData.heartBeats));
					map.put(BodyData.bloodPressures,
							Util.fetchData(BodyData.bloodPressures));

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_share:
			shareData(trends);
			return true;
		case R.id.action_refresh:
			loadData();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void initView() {
		trends = (LinearLayout) rootView.findViewById(R.id.trends);
		linearLayout_activities = (LinearLayout) rootView
				.findViewById(R.id.trend_activities);
		linearLayout_sleep = (LinearLayout) rootView
				.findViewById(R.id.trend_sleep);
		linearLayout_heartBeats = (LinearLayout) rootView
				.findViewById(R.id.trend_heart_beats);
		linearLayout_bloodPressures = (LinearLayout) rootView
				.findViewById(R.id.trend_blood_pressures);

		graph(graphView_activities, linearLayout_activities,
				BodyData.activities, R.id.trend_activities,"line",true);
		graph(graphView_sleep, linearLayout_sleep, BodyData.sleep,
				R.id.trend_sleep,"line",true);
		graph(graphView_heartBeats, linearLayout_heartBeats,
				BodyData.heartBeats, R.id.trend_heart_beats,"line",true);
		graph(graphView_bloodPressures, linearLayout_bloodPressures,
				BodyData.bloodPressures, R.id.trend_blood_pressures,"line",true);
	}
}
