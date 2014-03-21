package com.carethy.fragment;

import java.text.DecimalFormat;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.util.Util;

public class HomeFragment extends Fragment {
	private View rootView;
	private TextView activity;
	private TextView sleep;
	private TextView heartRate;
	private TextView bloodPressure;
	private ProgressDialog mProgressDialog = null;
	private int activityData;
	private float sleepData;
	private int heartRateData;
	private float bloodPressureData;
	public static DecimalFormat df = new DecimalFormat("#.#");

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_home, container, false);

		// add refresh icon to the action bar
		setHasOptionsMenu(true);

		loadData();

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		// add refresh button
		inflater.inflate(R.menu.fragment_home_actions, menu);
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
					activityData = Util.getActivityData();
					sleepData = Util.getSleepData();
					heartRateData = Util.getHeartRateData();
					bloodPressureData = Util.getBloodPressureData();

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

	private void initView() {
		activity = (TextView) rootView.findViewById(R.id.activity);
		activity.setText(activityData + " steps");

		sleep = (TextView) rootView.findViewById(R.id.sleep);
		sleep.setText(df.format(sleepData) + " hours");

		heartRate = (TextView) rootView.findViewById(R.id.heart_rate);
		heartRate.setText(heartRateData + " beats");

		bloodPressure = (TextView) rootView.findViewById(R.id.blood_pressure);
		bloodPressure.setText(df.format(bloodPressureData) + " mh");

	}

}