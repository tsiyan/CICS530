package com.example.restsample;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

//import com.example.sampleappy.MainActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	private Button mRegisterButton;
	private Button mLoginButton;
	private Button mAddRecordButton;
	private Button mGetRecordButton;
	private EditText mEditText;
	private String session_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
        mEditText = (EditText)findViewById(R.id.my_edit);
		mRegisterButton = (Button) findViewById(R.id.my_button);
		mLoginButton = (Button) findViewById(R.id.my_button2);

		mAddRecordButton = (Button) findViewById(R.id.my_button3);
		mGetRecordButton = (Button) findViewById(R.id.my_button4);
		
		mRegisterButton.setOnClickListener(getRegisterClickListener());
		mLoginButton.setOnClickListener(getLoginClickListener());
		mAddRecordButton.setOnClickListener(getAddRecordClickListener());
		mGetRecordButton.setOnClickListener(getGetRecordClickListener());
	}

	private OnClickListener getRegisterClickListener()
	{
		return new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				mRegisterButton.setClickable(false);
				new LongRunningGetIO().execute();
			}
			
			class LongRunningGetIO extends AsyncTask <Void, Void, String> 
			{
				
				protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException 
				{
			       InputStream in = entity.getContent();
			         StringBuffer out = new StringBuffer();
			         int n = 1;
			         while (n>0) {
			             byte[] b = new byte[4096];
			             n =  in.read(b);
			             if (n>0) out.append(new String(b, 0, n));
			         }
			         return out.toString();
			    }
				
				@Override
				protected String doInBackground(Void... params) {
					 HttpClient httpClient = new DefaultHttpClient();
					 HttpContext localContext = new BasicHttpContext();
		             boolean result = false;
		             String message;

		             HttpPost httpPost = new HttpPost("http://ec2-54-184-199-222.us-west-2.compute.amazonaws.com:80/rest/user/register?app_name=my-app1");
		             JSONObject object = new JSONObject();
		             try {

		                 object.put("email", "siyanfaisal@yahoo.com");
		                 object.put("new_password", "qwerty");
		                 object.put("last_name", "shash");
		                 object.put("first_name", "siyan");
		             } catch (Exception ex) {

		             }

		             try {
		             message = object.toString();
		             Log.d("message:>", "" + message);

		             httpPost.setEntity(new StringEntity(message, "UTF8"));
		             httpPost.setHeader("Content-type", "application/json");
		                 HttpResponse resp = httpClient.execute(httpPost, localContext);
		                 if (resp != null) {
		                	 Log.d("message:>", "Status Code: " + resp.getStatusLine().getStatusCode());
		                     if (resp.getStatusLine().getStatusCode() == 200)
		                         result = true;
		                 }
		             } catch (Exception e) {
		                 e.printStackTrace();

		             }
		             if(result)
		             {
		            	 return "Registered Successfully!";
		             }
		             else
		             {
		            	 return "Register Failed!!!!!!!";
		             }
				}	
				
				protected void onPostExecute(String results) {
					if (results!=null) {
						mEditText.setText(results);
					}
					mRegisterButton.setClickable(true);
				}
		    }
		};
	}
	
	private OnClickListener getLoginClickListener()
	{
		return new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				mLoginButton.setClickable(false);
				new LongRunningGetIO().execute();
			}
			
			class LongRunningGetIO extends AsyncTask <Void, Void, String> 
			{
				
				protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException 
				{
			       InputStream in = entity.getContent();
			         StringBuffer out = new StringBuffer();
			         int n = 1;
			         while (n>0) {
			             byte[] b = new byte[4096];
			             n =  in.read(b);
			             if (n>0) out.append(new String(b, 0, n));
			         }
			         return out.toString();
			    }
				
				@Override
				protected String doInBackground(Void... params) {
					 HttpClient httpClient = new DefaultHttpClient();
					 HttpContext localContext = new BasicHttpContext();
		             boolean result = false;
		             String message;
		             String text = "";

		             HttpPost httpPost = new HttpPost("http://ec2-54-184-199-222.us-west-2.compute.amazonaws.com:80/rest/user/session?app_name=my-app1");
		             JSONObject object = new JSONObject();
		             try {

		                 object.put("email", "siyanfaisal@yahoo.com");
		                 object.put("password", "qwerty");
		             } catch (Exception ex) {

		             }

		             try {
		             message = object.toString();
		             Log.d("message:>", "" + message);

		             httpPost.setEntity(new StringEntity(message, "UTF8"));
		             httpPost.setHeader("Content-type", "application/json");
		                 HttpResponse resp = httpClient.execute(httpPost, localContext);
		                 HttpEntity entity = resp.getEntity();

		                 text = getASCIIContentFromEntity(entity);
		                 JSONObject json = new JSONObject(text);
		                 session_id = json.getString("session_id");
		                 Log.d("message:>", "session_id: " + session_id);
		                 if (resp != null) {
		                	 Log.d("message:>", "Status Code: " + resp.getStatusLine().getStatusCode());
		                     if (resp.getStatusLine().getStatusCode() == 200)
		                         result = true;
		                 }
		             } catch (Exception e) {
		                 e.printStackTrace();

		             }
		             if(result)
		             {
		            	 return "Login Success! ==> " + text;
		             }
		             else
		             {
		            	 return "Login Failed!!!!!!! ==> " + text;
		             }
				}	
				
				protected void onPostExecute(String results) {
					if (results!=null) {
						mEditText.setText(results);
					}
					mLoginButton.setClickable(true);
				}
		    }
		};
	}
	private OnClickListener getAddRecordClickListener()
	{
		return new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				mAddRecordButton.setClickable(false);
				new LongRunningGetIO().execute();
			}
			
			class LongRunningGetIO extends AsyncTask <Void, Void, String> 
			{
				
				protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException 
				{
			       InputStream in = entity.getContent();
			         StringBuffer out = new StringBuffer();
			         int n = 1;
			         while (n>0) {
			             byte[] b = new byte[4096];
			             n =  in.read(b);
			             if (n>0) out.append(new String(b, 0, n));
			         }
			         return out.toString();
			    }
				
				@Override
				protected String doInBackground(Void... params) {
					 HttpClient httpClient = new DefaultHttpClient();
					 HttpContext localContext = new BasicHttpContext();
					 
		             boolean result = false;
		             String message;
		             String text = "";

		             HttpPost httpPost = new HttpPost("http://ec2-54-184-199-222.us-west-2.compute.amazonaws.com:80/rest/db/todo/running?id_field=name&fields=id%2C%20name%2C%20complete&app_name=my-app1");
		             httpPost.setHeader("X-DreamFactory-Session-Token", session_id);
		             JSONObject object = new JSONObject();
		             try {

		                 object.put("name", "running");
		                 object.put("complete", "true");
		             } catch (Exception ex) {

		             }

		             try {
		             message = object.toString();
		             Log.d("message:>", "" + message);

		             httpPost.setEntity(new StringEntity(message, "UTF8"));
		             httpPost.setHeader("Content-type", "application/json");
		                 HttpResponse resp = httpClient.execute(httpPost, localContext);
		                 HttpEntity entity = resp.getEntity();
		                 text = getASCIIContentFromEntity(entity);
		                 
		                 if (resp != null) {
		                	 Log.d("message:>", "Status Code: " + resp.getStatusLine().getStatusCode());
		                     if (resp.getStatusLine().getStatusCode() == 200)
		                         result = true;
		                 }
		             } catch (Exception e) {
		                 e.printStackTrace();

		             }
		             if(result)
		             {
		            	 return "Add Record Success! ==> " + text;
		             }
		             else
		             {
		            	 return "Add Record Failed!!!!!!! ==> " + text;
		             }
				}	
				
				protected void onPostExecute(String results) {
					if (results!=null) {
						mEditText.setText(results);
					}
					mAddRecordButton.setClickable(true);
				}
		    }
		};
	}
	private OnClickListener getGetRecordClickListener()
	{
		return new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				mGetRecordButton.setClickable(false);
				new LongRunningGetIO().execute();
			}
			
			class LongRunningGetIO extends AsyncTask <Void, Void, String> 
			{
				
				protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException 
				{
			       InputStream in = entity.getContent();
			         StringBuffer out = new StringBuffer();
			         int n = 1;
			         while (n>0) {
			             byte[] b = new byte[4096];
			             n =  in.read(b);
			             if (n>0) out.append(new String(b, 0, n));
			         }
			         return out.toString();
			    }
				
				@Override
				protected String doInBackground(Void... params) 
				{
					 HttpClient httpClient = new DefaultHttpClient();
					 HttpContext localContext = new BasicHttpContext();
		             HttpGet httpGet = new HttpGet("http://ec2-54-184-199-222.us-west-2.compute.amazonaws.com:80/rest/db/todo/running?id_field=name&fields=id%2C%20name%2C%20complete&app_name=my-app1");
		             httpGet.setHeader("X-DreamFactory-Session-Token", session_id);
		             boolean result = false;
		             String text = null;
		             try {
		                   HttpResponse response = httpClient.execute(httpGet, localContext);
		                   HttpEntity entity = response.getEntity();
		                   text = getASCIIContentFromEntity(entity);
			                 if (response != null)
			                 {
			                	 Log.d("message:>", "Status Code: " + response.getStatusLine().getStatusCode());
			                     if (response.getStatusLine().getStatusCode() == 200)
			                         result = true;
			                 }
		             } catch (Exception e) {
		            	 return e.getLocalizedMessage();
		             }
		             Log.d("message:>", "response===>" + text);
		             if(result)
		             {
		            	 return "Get Record Success! ==> " + text;
		             }
		             else
		             {
		            	 return "Get Record Failed!!!!!!! ==> " + text;
		             }
				}	
				
				protected void onPostExecute(String results) {
					if (results!=null) {
						mEditText.setText(results);
					}
					mGetRecordButton.setClickable(true);
				}
		    }
		};
	}

}