package com.carethy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.carethy.R;
import com.carethy.adapter.MyExpandableListAdapter;
import com.carethy.application.Carethy;
import com.carethy.database.DBRecomHelper;
import com.carethy.model.Group;
import com.carethy.model.Recommendation;

public class RecommendationsFragment extends Fragment {

	private View rootView;

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

		ArrayList<Recommendation> recomms = Carethy.datasource
				.getRecommendations(DBRecomHelper.RECOM_LIMIT);

		if (recomms.isEmpty()) {
			Toast.makeText(getActivity(), "No recommendation yet.",
					Toast.LENGTH_LONG).show();
		} else {
			SparseArray<Group> groups = new SparseArray<Group>();
			HashMap<String, ArrayList<Recommendation>> map = new HashMap<String, ArrayList<Recommendation>>();

			for (Recommendation recom : recomms) {

				String date = recom.getSaveDate();
				ArrayList<Recommendation> array;

				if (!map.containsKey(date)) {
					array = new ArrayList<Recommendation>();
				} else {
					array = map.get(date);
				}

				array.add(recom);
				map.put(date, array);
			}

			int count = 0;
			Iterator<Entry<String, ArrayList<Recommendation>>> it = map
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, ArrayList<Recommendation>> entry = it.next();

				Group group = new Group((String)entry.getKey());
				group.children = entry.getValue();
				groups.append(count, group);
				
				count++;
			}

			ExpandableListView listView = (ExpandableListView) rootView
					.findViewById(R.id.expandableListView);
			MyExpandableListAdapter adapter = new MyExpandableListAdapter(
					getActivity(), groups);
			listView.setAdapter(adapter);

		}

	}
}
