package com.carethy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "userinfo";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_WEIGHT = "weight";
	public static final String COLUMN_HEIGHT = "height";
	public static final String COLUMN_SEX = "sex";
	public static final String COLUMN_BIRTHDATE = "bitrhdate";
	
	private static final String DATABASE_NAME = "user.db";
	private static final int DATABASE_VERSION = 1;
	
	public UserDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	// recommendation table create sql statement
		private static final String USER_CREATE = "create table " + TABLE_NAME
				+ "(" + COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_WEIGHT + " integer not null," + COLUMN_HEIGHT
				+ " integer not null," + COLUMN_SEX
				+ " text not null," + COLUMN_BIRTHDATE + " text not null);";		

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(USER_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
}
