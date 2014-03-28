package com.carethy.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MedicationTable {
	
	 // Database table
	  public static final String TABLE_MEDICATION = "medication";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_MEDICINE = "medicine";

	  // Database creation SQL statement
	  private static final String DATABASE_CREATE = "create table " 
	      + TABLE_MEDICATION
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_MEDICINE + " text not null" 
	      + ");";

	  public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(MedicationTable.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICATION);
	    onCreate(database);
	  }
}
