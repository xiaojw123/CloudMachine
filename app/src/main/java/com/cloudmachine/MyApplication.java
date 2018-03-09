package com.cloudmachine;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.ScreenInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.CustomActivityManager;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LogUtils;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.photo.util.Res;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.litepal.LitePalApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;

import cn.jpush.android.api.JPushInterface;
/*正式环境
* UMENT_APPKEY修改
* 日志管理 IS_RELEASE true关闭*/
public class MyApplication extends Application {

    private static MyApplication baseApplication;
    public static Context mContext;

    private int pageSize = 10;
    private boolean isLogin;
    private boolean isFresh;
    private boolean isFlag = false;
    private int msgNum;
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

    public int getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public void setFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }

    private String catId;

    public boolean isFresh() {
        return isFresh;
    }

    public void setFresh(boolean isFresh) {
        this.isFresh = isFresh;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    private Hashtable<String, Object> memeryCache = new Hashtable<String, Object>();
    private static final int CACHE_TIME = 60 * 60000;//缓存失效时间

    public Hashtable<String, Object> getMemeryCache() {
        return memeryCache;
    }

    public void setMemeryCache(Hashtable<String, Object> memeryCache) {
        this.memeryCache = memeryCache;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public static ImageLoader imageLoader;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NETWORKTYPE_WAP = 1;
    /**
     * 2G网络
     */
    public static final int NETWORKTYPE_2G = 2;
    /**
     * 3G和3G以上网络
     */
    public static final int NETWORKTYPE_3G = 3;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI = 4;
    private static MyApplication mApplication;
    public static final boolean ENCRYPTED = true;

    public synchronized static MyApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLog.print("MyApplication crated at 2017-11-13, no y");
        initSocialConfig();
        this.mContext = this;
//        initSopfix();
       /* Glide.get(this).register(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(*//*RetrofitUtils.getOkHttpClient())*//*);*/
        //baseApplication赋值
        baseApplication = this;
        //初始化Glide
        Glide.get(this).register(GlideUrl.class,
                InputStream.class,
                new OkHttpUrlLoader.Factory(new Api(HostType.HOST_CLOUDM_YJX).okHttpClient));
        //初始化logger
        LogUtils.logInit(/*BuildConfig.LOG_DEBUG*/true);
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
        registerActivityLifecycleCallbacks(lifecycleCallback);

    }

    private void initDB() {
        LitePalApplication.initialize(this);
    }

    private void initSocialConfig() {
        Config.DEBUG=true;
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

    /**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @param context
     * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G}, *
     * {@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}*
     * {@link #NETWORKTYPE_WIFI}
     */

    private static int getNetWorkType(Context context) {
        int mNetWorkType = -1;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();

                mNetWorkType = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G
                        : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }

        return mNetWorkType;
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

    /**
     * 判断缓存数据是否可读
     *
     * @param cachefile
     * @return
     */
    private boolean isReadDataCache(String cachefile) {
        return readObject(cachefile) != null;
    }

    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    private boolean isExistDataCache(String cachefile) {
        boolean exist = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 判断缓存是否失效
     *
     * @param cachefile
     * @return
     */
    public boolean isCacheDataFailure(String cachefile) {
        boolean failure = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }

    /**
     * 保存对象
     *
     * @param ser
     * @param file
     * @throws java.io.IOException
     */
    public boolean saveObject(Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput(file, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public Serializable readObject(String file) {
        if (!isExistDataCache(file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            //反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Context getAppContext() {
        return baseApplication;
    }

  private  ActivityLifecycleCallbacks lifecycleCallback=new ActivityLifecycleCallbacks() {
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