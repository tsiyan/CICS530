package com.carethy.activity.input;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.carethy.R;
import com.carethy.adapter.TabsPagerAdapter;

public class InputActivity extends FragmentActivity implements ActionBar.OnNavigationListener,ActionBar.TabListener{
	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar mActionBar;
	private String[] tabs = {"Summary", "Graph"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);

		initView();
	}
	
	
	private void initView(){
		mViewPager = (ViewPager) findViewById(R.id.pager);
        mActionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 
        mViewPager.setAdapter(mAdapter);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
        //Up navigation 
		mActionBar.setDisplayHomeAsUpEnabled(true);
		
        // Adding Tabs
        for (String tab_name : tabs) {
            mActionBar.addTab(mActionBar.newTab().setText(tab_name).setTabListener(this));
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { 
            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    
	}
	
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {		
	}


	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
