package com.cloudmachine.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
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
import com.bumptech.glide.request.target.Target;
import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.autolayout.AutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxManager;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.AdBean;
import com.cloudmachine.bean.AuthInfoDetail;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.CustomActivityManager;
import com.cloudmachine.helper.DataSupportManager;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.activity.InfoAuthActivity;
import com.cloudmachine.ui.home.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.AppManager;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LoadingDialog;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.StatusBarCompat;
import com.cloudmachine.utils.TUtil;
import com.cloudmachine.utils.ToastUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import rx.functions.Action1;

public abstract class BaseAutoLayoutActivity<T extends BasePresenter, E extends BaseModel> extends AutoLayoutActivity {
    protected static final int AD_START = 1;//启动广告
    public static final int AD_ROLL = 2;//白条滚动广告
    public static final int AD_WINDOW = 3;//弹窗广告
    protected static final int AD_ACTIVITIES = 4;//活动中心广告
    public static final int REQ_CLEAR_WEBCACHE = 0x91;
    public static final int REQ_PIC_CAMERA = 0x92;
    public static final int REQ_GO_FACE = 0x93;
    public static final int REQ_CAMERA_PROOF = 0X94;
    public static final int RES_UPDATE_TIKCET = 0x110;
    public static final int RES_LOGIN_SUCCESS = 0x96;
    PopupWindow depositPayPop, mAdPop;
    public RxManager mRxManager;
    public T mPresenter;
    public E mModel;
    public Context mContext;
    private boolean isOpen = true;
    private Timer mTimer;
    private TextView mTimerTv;
    private int timeCount;
    protected String cmFilePath;
    protected int mActionType;
    public boolean isForbidenAd;

    private Activity topAct;
    protected PermissionsChecker mPerChecker;
    protected boolean isInfoUpdate;


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

    protected void getPersonalInfo(String uniqueId, int bnsType) {
        if (isNewAdd()) {
            return;
        }
        isInfoUpdate = true;
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getPersonalInformation(uniqueId, bnsType).compose(RxHelper.<AuthInfoDetail>handleResult()).subscribe(new RxSubscriber<AuthInfoDetail>(mContext) {
            @Override
            protected void _onNext(AuthInfoDetail infoDetail) {

                if (infoDetail != null) {
                    returnInfoDetail(infoDetail);
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

    public boolean isNewAdd() {
        return getIntent().getBooleanExtra(InfoAuthActivity.IS_NEW_ADD, true);
    }


    protected void returnInfoDetail(AuthInfoDetail infoDetail) {

    }

    protected void updatePersonalInfo(int bnsType, String uniqueId, String resideAddress, String income, String deviceId, String listUrl) {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).updatePersonalInformation(bnsType, uniqueId, resideAddress, income, deviceId, listUrl).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                ToastUtils.showToast(mContext, s);
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, "提交失败！");

            }
        }));

    }

    public boolean hasPermission(int reqCode, String... permissions) {
        if (mPerChecker == null) {
            mPerChecker = new PermissionsChecker(this);
        }
        if (mPerChecker.lacksPermissions(permissions)) {
            PermissionsActivity.startActivityForResult(this, reqCode,
                    permissions);
            return false;
        } else {
            return true;

        }
    }

    public boolean isGrandPermission(int resultCode, int permissionType) {
        if (resultCode == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            CommonUtils.showPermissionDialog(this, permissionType);
            return false;
        }


    }


    protected void registerObserverEvent(String eventName) {
        mRxManager.on(eventName, new Action1<Object>() {
            @Override
            public void call(Object result) {
                resultCallBack(result);
            }
        });

    }

    protected void resultCallBack(Object result) {

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
    protected void onResume() {
        super.onResume();
        if (isOpen) {
            MobclickAgent.onPageStart(getClass().getName());
        }
        MobclickAgent.onResume(this);
        showStartAd();
    }

    private void showStartAd() {
        Activity curAct = CustomActivityManager.getInstance().getTopActivity();
        AppLog.print("onresume___curAct__" + curAct + "__topAct__" + topAct);
        if (topAct == curAct) {
            AdBean curAdBean = DataSupportManager.findFirst(AdBean.class);
            if (curAdBean != null) {
                timeCount = curAdBean.getAdStopTime();
                int type = curAdBean.getDisplayType();
                switch (type) {
                    case 1://只展示一次
                        curAdBean.setDisplayType(0);
                        curAdBean.save();
                        showAdPop(curAdBean.getAdPicUrl(), curAdBean.getAdJumpLink());
                        break;
                    case 2:
                        AppLog.print("day of year___" + CommonUtils.getDateStamp());
                        if (curAdBean.getTimeStamp() != null) {
                            if (!curAdBean.getTimeStamp().equals(CommonUtils.getDateStamp())) {
                                curAdBean.setTimeStamp(CommonUtils.getDateStamp());
                                curAdBean.save();
                                showAdPop(curAdBean.getAdPicUrl(), curAdBean.getAdJumpLink());
                            }
                        } else {
                            curAdBean.setTimeStamp(CommonUtils.getDateStamp());
                            curAdBean.save();
                            showAdPop(curAdBean.getAdPicUrl(), curAdBean.getAdJumpLink());
                        }
                        break;
                    case 4:
                        showAdPop(curAdBean.getAdPicUrl(), curAdBean.getAdJumpLink());
                        break;

                }
            }
            obtainSystemAd(AD_START);
        }
    }


    public void obtainSystemAd(final int adType) {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getSystemAd(adType).compose(RxHelper.<List<AdBean>>handleResult())
                .subscribe(new RxSubscriber<List<AdBean>>(mContext) {
                    @Override
                    protected void _onNext(List<AdBean> items) {
                        switch (adType) {
                            case AD_START:
                                flushAdCache(items);
                                break;
                            case AD_ROLL:
                                updateAdRoll(items);
                                break;
                            case AD_WINDOW:
                                updateAdWindow(items);
                                break;
                            case AD_ACTIVITIES:
                                updateAdActivities(items);
                                break;
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        if (adType == AD_ACTIVITIES) {
                            updateAdActivitiesError();
                        }

                    }
                }));

    }

    private void flushAdCache(List<AdBean> items) {
        AdBean locAdBean = DataSupportManager.findFirst(AdBean.class);
        if (items != null && items.size() > 0) {
            AdBean remoteAdBean = items.get(0);
            if (remoteAdBean != null) {
                int remoteType = remoteAdBean.getDisplayType();
                if (remoteType != 0) {
                    if (locAdBean != null) {
                        int locType = locAdBean.getDisplayType();
                        if ((locType == 0 && remoteType == 1) || (locType == 2 && remoteType == 2)) {
                            String locLink = locAdBean.getAdPicUrl();
                            if (locLink != null && !locLink.equals(remoteAdBean.getAdPicUrl())) {
                                DataSupport.deleteAll(AdBean.class);
                                new GetImageCacheTask(remoteAdBean).execute();
                            }
                            return;
                        }
                        DataSupport.deleteAll(AdBean.class);
                    }
                    new GetImageCacheTask(remoteAdBean).execute();
                    return;
                }
            }
        }
        if (locAdBean != null) {
            DataSupport.deleteAll(AdBean.class);
        }
    }

    private class GetImageCacheTask extends AsyncTask<Void, Void, File> {

        AdBean mAdBean;

        private GetImageCacheTask(AdBean bean) {
            mAdBean = bean;
        }

        @Override
        protected File doInBackground(Void... params) {
            try {
                return Glide.with(mContext)
                        .load(mAdBean.getAdPicUrl())
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            //此path就是对应文件的缓存路径
            String path = result.getPath();
            AppLog.print("GET IMG PATH__" + path);
            if (!TextUtils.isEmpty(path)) {
                mAdBean.setAdPicUrl(path);
                mAdBean.save();
            }
        }
    }


    protected void updateAdWindow(List<AdBean> items) {

    }

    protected void updateAdRoll(List<AdBean> items) {

    }

    protected void updateAdActivities(List<AdBean> items) {

    }

    protected void updateAdActivitiesError() {

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
        if (!isForbidenAd) {
            topAct = CustomActivityManager.getInstance().getTopActivity();
            AppLog.print("onstop___" + topAct);
        } else {
            isForbidenAd = false;
            topAct = null;
        }

    }

    @Override
    protected void onDestroy() {
        closeAdPop();
        mContext = null;
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


    protected void startCamera() {
        isForbidenAd = true;
        Intent tpIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (tpIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File file = createImageFile();
            if (file != null) {
                cmFilePath = file.getAbsolutePath();
                Uri photoFile;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    String authority = mContext.getApplicationInfo().packageName + ".fileprovider";
                    photoFile = FileProvider.getUriForFile(this.mContext.getApplicationContext(), authority, file);
                } else {
                    photoFile = Uri.fromFile(file);
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    tpIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile);
                }
            } else {
                cmFilePath = null;
            }
        }
        startActivityForResult(tpIntent, REQ_CAMERA_PROOF);
    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            if (!storageDir.mkdir()) {
                return null;
            }
        }
        return new File(storageDir, imageFileName);
    }

}




























