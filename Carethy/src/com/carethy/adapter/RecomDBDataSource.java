package com.carethy.adapter;

import java.util.ArrayList;
import java.util.List;

import com.carethy.database.DBRecomHelper;
import com.carethy.model.Recommendation;
import com.carethy.util.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RecomDBDataSource {

	private SQLiteDatabase database;
	private DBRecomHelper dbHelper;

	private static long last_id = 0;
	private static long latest_id = 0;

	private String[] allColumns = { DBRecomHelper.COLUMN_ID,
			DBRecomHelper.COLUMN_RECOM_ID, DBRecomHelper.COLUMN_RECOM,
			DBRecomHelper.COLUMN_ISREAD, DBRecomHelper.COLUMN_URL,
			DBRecomHelper.COLUMN_DATE, DBRecomHelper.COLUMN_SEVERITY };

	public RecomDBDataSource(Context context) {
		dbHelper = new DBRecomHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Recommendation insertIntoTable(int recom_id, String recom,
			String url, int severity) {

		ContentValues values = new ContentValues();

		values.put(DBRecomHelper.COLUMN_RECOM_ID, recom_id);
		values.put(DBRecomHelper.COLUMN_RECOM, recom);

		values.put(DBRecomHelper.COLUMN_URL, url);
		values.put(DBRecomHelper.COLUMN_DATE, Util.getDate());
		values.put(DBRecomHelper.COLUMN_SEVERITY, severity);

		long insertId = database
				.insert(DBRecomHelper.TABLE_RECOM, null, values);
		Cursor cursor = database.query(DBRecomHelper.TABLE_RECOM, allColumns,
				DBRecomHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		Recommendation newReco = cursorToCurrentRow(cursor);
		latest_id = newReco.getId();

		cursor.close();
		return newReco;
	}

	private Recommendation cursorToCurrentRow(Cursor cursor) {
		Recommendation recom = new Recommendation();
		recom.setId(cursor.getLong(0));
		recom.setRecomId(cursor.getInt(1));
		recom.setRecom(cursor.getString(2));
		recom.setIsRead(cursor.getInt(3) == 1 ? true : false);
		recom.setUrl(cursor.getString(4));
		recom.setSavedate(cursor.getString(5));
		recom.setSeverity(cursor.getInt(6));
		return recom;
	}

	public List<Recommendation> getRecommendations(String limit) {
		List<Recommendation> recommendations = new ArrayList<Recommendation>();

		Cursor cursor;

		if (limit.isEmpty()) {
			cursor = database.query(DBRecomHelper.TABLE_RECOM, allColumns,
					null, null, null, null, DBRecomHelper.ORDER_RECOM_BY);
		} else {
			cursor = database
					.query(DBRecomHelper.TABLE_RECOM, allColumns, null, null,
							null, null, DBRecomHelper.ORDER_RECOM_BY, limit);
		}

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Recommendation recom = cursorToCurrentRow(cursor);
			recommendations.add(recom);
			cursor.moveToNext();
		}

		// make sure to close the cursor
		cursor.close();
		return recommendations;
	}

	public List<Recommendation> getNewRecommendations() {

		List<Recommendation> recommendations = new ArrayList<Recommendation>();

		if (last_id < latest_id) {
			int limit = (int) (latest_id - last_id);
			recommendations = getRecommendations("" + limit);
			last_id = latest_id;
		}

		return recommendations;
	}

	public int getTopRecommendationId() {

		Cursor cursor = database.query(DBRecomHelper.TABLE_RECOM, allColumns,
				null, null, null, null, DBRecomHelper.ORDER_RECOM_BY, "1");

		if (cursor.moveToFirst()) {
			return cursorToCurrentRow(cursor).getRecomId();
		} else {
			return 0;
		}
	}

	public void setIsReadTrue(long id) {

		ContentValues values = new ContentValues();
		values.put(DBRecomHelper.COLUMN_ISREAD, true);

		System.out.println(""
				+ (database.update(DBRecomHelper.TABLE_RECOM, values,
						DBRecomHelper.COLUMN_ID + " = " + id, null)));
	}
}
