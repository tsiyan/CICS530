package com.carethy.fragment;

import com.carethy.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecommendationsContentFragment extends AbstractContentFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_recommendations, container,
				false);
		
		initView();
		return rootView;
	}
	
	private void initView(){
		
	}
}
