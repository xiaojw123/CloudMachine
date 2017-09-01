package com.cloudmachine.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.cloudmachine.MyApplication;

public class MySharedPreferences {
	
	public static final String SHAREDPREFERENCES_NAME = "com_cloudmachine";
	public static final String key_score_update_time = "key_score_update_time";
	public static final String key_user_image_ = "key_user_image_";
	//登录方式 0 云机械方式登录 ，1 微信登录
	public static final String key_login_type = "key_login_type";

	public static void setSharedPBoolean(String key, boolean b) {
		 SharedPreferences sp = MyApplication.mContext.getSharedPreferences(
				 SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		 SharedPreferences.Editor edit = sp.edit();
		 edit.putBoolean(key, b);
		 edit.commit();
	 }
	 
	 public static boolean getSharedPBoolean(String key) {
		 SharedPreferences sp = MyApplication.mContext.getSharedPreferences(
				 SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		 return sp.getBoolean(key, false);
		 
	 }
	 
	 public static void setSharedPString(String key, String str) {
		 SharedPreferences sp = MyApplication.mContext.getSharedPreferences(
				 SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		 SharedPreferences.Editor edit = sp.edit();
		 edit.putString(key, str);
		 edit.commit();
	 }
	 
	 public static String getSharedPString(String key) {
		 SharedPreferences sp = MyApplication.mContext.getSharedPreferences(
				 SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		 return sp.getString(key, null);
	 }
	 public static String getSharedPString(String key,String def) {
		 SharedPreferences sp = MyApplication.mContext.getSharedPreferences(
				 SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		 return sp.getString(key, def);
	 }
	 public static void setSharedPLong(String key, long id) {
		 SharedPreferences sp = MyApplication.mContext.getSharedPreferences(
				 SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		 SharedPreferences.Editor edit = sp.edit();
		 edit.putLong(key, id);
		 edit.commit();
	 }
	 
	 public static long getSharedPLong(String key) {
		 SharedPreferences sp = MyApplication.mContext.getSharedPreferences(
				 SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		 return sp.getLong(key, 0);
		 
	 }
	 public static void setSharedPInt(String key, int id) {
		 SharedPreferences sp = MyApplication.mContext.getSharedPreferences(
				 SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		 SharedPreferences.Editor edit = sp.edit();
		 edit.putInt(key, id);
		 edit.commit();
	 }

	public static long getSharedPInt(String key) {
		SharedPreferences sp = MyApplication.mContext.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key, 0);

	}
}
