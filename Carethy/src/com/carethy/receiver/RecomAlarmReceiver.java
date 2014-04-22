package com.carethy.receiver;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
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
import com.carethy.fragment.RecoTask;
import com.carethy.notification.PopupWindow;
import com.carethy.util.Util;

public class RecomAlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (Util.hasDataFileChanged()) {
			try {
				JSONArray responseObjects = getNewRecommendation(context);
				JSONObject recom;
				int recomLength = responseObjects.length();

				if (recomLength > 1) {
					boolean severe = false;
					for (int i = 0; i < recomLength; i++) {
						if (responseObjects.getJSONObject(i).getInt("severity") > 3) {
							severe = true;
							break;
						}
					}

					if (severe) {
						new PopupWindow(context)
								.showPopup("You have multiple high severity recommendations");

					} else {
						sendNotification(context, "You have " + recomLength
								+ " recommendations");
					}

				} else {
					recom = responseObjects.getJSONObject(0);

					if (!recom.equals(null))
						if (recom.getInt("severity") > 3) {
							new PopupWindow(context).showPopup(recom
									.getString("recommendation"));

						} else {
							sendNotification(context,
									recom.getString("recommendation"));
						}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONArray getNewRecommendation(Context context) {

		AsyncTask<Object, Integer, JSONArray> task = new RecoTask(context,
				false);
		JSONArray result = null;

		task.execute(context);

		try {
			result = task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
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