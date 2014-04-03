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

import com.carethy.R;
import com.carethy.activity.MainActivity;
import com.carethy.application.Carethy;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class RecomAlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// String phoneNumberReciver="6045008590";// phone number to which SMS
		// to be send
		// String message="Hi I will be there later, See You soon";// message to
		// send
		// SmsManager sms = SmsManager.getDefault();
		// sms.sendTextMessage(phoneNumberReciver, null, message, null, null);
		// Show the toast like in above screen shot

		// Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show();
		// Vibrator vibrator = (Vibrator) context
		// .getSystemService(Context.VIBRATOR_SERVICE);
		// vibrator.vibrate(1500);

		String recom = getNewRecommendation(context);

		NotificationManager mNM;
		mNM = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);
		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.ic_launcher,
				"Check Recommendations", System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, MainActivity.class), 0);
		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(context, "Carethy says:", recom,
				contentIntent);
		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		mNM.notify(R.string.hello_world, notification);

	}

	public String getNewRecommendation(final Context context) {

		AsyncTask<Void, Integer, String> task = new AsyncTask<Void, Integer, String>() {
			String responseReco = "No New Recommendation";
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
			protected String doInBackground(Void... arg0) {
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
					Carethy.datasource.insertIntoTable(id, recommendation,
							"http://www.google.ca");
					responseReco = recommendation;
					// }
				} else {
					responseReco = "No Connection";
				}
				return responseReco;
			}

			@Override
			protected void onPostExecute(String result) {
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

		String result = "Something went wrong";
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

}