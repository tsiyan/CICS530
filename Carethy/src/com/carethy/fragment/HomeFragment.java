package com.carethy.fragment;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import android.widget.ScrollView;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.R.drawable;
import com.carethy.model.Recommendation;
import com.carethy.util.Util;
import com.carethy.application.Carethy;

public class HomeFragment extends Fragment {
	private View rootView;
	private LinearLayout linearLayout;
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

	private ScrollView scroll;
	private LinearLayout scrollPanel;
	private LinearLayout scrollInnerPanel;
	private TextView leftpanel1;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_home, container, false);

		// add refresh icon to the action bar
		setHasOptionsMenu(true);

		loadData();

		loadRecommendations();

		return rootView;
	}

	private void loadRecommendations() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.fragment_home_actions, menu);
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
		linearLayout = (LinearLayout) rootView.findViewById(R.id.panel);

		activity = (TextView) rootView.findViewById(R.id.activity);
		activity.setText(activityData + " steps");

		sleep = (TextView) rootView.findViewById(R.id.sleep);
		sleep.setText(df.format(sleepData) + " hours");

		heartRate = (TextView) rootView.findViewById(R.id.heart_rate);
		heartRate.setText(heartRateData + " beats");

		bloodPressure = (TextView) rootView.findViewById(R.id.blood_pressure);
		bloodPressure.setText(df.format(bloodPressureData) + " mh");

		scroll = (ScrollView) rootView.findViewById(R.id.scroll);
		scrollPanel = (LinearLayout) rootView.findViewById(R.id.scrollPanel);
		scrollInnerPanel = (LinearLayout) rootView
				.findViewById(R.id.scrollInnerPanel);

		fillRecommendations();
	}

	private void fillRecommendations() {
		LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		List<Recommendation> recomms = Carethy.datasource
				.getAllRecommendations();

		if (!recomms.isEmpty()) {
			for (Recommendation recom : recomms) {
				TextView tv = new TextView(this.getActivity());
				tv.setLayoutParams(lparams);
				tv.setBackgroundResource(drawable.recommendations_style);
				tv.setText(recom.getRecom());
				this.scrollInnerPanel.addView(tv);
			}
		}
	}

}