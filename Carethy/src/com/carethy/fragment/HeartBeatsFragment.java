package com.carethy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carethy.application.Carethy.BodyData;

public class HeartBeatsFragment extends GraphBaseFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = super.onCreateView(inflater, container, savedInstanceState);
		mBodyData = BodyData.heartBeats;

		return rootView;
	}
}
