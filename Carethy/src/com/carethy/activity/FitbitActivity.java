package com.carethy.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.carethy.R;

public class FitbitActivity extends Activity {
	
	private String accessPin;
	private WebView webView;
	private ConnectToFitbitTask mFitbitTask = null;
	
	// fitbit
	private String clientConsumerKey = "96c3e11bb34d4d778abb133aff0d56e6";
	private String clientSecret = "ae2e85a1829e4238874e1ef967bb422f";
	// Jawbone
	//String clientConsumerKey = "kEGuJPqOIIo";
	//String clientSecret = "caf815de70e364e28a689c8101d5e2740dcc2fe8";
	
	private OAuthConsumer consumer = new DefaultOAuthConsumer(clientConsumerKey, clientSecret);

	private OAuthProvider provider = new DefaultOAuthProvider(
				 "http://api.fitbit.com/oauth/request_token", 
				 "http://api.fitbit.com/oauth/access_token", 
				 "http://www.fitbit.com/oauth/authorize");	
			        
	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fitbit);
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AccessPinInterface(), "HtmlViewer");
        
		mFitbitTask = new ConnectToFitbitTask();
		mFitbitTask.execute((Void) null);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fitbit, menu);
		return true;
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	@SuppressLint("JavascriptInterface")
	public class ConnectToFitbitTask extends AsyncTask<Void, Void, Boolean> {
		private String authUrl = null;
		@Override
		protected Boolean doInBackground(Void... params) {

			
			try {	        
		        authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			return true;//loggedIn;
		}
		
		@Override
		protected void onPostExecute(final Boolean success) {
			
			webView.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageFinished(WebView view, String url) {
					if (url.contains("www.fitbit.com/oauth?")) {
						view.loadUrl("javascript:HtmlViewer.showHTML" +
	                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
						webView.setVisibility(View.GONE);
					}
				}
			});

			webView.loadUrl(authUrl);
			mFitbitTask = null;
		}
	}
	
	private class AccessPinInterface {
		JSONObject user;
        @JavascriptInterface
        public void showHTML(String html) {
        	int startIndex = html.toLowerCase().indexOf("<div class=\"pincode gap20\">");
        	accessPin = html.substring(startIndex + ("<div class=\"pincode gap20\">").length(), html.toLowerCase().indexOf("</div>", startIndex));
        	
        	try {
	        	provider.retrieveAccessToken(consumer, accessPin);
		        URL url = new URL("http://api.fitbit.com/1/user/-/profile.json");
		        HttpURLConnection request = (HttpURLConnection) url.openConnection();
		        consumer.sign(request);
		        request.connect();
		        BufferedReader in = new BufferedReader(new InputStreamReader(
	        		request.getInputStream()));
				String inputLine;
				String jsonFile = null;
				while ((inputLine = in.readLine()) != null) 
					jsonFile = inputLine;
				in.close();
				
				JSONObject obj;
				obj = new JSONObject(jsonFile);
				user = obj.getJSONObject("user");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						findViewById(R.id.gridLayout1).setVisibility(
								View.VISIBLE);
						EditText displayNameView = (EditText) findViewById(R.id.editText1);
						displayNameView.setText(user.optString(
								"displayName").toString());

						EditText dateOfBirthView = (EditText) findViewById(R.id.editText2);
						dateOfBirthView.setText(user.optString(
								"dateOfBirth").toString());

						EditText heightView = (EditText) findViewById(R.id.editText3);
						heightView.setText(user.optString(
								"height").toString());
					}
				});
        	}
        	catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
}
