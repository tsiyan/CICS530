package com.carethy.fragment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.application.Carethy;

public class RecommendationsFragment extends Fragment {

	private View rootView;
	private TextView recommendation1;
	private TextView recommendation2;
	private RecommendationTask mRecommendationTask = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_recommendations,
				container, false);

		initView();

		return rootView;
	}

	private void initView() {

		mRecommendationTask = new RecommendationTask();
		mRecommendationTask.execute((Void) null);
		
		recommendation1 = (TextView) rootView
				.findViewById(R.id.recommendation1);
		recommendation2 = (TextView) rootView
				.findViewById(R.id.recommendation2);
	}

	public class RecommendationTask extends AsyncTask<Void, Void, Boolean> {

		ProgressDialog jProgressDialog = null;
		String engineResponse = null;
		String recommendation;
		int id;
		URL url;
		
		protected void onPreExecute() {
			jProgressDialog = ProgressDialog.show(getActivity(),
					"Loading data...", "Please be patient.", true);
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				url = new URL("http://health-engine.herokuapp.com/");
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();

				engineResponse = getResponse(conn);

				JSONObject jRecom = new JSONObject(engineResponse);
				id = jRecom.getInt("id");
				recommendation = jRecom.getString("recommendation");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					recommendation1.setText(recommendation);
					jProgressDialog.dismiss();

					recommendation2.setText("stored: "
							+ Carethy.datasource.insertIntoTable(id,
									recommendation).toString());
				}
			});

			return true;
		}


		/**
		 * Sends json request to engine
		 * 
		 * @param conn
		 *            connection with the engine
		 * @return the string response
		 * @author jaspreet
		 */
		private String getResponse(HttpURLConnection conn) throws IOException,
				UnsupportedEncodingException, ProtocolException {

			String httpResponse;
			String tmpJson;

			InputStream is = getActivity().getAssets().open("jrequest.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			tmpJson = new String(buffer, "UTF-8");

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
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
				engineResponse = engineResponse.substring(5);
			return engineResponse;
		}
	}
}
