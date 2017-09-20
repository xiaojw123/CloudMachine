package com.cloudmachine.activities;


import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;

import cn.jpush.android.api.JPushInterface;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 */
public class LoadingActivity extends BaseAutoLayoutActivity implements Callback {
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(Constants.HANDLER_TIMER, 500);
        if (MemeberKeeper.getOauth(this) != null) {
            JPushInterface.setAliasAndTags(getApplicationContext(), MemeberKeeper.getOauth(this).getId().toString(), null, null);
        }                                                                //极光推送
    }

    @Override
    public void initPresenter() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_TIMER:
                redirectTo();
                break;
            case Constants.HANDLER_SWITCH_MAINACTIVITY:
                Constants.toActivity(this, HomeActivity.class, null);
                finish();
                break;
            case Constants.HANDLER_SWITCH_GUIDACTIVITY:
                Constants.toActivity(this, GuideActivity.class, null);
                finish();
                break;
        }
        return false;
    }


    private void redirectTo() {
        if (MySharedPreferences.getSharedPBoolean(Constants.KEY_isHomeGuide)) {
            mHandler.sendEmptyMessage(Constants.HANDLER_SWITCH_MAINACTIVITY);
        } else {
            MySharedPreferences.setSharedPBoolean(Constants.KEY_isHomeGuide, true);
            mHandler.sendEmptyMessage(Constants.HANDLER_SWITCH_GUIDACTIVITY);
        }

    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }


}