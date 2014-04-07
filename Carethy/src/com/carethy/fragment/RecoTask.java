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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.carethy.R;
import com.carethy.application.Carethy;
import com.carethy.model.Recommendation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class RecoTask extends AsyncTask<Object, Integer, Recommendation> {

	Recommendation responseReco = null;
	String engineResponse = null;
	String recommendation;
	int id;
	URL url;
	private boolean connEstablished = false;
	Context context;
	static ProgressDialog mProgressDialog;
	private boolean isHomeFragment;

	public RecoTask(Context context, Boolean isHomeFragment) {
		this.context = context;
		this.isHomeFragment = isHomeFragment;
	}

	protected void onPreExecute() {
		System.out.println("I am here");
		if (isHomeFragment) {
			mProgressDialog = ProgressDialog.show(context,
					"Fetching Recommendations...", "Please be patient.", true);
		}
	}

	@Override
	protected Recommendation doInBackground(Object... params) {

		try {
			url = new URL("http://health-engine.herokuapp.com/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			engineResponse = getResponse(conn);

			System.out.println(engineResponse);

			engineResponse = "{\"JOBJS\":" + engineResponse + "}";
			conn.disconnect();
			connEstablished = true;

			JSONObject responseObject = new JSONObject(engineResponse);
			JSONArray jRecomObjects = responseObject.getJSONArray("JOBJS");
			JSONObject jRecom = jRecomObjects.getJSONObject(0);
			id = jRecom.getInt("id");
			recommendation = jRecom.getString("recommendation");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Some error");
			e.printStackTrace();
		}

		if (connEstablished) {
			responseReco = Carethy.datasource.insertIntoTable(id,
					recommendation, "http://www.google.ca");
		}
		return responseReco;
	}

	private String getResponse(HttpURLConnection conn) throws IOException,
			UnsupportedEncodingException, ProtocolException {

		String httpResponse;
		String tmpJson;

		String[] sample_data_files = context.getResources().getStringArray(
				R.array.sample_data_files);
		String use_data_file = sample_data_files[Carethy.currentDataFileId];
		System.out.println(use_data_file);

		InputStream is = context.getAssets().open(use_data_file);
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
			engineResponse = engineResponse.substring(4);

		Carethy.currentDataFileId = Carethy.nextDataFileId;

		return engineResponse;
	}

	protected void onPostExecute() {
		if (isHomeFragment) {
			mProgressDialog.dismiss();
			context.notify();
		}
	}

}
