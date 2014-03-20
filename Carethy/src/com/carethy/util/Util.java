package com.carethy.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class Util {
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.CANADA);
		return sdf.format(date);
	}

	public static double[] fetchData(){
		Random rand = new Random();

		int count = 30;
		double[] values = new double[count];
		for (int i = 0; i < values.length; i++) {
			values[i] = Math.sin(i * (rand.nextDouble() * 0.1 + 0.3) + 2);
		}
		
		return values;
		
	}
}
