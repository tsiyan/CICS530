package com.carethy.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
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
		recomAlarmTrigger();

		fillRecommendations();
	}

	private void recomAlarmTrigger() {
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
	}

	private void fillRecommendations() {

		recoLinearLayout = (LinearLayout) rootView
				.findViewById(R.id.recoLinearLayout);

		List<Recommendation> recomms = new ArrayList<Recommendation>();

		recomms = Carethy.datasource.getRecommendations("");

		if (recomms.isEmpty()) {
			TextView tv = getTextView();
			tv.setText("No Stored Recommendations");
			recoLinearLayout.addView(tv);
		} else {

			for (final Recommendation recom : recomms) {

				final TextView tv = getTextView();
				if (recom.getRecomId() <= 300) {
					tv.setTextColor(Color.RED);
				} else {
					tv.setTextColor(Color.GREEN);
				}

				if (!recom.isRead()) {
					tv.setBackgroundResource(drawable.recommendation_bg_style);
				} else {
					tv.setBackgroundResource(drawable.recommendations_style);
				}

				tv.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						if (recom.isRead()) {
							Toast.makeText(getActivity(), "Redirecting to url",
									Toast.LENGTH_SHORT).show();

							String url = recom.getUrl();
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.setData(Uri.parse(url));
							startActivity(i);
						} else {
							Carethy.datasource.setIsReadTrue(recom.getId());
							recom.setIsRead(true);
							tv.setBackgroundResource(drawable.recommendations_style);
						}
					}
				});

				tv.setText(recom.getRecom());
				recoLinearLayout.addView(tv);
			}
		}
	}

	private TextView getTextView() {
		TextView tv = new TextView(this.getActivity());
		tv.setLayoutParams(lparams);
		return tv;
	}
}
