package com.carethy.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import com.carethy.application.Carethy;
import com.carethy.application.Carethy.BodyData;
import com.carethy.model.CarethyGraphData;
import com.carethy.model.Recommendation;
import com.jjoe64.graphview.GraphView.GraphViewData;

public class Util {
	public static Random rand = new Random();
	public static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd");

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
		int data_size = 0;
		ArrayList<CarethyGraphData> result = new ArrayList<CarethyGraphData>();

		switch (mBodyData) {
		case activities:
			List<Pair<Long, Integer>> activitiesList = getActivities();
			data_size = activitiesList.size() > count ? count : activitiesList
					.size();
			GraphViewData[] activitySeries = new GraphViewData[data_size];
			for (int i = 0; (i < data_size); i++) {
				activitySeries[i] = new GraphViewData(
						activitiesList.get(i).first,
						activitiesList.get(i).second);

				Log.e("util-send", activitiesList.get(i).first + " "
						+ activitiesList.get(i).second + " "
						+ activitySeries[i].valueX);
			}
			result.add(new CarethyGraphData("Duration", activitySeries));
			break;

		case bloodPressures:
			List<Pair<Long, Pair<Integer, Integer>>> bpList = getBloodPressures();
			data_size = bpList.size() > count ? count : bpList.size();

			GraphViewData[] systolicSeries = new GraphViewData[data_size];
			for (int i = 0; (i < data_size); i++) {
				systolicSeries[i] = new GraphViewData(bpList.get(i).first,
						bpList.get(i).second.first);
			}
			result.add(new CarethyGraphData("Systolic", systolicSeries));
			
			GraphViewData[] diastolicSeries = new GraphViewData[data_size];
			for (int i = 0; i < data_size; i++) {
				diastolicSeries[i] = new GraphViewData(bpList.get(i).first,
						bpList.get(i).second.second);
			}
			result.add(new CarethyGraphData("Diastolic", diastolicSeries));

			break;

		case heartBeats:
			List<Pair<Long, Integer>> hbList = getHeartBeats();
			data_size = hbList.size() > count ? count : hbList.size();
			GraphViewData[] heartbeatSeries = new GraphViewData[data_size];
			for (int i = 0; (i < data_size); i++) {
				heartbeatSeries[i] = new GraphViewData(hbList.get(i).first,
						hbList.get(i).second);
			}
			result.add(new CarethyGraphData("Counts", heartbeatSeries));
			break;

		case sleep:
			List<Pair<Long, Integer>> sleepList = getSleep();
			data_size = sleepList.size() > count ? count : sleepList.size();
			GraphViewData[] sleepSeries = new GraphViewData[data_size];
			for (int i = 0; (i < data_size); i++) {
				sleepSeries[i] = new GraphViewData(sleepList.get(i).first,
						sleepList.get(i).second);
			}
			result.add(new CarethyGraphData("minutesAsleep", sleepSeries));
			break;

		default:
			break;
		}

		return result;
	}

	public static List<Pair<Long, Integer>> getActivities() {
		List<Pair<Long, Integer>> result = new ArrayList<Pair<Long, Integer>>();
		JSONParser parser = new JSONParser();
		long json_date = 0;
		int json_duration = 0;
		if (Carethy.user_data.length() > 0) {
			try {
				JSONObject jsonObject = (JSONObject) parser
						.parse(Carethy.user_data);
				JSONArray msg = (JSONArray) jsonObject.get("activities");
				Iterator iterator = msg.iterator();
				while (iterator.hasNext()) {
					JSONObject innerObj = (JSONObject) iterator.next();
					try {
						Date date = formatter.parse((String) innerObj
								.get("date"));
						json_date = date.getTime();
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
					json_duration = (innerObj.get("duration")) != null ? ((Long) innerObj
							.get("duration")).intValue() : 0;
					result.add(new Pair<Long, Integer>(json_date, json_duration));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static List<Pair<Long, Pair<Integer, Integer>>> getBloodPressures() {
		List<Pair<Long, Pair<Integer, Integer>>> result = new ArrayList<Pair<Long, Pair<Integer, Integer>>>();
		JSONParser parser = new JSONParser();
		long json_date = 0;
		int json_systolic = 0;
		int json_diastolic = 0;
		if (Carethy.user_data.length() > 0) {
			try {
				JSONObject jsonObject = (JSONObject) parser
						.parse(Carethy.user_data);
				JSONArray msg = (JSONArray) jsonObject.get("bloodPressures");
				Iterator iterator = msg.iterator();
				while (iterator.hasNext()) {
					JSONObject innerObj = (JSONObject) iterator.next();
					try {
						Date date = formatter.parse((String) innerObj
								.get("date"));
						json_date = date.getTime();
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
					json_systolic = (innerObj.get("systolic")) != null ? ((Long) innerObj
							.get("systolic")).intValue() : 0;
					json_diastolic = (innerObj.get("diastolic")) != null ? ((Long) innerObj
							.get("diastolic")).intValue() : 0;
					result.add(new Pair<Long, Pair<Integer, Integer>>(
							json_date, new Pair<Integer, Integer>(
									json_systolic, json_diastolic)));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static List<Pair<Long, Integer>> getHeartBeats() {
		List<Pair<Long, Integer>> result = new ArrayList<Pair<Long, Integer>>();
		JSONParser parser = new JSONParser();
		long json_date = 0;
		int json_duration = 0;
		if (Carethy.user_data.length() > 0) {
			try {
				JSONObject jsonObject = (JSONObject) parser
						.parse(Carethy.user_data);
				JSONArray msg = (JSONArray) jsonObject.get("heartBeats");
				Iterator iterator = msg.iterator();
				while (iterator.hasNext()) {
					JSONObject innerObj = (JSONObject) iterator.next();
					try {
						Date date = formatter.parse((String) innerObj
								.get("date"));
						json_date = date.getTime();
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
					json_duration = (innerObj.get("count")) != null ? ((Long) innerObj
							.get("count")).intValue() : 0;
					result.add(new Pair<Long, Integer>(json_date, json_duration));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static List<Pair<Long, Integer>> getSleep() {
		List<Pair<Long, Integer>> result = new ArrayList<Pair<Long, Integer>>();
		JSONParser parser = new JSONParser();
		long json_date = 0;
		int json_duration = 0;
		if (Carethy.user_data.length() > 0) {
			try {
				JSONObject jsonObject = (JSONObject) parser
						.parse(Carethy.user_data);
				JSONArray msg = (JSONArray) jsonObject.get("sleep");
				Iterator iterator = msg.iterator();
				while (iterator.hasNext()) {
					JSONObject innerObj = (JSONObject) iterator.next();
					try {
						Date date = formatter.parse((String) innerObj
								.get("date"));
						json_date = date.getTime();
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
					json_duration = (innerObj.get("minutesAsleep")) != null ? ((Long) innerObj
							.get("minutesAsleep")).intValue() : 0;
					result.add(new Pair<Long, Integer>(json_date, json_duration));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static int getActivitiesData() {
		List<Pair<Long, Integer>> list = getActivities();
		if (list.size() > 0) {
			return list.get(0).second;
		} else { // in case we somehow cannot get data from json file
			return Math.abs(rand.nextInt(100));
		}
	}

	public static int getSleepData() {
		List<Pair<Long, Integer>> list = getSleep();
		if (list.size() > 0) {
			return list.get(0).second;
		} else { // in case we somehow cannot get data from json file
			return Math.abs(rand.nextInt(500));
		}
	}

	public static int getHeartBeatsData() {
		List<Pair<Long, Integer>> list = getHeartBeats();
		if (list.size() > 0) {
			return list.get(0).second;
		} else { // in case we somehow cannot get data from json file
			return Math.abs(rand.nextInt(150));
		}
	}

	public static int[] getBloodPressuresData() {
		List<Pair<Long, Pair<Integer, Integer>>> list = getBloodPressures();
		if (list.size() > 0) {
			return new int[] { list.get(0).second.first,
					list.get(0).second.second };
		} else { // in case we somehow cannot get data from json file
			return new int[] { rand.nextInt(200), rand.nextInt(100) };
		}
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

	// returns if the user data file has changed
	public static boolean hasDataFileChanged() {
		if (Carethy.currentDataFileId == Carethy.nextDataFileId) {
			return false;
		}
		return true;
	}

}
