package com.cloudmachine.activities;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.AdBean;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.umeng.analytics.MobclickAgent;

import org.litepal.crud.DataSupport;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 */
public class LoadingActivity extends BaseAutoLayoutActivity implements Callback {
    @BindView(R.id.screen_adimg)
    ImageView screenAdimg;
    @BindView(R.id.screen_adtimer_tv)
    TextView screenAdtimerTv;
    @BindView(R.id.screen_ad_layout)
    FrameLayout screenAdLayout;
    @BindView(R.id.mnbv)
    ImageView mnbv;
    @BindView(R.id.loading_splash_layout)
    RelativeLayout loadingSplashLayout;
    private Handler mHandler;
    Timer mTimer;
    int timeCount;
    int mCode;
    Object mObject;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);

        Uri uriData = getIntent().getData();
        String autority = null;
        if (uriData != null) {
            autority = uriData.getAuthority();
        }
        AppLog.print("loadig Act autoriy____" + autority);
        mHandler = new Handler(this);
        if (!TextUtils.isEmpty(autority)) {

            Message message = new Message();
            message.what = Constants.HANDLER_H5_JUMP;
            message.obj = autority;
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
            case Constants.HANDLER_SWITCH_MAINACTIVITY:
            case Constants.HANDLER_SWITCH_GUIDACTIVITY:
                startAdTimer(msg.what, msg.obj);
                break;
        }
        return false;
    }

    public void startAdTimer(final int code, final Object object) {
        mCode = code;
        mObject = object;
        AdBean item = DataSupport.findFirst(AdBean.class);
        if (item != null) {
            AppLog.print(" timeStamp____" + item.getTimeStamp() + "____currentMill__" + System.currentTimeMillis());
            if (item.getDisplayType() == 1
                    || (item.getDisplayType() == 2 && (item.getTimeStamp() == null || (item.getTimeStamp() != null && !item.getTimeStamp().equals(CommonUtils.getDateStamp()))))
                    || item.getDisplayType() == 3) {
                if (item.getDisplayType() == 1) {
                    item.setDisplayType(0);
                }
                if (item.getDisplayType() == 2) {
                    item.setTimeStamp(CommonUtils.getDateStamp());
                    item.save();
                }
                timeCount = item.getAdTime();
                screenAdLayout.setVisibility(View.VISIBLE);
                loadingSplashLayout.setVisibility(View.GONE);
                Glide.with(mContext).load(item.getAdLink()).into(screenAdimg);
                screenAdimg.setTag(item.getOpenLink());
                screenAdtimerTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelTimer();
                        gotoNextPage(code, object);
                    }
                });
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                screenAdtimerTv.setText("跳过" + timeCount + "s");
                                if (timeCount <= 0) {
                                    mTimer.cancel();
                                    mTimer = null;
                                    gotoNextPage(code, object);
                                    return;
                                }
                                timeCount--;
                            }
                        });

                    }
                }, 0, 1000);
                return;
            }
        }
        gotoNextPage(code, object);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        gotoNextPage(mCode, mObject);
    }

    public void gotoNextPage(int code, Object obj) {
        switch (code) {
            case Constants.HANDLER_H5_JUMP:
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.KEY_H5_AUTORITY, (String) obj);
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


    @OnClick({R.id.screen_adimg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.screen_adimg:
                cancelTimer();
                Object obj = view.getTag();
                if (obj != null) {
                    MobclickAgent.onEvent(this, MobEvent.TIME_H5_START_AD);
                    String openUrl = (String) obj;
                    Bundle bundle = new Bundle();
                    bundle.putString(QuestionCommunityActivity.H5_URL, openUrl);
                    Constants.toActivity(this, QuestionCommunityActivity.class, bundle);
                }
                break;
        }
    }

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}