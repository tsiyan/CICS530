package com.carethy.receiver;

import java.util.concurrent.ExecutionException;

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
import com.carethy.model.Recommendation;
import com.carethy.notification.PopupWindow;
import com.carethy.util.Util;

public class RecomAlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("nothings changed");
		if (Util.hasDataFileChanged()) {
			System.out.println("somethings changed");
			Recommendation recom = getNewRecommendation(context);
			System.out.println("Reco created");

			if (!recom.equals(null))
				if (recom.getRecomId() <= 300) {
					System.out.println("Reco created <= 300");
					new PopupWindow(context).showPopup(recom.getRecom());

				} else {
					System.out.println("Reco created > 300");
					sendNotification(context, recom.getRecom());
				}
		}
	}

	public Recommendation getNewRecommendation(Context context) {

		AsyncTask<Object, Integer, Recommendation> task = new RecoTask(context,false);
		Recommendation result = null;
		
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