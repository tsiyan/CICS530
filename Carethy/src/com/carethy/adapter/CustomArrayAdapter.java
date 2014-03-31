package com.carethy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.carethy.R;

public class CustomArrayAdapter extends ArrayAdapter<String> {
	private HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	private Context mContext;
	private List<String> values;

	public CustomArrayAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		for (int i = 0; i < objects.size(); ++i) {
			mIdMap.put(objects.get(i), i);
		}
		this.mContext = context;
		this.values = objects;
	}

	@Override
	public long getItemId(int position) {
		String item = getItem(position);
		return mIdMap.get(item);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
		TextView textViewMedication = (TextView) rowView
				.findViewById(R.id.medication);
		TextView textViewAlarm = (TextView) rowView.findViewById(R.id.alarm);

		textViewMedication.setText(values.get(position));
		textViewAlarm.setText("alarm");
		return rowView;
	}
}