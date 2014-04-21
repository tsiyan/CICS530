package com.carethy.application;

import java.io.IOException;
import java.io.InputStream;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.carethy.R;
import com.carethy.adapter.RecomDBDataSource;
import com.carethy.adapter.UserDBDataSource;
import com.carethy.database.MedicationDatabaseHelper;

public class Carethy extends Application 
{
	public static MedicationDatabaseHelper mMedicationDatabaseHelper;
	public static SQLiteDatabase mSQLiteDatabase;
	public static SharedPreferences mSharedPreferences;

	public static RecomDBDataSource datasource;
	public static UserDBDataSource userDatasource;
	public static String ISLOGGEDIN = "isLoggedIn";
	public static int nextDataFileId = 1;
	public static int currentDataFileId = 0;
	public static String user_data = "";
	
	public enum BodyData {
		activities, sleep, heartBeats, bloodPressures
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

		// user DB
		userDatasource = new UserDBDataSource(this);
		userDatasource.open();

		// Medication DB
		mMedicationDatabaseHelper = new MedicationDatabaseHelper(this);
		mSQLiteDatabase = mMedicationDatabaseHelper.getWritableDatabase();
		
		//get user data
		String[] sample_data_files = getApplicationContext().getResources().getStringArray(R.array.user_data_files);
		String use_data_file = sample_data_files[Carethy.currentDataFileId];
		try 
		{
			InputStream is = getApplicationContext().getAssets().open(use_data_file);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			user_data = new String(buffer, "UTF-8");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			user_data = "";
		}
	}

}
