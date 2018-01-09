package com.homemadebazar.util;

import android.util.Log;

/**
 * Created by atulraj on 23/12/17.
 */

public class LogUtils {
    private static boolean isDebug = true;

    public static void d(String tag, String message) {
        if (isDebug)
            Log.d(tag, message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }
}
