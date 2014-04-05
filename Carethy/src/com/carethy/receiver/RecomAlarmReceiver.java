package com.carethy.receiver;

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
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.carethy.R;
import com.carethy.activity.MainActivity;
import com.carethy.application.Carethy;
import com.carethy.model.Recommendation;
import com.carethy.notification.PopupWindow;

public class RecomAlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		Recommendation recom = getNewRecommendation(context);

		if (recom.getRecomId() <= 300) {
			new PopupWindow(context).showPopup(recom.getRecom());

		} else {
			sendNotification(context, recom.getRecom());
		}

	}

	public Recommendation getNewRecommendation(final Context context) {

		AsyncTask<Void, Integer, Recommendation> task = new AsyncTask<Void, Integer, Recommendation>() {
			Recommendation responseReco = null;
			String engineResponse = null;
			String recommendation;
			int id;
			URL url;
			private boolean connEstablished = false;

			// @Override
			protected void onPreExecute() {
				// mProgressDialog = ProgressDialog.show(getActivity(),
				// "Loading data...", "Please be patient.", true);
			}

			@Override
			protected Recommendation doInBackground(Void... arg0) {
				try {
					url = new URL("http://health-engine.herokuapp.com/");
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					engineResponse = getResponse(conn);
					conn.disconnect();
					connEstablished = true;

					JSONObject jRecom = new JSONObject(engineResponse);
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
					// int topId = Carethy.datasource.getTopRecommendationId();
					// if (topId != id) {
					responseReco = Carethy.datasource.insertIntoTable(id,
							recommendation, "http://www.google.ca");
					// }
				}
				// else {
				// responseReco = "No Connection";
				// }
				return responseReco;
			}

			@Override
			protected void onPostExecute(Recommendation result) {
			}

			private String getResponse(HttpURLConnection conn)
					throws IOException, UnsupportedEncodingException,
					ProtocolException {

				String httpResponse;
				String tmpJson;

				InputStream is = context.getAssets().open("jrequest.json");
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
					engineResponse = engineResponse.substring(5);
				return engineResponse;
			}
		};

		Recommendation result = null;
		task.execute((Void[]) null);

		try {
			result = task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public void sendNotification(Context context, String recommendation) {
		String notificationTitle = "Carethy";
		String notificationMessage = recommendation;

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(context, MainActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);

		Notification notif = new NotificationCompat.Builder(context)
				.setContentTitle(notificationTitle)
				.setContentText(notificationMessage)
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText(notificationMessage))
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pendingIntent).setTicker(notificationMessage)
				.build();

		notif.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(R.string.hello_world, notif);
	}
}