package com.cloudmachine.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.cloudmachine.R;
import com.cloudmachine.base.baserx.RxManager;
import com.cloudmachine.utils.AppManager;
import com.cloudmachine.utils.LoadingDialog;
import com.cloudmachine.utils.StatusBarCompat;
import com.cloudmachine.utils.TUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

public abstract class SupportBaseAutoActivity<T extends BasePresenter, E extends BaseModel> extends SupportAutoLayoutActivity {

    public RxManager mRxManager;
    public T mPresenter;
    public E mModel;
    public Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
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
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
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


}
