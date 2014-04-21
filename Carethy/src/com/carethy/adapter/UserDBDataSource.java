package com.carethy.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.carethy.database.UserDBHelper;

public class UserDBDataSource {

	private SQLiteDatabase database;
	private UserDBHelper dbHelper;
	
	public UserDBDataSource(Context context) {
		dbHelper = new UserDBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public boolean insertIntoTable(int height, int weight, String birthdate, String sex) {

		ContentValues values = new ContentValues();

		if (height > 0) {
			values.put(UserDBHelper.COLUMN_HEIGHT, height);
		}
		if (weight > 0) {
			values.put(UserDBHelper.COLUMN_WEIGHT, weight);
		}
		if (birthdate != null) {
			values.put(UserDBHelper.COLUMN_BIRTHDATE, birthdate);
		}
		if (sex != null) {
			values.put(UserDBHelper.COLUMN_SEX, sex);
		}

		long insertId = database.insert(UserDBHelper.TABLE_NAME, null, values);
		
		return insertId != -1;
	}
	
}
