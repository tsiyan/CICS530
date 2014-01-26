package com.carethy.adapter;

import com.carethy.fragment.ActivityFragment;
import com.carethy.fragment.ProfileFragment;
import com.carethy.fragment.SocialFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
	
	@Override
	public Fragment getItem(int index) {

        switch (index) {
        case 0:
            return new ProfileFragment();
        case 1:
            return new ActivityFragment();
        case 2:
            return new SocialFragment();
        }
 
        return null;
	}

	@Override
	public int getCount() {
		 // get item count - equal to number of tabs
        return 3;
	}

}
