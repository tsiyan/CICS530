package com.carethy.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.carethy.R;
import com.carethy.util.DBHelper;

public class Carethy extends Application {
	public static DBHelper mDBHelper;
	public static SQLiteDatabase mSQLiteDatabase;
	public static SharedPreferences mSharedPreferences;

	@Override
	public void onCreate() {
		super.onCreate();

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		mDBHelper = new DBHelper(this);
		mSQLiteDatabase = mDBHelper.getWritableDatabase();
	}

}
