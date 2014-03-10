package com.carethy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.carethy.R;
import com.carethy.activity.RegisterActivity;
import com.carethy.adapter.CustomArrayAdapter;

public class DevicesContentFragment extends AbstractContentFragment {
	private ListView mListView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_devices, container,
				false);

		initView(rootView);

		return rootView;
	}

	public void initView(View rootView) {
		// Fitbit and Jawbone buttons
		String[] DeviceArray = getResources().getStringArray(
				R.array.device_list);
		CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(),
				R.layout.rowlayout, DeviceArray);
		mListView = (ListView) rootView.findViewById(R.id.devicelist);

		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				Intent i;
				switch(position){
				case 0:
					i = new Intent(getActivity(), RegisterActivity.class);//TODO: change to fitbit activity
					startActivity(i);
					break;
				case 1:
					i = new Intent(getActivity(), RegisterActivity.class);//TODO: change to jawbone activity
					startActivity(i);
					break;
				}

			}
		});
	}

}
