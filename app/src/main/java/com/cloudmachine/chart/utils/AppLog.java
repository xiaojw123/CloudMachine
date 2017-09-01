package com.cloudmachine.chart.utils;

import android.util.Log;

import com.cloudmachine.MyApplication;

/**
 * Created by xiaojw on 2017/4/24.
 */

public class AppLog {
    private static final String LOG_TAG="cloudm";

    public static void print(String message) {
        if (!MyApplication.IS_RELEASE) {
            Log.d(LOG_TAG, message);
        }
    }

    public static void printURl(String message) {
        if (!MyApplication.IS_RELEASE) {
            Log.d("OkHttp", message);
        }
    }

    public static void printError(String message) {
        if (!MyApplication.IS_RELEASE) {
            Log.d(LOG_TAG, message);
        }

    }
}
