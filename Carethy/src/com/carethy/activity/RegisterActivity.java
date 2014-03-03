package com.carethy.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.carethy.R;
import com.carethy.activity.LoginActivity.UserLoginTask;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class RegisterActivity extends Activity {

	private View focusView = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private String mPasswordRepeat;

	// UI references.
	private EditText birthdayEditText;
	private ImageButton selectBirthdayImageButton;
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mPasswordRepeatView;
	private TextView mTermsTextView;

	private int year;
	private int month;
	private int day;
	static final int DATE_DIALOG_ID = 999;
	private RegisterTask mRegisterTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		// Show the Up button in the action bar.
		setupActionBar();

		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordRepeatView = (EditText) findViewById(R.id.password_repeat);

		findViewById(R.id.register_button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						register();
					}
				});

		mTermsTextView = (TextView) findViewById(R.id.register_terms);
		mTermsTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplication(),
						TermsConditionsActivity.class);
				startActivity(intent);
			}

		});

		addDatePickerListener();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void register() {
		mRegisterTask = new RegisterTask();
		mRegisterTask.execute((Void) null);
	}
	
	private boolean inputError() {
		boolean cancel = false;

		//TODO this has to be in a runOnUiThread(new Runnable() {... or whatever
		/*
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mPasswordRepeatView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mPasswordRepeat = mPasswordRepeatView.getText().toString();

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		} else if (TextUtils.isEmpty(mPasswordRepeat)) {
			mPasswordRepeatView
					.setError(getString(R.string.error_field_required));
			focusView = mPasswordRepeatView;
			cancel = true;
			// Check if passwords match
		} else if (!mPassword.equals(mPasswordRepeat)) {
			mPasswordRepeatView
					.setError(getString(R.string.error_no_match_password));
			focusView = mPasswordRepeatView;
			cancel = true;
		}
		*/
		//TODO change this to reasonable validations
		/*
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		*/
		return cancel;
	}

	private void addDatePickerListener() {
		birthdayEditText = (EditText) findViewById(R.id.birthday);
		selectBirthdayImageButton = (ImageButton) findViewById(R.id.birthday_image_button);
		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
 
		selectBirthdayImageButton.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);

			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			birthdayEditText.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));
		}
	};

	public class RegisterTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			if (inputError()) {
				focusView.requestFocus();
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						MongoClient mongoClient;
						try {
							mongoClient = new MongoClient("troup.mongohq.com",
									10037);

							final DB db = mongoClient.getDB("Carethy");
							boolean auth = db.authenticate("carethy",
									"carethy".toCharArray());
							if (auth) {

								DBCollection coll = db.getCollection("user");
								BasicDBObject doc = new BasicDBObject(
										"username", mEmail).append("password",
										mPassword);
								coll.insert(doc);
								Toast.makeText(RegisterActivity.this,
										"User successfully created",
										Toast.LENGTH_LONG).show();
							}
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					}
				});
				finish();
			}
			return true;
		}
	}
}
