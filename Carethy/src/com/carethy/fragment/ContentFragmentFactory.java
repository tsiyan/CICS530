package com.carethy.fragment;

import android.os.Bundle;

public class ContentFragmentFactory {
	public static AbstractContentFragment buildContentFragment(int position) {
		Bundle args = new Bundle();
		args.putInt(AbstractContentFragment.ARG_MENU_ITEM_INDEX, position);

		switch (position) {
		case 0:// "Home"
			return new HomeContentFragment();
		case 1:// "sleep"
			return new SleepContentFragment();
		case 2:// "heart rate"
			return new Heart
			break;
		case 3:// "blood pressure"

			break;
		case 4:// "devices"

			break;
		case 5:// "settings"

			break;
		default:
			break;
		}

		return null;

	}
}
