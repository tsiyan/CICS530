package com.carethy.notification;


import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.WindowManager;

public class PopupWindow {
	private Context context;
	private WindowManager wm;
	private PopupWindowLayout pv;
	KeyguardManager km;
	KeyguardLock kl;

	public PopupWindow(Context context) {
		this.context = context;
	}

	public void showPopup(String str) {
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
	            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
	            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
	            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
	            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;

		km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		kl = km.newKeyguardLock("MyKeyguardLock");
		kl.disableKeyguard();		

		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, getClass().getName());
		
		//Acquire the lock
		wl.acquire();	

		pv = new PopupWindowLayout(context, str, wm, kl);
		
		wm.addView(pv, params); 
		
		//vibrate 500 ms
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);
	
		//Release the lock
		wl.release();
	}

	public void removePopup() 
	{
		if (wm != null && pv != null) 
		{
			wm.removeView(pv);
		}
	}
}
