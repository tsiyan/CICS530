package com.carethy.fragment;

import java.io.IOException;
import java.io.InputStream;

import com.carethy.R;
import com.carethy.application.Carethy;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChangeDataFragment extends Fragment {
	public static String[] dataFileNames;
	View rootView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		rootView = inflater.inflate(R.layout.fragment_recommendations,
				container, false);

		initView();

		return rootView;
	}

	private void initView() {
		LinearLayout inner = (LinearLayout) rootView
				.findViewById(R.id.recoLinearLayout);
		TextView tv = new TextView(this.getActivity());
		tv.setBackgroundResource(R.drawable.recommendations_style);
		tv.setText("Data changed successfully : " + changeData());
		inner.addView(tv);
	}

	private String changeData() {
		dataFileNames = getResources()
				.getStringArray(R.array.sample_data_files);

		int currId = Carethy.nextDataFileId;
		if (currId == dataFileNames.length - 1) {
			Carethy.nextDataFileId = 0;
		} else {
			Carethy.nextDataFileId = Carethy.nextDataFileId + 1;
		}

		//get user data
		String[] sample_data_files = getResources().getStringArray(R.array.user_data_files);
		String use_data_file = sample_data_files[Carethy.currentDataFileId];
		try 
		{
			InputStream is = getActivity().getAssets().open(use_data_file);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			Carethy.user_data = new String(buffer, "UTF-8");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			Carethy.user_data = "";
		}
		return dataFileNames[Carethy.nextDataFileId];
	}

}
