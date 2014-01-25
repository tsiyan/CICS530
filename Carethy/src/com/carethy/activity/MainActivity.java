package com.carethy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.carethy.R;

public class MainActivity extends Activity {

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
	}
}
