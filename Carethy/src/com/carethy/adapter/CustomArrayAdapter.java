package com.carethy.adapter;

import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.carethy.R;

public class CustomArrayAdapter extends ArrayAdapter<String> {
	private HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	private Context mContext;
	private String[] values;

	public CustomArrayAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		for (int i = 0; i < objects.length; ++i) {
			mIdMap.put(objects[i], i);
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
		TextView mTextView = (TextView) rowView.findViewById(R.id.device);

		mTextView.setText(values[position]);

		Context context = parent.getContext();
		switch (position) {
		case 0:
			Drawable fitbit = context.getResources().getDrawable(
					R.drawable.ic_fitbit);
			fitbit.setBounds(0, 0, 100, 100);
			mTextView.setCompoundDrawables(fitbit, null, null, null);
			break;
		case 1:
			Drawable jawbone = context.getResources().getDrawable(
					R.drawable.ic_jawbone);
			jawbone.setBounds(0, 0, 100, 100);
			mTextView.setCompoundDrawables(jawbone, null, null, null);
		}

		return rowView;
	}
}