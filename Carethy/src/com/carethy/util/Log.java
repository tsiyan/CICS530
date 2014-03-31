package com.carethy.util;

import com.carethy.R;
import com.carethy.activity.MainActivity;

import android.os.SystemClock;
import android.util.Config;

public class Log {
    public final static String LOGTAG = "Carethy";

    @SuppressWarnings("deprecation")
	public static final boolean LOGV = MainActivity.DEBUG ? Config.LOGD : Config.LOGV;

    public static void v(String logMe) {
        android.util.Log.v(LOGTAG, /* SystemClock.uptimeMillis() + " " + */ logMe);
    }

    public static void e(String logMe) {
        android.util.Log.e(LOGTAG, logMe);
    }

    static void e(String logMe, Exception ex) {
        android.util.Log.e(LOGTAG, logMe, ex);
    }
}
