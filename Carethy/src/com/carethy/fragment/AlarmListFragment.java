package com.carethy.fragment;

import java.util.ArrayList;
import java.util.List;

import com.carethy.R;
import com.carethy.activity.CreateMedicationActivity;
import com.carethy.adapter.CustomArrayAdapter;
import com.carethy.alarmclock.AlarmDBHelper;
import com.carethy.alarmclock.AlarmDetailsActivity;
import com.carethy.alarmclock.AlarmListAdapter;
import com.carethy.alarmclock.AlarmManagerHelper;
import com.carethy.alarmclock.AlarmModel;
import com.carethy.object.Medication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AlarmListFragment extends ListFragment 
{

	private AlarmListAdapter mAdapter;
	private AlarmDBHelper dbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//getActivity().requestWindowFeature(Window.FEATURE_ACTION_BAR);
		
		// add "+" icon to the action bar
		setHasOptionsMenu(true);
	}

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.activity_alarm_list, container, false);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		// add "+" button
		inflater.inflate(R.menu.alarm_list, menu);
	}
	
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		dbHelper = new AlarmDBHelper(getActivity());
		
		mAdapter = new AlarmListAdapter(getActivity(), dbHelper.getAlarms());	
		setListAdapter(mAdapter);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
			case R.id.action_add_new_alarm: {
				startAlarmDetailsActivity(-1);
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
//		if (resultCode == Activity.RESULT_OK) {
	        mAdapter.setAlarms(dbHelper.getAlarms());
	        mAdapter.notifyDataSetChanged();
//	    }
	}
	
	public void setAlarmEnabled(long id, boolean isEnabled) {
		AlarmManagerHelper.cancelAlarms(getActivity());
		
		AlarmModel model = dbHelper.getAlarm(id);
		model.isEnabled = isEnabled;
		dbHelper.updateAlarm(model);
		
		AlarmManagerHelper.setAlarms(getActivity());
	}

	public void startAlarmDetailsActivity(long id) {
		Intent intent = new Intent(getActivity(), AlarmDetailsActivity.class);
		intent.putExtra("id", id);
		startActivityForResult(intent, 0);
	}
	
	public void deleteAlarm(long id) {
		final long alarmId = id;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Please confirm")
		.setTitle("Delete set?")
		.setCancelable(true)
		.setNegativeButton("Cancel", null)
		.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Cancel Alarms
				AlarmManagerHelper.cancelAlarms(getActivity());
				//Delete alarm from DB by id
				dbHelper.deleteAlarm(alarmId);
				//Refresh the list of the alarms in the adaptor
				mAdapter.setAlarms(dbHelper.getAlarms());
				//Notify the adapter the data has changed
				mAdapter.notifyDataSetChanged();
				//Set the alarms
				AlarmManagerHelper.setAlarms(getActivity());
			}
		}).show();
	}
}
