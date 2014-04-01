package com.carethy.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.R.drawable;
import com.carethy.application.Carethy;
import com.carethy.model.Recommendation;
import com.carethy.receiver.RecomAlarmReceiver;

public class RecommendationsFragment extends Fragment {

	private View rootView;
	private LinearLayout recoLinearLayout;
	private LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.WRAP_CONTENT);

	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_recommendations,
				container, false);

		initView();

		return rootView;
	}

	private void initView() {

		// ALARM STUFF
		alarmMgr = (AlarmManager) getActivity().getSystemService(
				getActivity().ALARM_SERVICE);
		Intent intent = new Intent(getActivity(), RecomAlarmReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
		// System.out.println("started");
		// Set the alarm to start at 8:30 a.m.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		calendar.set(Calendar.MINUTE, 35);
		// setRepeating() lets you specify a precise custom interval--in this
		// case, 20 minutes.
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), 1000 * 60 * 500, alarmIntent);
		// System.out.println("finished");

		recoLinearLayout = (LinearLayout) rootView
				.findViewById(R.id.recoLinearLayout);
		fillRecommendations();

	}

	private void fillRecommendations() {
		List<Recommendation> recomms = new ArrayList<Recommendation>();

		recomms = Carethy.datasource.getRecommendations("");

		if (recomms.isEmpty()) {
			TextView tv = getTextView();
			tv.setText("No Stored Recommendations");
			recoLinearLayout.addView(tv);
		} else {

			for (Recommendation recom : recomms) {

				TextView tv = getTextView();
				if (recom.getRecomId() <= 300) {
					tv.setTextColor(Color.RED);
				} else {
					tv.setTextColor(Color.GREEN);
				}
				tv.setText(recom.getRecom());
				recoLinearLayout.addView(tv);
			}
		}
	}

	private TextView getTextView() {
		TextView tv = new TextView(this.getActivity());
		tv.setLayoutParams(lparams);
		tv.setBackgroundResource(drawable.recommendations_style);
		return tv;
	}
}
