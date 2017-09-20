package com.cloudmachine.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.AutoLayoutActivity;
import com.cloudmachine.base.baserx.RxManager;
import com.cloudmachine.utils.AppManager;
import com.cloudmachine.utils.LoadingDialog;
import com.cloudmachine.utils.StatusBarCompat;
import com.cloudmachine.utils.TUtil;
import com.cloudmachine.utils.UMListUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

public abstract class BaseAutoLayoutActivity<T extends BasePresenter, E extends BaseModel> extends AutoLayoutActivity {

    public RxManager mRxManager;
    public T mPresenter;
    public E mModel;
    public Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        UMListUtil.getUMListUtil().sendStruEvent(this.getClass().getSimpleName(), this);//发送结构化事件
        super.onCreate(savedInstanceState);
        //初始化状态栏系列事件
        doBeforeSetcontentView();
        //初始化Rx管理器
        mRxManager = new RxManager();
        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }
        this.initPresenter();
        AppManager.getAppManager().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    /*********************子类实现*****************************/
    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    private void doBeforeSetcontentView() {

        //setStatusbarColor("#169ADA");
        // 默认着色状态栏
        SetStatusBarColor();
    }

    private void SetStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            AppLog.printError("SetStatusBarColor___");
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            // 激活状态栏设置
//            tintManager.setStatusBarTintEnabled(true);
//            // 使用颜色资源
//            tintManager.setStatusBarTintResource(R.color.main_color);
//            FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
//            LinearLayout container = (LinearLayout) decorView.getChildAt(0);
//            FrameLayout cotentContainer = (FrameLayout) container.getChildAt(1);
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cotentContainer.getLayoutParams();
//            params.topMargin = 50;
//        }

		StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.cor10));
    }

    public void setStatusbarColor(String rgb) {
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        } else {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);

            // 自定义颜色
            tintManager.setTintColor(Color.parseColor(rgb));
        }


    }

    @Override
    protected void onResume() {
        UMListUtil.getUMListUtil().startCustomEvent(this);//页面时长统计开始
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        UMListUtil.getUMListUtil().endCustomEvent(this);//页面时长统计结束
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        UMListUtil.getUMListUtil().removeList(this.getClass().getSimpleName());
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.onDestroy();
        mRxManager.clear();
        AppManager.getAppManager().removeActivity(this);//页面销毁,集合移除
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 开启浮动加载进度条
     */
    public void startProgressDialog() {
        LoadingDialog.showDialogForLoading(this);
    }

    /**
     * 开启浮动加载进度条
     *
     * @param msg
     */
    public void startProgressDialog(String msg) {
        LoadingDialog.showDialogForLoading(this, msg, true);
    }

    /**
     * 停止浮动加载进度条
     */
    public void stopProgressDialog() {
        LoadingDialog.cancelDialogForLoading();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}





























/*Set<Map.Entry<String, String>> stru1Entry = UMListUtil.stru1Map.entrySet();
        Set<Map.Entry<String, String>> stru2Entry = UMListUtil.stru2Map.entrySet();*//*

for (int i = 0 ;i < mActivityStack.size();i++) {
		for (Map.Entry<String, String> stru1entry : stru1Entry) {
		if (mActivityStack.get(i).getClass().getSimpleName().equals(stru1entry.getKey())) {
		UMListUtil.stru1.add(stru1entry.getValue());
		}
		}
		for (Map.Entry<String, String> stru2entry : stru2Entry
		) {
		if (mActivityStack.get(i).getClass().getSimpleName().equals(stru2entry.getKey())) {
		UMListUtil.stru2.add(stru2entry.getValue());
		}
		}
		}
		MobclickAgent.onEvent(this,UMListUtil.stru1,1,"");
		MobclickAgent.onEvent(this,UMListUtil.stru2,1,"");*/


/*

Constants.MyLog(getClass().getSimpleName());
		Constants.MyLog("获取到信息");
		mCustomMap = UMListUtil.customMap;
		Set<Map.Entry<String, String>> entrySet = mCustomMap.entrySet();
		for (Map.Entry<String,String> entry: entrySet
		) {
		if (entry.getKey().equals(getClass().getSimpleName())) {
		MobclickAgent.onPageStart(entry.getValue());
		}
		}*/
