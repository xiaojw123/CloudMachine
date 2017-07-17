package com.cloudmachine.utils.mpchart;

public class ValueFormatUtil {

//	public static final String getWorkTime(float value,String defaultStr){
//		if(value>0){
//			int h = (int)value;
//			int m = (int)((value-h)*60);
//			if(h>0){
//				if(m>0)
//					return h+ResV.getString(R.string.work_time_text2)+m+ResV.getString(R.string.work_time_text3);
//				else
//					return h+ResV.getString(R.string.work_time_text2);
//			}else{
//				return m+ResV.getString(R.string.work_time_text3);
//			}
//
//		}else{
//			return defaultStr;//mFormat.format(value);
//		}
//	}


	public static  String getWorkTime(float value,String defaultStr){
		if(value>0){
			int h = (int)value;
			int m = Math.round(( value-h)*60);
			if(h>0){
				if(m>0)
					return h+"时"+m+"分";
				else
					return h+"时";
			}else{
				return m+"分";
			}

		}else{
			return defaultStr;//mFormat.format(value);
		}
	}
	
}
