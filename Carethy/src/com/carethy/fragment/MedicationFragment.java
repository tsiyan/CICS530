package com.carethy.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.carethy.R;
import com.carethy.activity.CreateMedicationActivity;
import com.carethy.adapter.CustomArrayAdapter;
import com.carethy.model.Medication;
import com.carethy.receiver.AlarmReceiver;

public class MedicationFragment extends Fragment {
	private View rootView;
	private ListView mListView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_medication, container,
				false);

		// add "+" icon to the action bar
		setHasOptionsMenu(true);

		initView(rootView);
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		// add "+" button
		inflater.inflate(R.menu.fragment_medication_actions, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(getActivity(),
				CreateMedicationActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	private void initView(View rootView) {
		// ListView for medications
		mListView = (ListView) rootView.findViewById(R.id.listView);

		List<String> MedicationList = new ArrayList<String>();
		MedicationList.add((new Medication("Advil")).medicine);
		MedicationList.add((new Medication("Tynol")).medicine);
		MedicationList.add((new Medication("HFZ")).medicine);
		MedicationList.add((new Medication("Ophones")).medicine);

		CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(),
				R.layout.rowlayout, MedicationList);
		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {

				Intent i = new Intent(getActivity(),
						CreateMedicationActivity.class);
				startActivity(i);
			}
		});
		
		setupAlarm(5);
	}

	private void setupAlarm(int seconds) {
		AlarmManager alarmManager = (AlarmManager) getActivity()
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(getActivity(), AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
				0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Getting current time and add the seconds in it
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, seconds);

		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);
	}
}