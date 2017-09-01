package com.cloudmachine.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cloudmachine.R;
import com.cloudmachine.bean.ScreenInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String TAG = "PushDemoActivity";
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";

    public static String logStringCache = "";
    
    public static DisplayImageOptions displayContentImageOptions  = new DisplayImageOptions.Builder()
	.showImageOnFail(R.drawable.mc_default_icon)
	.showImageForEmptyUri(R.drawable.mc_default_icon)
	.showImageOnFail(R.drawable.mc_default_icon)
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.displayer(new RoundedBitmapDisplayer(0))
	.build();
    
    public static DisplayImageOptions displayImageOptions  = new DisplayImageOptions.Builder()
	.showImageOnFail(R.drawable.ic_default_head)
	.showImageForEmptyUri(R.drawable.ic_default_head)
	.showImageOnFail(R.drawable.ic_default_head)
	.cacheInMemory(true)
	.cacheOnDisc(true)
	.displayer(new RoundedBitmapDisplayer(10))
	.build();

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    // 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
    public static boolean hasBind(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String flag = sp.getString("bind_flag", "");
        if ("ok".equalsIgnoreCase(flag)) {
            return true;
        }
        return false;
    }

    public static void setBind(Context context, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }
    

    public static void MyLog(String msg){
    	Log.e("Test", msg);
    }
   
    public static int dip2px( float dpValue){
	     return (int)(dpValue*ScreenInfo.screen_density + 0.5f);
	  }
    
    public static float countAngle(float n, float sum){
    	float angle = 0f;
    	angle = n/sum*270;
    	return angle;
    }
    
    public static int countDuration(float n, float sum){
    	return (int)(n/sum*4000);
    }
    public static void getViewHW(final View view,final Handler handler){
    	ViewTreeObserver vto2 = view.getViewTreeObserver();   
    	vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
    		@Override   
    		public void onGlobalLayout() { 
    			view.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
    			
    			Utils.MyLog("3:\n"+view.getHeight()+","+view.getWidth()); 
    			Message msg = Message.obtain();
    			msg.what = Constants.HANDLER_GETVIEWHW;
    			msg.arg1 = view.getWidth();
    			msg.arg2 = view.getHeight();
    			handler.sendMessage(msg);
    		}   
    	});  
    }
    public static void initMeterImageHW(final View view){
    	ViewTreeObserver vto2 = view.getViewTreeObserver();   
    	vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
    		@Override   
    		public void onGlobalLayout() { 
    			view.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
    			
    			Utils.MyLog("3:\n"+view.getHeight()+","+view.getWidth()); 
    			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(view.getWidth(), view.getWidth());
    			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
    			view.setLayoutParams(lp);
    		}   
    	});  
    }
    public static void initViewHW_L(final View view){
        ViewTreeObserver vto2 = view.getViewTreeObserver();   
        vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
            @Override   
            public void onGlobalLayout() { 
            	view.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
            	
            	Utils.MyLog("3:\n"+view.getHeight()+","+view.getWidth()); 
            	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(view.getWidth(), view.getWidth());
            	view.setLayoutParams(lp);
            }   
        });  
    }
    

	
	public static Bitmap getBitmap(String imageUrl) {  
        Bitmap mBitmap = null;  
        try {  
            URL url = new URL(imageUrl);  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            InputStream is = conn.getInputStream();  
            mBitmap = BitmapFactory.decodeStream(is);  
  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        return mBitmap;  
    }  

	public static BasicNameValuePair addBasicValue(String name, String value){
		if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(value))
			return new BasicNameValuePair(name,value);
		return null;
	}
	
	public static String getPwdStr(String pwd){
			/*String md5Str = EncryptUtils.MD5(pwd);
			String baseStr = new String(Base64.encode(md5Str.getBytes(), Base64.DEFAULT));
			return baseStr;*/
		return pwd;
	}
	public static String getStrBase64(String str){
		return new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
	}
	
}
