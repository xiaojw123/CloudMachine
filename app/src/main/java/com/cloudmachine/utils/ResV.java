package com.cloudmachine.utils;

import android.content.Context;
import android.content.res.Resources;

public class ResV {
	// 文件路径名
//	private static String pkgName;
	// R文件的对象
	private static Resources resources;

	// 初始化文件夹路径和R资源
	public static void init(Context context) {
//		pkgName = context.getPackageName();
		resources = context.getResources();
	}
	
	public static String getString(int id){
		return resources.getString(id);
	}
}
