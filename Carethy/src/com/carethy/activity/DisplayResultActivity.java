package com.carethy.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.carethy.R;

public class DisplayResultActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_result);
		
		initView();
}

	private void initView(){
		//Up navigation 
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent=getIntent();
		String message=intent.getStringExtra("From");
		Toast.makeText(this,message, Toast.LENGTH_LONG).show();
	}
	
}