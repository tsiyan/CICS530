package com.carethy.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
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

		ListPreference pref_profile_sex = (ListPreference) findPreference("sex");
		String sex = getPreferenceScreen().getSharedPreferences().getString(
				"sex", "");
		if (!sex.equals("")) {
			pref_profile_sex.setSummary((sex));
		}

		Preference pref_profile_age = findPreference("age");
		pref_profile_age.setSummary(getPreferenceScreen()
				.getSharedPreferences().getString("age", ""));// TODO:json

		Preference pref_profile_weight = findPreference("weight");
		String weight = getPreferenceScreen().getSharedPreferences().getString(
				"weight", "");
		if (!weight.equals("")) {
			pref_profile_weight.setSummary(weight + " Kg");// TODO:json
		}

		Preference pref_profile_height = findPreference("height");
		String height = getPreferenceScreen().getSharedPreferences().getString(
				"height", "");
		if (!height.equals("")) {
			pref_profile_height.setSummary(height + " Cm");// TODO:json
		}


		Preference pref_terms_conditions = findPreference("terms_conditions");
		pref_terms_conditions
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(
								getActivity(),
								com.carethy.activity.TermsConditionsActivity.class);
						startActivity(intent);
						return false;
					}

				});

		Preference pref_logout = findPreference("logout");
		pref_logout
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {

						new AlertDialog.Builder(getActivity())
								.setIcon(R.drawable.ic_launcher)
								.setTitle("Carethy")
								.setMessage("Log Out?")
								.setNegativeButton(android.R.string.no, null)
								.setPositiveButton(android.R.string.yes,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface arg0,
													int arg1) {

												Intent intent = new Intent(
														getActivity(),
														com.carethy.activity.LoginActivity.class);
												startActivity(intent);
												getActivity().finish();
											}
										}).create().show();
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
		if (key.equals("sex")) {
			ListPreference pref = (ListPreference) findPreference(key);
			pref.setSummary(getPreferenceScreen().getSharedPreferences()
					.getString(key, "").equals("0") ? "Male" : "Female");

		}
		if (key.equals("age")) {
			Preference pref = (Preference) findPreference(key);
			pref.setSummary(getPreferenceScreen().getSharedPreferences()
					.getString(key, ""));

		}

		if (key.equals("weight")) {

			Preference pref = (Preference) findPreference(key);
			pref.setSummary(getPreferenceScreen().getSharedPreferences()
					.getString(key, "") + " Kg");

		}

		if (key.equals("height")) {
			Preference pref = (Preference) findPreference(key);
			pref.setSummary(getPreferenceScreen().getSharedPreferences()
					.getString(key, "") + " Cm");

		}

		if (key.equals("change_password")) {

			// TODO: json
		}
	}
}
