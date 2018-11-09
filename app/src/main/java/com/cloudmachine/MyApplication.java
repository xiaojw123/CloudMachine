package com.cloudmachine;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.ScreenInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.CustomActivityManager;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.photo.util.Res;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.litepal.LitePalApplication;

import java.io.InputStream;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    private static MyApplication baseApplication;
    public static Context mContext;
    private boolean isLogin;
    private boolean isFlag = false;
    private Member tempMember;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public Member getTempMember() {
        return tempMember;
    }

    public void setTempMember(Member tempMember) {
        this.tempMember = tempMember;
    }


    public boolean isFlag() {
        return isFlag;
    }

    public void setFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }


    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public static ImageLoader imageLoader;

    private static MyApplication mApplication;

    public synchronized static MyApplication getInstance() {
        return mApplication;
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLog.print("MyApplication crated at 2017-11-13, no y");
        initSocialConfig();
        mContext = this;
//        initSopfix();
       /* Glide.get(this).register(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(*//*RetrofitUtils.getOkHttpClient())*//*);*/
        //baseApplication赋值
        baseApplication = this;
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
        Res.init(this);
        Constants.photoBimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.icon_addpic_nm);
        DisplayMetrics dm = this.getApplicationContext().getResources().getDisplayMetrics();
        ScreenInfo.screen_width = dm.widthPixels;
        ScreenInfo.screen_height = dm.heightPixels;
        ScreenInfo.screen_density = dm.density;
        ScreenInfo.screen_densityDpi = dm.densityDpi;

        //		Utils.MyLog(ScreenInfo.screen_height+"*"+ScreenInfo.screen_width+":"+ScreenInfo.screen_density
        //				+":"+ScreenInfo.screen_densityDpi);

        mApplication = this;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new UsingFreqLimitedMemoryCache(1 * 1024 * 1024))
                .discCacheFileCount(60)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        //		CrashHandler.getInstance().init(this);
        ResV.init(this);
        initDB();
        initLocData();
        registerActivityLifecycleCallbacks(lifecycleCallback);
    }

    private void initLocData() {
        UserHelper.TOKEN = UserHelper.getValue(this, Constants.KEY_TOKEN);
        UserHelper.ID = UserHelper.getValue(this, Constants.KEY_ID);
        if (BuildConfig.DEBUG) {
            ApiConstants.initHost(this);
        }
    }

    private void initDB() {
        LitePalApplication.initialize(this);
    }

    private void initSocialConfig() {
        Config.DEBUG = true;
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wxfb6afbcc23f867df", "3c69a7395f5e54009accf1e1194d553c");
        PlatformConfig.setQQZone("1105584331", "2KIceJS92PMlkFKw");
        PlatformConfig.setSinaWeibo("41475887", "9da46957176d1e1e360c1252f54e94a9", "http://www.cloudm.com/");
    }


    private void initSopfix() {
// initialize最好放在attachBaseContext最前面
//        SophixManager.getInstance().setContext(this)
//                .setAppVersion(VersionU.getVersionName())
//                .setAesKey(null)
//                .setEnableDebug(false)
//                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
//                    @Override
//                    public void onLoad(int mode, int code, String info, int handlePatchVersion) {
//                        // 补丁加载回调通知
//                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
//                            // 表明补丁加载成功
//                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
//                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
//                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
//                        } else {
//                            SophixManager.getInstance().cleanPatches();
//                            // 其它错误信息, 查看PatchStatus类说明
//                        }
//
//                    }
//                }).initialize();
//// queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
//        SophixManager.getInstance().queryAndLoadNewPatch();

    }

    /**
     * 监测网络状态的状态
     *
     * @return if can connection the internet,return true
     */
    public boolean isOpenNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }


    public static Context getAppContext() {
        return baseApplication;
    }

    private ActivityLifecycleCallbacks lifecycleCallback = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            CustomActivityManager.getInstance().setTopActivity(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

}
