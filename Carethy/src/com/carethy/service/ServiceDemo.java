package com.carethy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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

}