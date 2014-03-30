package com.carethy.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.carethy.R;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		Preference pref_terms_conditions = findPreference("terms_conditions");
		pref_terms_conditions.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(getActivity(),
						com.carethy.activity.TermsConditionsActivity.class);
				startActivity(intent);
				return false;
			}

		});
		
		Preference pref_logout = findPreference("logout");
		pref_logout.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent(getActivity(),
						com.carethy.activity.LoginActivity.class);
				startActivity(intent);
				return false;
			}

		});

	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceManager().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

	}

	@Override
	public void onPause() {
		getPreferenceManager().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals("notification_time")
				|| key.equals("recommendation_category")) {

			MultiSelectListPreference pref = (MultiSelectListPreference) findPreference(key);
			String summary = "";
			String[] selected = pref.getValues().toArray(new String[] {});
			for (int i = 0; i < selected.length; i++) {
				summary += " " + selected[i];
			}
			pref.setSummary(summary);
		}
	}
}
