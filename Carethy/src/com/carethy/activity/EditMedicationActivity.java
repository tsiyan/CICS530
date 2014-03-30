package com.carethy.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.carethy.R;
import com.carethy.contentprovider.MedicationContentProvider;
import com.carethy.database.MedicationTable;

public class EditMedicationActivity extends Activity {
	private Uri mUri;

	private EditText mEditText;
	private Button confirmButton;
	private Button deleteButton;
	private Button timePickerButton;
	private TextView tvDisplayTime;
	private Button datePickerButton;
	private TextView tvDisplayDate;

	private Calendar c = Calendar.getInstance();;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_edit_medication);

		initView(bundle);

	}

	private void initView(Bundle bundle) {
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		mEditText = (EditText) findViewById(R.id.medication_edit);
		mUri = (bundle == null) ? null : (Uri) bundle
				.getParcelable(MedicationContentProvider.CONTENT_ITEM_TYPE);

		if (extras != null) {
			mUri = extras
					.getParcelable(MedicationContentProvider.CONTENT_ITEM_TYPE);

			fillData(mUri);
		}

		confirmButton = (Button) findViewById(R.id.medication_confirm_button);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (TextUtils.isEmpty(mEditText.getText().toString())) {
					Toast.makeText(EditMedicationActivity.this,
							"Please enter the medicine", Toast.LENGTH_SHORT)
							.show();
				} else {
					setResult(RESULT_OK);
					finish();
				}
			}

		});

		deleteButton = (Button) findViewById(R.id.medication_delete_button);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (mUri != null) {
					getContentResolver().delete(mUri, null, null);
				}
				setResult(RESULT_OK);
				finish();
			}

		});

		tvDisplayTime = (TextView) findViewById(R.id.tvTime);

		timePickerButton = (Button) findViewById(R.id.medication_timepicker_button);
		timePickerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				c.setTimeInMillis(System.currentTimeMillis());
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				new TimePickerDialog(EditMedicationActivity.this,
						new OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// set current time into textview
								tvDisplayTime.setText(new StringBuilder()
										.append(pad(hourOfDay)).append(":")
										.append(pad(minute)));
							}
						}, hour, minute, true).show();
			}
		});

		tvDisplayDate = (TextView) findViewById(R.id.tvDate);

		datePickerButton = (Button) findViewById(R.id.medication_datepicker_button);
		datePickerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int mYear = c.get(Calendar.YEAR);
				int mMonth = c.get(Calendar.MONTH);
				int mDay = c.get(Calendar.DAY_OF_MONTH);

				new DatePickerDialog(EditMedicationActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								tvDisplayDate.setText(dayOfMonth + "-"
										+ (monthOfYear + 1) + "-" + year);

							}
						}, mYear, mMonth, mDay).show();

			}
		});

	}

	private void fillData(Uri uri) {
		String[] projection = { MedicationTable.COLUMN_MEDICINE };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();

			mEditText.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(MedicationTable.COLUMN_MEDICINE)));

			cursor.close();
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(MedicationContentProvider.CONTENT_ITEM_TYPE,
				mUri);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	private void saveState() {
		String medicine = mEditText.getText().toString();

		if (medicine.length() == 0) {
			return;
		}

		ContentValues values = new ContentValues();
		values.put(MedicationTable.COLUMN_MEDICINE, medicine);

		if (mUri == null) {
			mUri = getContentResolver().insert(
					MedicationContentProvider.CONTENT_URI, values);
		} else {
			getContentResolver().update(mUri, values, null, null);
		}
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
}