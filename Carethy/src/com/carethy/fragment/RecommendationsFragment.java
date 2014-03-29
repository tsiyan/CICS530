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
import android.widget.EditText;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.activity.RegisterActivity.RegisterTask;

public class RecommendationsFragment extends Fragment {

	private View rootView;
	private TextView recommendation1;
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
	}

	public class RecommendationTask extends AsyncTask<Void, Void, Boolean> {
		String recom = null;
		String recommendation;
		int id;
		ProgressDialog jProgressDialog = null;
		
		protected void onPreExecute() {
			jProgressDialog = ProgressDialog.show(getActivity(),
					"Loading data...", "Please be patient.", true);
		}
		
		//TODO : Send notification message according to id value
		protected void onPostExecute(){
			
		}

		
		@Override
		protected Boolean doInBackground(Void... params) {
			
			try {

				URL url;

				url = new URL("http://health-engine.herokuapp.com/");
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();

				recom = getResponse(conn);

				JSONObject jRecom = new JSONObject(recom);
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
					recommendation1 = (TextView) rootView
							.findViewById(R.id.recommendation1);
					recommendation1.setText(recommendation);
					jProgressDialog.dismiss();
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
		 * @throws IOException
		 * @throws UnsupportedEncodingException
		 * @throws ProtocolException
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
				recom += httpResponse;
			in.close();

			// TODO: Get the null removed from the engine team
			// or find its significance
			if (recom.startsWith("null"))
				recom = recom.substring(5);
			return recom;
		}
	}
}
