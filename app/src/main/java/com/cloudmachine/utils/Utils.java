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



    public static int dip2px( float dpValue){
	     return (int)(dpValue*ScreenInfo.screen_density + 0.5f);
	  }


	
}
