package com.carethy.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.carethy.R;

public class MainActivity extends Activity {
	private Button mNotifyButton; 
	private Button mDialogButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}
	
	private void initView(){
		final Spinner spinner = (Spinner)findViewById(R.id.tracked_item_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.tracked_item_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		
		Button button=(Button) findViewById(R.id.display_button);
		button.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getApplicationContext(),DisplayResultActivity.class);
				intent.putExtra("From",spinner.getSelectedItem().toString());
				startActivity(intent);
			}
		});
		
		
		mNotifyButton = (Button) findViewById(R.id.button1);
		mDialogButton = (Button) findViewById(R.id.button2);
		mDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MainActivity.this);
				alertDialogBuilder.setTitle("Warning");
				alertDialogBuilder
						.setMessage("Your blood pressure is high, please take hypotensor!!!")
						.setCancelable(false)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
										// finish();
									}
								});
				// .setNegativeButton("Cancel",
				// new DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog,
				// int id) {
				// dialog.cancel();
				// }
				// });
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});
	}		
	
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void sendNotification(View view) {
		String notificationTitle = "Carethy";
		String notificationMessage = "You need to get more sleep.";

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Intent intent = new Intent(MainActivity.this, DisplayResultActivity.class);
//		EditText editText = (EditText) findViewById(R.id.edit_message);
//		String message = editText.getText().toString();
//		intent.putExtra(EXTRA_MESSAGE, message);
//		intent.putExtra(NOTIFICATION_TITLE, notificationTitle);
//		intent.putExtra(NOTIFICATION_MESSAGE, notificationMessage);

		PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
				intent, 0);

		Notification notif = new Notification.Builder(this)
				.setContentTitle(notificationTitle)
				.setContentText(notificationMessage)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pendingIntent).setTicker(notificationMessage)
				.build();

		notif.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, notif);
	}
}
