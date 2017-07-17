package com.cloudmachine.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.struc.News;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 */
public class LoadingActivity extends BaseAutoLayoutActivity implements Callback {


    private Handler mHandler;
    private List<News> displaylist = new ArrayList<News>();
    private boolean isGetData, isTimeOut;
    private Timer myTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater()
                .inflate(R.layout.activity_loading, null);
        setContentView(view);

        mHandler = new Handler(this);
//		new NewListAsync("1","0",this, mHandler).execute();
        myTimer = new Timer(true);
        myTimer.schedule(new ScanningTimerTask(), 500);                    //定时器每隔500毫秒发送一条指定的消息
        if (MemeberKeeper.getOauth(this) != null) {
            JPushInterface.setAliasAndTags(getApplicationContext(), MemeberKeeper.getOauth(this).getId().toString(), null, null);
        }                                                                //极光推送
        /*// 渐变
        AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				isTimeOut = true;
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});*/
    }

    @Override
    public void initPresenter() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case Constants.HANDLER_TIMER:
                isTimeOut = true;
                redirectTo();
                break;
            case Constants.HANDLER_GETCOMMUNITYLIST_SUCCESS:
                displaylist = (List<News>) msg.obj;
                isGetData = true;
                redirectTo();
                break;
            case Constants.HANDLER_GETCOMMUNITYLIST_FAIL:
                isGetData = true;
                break;
            case Constants.HANDLER_SWITCH_MAINACTIVITY:
                Intent intent = null;
                intent = new Intent(this, HomeActivity.class);
                Bundle bundle = new Bundle();
                ArrayList list = new ArrayList();
                list.add(displaylist);
                bundle.putParcelableArrayList("list", list);
                intent.putExtras(bundle);
        /*	SharedPreferences preferences = getSharedPreferences("login",
                    Context.MODE_PRIVATE);
			int i = preferences.getInt("isLogin", 0);
			double versionName = VersionU.getVersionName(this);
			String saveVersion = preferences.getString("version", "1.0");
			if(versionName>Double.parseDouble(saveVersion)){
				intent = new Intent(this, GuideActivity.class);
			}else{
				intent = new Intent(this, LoginActivity.class);
			}*/
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

                break;
            case Constants.HANDLER_SWITCH_GUIDACTIVITY:
                Bundle b = new Bundle();
                ArrayList l2 = new ArrayList();
                l2.add(displaylist);
                b.putParcelableArrayList("list", l2);
                Constants.toActivity(this, GuideActivity.class, b);
                finish();
                break;
        }
        return false;
    }


    /**
     * 跳转
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void redirectTo() {
        if (/*isGetData &&*/ isTimeOut) {
            if (MySharedPreferences.getSharedPBoolean(Constants.KEY_isHomeGuide)) {
                mHandler.sendEmptyMessage(Constants.HANDLER_SWITCH_MAINACTIVITY);
            } else {
                MySharedPreferences.setSharedPBoolean(Constants.KEY_isHomeGuide, true);
                mHandler.sendEmptyMessage(Constants.HANDLER_SWITCH_GUIDACTIVITY);
            }
        }

    }

    //****************************************************************
    // 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
    //****************************************************************
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

    private boolean isFirstEnter(Context context, String className) {
        if (context == null || className == null || "".equalsIgnoreCase(className)) return false;
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE)
                .getString(KEY_GUIDE_ACTIVITY, "");//取得所有类名
        if (mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }

    class ScanningTimerTask extends TimerTask {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            message.what = Constants.HANDLER_TIMER;
            mHandler.sendMessage(message);
        }

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        JPushInterface.onResume(this);
        Constants.MyLog(getDeviceInfo(this));
        super.onResume();
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}