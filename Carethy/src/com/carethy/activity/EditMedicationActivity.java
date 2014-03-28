package com.carethy.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.carethy.R;
import com.carethy.contentprovider.MedicationContentProvider;
import com.carethy.database.MedicationTable;

public class EditMedicationActivity extends Activity {
	private EditText mEditText;
	private Uri mUri;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_edit_medication);

		mEditText = (EditText) findViewById(R.id.medication_edit);
		Button confirmButton = (Button) findViewById(R.id.medication_confirm_button);
		Button deleteButton = (Button) findViewById(R.id.medication_delete_button);

		Bundle extras = getIntent().getExtras();

		mUri = (bundle == null) ? null : (Uri) bundle
				.getParcelable(MedicationContentProvider.CONTENT_ITEM_TYPE);

		if (extras != null) {
			mUri = extras
					.getParcelable(MedicationContentProvider.CONTENT_ITEM_TYPE);

			fillData(mUri);
		}

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

		deleteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (mUri != null) {
					getContentResolver().delete(mUri, null, null);
				}
				setResult(RESULT_OK);
				finish();
			}

		});

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
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
}