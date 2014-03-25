package com.carethy.fragment;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import com.carethy.R;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	public static final String KEY_PREF_DATE_FORMAT = "date_format";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		// Set the saved value
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
		ListPreference pref = (ListPreference) findPreference(KEY_PREF_DATE_FORMAT);
		pref.setSummary(sharedPreferences.getString(KEY_PREF_DATE_FORMAT, ""));
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
		if (key.equals(KEY_PREF_DATE_FORMAT)) {
			ListPreference pref = (ListPreference) findPreference(key);
			pref.setSummary(sharedPreferences.getString(key, ""));
		}

	}
}
