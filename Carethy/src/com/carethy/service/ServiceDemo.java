package com.carethy.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.carethy.R;
import com.carethy.activity.MainActivity;
import com.carethy.notification.PopupWindow;

public class ServiceDemo extends Service {
	private static final String TAG = "Test";
	private PopupWindow pw;
	private String recommendation  = "In the past 7 days you had as many as 10.5 hours sleep per day on average. The recommended sleep is 7-8 hours a day."; 

	@Override
	public void onCreate() {
		Log.i(TAG, "Service onCreate--->");
		super.onCreate();
		pw = new PopupWindow(getApplicationContext());
		try {
			Thread.sleep(5000);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "Service onStart--->");
		// super.onStart(intent, startId);
		pw.showPopup(this.recommendation);
		this.sendNotification(this.recommendation);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "Service onDestroy--->");
		// super.onDestroy();
		pw.removePopup();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public void sendNotification(String recommendation) {
		String notificationTitle = "Carethy";
		String notificationMessage = recommendation;

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Intent intent = new Intent(getBaseContext(), MainActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		
//		Intent dismissIntent = new Intent(this, MainActivity.class);
//		dismissIntent.setAction("com.carethy.ACTION_DISMISS");
//		PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);
//
//		Intent snoozeIntent = new Intent(this, MainActivity.class);
//		snoozeIntent.setAction("com.carethy.ACTION_SNOOZE");
//		PendingIntent piSnooze = PendingIntent.getService(this, 0, snoozeIntent, 0);

		Notification notif = new NotificationCompat.Builder(this)
				.setContentTitle(notificationTitle)
				.setContentText(notificationMessage)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationMessage))
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pendingIntent).setTicker(notificationMessage)
//				.addAction(R.drawable.ic_action_discard, "Dismiss", piDismiss)
//				.addAction(R.drawable.ic_recommendations, "Snooze", piSnooze)
				.build();

		notif.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, notif);
	}

}