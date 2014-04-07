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
import android.view.Gravity;
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
	private LinearLayout innerLayeout;
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
		fillRecommendations();
	}

	private void fillRecommendations() {

		innerLayeout = (LinearLayout) rootView
				.findViewById(R.id.scrollRecoInnerPanel);

		List<Recommendation> recomms = new ArrayList<Recommendation>();

		recomms = Carethy.datasource.getRecommendations("");

		if (recomms.isEmpty()) {
			TextView tv = getTextView();
			tv.setText("No Stored Recommendations");
			innerLayeout.addView(tv);
		} else {

			String datefield = "";

			for (final Recommendation recom : recomms) {
				String recomdate = recom.getSaveDate();

				if (!datefield.equals(recomdate)) {
					LayoutParams dvparams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					dvparams.gravity = Gravity.CENTER_HORIZONTAL;
					dvparams.topMargin = 10;
					TextView dateview = new TextView(this.getActivity());
					dateview.setBackgroundResource(R.drawable.recom_date_style);
					dateview.setText(recomdate);
					dateview.setLayoutParams(dvparams);
					dateview.setGravity(Gravity.CENTER | Gravity.BOTTOM);
					innerLayeout.addView(dateview);
					datefield = recomdate;
				}

				final TextView tv = getTextView();
				
				// Siyan
				if (recom.getSeverity() > 3) {
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
							if (!url.startsWith("http")) {
								url = "http://" + url;
							}

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
				innerLayeout.addView(tv);
			}
		}
	}

	private TextView getTextView() {
		TextView tv = new TextView(this.getActivity());
		tv.setLayoutParams(lparams);
		return tv;
	}
}
