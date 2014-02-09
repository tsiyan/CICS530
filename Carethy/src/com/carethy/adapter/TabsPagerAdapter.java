package com.carethy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.carethy.fragment.GraphFragment;
import com.carethy.fragment.SummaryFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
	
	@Override
	public Fragment getItem(int index) {

        switch (index) {
        case 0:
            return new SummaryFragment();
        case 1:
            return new GraphFragment();
        }
 
        return null;
	}

	@Override
	public int getCount() {
		 // get item count - equal to number of tabs
        return 2;
	}

}
