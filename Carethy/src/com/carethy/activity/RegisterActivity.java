package com.carethy.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.carethy.R;

public class RegisterActivity extends Activity {

	private View focusView = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private String mPasswordRepeat;
	private String mFirstName;
	private String mLastName;
	
	private String mHeight;
	private String mWeight;
	private String mBirthdate;
	
	private String mRadioSex;

	// UI references.
	private EditText birthdayEditText;
	private ImageButton selectBirthdayImageButton;
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mPasswordRepeatView;
	private EditText mFirstNameView;
	private EditText mLastNameView;
	private TextView mTermsTextView;
	private EditText mHeightView;
	private EditText mWeightView;	
	private EditText mBirthdateView;	
	private RadioGroup mRadioSexView;
	private RadioButton mRadioSexSelectedView;

	private Button regButton;
	
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

		mFirstNameView = (EditText) findViewById(R.id.first_name);
		mLastNameView = (EditText) findViewById(R.id.last_name);
		mHeightView = (EditText) findViewById(R.id.height);
		mWeightView = (EditText) findViewById(R.id.weight);
		mBirthdateView = (EditText) findViewById(R.id.birthday);
		mRadioSexView = (RadioGroup) findViewById(R.id.radioSex);
		int selectedSex = mRadioSexView.getCheckedRadioButtonId();
		mRadioSexSelectedView = (RadioButton) findViewById(selectedSex);
		
		regButton = (Button) findViewById(R.id.register_button2);
		regButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//TODO make it visible that button is disabled
						regButton.setEnabled(false);
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

	public void register() {
		mRegisterTask = new RegisterTask();
		mRegisterTask.execute((Void) null);
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
	
	private boolean inputError() {
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mPasswordRepeatView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mPasswordRepeat = mPasswordRepeatView.getText().toString();
		mFirstName = mFirstNameView.getText().toString();
		mLastName = mLastNameView.getText().toString();
		mHeight = mHeightView.getText().toString();
		mWeight = mWeightView.getText().toString();
		mBirthdate = mBirthdateView.getText().toString();
		mRadioSex = mRadioSexSelectedView.getText().toString();
		
		boolean cancel = false;

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

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@") || !mEmail.contains(".")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mFirstName)) {
			mFirstNameView.setError(getString(R.string.error_field_required));
			focusView = mFirstNameView;
			cancel = true;
		}
		if (TextUtils.isEmpty(mLastName)) {
			mLastNameView.setError(getString(R.string.error_field_required));
			focusView = mLastNameView;
			cancel = true;
		}
		if (TextUtils.isEmpty(mHeight)) {
			mHeightView.setError(getString(R.string.error_field_required));
			focusView = mHeightView;
			cancel = true;
		}
		else {
			try {
				if (Integer.valueOf(mHeight) < 50 || Integer.valueOf(mHeight) > 280) {
					mHeightView.setError(getString(R.string.invalidValue));
					focusView = mHeightView;
					cancel = true;
				}
			} catch(NumberFormatException e) {
				mHeightView.setError(getString(R.string.invalidValue));
				focusView = mHeightView;
				cancel = true;
			}
		}
		if (TextUtils.isEmpty(mWeight)) {
			mWeightView.setError(getString(R.string.error_field_required));
			focusView = mWeightView;
			cancel = true;
		}
		else {
			try {
				if (Integer.valueOf(mWeight) < 5 || Integer.valueOf(mWeight) > 650) {
					mWeightView.setError(getString(R.string.invalidValue));
					focusView = mWeightView;
					cancel = true;
				}
			} catch(NumberFormatException e) {
				mWeightView.setError(getString(R.string.invalidValue));
				focusView = mWeightView;
				cancel = true;
			}
		}
		if (TextUtils.isEmpty(mBirthdate)) {
			mBirthdateView.setError(getString(R.string.error_field_required));
			focusView = mBirthdateView;
			cancel = true;
		}
		else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			sdf.setLenient(true);
			try {
				sdf.parse(mBirthdate);
			} catch (ParseException e) {
				mBirthdateView.setError(getString(R.string.invalidDate));
				focusView = mBirthdateView;
				cancel = true;
			}
		}
		return cancel;
	}
	
	//new
	public class RegisterTask extends AsyncTask<Void, Void, Boolean> {
		private boolean result = false;
		@Override
		protected Boolean doInBackground(Void... params) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (inputError()) {
						focusView.requestFocus();
					} else {
						result = true;
					}
				}
			});
			if (result) {
				result = false;
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();

				HttpPost httpPost = new HttpPost(
						"https://dsp-carethy.cloud.dreamfactory.com/rest/user/register?app_name=carethy");
				JSONObject userData = new JSONObject();
				JSONObject userInfo = new JSONObject();
				try {

					userData.put("email", mEmail);
					userData.put("new_password", mPassword);
					userData.put("last_name", mLastName);
					userData.put("first_name", mFirstName);
					
					userInfo.put("email", mEmail);
					userInfo.put("weight", mWeight);
					userInfo.put("height", mHeight);
					userInfo.put("birthdate", mBirthdate);
					userInfo.put("sex", mRadioSex);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				try {
					httpPost.setEntity(new StringEntity(userData.toString(), "UTF8"));
					httpPost.setHeader("Content-type", "application/json");
					// register the new user
					HttpResponse resp = httpClient.execute(httpPost, localContext);
					if (resp != null && resp.getStatusLine().getStatusCode() == 200) {
						// log the user in
						resp.getEntity().consumeContent();
						httpPost = new HttpPost("https://dsp-carethy.cloud.dreamfactory.com/rest/user/session?app_name=carethy");
						userData = new JSONObject();
						userData.put("email", mEmail);
						userData.put("password", mPassword);
						httpPost.setEntity(new StringEntity(userData.toString(), "UTF8"));
						httpPost.setHeader("Content-type", "application/json");
						resp = httpClient.execute(httpPost, localContext);
						if (resp != null && resp.getStatusLine().getStatusCode() == 200) {
							HttpEntity entity = resp.getEntity();							
							JSONObject responseJSON = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
							MainActivity.setDREAMFACTORYTOKEN(responseJSON.getString("session_id"));
						}
						
						// enter the new users data into the db
						httpPost = new HttpPost(
								"https://dsp-carethy.cloud.dreamfactory.com/rest/mongohq/user?app_name=carethy");
						httpPost.setEntity(new StringEntity(userInfo.toString(), "UTF8"));
						httpPost.setHeader("Content-type", "application/json");
						httpPost.setHeader("X-DreamFactory-Session-Token", MainActivity.getDREAMFACTORYTOKEN());
						resp = httpClient.execute(httpPost, localContext);
						if (resp != null && resp.getStatusLine().getStatusCode() == 200) {
							result = true;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (result) {
					MainActivity.setLoggedIn(true);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			regButton.setEnabled(true);
			if (success) {
				Toast.makeText(RegisterActivity.this,
						"The user has successfully been registered.",
						Toast.LENGTH_LONG).show();
				finish();
			} else {
				Toast.makeText(RegisterActivity.this,
						"An error occurred during the registration. Please check your input data.",
						Toast.LENGTH_LONG).show();
			}
		}
	}

}
