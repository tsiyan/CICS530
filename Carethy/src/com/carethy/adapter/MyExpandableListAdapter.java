package com.carethy.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.application.Carethy;
import com.carethy.model.Group;
import com.carethy.model.Recommendation;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

	private final SparseArray<Group> groups;
	public LayoutInflater inflater;
	public Activity activity;

	public MyExpandableListAdapter(Activity act, SparseArray<Group> groups) {
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final Recommendation recom = (Recommendation) getChild(groupPosition,
				childPosition);
		final String children = recom.getRecom();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.rowlayout_recommendation,
					null);
		}
		
		final RelativeLayout row = (RelativeLayout) convertView
				.findViewById(R.id.recommendation_row);
		ImageView mImageView = (ImageView) convertView
				.findViewById(R.id.recommendation_level);
		Drawable drawable = null;
		if (recom.getSeverity() < 3) {
			drawable = activity.getResources().getDrawable(
					R.drawable.ic_alert_green);
		} else if (recom.getSeverity() == 3) {
			drawable = activity.getResources().getDrawable(
					R.drawable.ic_alert_orange);
		} else {
			drawable = activity.getResources().getDrawable(R.drawable.ic_alert);
		}
		mImageView.setImageDrawable(drawable);

		final TextView text = (TextView) convertView
				.findViewById(R.id.recommendation_content);
		text.setText(children);

		if (!recom.isRead()) {
			text.setTextAppearance(activity, R.style.boldText);
			row.setBackgroundColor(Color.WHITE);
		}
		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (recom.isRead()) {
					String url = recom.getUrl();
					if (!url.startsWith("http")) {
						url = "http://" + url;
					}

					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					activity.startActivity(i);
				} else {
					Carethy.datasource.setIsReadTrue(recom.getId());
					recom.setIsRead(true);
					text.setTextAppearance(activity, R.style.TextViewStyle);
					row.setBackgroundColor(Color.TRANSPARENT);
				}
			}

		});

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.expandablelistrow_group,
					null);
		}
		Group group = (Group) getGroup(groupPosition);

		((CheckedTextView) convertView).setText(group.string);
		((CheckedTextView) convertView).setChecked(isExpanded);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}