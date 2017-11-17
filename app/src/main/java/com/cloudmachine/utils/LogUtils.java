package com.cloudmachine.utils;


import com.cloudmachine.BuildConfig;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * 如果用于android平台，将信息记录到“LogCat”。如果用于java平台，将信息记录到“Console”
 * 使用logger封装
 */
public class LogUtils {
    /**
     * 在application调用初始化
     */
    public static void logInit(boolean debug) {
        if (BuildConfig.DEBUG) {
            Logger.init(Constants.DEBUG_TAG)                 // default PRETTYLOGGER or use just init()
                    .methodCount(2)                 // default 2
                    .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                    .methodOffset(0);                // default 0
        } else {
            Logger.init()                 // default PRETTYLOGGER or use just init()
                    .methodCount(3)                 // default 2
                    .hideThreadInfo()               // default shown
                    .logLevel(LogLevel.NONE)        // default LogLevel.FULL
                    .methodOffset(2);
        }
    }
    public static void logd(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Logger.d(tag,message);
        }
    }
    public static void logd(String message) {
        if (BuildConfig.DEBUG) {
            Logger.d(message);
        }
    }
    public static void loge(Throwable throwable, String message, Object... args) {
        if (BuildConfig.DEBUG) {
            Logger.e(throwable, message, args);
        }
    }

    public static void loge(String message, Object... args) {
        if (BuildConfig.DEBUG) {
            Logger.e(message, args);
        }
    }

    public static void logi(String message, Object... args) {
        if (BuildConfig.DEBUG) {
            Logger.i(message, args);
        }
    }
    public static void logv(String message, Object... args) {
        if (BuildConfig.DEBUG) {
            Logger.v(message, args);
        }
    }
    public static void logw(String message, Object... args) {
        if (BuildConfig.DEBUG) {
            Logger.v(message, args);
        }
    }
    public static void logwtf(String message, Object... args) {
        if (BuildConfig.DEBUG) {
            Logger.wtf(message, args);
        }
    }

    public static void logjson(String message) {
        if (BuildConfig.DEBUG) {
            Logger.json(message);
        }
    }
    public static void logxml(String message) {
        if (BuildConfig.DEBUG) {
            Logger.xml(message);
        }
    }
}
