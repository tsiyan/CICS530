package com.carethy.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.carethy.R;
<<<<<<< HEAD
import com.carethy.adapter.RecomDBDataSource;
import com.carethy.util.DBHelper;
=======
import com.carethy.database.MedicationDatabaseHelper;
>>>>>>> origin/master

public class Carethy extends Application {
	public static MedicationDatabaseHelper mMedicationDatabaseHelper;
	public static SQLiteDatabase mSQLiteDatabase;
	public static SharedPreferences mSharedPreferences;
	
	public static RecomDBDataSource datasource;

	@Override
	public void onCreate() {
		super.onCreate();

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
<<<<<<< HEAD
		mDBHelper = new DBHelper(this);
		mSQLiteDatabase = mDBHelper.getWritableDatabase();
		
		datasource = new RecomDBDataSource(this);
		datasource.open();
=======
		mMedicationDatabaseHelper = new MedicationDatabaseHelper(this);
		mSQLiteDatabase = mMedicationDatabaseHelper.getWritableDatabase();
>>>>>>> origin/master
	}

}
