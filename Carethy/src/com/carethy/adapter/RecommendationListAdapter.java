package com.carethy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.application.Carethy;
import com.carethy.model.Recommendation;

public class RecommendationListAdapter extends ArrayAdapter<Recommendation> {
	private Context mContext;
	private ArrayList<Recommendation> list;

	public RecommendationListAdapter(Context context, int resource,
			ArrayList<Recommendation> objects) {
		super(context, resource, objects);
		mContext = context;
		list = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout_recommendation,
				parent, false);

		final RelativeLayout row = (RelativeLayout) rowView
				.findViewById(R.id.recommendation_row);
		final TextView recomTextView = (TextView) rowView
				.findViewById(R.id.recommendation_content);
		final Recommendation recom = list.get(position);
		recomTextView.setText(recom.getRecom() + "\n" + recom.getSaveDate());

		ImageView mImageView = (ImageView) rowView
				.findViewById(R.id.recommendation_level);
		Drawable drawable = null;
		if (recom.getSeverity() < 3) {
			drawable = mContext.getResources().getDrawable(
					R.drawable.ic_alert_green);
		} else if (recom.getSeverity() == 3) {
			drawable = mContext.getResources().getDrawable(
					R.drawable.ic_alert_orange);
		} else {
			drawable = mContext.getResources().getDrawable(R.drawable.ic_alert);
		}
		mImageView.setImageDrawable(drawable);

		if (!recom.isRead()) {
			recomTextView.setTextAppearance(mContext, R.style.boldText);
			row.setBackgroundColor(Color.WHITE);
			recomTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}
		recomTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (recom.isRead()) {
					String url = recom.getUrl();
					if (!url.startsWith("http")) {
						url = "http://" + url;
					}

					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					mContext.startActivity(i);
				} else {
					Carethy.datasource.setIsReadTrue(recom.getId());
					recom.setIsRead(true);
					recomTextView.setTextAppearance(mContext,
							R.style.TextViewStyle);
					row.setBackgroundColor(Color.TRANSPARENT);
					recomTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.ic_arrow, 0);
				}
			}

		});
		return rowView;
	}
}