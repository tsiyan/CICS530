package com.carethy.fragment;

import android.app.Fragment;


public class ContentFragmentFactory {
	public static Fragment buildContentFragment(int position) {
		Fragment homeFragment=new HomeFragment();
		Fragment activityFragment=new ActivityFragment();
		Fragment sleepFragment=new SleepFragment();
		Fragment heartRateFragment=new HeartRateFragment();
		Fragment bloodPressureFragment=new BloodPressureFragment();
		Fragment recommendationsFragment=new RecommendationsFragment();
		Fragment medicationFragment=new MedicationFragment();
		Fragment settingsFragment=new SettingsFragment();
		
		switch (position) {
		case 0:// "Home"
			return homeFragment;
		case 1:// "Activity"
			return activityFragment;
		case 2:// "Sleep"
			return sleepFragment;
		case 3:// "Heart Rate"
			return heartRateFragment;
		case 4:// "Blood Pressure"
			return bloodPressureFragment;
		case 5:// "Recommendations"
			return recommendationsFragment;
		case 6:// "Medication"
			return medicationFragment;
		case 7:// "Settings"
			return settingsFragment;
		default:
			break;
		}

		return null;

	}
}
