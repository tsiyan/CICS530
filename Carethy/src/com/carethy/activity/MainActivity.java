package com.carethy.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.carethy.R;
import com.carethy.adapter.NavDrawerListAdapter;
import com.carethy.fragment.ContentFragmentFactory;
import com.carethy.model.NavDrawerItem;
import com.carethy.service.ServiceDemo;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * This must be false for production. If true, turns on logging,
	 * 
	 **/
	public final static boolean DEBUG = false;

	private boolean loggedIn = true;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// Sliding menu items
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private String[] navMenuItemTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter mNavDrawerListAdapter;
	private static int mPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (!loggedIn) {
			Intent intent = new Intent(this, LoginActivity.class);
			// Closing all the Activities
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// Add new Flag to start new Activity
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		} else {
			initView(savedInstanceState);
			/*
			 * *******************start the ServiceDomo which pops up
			 * window***********************************
			 */
			startService(new Intent(this, ServiceDemo.class));

		}

	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void initView(Bundle savedInstanceState) {
		mTitle = mDrawerTitle = getTitle();
		// load sliding menu items
		navMenuItemTitles = getResources().getStringArray(
				R.array.drawer_menu_item_titles);

		// load nav drawer icons
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		navDrawerItems = new ArrayList<NavDrawerItem>();
		// add nav drawer items to array
		navDrawerItems.add(new NavDrawerItem(navMenuItemTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuItemTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuItemTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuItemTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuItemTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuItemTitles[5], navMenuIcons
				.getResourceId(5, -1), true, "1"));
		navDrawerItems.add(new NavDrawerItem(navMenuItemTitles[6], navMenuIcons
				.getResourceId(6, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuItemTitles[7], navMenuIcons
				.getResourceId(7, -1)));

		// recycle the typed array
		navMenuIcons.recycle();

		// set the nav drawer list adapter
		mNavDrawerListAdapter = new NavDrawerListAdapter(
				getApplicationContext(), navDrawerItems);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(mNavDrawerListAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_navigation_drawer, /*
										 * nav drawer image to replace 'Up'
										 * caret
										 */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		selectItem(mPosition);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mPosition = position;
			selectItem(position);
		}
	}

	private void replaceFragment(int position) {
		// update the main content by replacing fragments
		Fragment fragment = ContentFragmentFactory
				.buildContentFragment(position);

		getFragmentManager()
				.beginTransaction()
				.replace(R.id.content_frame, fragment,
						"fragment_id_" + position).commit();
	}

	private void selectItem(int position) {
		replaceFragment(position);
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(navMenuItemTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void onDestroy() {
		super.onDestroy();
		// Toast.makeText(this, "finish() -> onDestory()",
		// Toast.LENGTH_SHORT).show();
		loggedIn = false;
	}
}