package com.carethy.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import com.carethy.R;

public class CreateMedicationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_medication);

		initView();
	}

	private void initView() {
		// Up navigation
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
	}
}
