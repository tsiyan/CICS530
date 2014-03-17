package com.carethy.fragment;

import android.os.Bundle;

public class ContentFragmentFactory {
	public static AbstractContentFragment buildContentFragment(int position) {
		Bundle args = new Bundle();
		args.putInt(AbstractContentFragment.ARG_MENU_ITEM_INDEX, position);

		switch (position) {
		case 0:// "Home"
			return new HomeContentFragment();
		case 1:// "Activity"
			return new ActivityContentFragment();
		case 2:// "Sleep"
			return new SleepContentFragment();
		case 3:// "Heart Rate"
			return new HeartContentFragment();
		case 4:// "Blood Pressure"
			return new BloodPressureContentFragment();
		case 5:// "Recommendations"
			return new RecommendationsContentFragment();
		case 6:// "Devices"
			return new DevicesContentFragment();
		case 7:// "Settings"
			return new SettingsContentFragment();
		default:
			break;
		}

		return null;

	}
}
