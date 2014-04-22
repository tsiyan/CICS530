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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.carethy.R;
import com.carethy.activity.MainActivity;
import com.carethy.application.Carethy;
import com.carethy.model.Recommendation;
import com.carethy.util.Util;

public class RecoTask extends AsyncTask<Object, Integer, JSONArray> {

	Recommendation responseReco = null;
	String engineResponse = null;
	private boolean connEstablished = false;
	Context context;
	static ProgressDialog mProgressDialog;
	private boolean isHomeFragment;

	JSONArray jRecomObjects;

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
	protected JSONArray doInBackground(Object... params) {

		try {
			URL url = new URL("http://health-engine.herokuapp.com/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			engineResponse = getResponse(conn);

			System.out.println(engineResponse);

			engineResponse = "{\"JOBJS\":" + engineResponse + "}";
			conn.disconnect();
			connEstablished = true;

			JSONObject responseObject = new JSONObject(engineResponse);
			jRecomObjects = responseObject.getJSONArray("JOBJS");
			// JSONObject jRecom = jRecomObjects.getJSONObject(0);
			
			// SAVE TO CLOUD // SAVE MULTIPLE RECOS
			saveToCloud();

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
			String recommendation = null;
			int id = 0;
			String recoUrl = null;
			int severity = 0;

			for (int i = 0; i < jRecomObjects.length(); i++) {
				JSONObject recom = null;
				try {
					recom = jRecomObjects.getJSONObject(i);

					id = recom.getInt("id");
					recommendation = recom.getString("recommendation");
					recoUrl = recom.getString("url");
					severity = recom.getInt("severity");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				responseReco = Carethy.datasource.insertIntoTable(id,
						recommendation, recoUrl, severity);
			}
		}
		return jRecomObjects;
	}

	private void saveToCloud() throws JSONException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();

		for (int i = 0; i < jRecomObjects.length(); i++) {
			JSONObject recom = jRecomObjects.getJSONObject(i);
			JSONObject recoData = new JSONObject();

			try {
				recoData.put("recom_id", recom.getInt("id"));
				recoData.put("recom", recom.getString("recommendation"));
				recoData.put("url", recom.getString("url"));
				recoData.put("severity", recom.getInt("severity"));
				recoData.put("savedate", Util.getTimestamp());

				// enter the reco data into the db
				HttpPost httpPost = new HttpPost(
						"https://dsp-carethy.cloud.dreamfactory.com/rest/mongohq/recommendations?app_name=carethy");
				httpPost.setEntity(new StringEntity(recoData.toString(), "UTF8"));
				httpPost.setHeader("Content-type", "application/json");
				httpPost.setHeader("X-DreamFactory-Session-Token",
						MainActivity.getDREAMFACTORYTOKEN());
				HttpResponse resp = httpClient.execute(httpPost, localContext);

				if (resp != null && resp.getStatusLine().getStatusCode() == 200) {
					resp.getEntity().consumeContent();
				} else {
					throw new Exception(
							"Something went wrong while saving to cloud");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
