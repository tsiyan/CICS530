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

import com.carethy.model.Recommendation;

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

	public static double[] fetchData() {

		int count = 30;
		double[] values = new double[count];
		for (int i = 0; i < values.length; i++) {
			values[i] = Math.sin(i * (rand.nextDouble() * 0.1 + 0.3) + 2);
		}

		return values;

	}

	public static int getActivityData() {
		return Math.abs(rand.nextInt() / 1000000);
	}

	public static float getSleepData() {
		return Math.abs(rand.nextFloat() * 10);
	}

	public static int getHeartRateData() {
		return Math.abs(rand.nextInt() / 1000000);
	}

	public static float getBloodPressureData() {
		return Math.abs(rand.nextFloat() * 1000);
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
