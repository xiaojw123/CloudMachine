package com.cloudmachine.helper;

import android.app.Activity;

import java.lang.ref.WeakReference;

public class CustomActivityManager {

    private static CustomActivityManager customActivityManager = new CustomActivityManager();
    private WeakReference<Activity> topActivity;

    private CustomActivityManager() {

    }

    public static CustomActivityManager getInstance(){
        return customActivityManager;
    }

    public Activity getTopActivity() {
        if (topActivity!=null){
            return topActivity.get();
        }
        return null;
    }

    public void setTopActivity(Activity topActivity) {
        this.topActivity = new WeakReference<>(topActivity);
    }

}
