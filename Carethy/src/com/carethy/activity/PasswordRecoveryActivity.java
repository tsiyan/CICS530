package com.carethy.activity;

import com.carethy.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class PasswordRecoveryActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_recovery);

		initView();
	}

	private void initView() {
		// Up navigation
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

}
