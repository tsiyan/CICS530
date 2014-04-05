package com.carethy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBRecomHelper extends SQLiteOpenHelper {

	public static final String TABLE_RECOM = "recommendation";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_RECOM = "recom";
	public static final String COLUMN_RECOM_ID = "recom_id";
	public static final String COLUMN_ISREAD = "isRead";
	public static final String COLUMN_URL = "url";
	public static final String COLUMN_DATE = "savedate";
	public static final String ORDER_RECOM_BY = "_id desc";
	public static final String RECOM_LIMIT = "8";

	private static final String DATABASE_NAME = "recommendation.db";
	private static final int DATABASE_VERSION = 1;

	// recommendation table create sql statement
	private static final String RECOMM_CREATE = "create table " + TABLE_RECOM
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_RECOM_ID + " integer not null," + COLUMN_RECOM
			+ " text not null," + COLUMN_ISREAD
			+ " integer not null default false," + COLUMN_URL + " text,"
			+ COLUMN_DATE + " text not null" + ");";

	public DBRecomHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(RECOMM_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOM);
		onCreate(db);
	}
}