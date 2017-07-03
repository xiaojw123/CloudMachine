package com.github.mikephil.charting.utils;

import android.util.Log;

/**
 * Created by xiaojw on 2017/4/24.
 */

public class AppLog {
    private static final boolean isOnline = false;
    private static final String LOG_TAG="cloudm";

    public static void print(String message) {
        if (!isOnline) {
            Log.d(LOG_TAG, message);
        }
    }

    public static void printError(String message) {
        if (!isOnline) {
            Log.d(LOG_TAG, message);
        }

    }
}
