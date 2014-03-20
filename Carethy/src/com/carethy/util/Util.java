package com.carethy.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",Locale.US);
		return sdf.format(date);
	}

}
