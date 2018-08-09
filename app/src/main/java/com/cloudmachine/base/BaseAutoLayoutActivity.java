package com.cloudmachine.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.adapter.PhotoAdapter;
import com.cloudmachine.autolayout.AutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxManager;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.AdBean;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.CustomActivityManager;
import com.cloudmachine.helper.DataSupportManager;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.AppManager;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LoadingDialog;
import com.cloudmachine.utils.StatusBarCompat;
import com.cloudmachine.utils.TUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.iwf.photopicker.PhotoPicker;
import rx.functions.Action1;

public abstract class BaseAutoLayoutActivity<T extends BasePresenter, E extends BaseModel> extends AutoLayoutActivity {
    public static final int REQ_CLEAR_WEBCACHE = 0x91;
    public static final int REQ_PIC_CAMERA = 0x92;
    public static final int REQ_GO_FACE = 0x93;
    PopupWindow depositPayPop, mAdPop;
    public RxManager mRxManager;
    public T mPresenter;
    public E mModel;
    public Context mContext;
    private boolean isOpen = true;
    private Timer mTimer;
    private TextView mTimerTv;
    private int timeCount;
    public boolean isForbidenAd;


    private Activity topAct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppLog.print("BaseAcon onCreate");
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

    protected  void registerObserverEvent(String eventName) {
        mRxManager.on(eventName, new Action1<Object>() {
            @Override
            public void call(Object result) {
                resultCallBack(result);
            }
        });

    }

    protected  void resultCallBack(Object result) {

    }

    protected String getPageType() {
        return getIntent().getStringExtra(Constants.PAGET_TYPE);
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

    public void setUPageStatistics(boolean isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppLog.print("BaseAc onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppLog.print("BaseAc onRestart");
    }

//    int time;
//    int curTime;
//    int count;
//    AdBean curAdBean;
//
//    //
//    public void showAd() {
//        final List<AdBean> adBeenList = DataSupport.findAll(AdBean.class);
//        for (AdBean bean : adBeenList) {
//            time += bean.getAdTime();
//        }
//        //displayType 4 b
//        curAdBean = adBeenList.get(0);
//        curTime = curAdBean.getAdTime();
//        if (curAdBean.getDisplayType()==1){
//
//        }
//
//        Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        time--;
//                        curTime--;
//                        if (time > 0) {
//                            if (curTime == 0) {
//                                count++;
//                                curAdBean = adBeenList.get(count);
//                                curTime = curAdBean.getAdTime();
////                            Glide.with(mContext).load(curAdBean.getAdTime()).into(i)
//                            }
//
//                        } else {
//                            //取消定时器
//                        }
//
//                    }
//                });
//
//            }
//        }, 1000);
//
//
//        List<AdBean> adBeens = new ArrayList<>();
//        List<AdBean> showAdBeans = new ArrayList<>();
//        for (AdBean bean : adBeens) {
//            if (bean.getYn() == 0) {
//                showAdBeans.add(bean);
//            }
//        }
//        DataSupport.saveAll(showAdBeans);
//
//    }

    @Override
    protected void onResume() {
        AppLog.print("BaseAc onResume");
        super.onResume();
        if (isOpen) {
            MobclickAgent.onPageStart(getClass().getName());
        }
        MobclickAgent.onResume(this);
        Activity curAct = CustomActivityManager.getInstance().getTopActivity();
        AppLog.print("curAct__" + curAct + ", topAct__" + topAct);
        if (topAct == curAct) {
            AppLog.print("CurActivity被唤醒");
            AdBean curAdBean = DataSupportManager.findFirst(AdBean.class);
            if (curAdBean != null) {
                timeCount = curAdBean.getAdTime();
                int type = curAdBean.getDisplayType();
                switch (type) {
                    case 1://只展示一次
                        curAdBean.setDisplayType(0);
                        curAdBean.save();
                        showAdPop(curAdBean.getAdLink(), curAdBean.getOpenLink());
                        break;
                    case 2:
                        AppLog.print("day of year___" + CommonUtils.getDateStamp());
                        if (curAdBean.getTimeStamp() != null) {
                            if (!curAdBean.getTimeStamp().equals(CommonUtils.getDateStamp())) {
                                curAdBean.setTimeStamp(CommonUtils.getDateStamp());
                                curAdBean.save();
                                showAdPop(curAdBean.getAdLink(), curAdBean.getOpenLink());
                            }
                        } else {
                            curAdBean.setTimeStamp(CommonUtils.getDateStamp());
                            curAdBean.save();
                            showAdPop(curAdBean.getAdLink(), curAdBean.getOpenLink());
                        }
                        break;
                    case 4:
                        showAdPop(curAdBean.getAdLink(), curAdBean.getOpenLink());
                        break;

                }
            }
            mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).getStartAd().compose(RxHelper.<List<AdBean>>handleResult()).subscribe(new RxSubscriber<List<AdBean>>(mContext) {
                @Override
                protected void _onNext(List<AdBean> remoteAdBeenList) {
                    AdBean locAdBean = DataSupportManager.findFirst(AdBean.class);
                    if (remoteAdBeenList != null && remoteAdBeenList.size() > 0) {
                        AdBean remoteAdBean = remoteAdBeenList.get(0);
                        if (remoteAdBean != null) {
                            int remoteType = remoteAdBean.getDisplayType();
                            if (remoteType != 0) {
                                if (locAdBean != null) {
                                    int locType = locAdBean.getDisplayType();
                                    if ((locType == 0 && remoteType == 1) || (locType == 2 && remoteType == 2)) {
                                        String locLink = locAdBean.getAdLink();
                                        if (locLink != null && !locLink.equals(remoteAdBean.getAdLink())) {
                                            DataSupport.deleteAll(AdBean.class);
                                            remoteAdBean.save();
                                            Glide.with(mContext).load(remoteAdBean.getAdLink());
                                        }
                                        return;
                                    }
                                    DataSupport.deleteAll(AdBean.class);
                                }
                                remoteAdBean.save();
                                Glide.with(mContext).load(remoteAdBean.getAdLink());
                                return;
                            }
                        }
                    }
                    if (locAdBean != null) {
                        DataSupport.deleteAll(AdBean.class);
                    }
                }

                @Override
                protected void _onError(String message) {

                }
            }));

        }


        AppLog.print("这是真的吗");


    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLog.print("BaseAc onPause");
        if (isOpen) {
            MobclickAgent.onPageEnd(getClass().getName());
        }
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLog.print("BaseAct onStop__" + isForbidenAd);
        if (!isForbidenAd) {
            topAct = CustomActivityManager.getInstance().getTopActivity();
        } else {
            isForbidenAd = false;
            topAct = null;
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        closeAdPop();
        super.onDestroy();
        AppLog.print("BaseAct onDestroy__");
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
        switch (requestCode) {
            case REQ_CLEAR_WEBCACHE://清理H5缓存
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    CommonUtils.showPermissionDialog(this, Constants.PermissionType.STORAGE);
                } else {
                    clearWebCahe();
                }
                break;


        }

    }


    // TODO: 2017/12/18 全屏广告页
    public void showAdPop(String adLink, final String openLink) {
        if (mAdPop == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.pop_screen_add, null);
            ImageView img = (ImageView) contentView.findViewById(R.id.screen_adimg);
            mTimerTv = (TextView) contentView.findViewById(R.id.screen_adtimer_tv);
            Glide.with(mContext).load(adLink).into(img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeAdPop();
                    Bundle db = new Bundle();
                    db.putString(QuestionCommunityActivity.H5_URL, openLink);
                    Constants.toActivity(BaseAutoLayoutActivity.this, QuestionCommunityActivity.class, db);
                }
            });
            mTimerTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeAdPop();
                }
            });
            mAdPop = CommonUtils.getAnimPop(contentView);
            mAdPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer = null;
                    }
                }
            });
        }
        mAdPop.showAtLocation(getWindow().getDecorView(), Gravity.FILL, 0, 0);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTimerTv.setText("跳过" + timeCount + "S");
                        if (timeCount <= 0) {
                            closeAdPop();
                        }
                        timeCount--;
                    }
                });
            }
        }, 0, 1000);
    }

    //通知拉起H5页云盒子支付
    public void showDepsitPayPop(final String url) {
        if (depositPayPop == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.pop_home_ad, null);
            View bgView = contentView.findViewById(R.id.home_pop_bg);
            ImageView img = (ImageView) contentView.findViewById(R.id.pop_ad_img);
            img.setImageResource(R.drawable.icon_deposit_pay);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDepoistPop();
                    Bundle db = new Bundle();
                    db.putString(QuestionCommunityActivity.H5_URL, url);
                    Constants.toActivity(BaseAutoLayoutActivity.this, QuestionCommunityActivity.class, db);
                }
            });
            ImageView closeImg = (ImageView) contentView.findViewById(R.id.pop_ad_close_img);
            closeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDepoistPop();
                }
            });
            bgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDepoistPop();
                }
            });
            depositPayPop = CommonUtils.getAnimPop(contentView);
        }
        depositPayPop.showAtLocation(getWindow().getDecorView(), Gravity.FILL, 0, 0);

    }

    public void closeDepoistPop() {
        if (depositPayPop != null && depositPayPop.isShowing()) {
            depositPayPop.dismiss();
        }
    }

    public void closeAdPop() {
        if (mAdPop != null && mAdPop.isShowing()) {
            mAdPop.dismiss();
        }
    }


    public void clearWebCahe() {
        AppLog.print("clear webview cache data__");
        try {
            // 清除cookie即可彻底清除缓存
            CookieSyncManager.createInstance(mContext);
            CookieManager.getInstance().removeAllCookie();
            // WebView 缓存文件
            File appCacheDir = new File(mContext.getCacheDir().getAbsolutePath() + "/webviewCache");

            if (appCacheDir.exists()) {
                deleteFile(appCacheDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }
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
