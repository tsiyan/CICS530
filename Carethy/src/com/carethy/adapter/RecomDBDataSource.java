package com.carethy.adapter;

import java.util.ArrayList;
import java.util.List;

import com.carethy.model.Recommendation;
import com.carethy.util.DBRecomHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RecomDBDataSource {

	private SQLiteDatabase database;
	private DBRecomHelper dbHelper;
	private String[] allColumns = { DBRecomHelper.COLUMN_ID,
			DBRecomHelper.COLUMN_RECOM_ID, DBRecomHelper.COLUMN_RECOM };

	public RecomDBDataSource(Context context) {
		dbHelper = new DBRecomHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Recommendation insertIntoTable(int recom_id, String recom) {
		ContentValues values = new ContentValues();
		values.put(DBRecomHelper.COLUMN_RECOM_ID, recom_id);
		values.put(DBRecomHelper.COLUMN_RECOM, recom);

		long insertId = database
				.insert(DBRecomHelper.TABLE_RECOM, null, values);
		Cursor cursor = database.query(DBRecomHelper.TABLE_RECOM, allColumns,
				DBRecomHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		Recommendation newReco = cursorToCurrentRow(cursor);

		cursor.close();
		return newReco;
	}

	private Recommendation cursorToCurrentRow(Cursor cursor) {
		Recommendation recom = new Recommendation();
		recom.setId(cursor.getLong(0));
		recom.setRecomId(cursor.getInt(1));
		recom.setRecom(cursor.getString(2));
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

}
