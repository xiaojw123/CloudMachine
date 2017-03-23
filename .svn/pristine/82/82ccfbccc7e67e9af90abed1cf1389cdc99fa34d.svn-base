package com.cloudmachine.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * 时间工具类
 * 
 * @author way
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	public static String converTime(long time) {
		long currentSeconds = System.currentTimeMillis() / 1000;
		long timeGap = currentSeconds - time;// 与现在时间相差秒数
		String timeStr = null;
		if (timeGap > 24 * 2 * 60 * 60) {// 2天以上就返回标准时间
			timeStr = getDayTime(time * 1000) + " " + getMinTime(time * 1000);
		} else if (timeGap > 24 * 60 * 60) {// 1天-2天
			timeStr = timeGap / (24 * 60 * 60) + "天前";
		} else if (timeGap > 60 * 60) {// 1小时-24小时
			timeStr = timeGap / (60 * 60) + "小时前";
		} else if (timeGap > 60) {// 1分钟-59分钟
			timeStr = timeGap / 60 + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}

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
