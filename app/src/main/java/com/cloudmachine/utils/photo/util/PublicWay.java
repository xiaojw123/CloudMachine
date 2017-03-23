package com.cloudmachine.utils.photo.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.graphics.BitmapFactory;

/**
 * 存放所有的list在最后退出时一起关闭
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:50:49
 */
public class PublicWay {
	public static List<Activity> activityList = new ArrayList<Activity>();
	
	public static int num = 9;
	
	public static int getTempSelectNum(){
		int num = 0;
		int size = Bimp.tempSelectBitmap.size();
		for(int i=0; i<size; i++){
			if(Bimp.tempSelectBitmap.get(i).isSelected()){
				num++;
			}
		}
		return num;
	}
	
}
