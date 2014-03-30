package com.carethy.fragment;

import java.io.File;
import java.text.DecimalFormat;
<<<<<<< HEAD
import java.util.List;
=======
import java.util.ArrayList;
>>>>>>> origin/master

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
<<<<<<< HEAD
=======
import android.graphics.drawable.GradientDrawable;
>>>>>>> origin/master
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
<<<<<<< HEAD
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.R.drawable;
import com.carethy.model.Recommendation;
import com.carethy.util.HackUtils;
=======
import android.widget.ListView;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.adapter.RecommendationListAdapter;
import com.carethy.model.Recommendation;
>>>>>>> origin/master
import com.carethy.util.Util;
import com.carethy.application.Carethy;

public class HomeFragment extends Fragment {
	private View rootView;
	private LinearLayout panel;
	private TextView activity;
	private TextView sleep;
	private TextView heartRate;
	private TextView bloodPressure;
	private ListView mListView;
	private RecommendationListAdapter adapter;
	private ProgressDialog mProgressDialog = null;
	private int activityData;
	private float sleepData;
	private int heartRateData;
	private float bloodPressureData;
	public static DecimalFormat df = new DecimalFormat("#.#");

	private LinearLayout scrollInnerPanel;
	LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.WRAP_CONTENT);

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// test-code - For force data into recom table
		if (HackUtils.test) {
			HackUtils.hackIntoRecomDB(100, "High alert");
		}
		
		rootView = inflater.inflate(R.layout.fragment_home, container, false);

		// add refresh icon to the action bar
		setHasOptionsMenu(true);

		loadData();

		return rootView;
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
		panel.setDrawingCacheEnabled(true);// enable cache
		panel.buildDrawingCache(true);

		Bitmap imageData = Bitmap.createBitmap(panel.getDrawingCache());

		panel.setDrawingCacheEnabled(false); // clear cache

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
		panel = (LinearLayout) rootView.findViewById(R.id.panel);

		activity = (TextView) rootView.findViewById(R.id.activity);
		activity.setText(activityData + "\nsteps");
		((GradientDrawable) activity.getBackground()).setColor(Color
				.parseColor("#ff5900"));

		sleep = (TextView) rootView.findViewById(R.id.sleep);
		sleep.setText(df.format(sleepData) + "\nhours");
		((GradientDrawable) sleep.getBackground()).setColor(Color
				.parseColor("#ff9a00"));

		heartRate = (TextView) rootView.findViewById(R.id.heart_rate);
		heartRate.setText(heartRateData + "\nbeats");
		((GradientDrawable) heartRate.getBackground()).setColor(Color
				.parseColor("#0d56a6"));

		bloodPressure = (TextView) rootView.findViewById(R.id.blood_pressure);
		bloodPressure.setText(df.format(bloodPressureData) + "\nmh");
		((GradientDrawable) bloodPressure.getBackground()).setColor(Color
				.parseColor("#00a876"));

<<<<<<< HEAD
		scrollInnerPanel = (LinearLayout) rootView
				.findViewById(R.id.scrollInnerPanel);

		fillRecommendations();
	}

	private void fillRecommendations() {
		List<Recommendation> recomms = Carethy.datasource
				.getRecommendations(""); // DBRecomHelper.RECOM_LIMIT);

		if (!recomms.isEmpty()) {
			for (Recommendation recom : recomms) {

				TextView tv = getTextView();
				if (recom.getRecomId() <= 300) {
					tv.setTextColor(Color.RED);
				} else {
					tv.setTextColor(Color.GREEN);
				}
				tv.setText(recom.getRecom());
				this.scrollInnerPanel.addView(tv);
			}
		} else {
			TextView tv = getTextView();
			tv.setText("No Stored Recommendations");
			this.scrollInnerPanel.addView(tv);
		}
	}

	private TextView getTextView() {
		TextView tv = new TextView(this.getActivity());
		tv.setLayoutParams(lparams);
		tv.setBackgroundResource(drawable.recommendations_style);
		return tv;
=======
		mListView = (ListView) rootView.findViewById(R.id.home_listview);

		final ArrayList<Recommendation> list = Util.getRecommendation();

		adapter = new RecommendationListAdapter(getActivity(),
				android.R.layout.simple_list_item_1, list);

		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final Recommendation item = (Recommendation) parent.getItemAtPosition(position);
				view.animate().setDuration(100).alpha(0)
						.withEndAction(new Runnable() {
							@Override
							public void run() {
								list.remove(item);
								adapter.notifyDataSetChanged();
								view.setAlpha(1);
							}
						});
			}

		});
>>>>>>> origin/master
	}

}