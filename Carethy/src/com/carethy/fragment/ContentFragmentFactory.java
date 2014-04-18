package com.carethy.fragment;

import android.app.Fragment;


public class ContentFragmentFactory {
	public static Fragment buildContentFragment(int position) {
		Fragment homeFragment=new HomeFragment();
		Fragment activitiesFragment=new ActivitiesFragment();
		Fragment sleepFragment=new SleepFragment();
		Fragment heartBeatsFragment=new HeartBeatsFragment();
		Fragment bloodPressuresFragment=new BloodPressuresFragment();
		Fragment trendsFragment=new TrendsFragment();
		Fragment recommendationsFragment=new RecommendationsFragment();
		Fragment medicationFragment=new AlarmListFragment();
		Fragment settingsFragment=new SettingsFragment();
		Fragment changeDataFragment=new ChangeDataFragment();
		
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
		case 5:// "Trends"
			return trendsFragment;
		case 6:// "Recommendations"
			return recommendationsFragment;
		case 7:// "Medication"
			return medicationFragment;
		case 8:// "Settings"
			return settingsFragment;
		case 9:// "Settings"
			return changeDataFragment;
		default:
			break;
		}

		return null;

	}
}
