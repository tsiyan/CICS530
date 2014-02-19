package com.carethy.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import com.carethy.R;

public class TermsConditionsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_conditions);

		initView();
	}

	private void initView() {
		// Up navigation
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	
}
