package com.carethy.activity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.carethy.R;

public class PasswordRecoveryActivity extends Activity {
	private String mEmail;
	private EditText mEmailView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_recovery);

		mEmailView = (EditText) findViewById(R.id.email_recovery);
		mEmailView.setText(mEmail);
		
		initView();
		
		findViewById(R.id.send_password_button).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				PasswordTask passwordTask = new PasswordTask();
				passwordTask.execute((Void) null);
			}
			
			
		});
	}

	private void initView() {
		// Up navigation
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public class PasswordTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			mEmail = mEmailView.getText().toString();
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
	
			HttpPost httpPost = new HttpPost("https://dsp-carethy.cloud.dreamfactory.com/rest/user/password?reset=true&app_name=carethy");
			JSONObject userData = new JSONObject();
			try {
				userData.put("email", mEmail);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	
			try {
				httpPost.setEntity(new StringEntity(userData.toString(), "UTF8"));
				httpPost.setHeader("Content-type", "application/json");
				HttpResponse resp = httpClient.execute(httpPost, localContext);
				if (resp == null || resp.getStatusLine().getStatusCode() != 200) {
					return false;
				}
				else {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				Toast.makeText(PasswordRecoveryActivity.this,
						"An email with password reset instructions has been sent to you.",
						Toast.LENGTH_LONG).show();
				finish();
			} else {
				Toast.makeText(PasswordRecoveryActivity.this,
						"The supplied email was not found in the system.",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
