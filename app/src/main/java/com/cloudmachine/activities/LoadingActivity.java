package com.cloudmachine.activities;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.chart.utils.AppLog;
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
        Uri uriData = getIntent().getData();
        String autority = null;
        if (uriData != null) {
            autority = uriData.getAuthority();
        }
        AppLog.print("loadig Act autoriy____"+autority);
        mHandler = new Handler(this);
        if (!TextUtils.isEmpty(autority)) {
            Message message=new Message();
            message.what=Constants.HANDLER_H5_JUMP;
            message.obj=autority;
            mHandler.sendMessageDelayed(message, 500);
        } else {
            mHandler.sendEmptyMessageDelayed(Constants.HANDLER_TIMER, 500);
        }
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
            case Constants.HANDLER_H5_JUMP:
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.KEY_H5_AUTORITY,(String) msg.obj);
                Constants.toActivity(this, HomeActivity.class, bundle);
                finish();
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