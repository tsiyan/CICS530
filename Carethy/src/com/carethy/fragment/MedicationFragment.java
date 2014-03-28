package com.carethy.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.SimpleCursorAdapter;

import com.carethy.R;
import com.carethy.activity.EditMedicationActivity;
import com.carethy.contentprovider.MedicationContentProvider;
import com.carethy.database.MedicationTable;

public class MedicationFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private View rootView;
	private ListView mListView;

	private SimpleCursorAdapter adapter;

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
		Intent intent = new Intent(getActivity(), EditMedicationActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	private void initView(View rootView) {
		String[] from = new String[] { MedicationTable.COLUMN_MEDICINE };
		int[] to = new int[] { R.id.medication };

		getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.rowlayout,
				null, from, to, 0);

		mListView = (ListView) rootView.findViewById(R.id.listView);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {

				Uri uri = Uri.parse(MedicationContentProvider.CONTENT_URI + "/"
						+ id);

				Intent intent = new Intent(getActivity(),
						EditMedicationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(
						MedicationContentProvider.CONTENT_ITEM_TYPE, uri);
				intent.putExtras(bundle);
				startActivity(intent);

				adapter.notifyDataSetChanged();
				adapter.notifyDataSetInvalidated();
			}
		});
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { MedicationTable.COLUMN_ID,
				MedicationTable.COLUMN_MEDICINE };
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				MedicationContentProvider.CONTENT_URI, projection, null, null,
				null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	// private void setupAlarm(int seconds) {
	// AlarmManager alarmManager = (AlarmManager) getActivity()
	// .getSystemService(Context.ALARM_SERVICE);
	// Intent intent = new Intent(getActivity(), AlarmReceiver.class);
	// PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
	// 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	//
	// // Getting current time and add the seconds in it
	// Calendar cal = Calendar.getInstance();
	// cal.add(Calendar.SECOND, seconds);
	//
	// alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
	// pendingIntent);
	// }
}
