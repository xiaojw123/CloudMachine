package com.cloudmachine.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author way
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {



	public static String getChatTime(long time) {
		return getPrefix(time) + getMinTime(time);
	}

	public static String getPrefix(long time) {
		long currentSeconds = System.currentTimeMillis() / 1000;
		long timeGap = currentSeconds - time / 1000;// 与现在时间相差秒数
		String prefix = "";
		if (timeGap > 24 * 2 * 60 * 60) {
			prefix = getDayTime(time * 1000) + " ";
		} else if (timeGap > 24 * 2 * 60 * 60) {
			prefix = "前天 ";
		} else if (timeGap > 24 * 60 * 60) {
			prefix = "昨天 ";
		} else {
			prefix = "今天 ";
		}
		return prefix;
	}

	public static String getDayTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("YY-MM-dd");
		return format.format(new Date(time));
	}

	public static String getMinTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}
}
