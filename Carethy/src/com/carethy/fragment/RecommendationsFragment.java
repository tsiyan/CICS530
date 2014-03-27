package com.carethy.fragment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.carethy.R;

public class RecommendationsFragment extends Fragment {
	
	private View rootView;
	private TextView recommendation1;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_recommendations, container,
				false);
		
		initView();
		return rootView;
	}
	
	private void initView(){
		String recommendation1Text = getRecommendation();
		
		
		
		recommendation1 = (TextView) rootView.findViewById(R.id.recommendation1);
		recommendation1.setText(recommendation1Text + " beats");
	}
	
	private String getRecommendation() {
		String tmpJson = "{ 		    'age': 25,		    'weight': 68,		    'height': 168,		    'sex': 'Male',		    'sleep': 		        [		            {'time': '2012-04-23T18:25:43.511Z', 'quantitity': 6, 'quality': 5},		            {'time': '2012-04-22T18:25:43.511Z', 'quantitity': 6, 'quality': 5},		            {'time': '2012-04-21T18:25:43.511Z', 'quantitity': 6, 'quality': 5},		            {'time':'2012-04-20T18:25:43.511Z', 'quantitity': 6, 'quality': 5}		        ],		    'activity': 		        [		            {'time': '2012-04-23T18:25:43.511Z', 'steps': 2500},		            {'time':'2012-04-22T18:25:43.511Z', 'steps': 3000},		            {'time': '2012-04-21T18:25:43.511Z', 'steps': 2000}		        ],		    'blood_pressure': 		        [		            {'time':'2012-04-23T18:25:43.511Z', 'systolic': 115, 'diastolic': 75}		        ]		    'pulse':		        [		            {'time': '2012-04-23T18:25:43.511Z', 'bpm': 75}		        ]		}";
		String recom = null;
		
		URL url;
		try {
			url = new URL("http://health-engine.herokuapp.com/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(tmpJson);
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
				recom = inputLine;
			in.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return recom;
	}
}
