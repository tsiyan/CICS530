package com.carethy.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.carethy.R;
import com.carethy.activity.input.InputActivity;


public class MainActivity extends Activity implements ActionBar.OnNavigationListener{

	private boolean loggedIn = false;
	private MenuItem refreshMenuItem;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		if (!loggedIn) {
			Intent intent = new Intent(this, LoginActivity.class);
		    startActivity(intent);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	  @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Take appropriate action for each action item click
	        switch (item.getItemId()) {
	        case R.id.action_refresh:
	        	refreshMenuItem = item;
	        	 // load the data from server
	            new SyncData().execute();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }
	  
	private void initView(){
		
	}
	
	public void activityInput(View v){
		Intent intent = new Intent(this, InputActivity.class);
	    startActivity(intent);
	}
	
	 /**
     * Async task to load the data from server
     * **/
    private class SyncData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            // set the progress bar view
            refreshMenuItem.setActionView(R.layout.action_progressbar);
            refreshMenuItem.expandActionView();
        }
 
        @Override
        protected String doInBackground(String... params) {
            // just set a timer for now.
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
 
        @Override
        protected void onPostExecute(String result) {
            refreshMenuItem.collapseActionView();
            // remove the progress bar view
            refreshMenuItem.setActionView(null);
        }
    }


	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}
}