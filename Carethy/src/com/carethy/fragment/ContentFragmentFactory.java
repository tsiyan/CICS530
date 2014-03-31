package com.carethy.fragment;

import android.app.Fragment;


public class ContentFragmentFactory {
	public static Fragment buildContentFragment(int position) {
		Fragment homeFragment=new HomeFragment();
		Fragment activitiesFragment=new ActivitiesFragment();
		Fragment sleepFragment=new SleepFragment();
		Fragment heartBeatsFragment=new HeartBeatsFragment();
		Fragment bloodPressuresFragment=new BloodPressuresFragment();
		Fragment recommendationsFragment=new RecommendationsFragment();
		Fragment medicationFragment=new AlarmListFragment();
//		Fragment medicationFragment=new MedicationFragment();
		Fragment settingsFragment=new SettingsFragment();
		
		switch (position) {
		case 0:// "Home"
			return homeFragment;
		case 1:// "Activity"
			return activitiesFragment;
		case 2:// "Sleep"
			return sleepFragment;
		case 3:// "Heart Rate"
			return heartBeatsFragment;
		case 4:// "Blood Pressure"
			return bloodPressuresFragment;
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
