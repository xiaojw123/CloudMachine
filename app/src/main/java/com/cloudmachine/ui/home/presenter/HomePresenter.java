package com.cloudmachine.ui.home.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.H5Config;
import com.cloudmachine.bean.LocationBean;
import com.cloudmachine.bean.MenuBean;
import com.cloudmachine.bean.VersionInfo;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.DataSupportManager;
import com.cloudmachine.helper.LocationManager;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.cloudmachine.ui.home.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.VersionU;
import com.cloudmachine.widget.MenuTextView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by xiaojw on 2017/5/22.
 */

public class HomePresenter extends HomeContract.Presenter {
    private AMapLocationClient mLocClient;
    private MessageReceiver mMessageReceiver;
    private long ExitTime = 0;

    @Override
    public void initHomeMenu(final boolean isFlush) {
        mRxManage.add(mModel.getHomeMenu().subscribe(new RxSubscriber<List<MenuBean>>(mContext) {

            @Override
            protected void _onNext(List<MenuBean> menuBeen) {
                mView.updateView(menuBeen);
                if (isFlush) {
                    mView.reloadUrl();
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }


    @Override
    public void getVersionInfo() {
        mRxManage.add(mModel.getVersionInfo().subscribe(new RxSubscriber<VersionInfo>(mContext) {
            @Override
            protected void _onNext(VersionInfo info) {
                if (null != info) {
                    boolean isCanUpdate = CommonUtils.checVersion(VersionU.getVersionName(), info.getVersion());
                    if (isCanUpdate) {
                        Constants.updateVersion(mContext,
                                info.getMustUpdate(), info.getMessage(), info.getLink());
                    }
                }

            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

    @Override
    public void updateUnReadMessage() {
        mRxManage.add(mModel.getMessageUntreatedCount().subscribe(new RxSubscriber<JsonObject>(mContext, false) {
            @Override
            protected void _onNext(JsonObject resultJobj) {
                if (resultJobj != null && !resultJobj.isJsonNull()) {

                    JsonElement je1 = resultJobj.get("messageCount");
                    JsonElement je2 = resultJobj.get("orderCount");

                    int msgCount = 0;
                    int orderCount = 0;
                    if (je1 != null && !je1.isJsonNull()) {
                        msgCount = je1.getAsInt();
                    }
                    if (je2 != null && !je2.isJsonNull()) {
                        orderCount = je2.getAsInt();
                    }
                    mView.updateMessageCount(msgCount, orderCount);
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }


    @Override
    public void initH5Config() {
        mRxManage.add(mModel.getConfigInfo().subscribe(new RxSubscriber<H5Config>(mContext) {
            @Override
            protected void _onNext(H5Config config) {
                if (config != null) {
                    H5Config.PagesBean pg = config.getPages();
                    if (pg != null) {
                        ApiConstants.AppBoxDetail = pg.getAppBoxDetail();
                        ApiConstants.AppCouponHelper = pg.getAppCouponHelper();
                        ApiConstants.AppUseHelper = pg.getAppUseHelper();
                        ApiConstants.AppWorkTimeStatistics = pg.getAppWorkTimeStatistics();
                        ApiConstants.AppOrderList = pg.getAppOrderList();
                        ApiConstants.AppWalletHelper = pg.getAppWalletHelper();
                        ApiConstants.AppFeedback = pg.getAppFeedback();
                        ApiConstants.AppQR = pg.getAppQR();
                        ApiConstants.APPForgetPassword = pg.getAPPForgetPassword();
                        ApiConstants.APPFwtk = pg.getAPPFwtk();
                        ApiConstants.APPRzgj = pg.getAPPRzgj();
                        ApiConstants.APPBmxy = pg.getAPPBmxy();
                        ApiConstants.APPSjyysxy = pg.getAPPSjyysxy();
                        ApiConstants.AppBankList = pg.getAppBankList();
                        ApiConstants.AppBankCmVerify = pg.getAppBankCmVerify();
                        ApiConstants.AppExChange=pg.getAppExChange();
                    }
                }
                mView.updateH5View();
            }

            @Override
            protected void _onError(String message) {
                mView.updateH5View();
            }
        }));
    }


    public void initLocation() {
        mLocClient = LocationManager.getInstence().getLocationClient(mContext);
        mLocClient.setLocationListener(locationListener);
        mLocClient.startLocation();
    }

    private AMapLocationListener locationListener = new AMapLocationListener() {


        @Override
        public void onLocationChanged(AMapLocation location) {
            mLocClient.stopLocation();
            saveLocation(location);
        }
    };

    private void saveLocation(AMapLocation location) {
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            AppLog.print("save location lat:" + lat + ", lng:" + lng);
            if (lat != 0 && lng != 0) {
                LocationBean locBean = DataSupportManager.findFirst(LocationBean.class);
                if (locBean != null) {
                    DataSupportManager.deleteAll(LocationBean.class);
                }
                LocationBean bean = new LocationBean();
                bean.setLat(String.valueOf(lat));
                bean.setLng(String.valueOf(lng));
                String address = location.getAddress();
                String provice = location.getProvince();
                String city = location.getCity();
                String district = location.getDistrict();
                if (!TextUtils.isEmpty(address)) {
                    try {
                        bean.setAddress(URLEncoder.encode(address, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(provice)) {
                    try {
                        bean.setProvince(URLEncoder.encode(provice, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(city)) {
                    try {
                        bean.setCity(URLEncoder.encode(city, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(district)) {
                    try {
                        bean.setDistrict(URLEncoder.encode(district, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                bean.save();
            }
        }

    }

    public void unRegisterMsgReceiver() {
        if (null != mMessageReceiver) {
            mContext.unregisterReceiver(mMessageReceiver);
        }
    }

    public void registerMsgReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(HomeActivity.MESSAGE_RECEIVED_ACTION);
        mContext.registerReceiver(mMessageReceiver, filter);
    }

    public void initUmeng() {
        MobclickAgent.enableEncrypt(true); // 友盟统计
        MobclickAgent.openActivityDurationTrack(false);
        ((BaseAutoLayoutActivity) mContext).setUPageStatistics(false);
    }

    public boolean exit(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - ExitTime) > 2000) {
                Toast.makeText(mContext,
                        ResV.getString(R.string.main_activity_exit), Toast.LENGTH_SHORT).show();
                ExitTime = System.currentTimeMillis();
            } else {
//                clearCache();
                ((BaseAutoLayoutActivity) mContext).finish();
                try {
                    MobclickAgent.onKillProcess(mContext);
                    System.exit(0);
                } catch (Exception e) {
                    AppLog.print("Finish Error__" + e);
                }

            }
            return true;
        }
        return true;

    }

    public String getQrUrl(String resultStr) {
        String resultEncodeStr = null;
        if (resultStr != null) {
            if (!resultStr.contains("qrfrom")) {

                if (resultStr.contains("?")) {
                    resultStr += "&";
                } else {
                    if (resultStr.startsWith("http")) {
                        resultStr += "?";
                    } else {
                        resultStr += "&";
                    }
                }
                resultStr += "qrfrom=cloudmApp";
            }
            if (UserHelper.isLogin(mContext)) {
                resultStr += ("&" + Constants.MEMBER_ID + "=" + UserHelper.getMemberId(mContext));
            }
            try {
                resultEncodeStr = URLEncoder.encode(resultStr, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return ApiConstants.AppQR + "?content=" + resultEncodeStr;

    }

    public boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {

            mCamera = Camera.open();
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            if (mCamera != null) {
                mCamera.release();
            }
        }
        return canUse;
    }

    public void registerNewVersionRemid() {
        mRxManage.on(HomeActivity.RXEVENT_UPDATE_REMIND, new Action1<Boolean>() {
            @Override
            public void call(Boolean o) {
                mView.updateVersionRemind(o);
            }
        });
        mRxManage.post(HomeActivity.RXEVENT_UPDATE_REMIND, null);
    }


    public View getMenuView(MenuBean menuBean, View.OnClickListener listener, int leftMargin) {
        MenuTextView menuTv = new MenuTextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = menuBean.getMenuSort() == 1 ? 0 : leftMargin;
        menuTv.setLayoutParams(params);
        if (menuBean.getMenuHot() == 1) {
            menuTv.setMenuHot(true);
        }
        menuTv.setTag(menuBean);
        menuTv.setOnClickListener(listener);
        menuTv.setGravity(Gravity.CENTER);
        menuTv.setTextColor(mContext.getResources().getColorStateList(R.color.coupon_title_textcolor));
        menuTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.siz3));
        menuTv.setText(menuBean.getMenuTitle());
        return menuTv;
    }


    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (HomeActivity.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(HomeActivity.KEY_MESSAGE);
                String extras = intent.getStringExtra(HomeActivity.KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(HomeActivity.KEY_MESSAGE + " : ").append(messge).append("\n");
                if (!TextUtils.isEmpty(extras)) {
                    showMsg.append(HomeActivity.KEY_EXTRAS + " : " + extras + "\n");
                }
                Constants.MyToast(showMsg.toString());
            }
        }

    }

}
