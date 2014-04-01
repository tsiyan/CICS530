package com.carethy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class RecomAlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
//		String phoneNumberReciver="6045008590";// phone number to which SMS to be send
//        String message="Hi I will be there later, See You soon";// message to send
//        SmsManager sms = SmsManager.getDefault(); 
//        sms.sendTextMessage(phoneNumberReciver, null, message, null, null);
// 		Show the toast  like in above screen shot
        Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show();
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(1500);
	}
	
}