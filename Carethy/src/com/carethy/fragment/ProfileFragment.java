package com.carethy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.carethy.R;
import com.carethy.activity.DisplayResultActivity;

public class ProfileFragment extends Fragment{
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	 
	        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
	        
	        final Spinner spinner = (Spinner)rootView.findViewById(R.id.tracked_item_spinner);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
			        R.array.tracked_item_array, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			
			
			Button button=(Button) rootView.findViewById(R.id.display_button);
			button.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity().getBaseContext(),DisplayResultActivity.class);
					intent.putExtra("category",spinner.getSelectedItem().toString());
					intent.putExtra("type","bar");
					startActivity(intent);
				}
			});	
			
	        return rootView;
	    }
	 
}
