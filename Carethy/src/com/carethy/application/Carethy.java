package com.carethy.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.carethy.R;
import com.carethy.adapter.RecomDBDataSource;
import com.carethy.database.MedicationDatabaseHelper;

public class Carethy extends Application {
	public static MedicationDatabaseHelper mMedicationDatabaseHelper;
	public static SQLiteDatabase mSQLiteDatabase;
	public static SharedPreferences mSharedPreferences;

	public static RecomDBDataSource datasource;
	public static String ISLOGGEDIN = "isLoggedIn";
	public static int nextDataFileId = 1;
	public static int currentDataFileId = 0;

	public enum BodyData {
		activities, sleep, heartBeats, bloodPressure
	};

	@Override
	public void onCreate() {
		super.onCreate();

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		// Recommendation DB
		datasource = new RecomDBDataSource(this);
		datasource.open();

		// Medication DB
		mMedicationDatabaseHelper = new MedicationDatabaseHelper(this);
		mSQLiteDatabase = mMedicationDatabaseHelper.getWritableDatabase();
	}

}
