package com.carethy.fragment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carethy.R;
import com.carethy.adapter.RecommendationListAdapter;
import com.carethy.application.Carethy;
import com.carethy.database.DBRecomHelper;
import com.carethy.model.Recommendation;
import com.carethy.util.HackUtils;
import com.carethy.util.Util;

public class HomeFragment extends Fragment {
	private View rootView;
	private LinearLayout panel;
	private TextView activities;
	private TextView sleep;
	private TextView heartBeats;
	private TextView bloodPressures;
	private ListView mListView;
	private ProgressDialog mProgressDialog = null;
	private int activitiesData;
	private int sleepData;
	private int heartBeatsData;
	private int[] bloodPressuresData;
	public static DecimalFormat df = new DecimalFormat("#.#");

	private boolean isDataFileChanged = false;

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

			String engineResponse = null;
			private boolean connEstablished = false;

			String recommendation;
			int id;
			String recoUrl;
			int severity;

			@Override
			protected void onPreExecute() {
				mProgressDialog = ProgressDialog.show(getActivity(),
						"Loading data...", "Please be patient.", true);
			}

			@Override
			protected Void doInBackground(Void... arg0) {
				try {
					Thread.sleep(500);
					activitiesData = Util.getActivitiesData();
					sleepData = Util.getSleepData();
					heartBeatsData = Util.getHeartBeatsData();
					bloodPressuresData = Util.getBloodPressuresData();

					if (Util.hasDataFileChanged()) {
						isDataFileChanged = true;
						try {
							URL url = new URL(
									"http://health-engine.herokuapp.com/");
							HttpURLConnection conn = (HttpURLConnection) url
									.openConnection();
							engineResponse = getResponse(conn);

							System.out.println(engineResponse);

							engineResponse = "{\"JOBJS\":" + engineResponse
									+ "}";
							conn.disconnect();
							connEstablished = true;

							JSONObject responseObject = new JSONObject(
									engineResponse);
							JSONArray jRecomObjects = responseObject
									.getJSONArray("JOBJS");

							// HACK TO SHOW DIFF RECOS - change to 0
							JSONObject jRecom = jRecomObjects
									.getJSONObject(Carethy.currentDataFileId);
							id = jRecom.getInt("id");
							recommendation = jRecom.getString("recommendation");
							recoUrl = jRecom.getString("url");
							severity = jRecom.getInt("severity");

						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (isDataFileChanged) {
					if (connEstablished) {
						Carethy.datasource.insertIntoTable(id, recommendation,
								recoUrl, severity);
					} else {
						Toast.makeText(getActivity(), "No Connection",
								Toast.LENGTH_SHORT).show();
					}
					isDataFileChanged = false;
				} else {
					Toast.makeText(getActivity(), "No change in Data",
							Toast.LENGTH_SHORT).show();
				}

				initView();
				mProgressDialog.dismiss();
			}

			/**
			 * Sends json request to engine
			 * 
			 * @param conn
			 *            connection with the engine
			 * @return the string response
			 * @author jaspreet
			 */
			private String getResponse(HttpURLConnection conn)
					throws IOException, UnsupportedEncodingException,
					ProtocolException {

				String httpResponse;
				String tmpJson;

				String[] sample_data_files = getActivity().getResources()
						.getStringArray(R.array.sample_data_files);
				String use_data_file = sample_data_files[Carethy.currentDataFileId];
				System.out.println(use_data_file);

				InputStream is = getActivity().getAssets().open(use_data_file);
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				tmpJson = new String(buffer, "UTF-8");

				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				DataOutputStream wr = new DataOutputStream(
						conn.getOutputStream());
				wr.writeBytes(tmpJson);
				wr.flush();
				wr.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				while ((httpResponse = in.readLine()) != null)
					engineResponse += httpResponse;
				in.close();
				conn.disconnect();

				// TODO: Get the null removed from the engine team
				// or find its significance
				if (engineResponse.startsWith("null"))
					engineResponse = engineResponse.substring(4);

				Carethy.currentDataFileId = Carethy.nextDataFileId;

				return engineResponse;
			}
		};
		task.execute((Void[]) null);
	}

	private void initView() {
		panel = (LinearLayout) rootView.findViewById(R.id.panel);

		activities = (TextView) rootView.findViewById(R.id.activity);
		activities.setText(activitiesData + "\nmins");
		((GradientDrawable) activities.getBackground()).setColor(Color
				.parseColor("#1aa083"));

		bloodPressures = (TextView) rootView.findViewById(R.id.blood_pressure);
		bloodPressures.setText(bloodPressuresData[0] + "\n"
				+ bloodPressuresData[1]);
		((GradientDrawable) bloodPressures.getBackground()).setColor(Color
				.parseColor("#fcb150"));

		heartBeats = (TextView) rootView.findViewById(R.id.heart_rate);
		heartBeats.setText(heartBeatsData + "\ncount");
		((GradientDrawable) heartBeats.getBackground()).setColor(Color
				.parseColor("#cc324b"));

		sleep = (TextView) rootView.findViewById(R.id.sleep);
		sleep.setText(df.format(sleepData) + "\nmins");
		((GradientDrawable) sleep.getBackground()).setColor(Color
				.parseColor("#2c3e50"));

		// get recommendations and fill
		mListView = (ListView) rootView.findViewById(R.id.home_listview);

		fillRecommendations();
	}

	private void fillRecommendations() {
		ArrayList<Recommendation> recomms = Carethy.datasource
				.getRecommendations(DBRecomHelper.RECOM_LIMIT);

		if (recomms.isEmpty()) {
			Toast.makeText(getActivity(), "No recommendation yet.",
					Toast.LENGTH_LONG).show();
		} else {
			RecommendationListAdapter adapter = new RecommendationListAdapter(
					getActivity(), R.layout.rowlayout_recommendation, recomms);
			mListView.setAdapter(adapter);
		}
	}
}