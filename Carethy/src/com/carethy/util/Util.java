package com.carethy.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

import com.carethy.application.Carethy.BodyData;
import com.carethy.model.CarethyGraphData;
import com.carethy.model.Recommendation;
import com.jjoe64.graphview.GraphView.GraphViewData;

public class Util {
	public static Random rand = new Random();

	public static boolean saveImage(Bitmap imageData, String filename) {
		// get path to external storage (SD card)
		File storageDir = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "Carethy");

		// create storage directories, if they don't exist
		storageDir.mkdirs();

		try {
			String filePath = storageDir.toString() + File.separator + filename;
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			imageData.compress(CompressFormat.PNG, 100, bos);

			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		}

		return true;
	}

	public static String getTimestamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.CANADA);
		return sdf.format(date);
	}

	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
		return sdf.format(date);
	}

	public static ArrayList<CarethyGraphData> fetchData(BodyData mBodyData) {
		int count = 10;
		long now = new Date().getTime();
		ArrayList<CarethyGraphData> result = new ArrayList<CarethyGraphData>();

		GraphViewData[] timeSeries = new GraphViewData[count];
		for (int i = 0; i < count; i++) {
			timeSeries[i] = new GraphViewData(now + (i * 60 * 60 * 24 * 1000),
					rand.nextInt(20));
		}

		switch (mBodyData) {
		case activities:
			result.add(new CarethyGraphData("Duration", timeSeries));
			break;

		case bloodPressure:
			result.add(new CarethyGraphData("Systolic", timeSeries));

			GraphViewData[] mGraphViewData1 = new GraphViewData[count];
			for (int i = 0; i < count; i++) {
				mGraphViewData1[i] = new GraphViewData(now
						+ (i * 60 * 60 * 24 * 1000), rand.nextInt(20));

			}
			result.add(new CarethyGraphData("Diastolic", mGraphViewData1));
			break;

		case heartBeats:
			result.add(new CarethyGraphData("Counts", timeSeries));
			break;

		case sleep:
			result.add(new CarethyGraphData("minutesAsleep", timeSeries));
			break;

		default:
			break;
		}

		return result;
	}

	public static int getActivitiesData() {
		return Math.abs(rand.nextInt(100));
	}

	public static int getSleepData() {
		return Math.abs(rand.nextInt(500));
	}

	public static int getHeartBeatsData() {
		return Math.abs(rand.nextInt(150));
	}

	public static int[] getBloodPressuresData() {
		return new int[] { rand.nextInt(200), rand.nextInt(100) };
	}

	public static ArrayList<Recommendation> getRecommendation() {
		ArrayList<Recommendation> list = new ArrayList<Recommendation>();
		Recommendation r0 = new Recommendation(0, 0, "recom0");
		Recommendation r1 = new Recommendation(1, 1, "recom1");
		Recommendation r2 = new Recommendation(2, 1, "recom2");
		Recommendation r3 = new Recommendation(3, 0, "recom3");
		Recommendation r4 = new Recommendation(4, 1, "recom4");
		Recommendation r5 = new Recommendation(5, 0, "recom5");
		list.add(r0);
		list.add(r1);
		list.add(r2);
		list.add(r3);
		list.add(r4);
		list.add(r5);

		return list;
	}

}
