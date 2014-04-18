package com.carethy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.model.Recommendation;

public class RecommendationListAdapter extends ArrayAdapter<Recommendation> {
	private Context mContext;
	private ArrayList<Recommendation> list;
	
	public RecommendationListAdapter(Context context, int resource,
			ArrayList<Recommendation> objects) {
		super(context, resource, objects);
		mContext=context;
		list=objects;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout_recommendation,
				parent, false);
		ImageView mImageView = (ImageView) rowView
				.findViewById(R.id.recommendation_level);

		Drawable drawable = null;
		switch (list.get(position).getRecomId() % 2) {
		case 0:
			drawable = mContext.getResources().getDrawable(
					R.drawable.ic_settings);
			break;
		case 1:
			drawable = mContext.getResources().getDrawable(
					R.drawable.ic_action_email);
			break;
//		case 2:
//			drawable = mContext.getResources().getDrawable(
//					R.drawable.ic_action_password);
//			break;
//		case 3:
//			drawable = mContext.getResources().getDrawable(R.drawable.ic_home);
//			break;
//		case 4:
//			drawable = mContext.getResources().getDrawable(
//					R.drawable.ic_action_time);
//			break;
//		case 5:
//			drawable = mContext.getResources().getDrawable(
//					R.drawable.ic_activity);
//			break;
		}

		mImageView.setImageDrawable(drawable);

		TextView recommendationContent = (TextView) rowView
				.findViewById(R.id.recommendation_content);

		recommendationContent.setText(list.get(position).getRecom());
		return rowView;
	}
}