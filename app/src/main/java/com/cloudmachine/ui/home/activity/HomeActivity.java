package com.cloudmachine.ui.home.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cloudmachine.R;
import com.cloudmachine.activities.AboutCloudActivity;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.activities.ViewCouponActivityNew;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.LocationBean;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.MenuBean;
import com.cloudmachine.bean.VersionInfo;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.DataSupportManager;
import com.cloudmachine.helper.LocationManager;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.net.task.GetVersionAsync;
import com.cloudmachine.ui.home.activity.fragment.DeviceFragment;
import com.cloudmachine.ui.home.activity.fragment.MaintenanceFragment;
import com.cloudmachine.ui.home.activity.fragment.WebFragment;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.cloudmachine.ui.home.model.HomeModel;
import com.cloudmachine.ui.home.model.PopItem;
import com.cloudmachine.ui.home.presenter.HomePresenter;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.ui.homepage.activity.ViewMessageActivity;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.ui.personal.activity.PersonalDataActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.VersionU;
import com.cloudmachine.widget.MenuTextView;
import com.cloudmachine.widget.NotfyImgView;
import com.cloudmachine.zxing.activity.CaptureActivity;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/*
* 更新通知提醒需测试*/
public class HomeActivity extends BaseAutoLayoutActivity<HomePresenter, HomeModel> implements Handler.Callback, HomeContract.View, View.OnClickListener {
    public static final String KEY_HOME_MENU = "key_home_menu";
    public static final String RXEVENT_UPDATE_REMIND = "rxevent_update_remind";
    public static final String KEY_H5_AUTORITY = "key_h5_autority";
    private static final String AUTORITY_YUNBOX = "yunbox";
    private static final String AUTORITY_MYORDER = "myOrder";
    private static final String AUTORITY_HTTP = "http";
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private static final String ACT_SP_NAME = "activities_sp";
    private static final String KEY_ACT_SIZE = "key_act_size";
    public static final int PEM_REQCODE_WRITESD = 0x113;
    public static final int PEM_REQCODE_CAMERA = 0x114;
    public static final int REQ_CODE_SCAN_QRCODE = 0x222;
    private static final int REQ_FINE_LOCATION = 0x12;
    private String downLoadLink;
    @BindView(R.id.home_me_img)
    ImageView homeMeImg;
    @BindView(R.id.home_actvite_img)
    NotfyImgView homeActviteImg;
    @BindView(R.id.home_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.home_head_img)
    CircleImageView homeHeadImg;
    @BindView(R.id.home_nickname_tv)
    TextView homeNicknameTv;
    @BindView(R.id.item_message)
    FrameLayout itemMessage;
    @BindView(R.id.item_repair_history)
    FrameLayout itemRepairHistory;
    @BindView(R.id.item_purse)
    FrameLayout itemPurse;
    @BindView(R.id.item_machine_knowledge)
    FrameLayout itemMachineKownledge;
    @BindView(R.id.item_about)
    LinearLayout itemAbout;
    @BindView(R.id.home_me_layout)
    LinearLayout homeMeLayout;
    @BindView(R.id.home_head_layout)
    FrameLayout homeHeadLyout;
    @BindView(R.id.home_title_layout1)
    RelativeLayout titleLayout1;
    @BindView(R.id.item_message_tv)
    TextView itemMessageTv;
    @BindView(R.id.item_message_nimg)
    NotfyImgView itmeMessageNimg;
    @BindView(R.id.home_fragment_cotainer)
    FrameLayout framgmentCotainer;
    @BindView(R.id.home_title_device)
    TextView deviceTv;
    @BindView(R.id.home_title_maintenance)
    TextView maintenanceTv;
    @BindView(R.id.home_title_clock)
    TextView clockTv;

    @BindView(R.id.home_me_img_alert)
    View meAlert;
    @BindView(R.id.item_my_order)
    FrameLayout itemMyOrder;
    @BindView(R.id.item_about_niv)
    NotfyImgView aboutNimg;
    @BindView(R.id.home_san_img)
    ImageView scanImg;
    @BindView(R.id.home_guide_container)
    RelativeLayout guideContainer;
    @BindView(R.id.icon_guide_scan)
    ImageView guideScanImg;
    @BindView(R.id.icon_guide_scan_text)
    ImageView guideScanText;
    @BindView(R.id.home_guide_exp)
    ImageView guideExpImg;
    @BindView(R.id.home_guide_exp_text)
    ImageView guideEXpText;
    @BindView(R.id.home_guide_sure_btn)
    Button guideSureBtn;
    @BindView(R.id.item_order_nimg)
    NotfyImgView itemOrderNimg;
    @BindView(R.id.item_purse_tv)
    TextView purseTv;
    @BindView(R.id.home_menu_container)
    LinearLayout homeMenuCotainer;
    @BindView(R.id.home_menu_hsv)
    HorizontalScrollView homeMenuHsv;
    @BindView(R.id.home_menu_default)
    LinearLayout homeMenuDefault;


    ImageView promotionImg;
    PopupWindow promotionPop;
    List<PopItem> mItems;
    private Handler mHandler;
    private MessageReceiver mMessageReceiver;

    Fragment deviceFragment, maintenaceFragment, h5Fragment;
    int mustUpdate;
    int leftMargin;
    int lastSelIndex;
    PermissionsChecker mChecker;
    long lastMemberId;
    boolean isFirst = true;
    SparseArray<WebFragment> webFmtArray = new SparseArray<>();
    WebFragment selWebFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mHandler = new Handler(this);
        mChecker = new PermissionsChecker(this);
        MobclickAgent.enableEncrypt(true); // 友盟统计
        MobclickAgent.openActivityDurationTrack(false);
        setUPageStatistics(false);
        registerMessageReceiver();
        initUpdateConfig();
        new GetVersionAsync(mContext, mHandler).execute();
        mPresenter.getH5ConfigInfo();
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_FINE_LOCATION);
        } else {
            initLocation();
        }
    }

    private void initHomeMenu(String memberId, final boolean isFlush) {
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).getHeadMenu(memberId).compose(RxHelper.<List<MenuBean>>handleResult()).subscribe(new RxSubscriber<List<MenuBean>>(mContext) {

            @Override
            protected void _onNext(List<MenuBean> menuBeen) {
                updateView(menuBeen);
                if (isFlush) {
                    reloadUrl();
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意授权
                initLocation();
            } else {
                //用户拒绝授权
                CommonUtils.showPermissionDialog(this, Constants.PermissionType.LOCATION);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void initLocation() {
        AMapLocationClient locClient = LocationManager.getInstence().getLocationClient(getApplicationContext());
        locClient.setLocationListener(locationListener);
        locClient.startLocation();
    }

    private AMapLocationListener locationListener = new AMapLocationListener() {


        @Override
        public void onLocationChanged(AMapLocation location) {
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
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
    };


    public void updateView(List<MenuBean> homeMenuBeans) {
        if (homeMenuBeans != null && homeMenuBeans.size() > 0) {
            if (homeMenuBeans.size() > 2) {
                leftMargin = (int) getResources().getDimension(R.dimen.dimen_size_23);
            } else {
                ViewGroup.LayoutParams params = homeMenuHsv.getLayoutParams();
                if (params != null) {
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                leftMargin = (int) getResources().getDimension(R.dimen.dimen_size_37);
            }
            homeMenuHsv.setVisibility(View.VISIBLE);
            homeMenuDefault.setVisibility(View.GONE);
            Collections.sort(homeMenuBeans, new Comparator<MenuBean>() {
                @Override
                public int compare(MenuBean o1, MenuBean o2) {
                    return o1.getMenuSort() - o2.getMenuSort();
                }
            });
            homeMenuCotainer.removeAllViews();
            for (MenuBean bean : homeMenuBeans) {
                if (bean.getYn() == 0) {
                    homeMenuCotainer.addView(getMenuView(bean));
                }
            }
            showFragmentNew((TextView) homeMenuCotainer.getChildAt(0));
        } else {
            homeMenuHsv.setVisibility(View.GONE);
            homeMenuDefault.setVisibility(View.VISIBLE);
            showFragment(deviceTv);
        }

    }


    private String getAuthory() {
        return getIntent().getStringExtra(KEY_H5_AUTORITY);
    }


    private void initUpdateConfig() {
        checkAppVersion();
        mRxManager.on(RXEVENT_UPDATE_REMIND, new Action1<Object>() {
            @Override
            public void call(Object o) {
                checkAppVersion();
            }
        });

    }

    private void checkAppVersion() {
        String newVersion = MySharedPreferences.getSharedPString(Constants.KEY_NewVersion);
        if (!TextUtils.isEmpty(newVersion)) {
            boolean isUpdate = CommonUtils.checVersion(VersionU.getVersionName(), newVersion);
            if (isUpdate) {
                meAlert.setVisibility(View.VISIBLE);
                aboutNimg.setNotifyPointVisible(true);
            }
        }

    }


    private void showFragment(View titleView) {
        if (titleView.isSelected()) {
            return;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (titleView == deviceTv) {
            deviceTv.setSelected(true);
            maintenanceTv.setSelected(false);
            clockTv.setSelected(false);
            if (deviceFragment == null) {
                deviceFragment = new DeviceFragment();
                if (maintenaceFragment != null) {
                    ft.hide(maintenaceFragment);
                }
                if (h5Fragment != null) {
                    ft.hide(h5Fragment);
                }
                ft.add(R.id.home_fragment_cotainer, deviceFragment);
            } else {
                if (deviceFragment.isVisible()) {
                    return;
                }
                if (maintenaceFragment != null) {
                    ft.hide(maintenaceFragment);
                }
                if (h5Fragment != null) {
                    ft.hide(h5Fragment);
                }
                ft.show(deviceFragment);
            }
        }
        if (titleView == maintenanceTv) {
            deviceTv.setSelected(false);
            maintenanceTv.setSelected(true);
            clockTv.setSelected(false);
            if (maintenaceFragment == null) {
                maintenaceFragment = new MaintenanceFragment();
                if (deviceFragment != null) {
                    ft.hide(deviceFragment);
                }
                if (h5Fragment != null) {
                    ft.hide(h5Fragment);
                }
                ft.add(R.id.home_fragment_cotainer, maintenaceFragment);
            } else {
                if (maintenaceFragment.isVisible()) {
                    return;
                }
                if (deviceFragment != null) {
                    ft.hide(deviceFragment);
                }
                if (h5Fragment != null) {
                    ft.hide(h5Fragment);
                }
                ft.show(maintenaceFragment);
            }
        }
        if (titleView == clockTv) {
            deviceTv.setSelected(false);
            maintenanceTv.setSelected(false);
            clockTv.setSelected(true);
            if (h5Fragment == null) {
                h5Fragment = new WebFragment(ApiConstants.CLOCK);
                if (deviceFragment != null) {
                    ft.hide(deviceFragment);
                }
                if (maintenaceFragment != null) {
                    ft.hide(maintenaceFragment);
                }
                ft.add(R.id.home_fragment_cotainer, h5Fragment);
            } else {
                if (h5Fragment.isVisible()) {
                    return;
                }
                if (deviceFragment != null) {
                    ft.hide(deviceFragment);
                }
                if (maintenaceFragment != null) {
                    ft.hide(maintenaceFragment);
                }
                ((WebFragment) h5Fragment).loadUrl();
                ft.show(h5Fragment);
            }
        }
        ft.commit();
    }


    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public View getMenuView(MenuBean menuBean) {
//            <TextView
//        android:id="@+id/home_title_device"
//        android:layout_width="wrap_content"
//        android:layout_height="match_parent"
//        android:gravity="center"
//        android:text="设备"
//        android:textColor="@color/cor8"
//        android:textSize="@dimen/siz3" />
        MenuTextView menuTv = new MenuTextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = menuBean.getMenuSort() == 1 ? 0 : leftMargin;
        menuTv.setLayoutParams(params);
        if (menuBean.getMenuHot() == 1) {
            menuTv.setMenuHot(true);
        }
        menuTv.setTag(menuBean);
        menuTv.setOnClickListener(onMenuClickListener);
        menuTv.setGravity(Gravity.CENTER);
        menuTv.setTextColor(getResources().getColorStateList(R.color.coupon_title_textcolor));
        menuTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.siz3));
        menuTv.setText(menuBean.getMenuTitle());
        return menuTv;
    }

    private View.OnClickListener onMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showFragmentNew((TextView) v);
        }
    };

    private void showFragmentNew(TextView titleView) {
        if (titleView.isSelected()) {
            return;
        }
        int selectIndex = 0;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MenuBean menuBean = (MenuBean) titleView.getTag();
        for (int i = 0; i < homeMenuCotainer.getChildCount(); i++) {
            TextView view = (TextView) homeMenuCotainer.getChildAt(i);
            if (titleView == view) {
                selectIndex = i;
                titleView.setSelected(true);
            } else {
                view.setSelected(false);
            }

        }
        int scrollX = (selectIndex - lastSelIndex) * leftMargin;
        homeMenuHsv.smoothScrollBy(scrollX, 0);
        lastSelIndex = selectIndex;
        switch (menuBean.getMenuMake()) {
            case 1://设备
                if (deviceFragment == null) {
                    deviceFragment = new DeviceFragment();
                    ft.add(R.id.home_fragment_cotainer, deviceFragment);
                } else {
                    if (deviceFragment.isVisible()) {
                        return;
                    }
                    ft.show(deviceFragment);
                }
                if (maintenaceFragment != null) {
                    ft.hide(maintenaceFragment);
                }
//                if (h5Fragment != null) {
//                    ft.hide(h5Fragment);
//                }
                hidenWebFragment(ft, null);
                break;
            case 2://维修
                if (deviceFragment != null && deviceFragment.isVisible()) {
                    ft.hide(deviceFragment);
                }
//                if (h5Fragment != null && h5Fragment.isVisible()) {
//                    ft.hide(h5Fragment);
//                }
                hidenWebFragment(ft, null);
                if (maintenaceFragment == null) {
                    maintenaceFragment = new MaintenanceFragment();
                    ft.add(R.id.home_fragment_cotainer, maintenaceFragment);
                } else {
                    if (maintenaceFragment.isVisible()) {
                        return;
                    }
                    ft.show(maintenaceFragment);
                }
                break;
            case 3://H5
                if (deviceFragment != null && deviceFragment.isVisible()) {
                    ft.hide(deviceFragment);
                }
                if (maintenaceFragment != null && maintenaceFragment.isVisible()) {
                    ft.hide(maintenaceFragment);
                }
//                if (h5Fragment == null) {
//                    h5Fragment = new WebFragment(menuBean.getMenuLink());
//                    ft.add(R.id.home_fragment_cotainer, h5Fragment);
//                } else {
//                    ((WebFragment) h5Fragment).loadUrl(menuBean.getMenuLink());
//                    ft.show(h5Fragment);
//                }
                int id = menuBean.getId();
                String menuLink = menuBean.getMenuLink();
                WebFragment fmt = webFmtArray.get(id);
                hidenWebFragment(ft, fmt);
                if (fmt == null) {
                    fmt = new WebFragment(menuLink);
                    ft.add(R.id.home_fragment_cotainer, fmt);
                    webFmtArray.put(id, fmt);
                } else {
                    fmt.loadUrl();
                    ft.show(fmt);
                }
                selWebFragment = fmt;
                break;


        }
        ft.commit();


    }

    private void hidenWebFragment(FragmentTransaction ft, WebFragment fmt) {
        for (int i = 0; i < webFmtArray.size(); i++) {
            WebFragment childFm = webFmtArray.valueAt(i);
            if (childFm != fmt && childFm.isVisible()) {
                ft.hide(childFm);
            }
        }
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!TextUtils.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                Constants.MyToast(showMsg.toString());
            }
        }

    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.TIME_HOME);
        loadData();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(homeMeLayout);
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    public void requestPromotion(long memberId) {
        if (promotionPop == null || !promotionPop.isShowing()) {
            AppLog.print("getPromotionInfo___");
            mPresenter.getPromotionInfo(memberId);
//            testPromption();
        }
    }

    //初始化消息、机器列表
    private void loadData() {
        Member member = MemeberKeeper.getOauth(this);
        if (member != null) {
            long memberId = member.getId();
            Glide.with(mContext).load(member.getLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .error(R.drawable.ic_default_head)
                    .into(homeHeadImg);
            homeNicknameTv.setText(member.getNickName());
            if (isFirst) {
                isFirst = false;
                initHomeMenu(String.valueOf(memberId), false);
            } else {
                if (lastMemberId != memberId) {
                    initHomeMenu(String.valueOf(memberId), true);
                }
            }
            lastMemberId = memberId;
            mPresenter.getCountByStatus(memberId, 0);
            mPresenter.updateUnReadMessage(memberId);
        } else {
            homeHeadImg.setImageResource(R.drawable.ic_default_head);
            homeNicknameTv.setText("登录");
            if (aboutNimg.isNotifyVisbile()) {
                meAlert.setVisibility(View.VISIBLE);
            } else {
                meAlert.setVisibility(View.GONE);
            }
            itmeMessageNimg.setNotifyPointVisible(false);
            itemOrderNimg.setNotifyPointVisible(false);
            if (isFirst) {
                isFirst = false;
                initHomeMenu(null, false);
            } else {
                if (lastMemberId != -1) {
                    initHomeMenu(null, true);
                }
            }
            lastMemberId = -1;
        }
        mPresenter.getHomeBannerInfo();

    }


    private void reloadUrl() {
        if (h5Fragment != null && h5Fragment.isVisible()) {
            ((WebFragment) h5Fragment).loadUrl();
        }
        for (int i = 0; i < webFmtArray.size(); i++) {
            WebFragment childFmt = webFmtArray.valueAt(i);
            if (childFmt.isVisible()) {
                childFmt.loadUrl();
            }
        }
    }


    public void updateDeviceMessage() {
        if (UserHelper.isLogin(this)) {
            mPresenter.updateUnReadMessage(UserHelper.getMemberId(this));
            mRxManager.post(Constants.UPDATE_DEVICE_LIST, null);
        }
    }


    @OnClick({R.id.home_title_clock, R.id.home_guide_sure_btn, R.id.home_san_img, R.id.item_my_order, R.id.home_title_device, R.id.home_title_maintenance, R.id.home_head_layout, R.id.item_message, R.id.item_repair_history, R.id.item_purse, R.id.item_machine_knowledge, R.id.item_about, R.id.home_me_img, R.id.home_actvite_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_guide_sure_btn:
                guideContainer.setClickable(false);
                guideContainer.setVisibility(View.GONE);
                break;
            case R.id.home_san_img://扫描车牌二维码
                gotoScanCode();
                break;
            case R.id.item_my_order:
                MobclickAgent.onEvent(this, MobEvent.TIME_H5_MY_ORDER_PAGE);
                Bundle bundle = new Bundle();
                bundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppOrderList);
                Constants.toActivity(this, QuestionCommunityActivity.class, bundle);
                break;

            case R.id.home_title_device:
            case R.id.home_title_maintenance:
            case R.id.home_title_clock:
                showFragment(view);
                break;

            case R.id.home_pop_bg:
            case R.id.pop_ad_close_img:
                dismissPop();
                break;
            case R.id.home_me_img:
                if (UserHelper.isLogin(this)) {
                    drawerLayout.openDrawer(homeMeLayout);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
            case R.id.home_actvite_img:
                homeActviteImg.setNotifyPointVisible(false);
                Constants.toActivity(this, ActivitesActivity.class, null);
                break;

            case R.id.item_message:
                if (UserHelper.isLogin(this)) {
                    Constants.toActivity(this, ViewMessageActivity.class, null);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
            case R.id.item_repair_history://报修记录
                if (UserHelper.isLogin(this)) {
                    Constants.toActivity(this, RepairRecordNewActivity.class, null);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
            case R.id.item_purse://钱包-因贷款需求开放
                // TODO: 2018/1/25 Purse_WX_test
                if (UserHelper.isLogin(this)) {
                    Constants.toActivity(this, PurseActivity.class, null);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
            case R.id.item_machine_knowledge://我的二维码
                if (UserHelper.isLogin(this)) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppMachineKnowledge);
                    Constants.toActivity(this, QuestionCommunityActivity.class, mBundle);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
            case R.id.item_about://关于与帮助
                Constants.toActivity(this, AboutCloudActivity.class, null);
                break;
            case R.id.home_head_layout:
                if (UserHelper.isLogin(this)) {
                    Member member = MemeberKeeper.getOauth(this);
                    Bundle headB = new Bundle();
                    headB.putSerializable("memberInfo", member);
                    Constants.toActivity(this, PersonalDataActivity.class, headB);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
        }
    }

    public void gotoScanCode() {
        if (mChecker.lacksPermissions(Manifest.permission.CAMERA)) {
            PermissionsActivity.startActivityForResult(this, PEM_REQCODE_CAMERA,
                    Manifest.permission.CAMERA);
        } else {
            scanQRCode();
        }
    }


    private void dismissPop() {
        ++curAdPos;
        if (mItems != null && mItems.size() > curAdPos) {
            showItemPop(curAdPos);
        } else {
            curAdPos = 0;
            if (promotionPop.isShowing()) {
                promotionPop.dismiss();
            }
        }
    }


    private void setPurseTv(int resTextId, int resDrawableId) {
        purseTv.setText(getResources().getString(resTextId));
        Drawable leftDrawable = getResources().getDrawable(resDrawableId);
        leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(), leftDrawable.getIntrinsicHeight());
        purseTv.setCompoundDrawables(leftDrawable, null, null, null);
    }

    //消息小红点
    @Override
    public void updateMessageCount(int count) {
        if (count > 0) {
            itmeMessageNimg.setNotifyPointVisible(true);
            meAlert.setVisibility(View.VISIBLE);
        } else {
            itmeMessageNimg.setNotifyPointVisible(false);
            if (itemOrderNimg.isNotifyVisbile()) {
                return;
            }
            if (aboutNimg.isNotifyVisbile()) {
                return;
            }
            meAlert.setVisibility(View.GONE);
        }
    }

    public void updateOrderView(boolean hasOrder) {
        if (hasOrder) {
            itemOrderNimg.setNotifyPointVisible(true);
            if (meAlert.getVisibility() != View.VISIBLE) {
                meAlert.setVisibility(View.VISIBLE);
            }
        } else {
            itemOrderNimg.setNotifyPointVisible(false);
            if (itmeMessageNimg.isNotifyVisbile()) {
                return;
            }
            if (aboutNimg.isNotifyVisbile()) {
                return;
            }
            meAlert.setVisibility(View.GONE);
        }
    }


    @Override
    public void updatePromotionInfo(List<PopItem> items) {
        AppLog.print("updatePromotionInfo items___" + items);
        if (items == null || items.size() <= 0) {
            return;
        }
        mItems = items;
        if (promotionPop == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.pop_home_ad, null);
            View bgView = contentView.findViewById(R.id.home_pop_bg);
            promotionImg = (ImageView) contentView.findViewById(R.id.pop_ad_img);
            ImageView closeImg = (ImageView) contentView.findViewById(R.id.pop_ad_close_img);
            closeImg.setOnClickListener(this);
            bgView.setOnClickListener(this);
            promotionPop = CommonUtils.getAnimPop(contentView);
        }
        showItemPop(0);
    }


    @Override
    public void updateActivitySize(int count) {
        AppLog.print("updateActivitySize__size_" + count);
        SharedPreferences sp = getSharedPreferences(ACT_SP_NAME, MODE_PRIVATE);
        int actSize = sp.getInt(KEY_ACT_SIZE, -1);
        if (actSize != -1) {
            if (count > actSize) {
                //红点
                homeActviteImg.setNotifyPointVisible(true);
            } else {
                homeActviteImg.setNotifyPointVisible(false);
            }
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_ACT_SIZE, count);
        editor.commit();
    }

    @Override
    public void updateH5View() {
        if (!TextUtils.isEmpty(ApiConstants.AppMachineKnowledge)) {
            itemMachineKownledge.setVisibility(View.VISIBLE);
        }
        String authory = getAuthory();
        if (AUTORITY_YUNBOX.equals(authory)) {
            jumpH5Page(ApiConstants.AppBoxDetail);
        } else if (AUTORITY_MYORDER.equals(authory)) {
            if (UserHelper.isLogin(this)) {
                jumpH5Page(ApiConstants.AppOrderList);
            }
        } else {
            if (!TextUtils.isEmpty(authory)) {
                if (AUTORITY_HTTP.equals(authory.substring(0, 4))) {
                    jumpH5Page(authory);
                }

            }
        }
    }

    private int curAdPos;

    private void showItemPop(int pos) {
        curAdPos = pos;
        PopItem item = mItems.get(pos);
        if (item != null) {
            Glide.with(this).load(item.getActPictureLink()).into(new GlideDrawableImageViewTarget(promotionImg) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    if (!promotionPop.isShowing()) {
                        promotionPop.showAtLocation(getWindow().getDecorView(), Gravity.FILL, 0, 0);
                    }
                }
            });
            final String jumpLink = item.getJumpLink();
            final int popupType = item.getPopupType();
            AppLog.print("popType___" + popupType + ",  jumpLink__" + jumpLink);
            promotionImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupType != 1) {
                        if (popupType == 3) {
                            dismissPop();
                            Constants.toActivity(HomeActivity.this, ViewCouponActivityNew.class, null, false);
                        } else {
                            if (!TextUtils.isEmpty(jumpLink)) {
                                MobclickAgent.onEvent(HomeActivity.this, MobEvent.COUNT_HOME_ALERT_AD_CLICK);
                                dismissPop();
                                Bundle bundle = new Bundle();
                                bundle.putString(QuestionCommunityActivity.H5_URL, jumpLink);
                                Constants.toActivity(HomeActivity.this, QuestionCommunityActivity.class, bundle);
                            }
                        }
                    }
                }
            });
        } else {
            promotionImg.setOnClickListener(this);
        }

    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {

            case Constants.HANDLER_GETVERSION_SUCCESS:
                VersionInfo vInfo = (VersionInfo) msg.obj;
                if (null != vInfo) {
                    boolean isUpdate = CommonUtils.checVersion(VersionU.getVersionName(), vInfo.getVersion());
                    if (isUpdate) {
                        mustUpdate = vInfo.getMustUpdate();
                        Constants.updateVersion(this, mHandler,
                                mustUpdate, vInfo.getMessage(), vInfo.getLink());
                    }

                }
                break;
            case Constants.HANDLER_VERSIONDOWNLOAD:
                downLoadLink = (String) msg.obj;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mChecker.lacksPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionsActivity.startActivityForResult(this, PEM_REQCODE_WRITESD,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                } else {
                    Constants.versionDownload(HomeActivity.this, downLoadLink);
                }
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (null != mMessageReceiver) {
            unregisterReceiver(mMessageReceiver);
        }
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // 双击退出页面

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - ExitTime) > 2000) {
                Toast.makeText(getApplicationContext(),
                        ResV.getString(R.string.main_activity_exit), Toast.LENGTH_SHORT).show();
                ExitTime = System.currentTimeMillis();
            } else {
//                clearCache();
                this.finish();
                try {
                    MobclickAgent.onKillProcess(this);
                    System.exit(0);
                } catch (Exception e) {
                    AppLog.print("Finish Error__" + e);
                }

            }
            return true;
        }
        return true;
    }

    private long ExitTime = 0;


    public void jumpH5Page(String h5Url) {
        Bundle bundle = new Bundle();
        bundle.putString(QuestionCommunityActivity.H5_URL, h5Url);
        Constants.toActivity(this, QuestionCommunityActivity.class, bundle);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {


            case PEM_REQCODE_CAMERA:

                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    ToastUtils.showToast(this, "摄像头打开失败！！");
                    CommonUtils.showPermissionDialog(this, Constants.PermissionType.CAMERA);
                } else {
                    scanQRCode();
                }
                break;
            case PEM_REQCODE_WRITESD:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    ToastUtils.showToast(this, "更新失败！！");
                    if (mustUpdate == 1) {
                        CommonUtils.showFinishPermissionDialog(this);
                    } else {
                        CommonUtils.showPermissionDialog(this, Constants.PermissionType.STORAGE);
                    }
                } else {
                    Constants.versionDownload(HomeActivity.this, downLoadLink);
                }
                break;
            case REQ_CODE_SCAN_QRCODE:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    String resultStr = bundle.getString("qr_scan_result");
                    AppLog.print("scan__result__" + resultStr);
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
                        if (UserHelper.isLogin(this)) {
                            resultStr += ("&" + QuestionCommunityActivity.PARAMS_KEY_MEMBERID + "=" + UserHelper.getMemberId(this));
                        }
                        try {
                            resultEncodeStr = URLEncoder.encode(resultStr, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
//                    String qrUrl = "http://www.cloudm.com/static/qr.html?content=" + resultEncodeStr;
                    String qrUrl = ApiConstants.AppQR + "?content=" + resultEncodeStr;
                    Bundle qBundle = new Bundle();
                    qBundle.putBoolean(QuestionCommunityActivity.QR_CODE, true);
                    qBundle.putString(QuestionCommunityActivity.H5_URL, qrUrl);
                    Constants.toActivity(this, QuestionCommunityActivity.class, qBundle);
                }
                break;
            default:
                if (resultCode == RES_UPDATE_TIKCET) {
                    if (selWebFragment != null) {
                        selWebFragment.loadUrl();
                    }
                }
                break;

        }


    }

    public void scanQRCode() {
        if (isCameraCanUse()) {
            Constants.toActivityForR(this, CaptureActivity.class, null, REQ_CODE_SCAN_QRCODE);
        } else {
            Toast.makeText(this, "请打开此应用的摄像头权限！", Toast.LENGTH_SHORT).show();
        }
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
            if (mCamera != null)
                mCamera.release();
            mCamera = null;
        }
        return canUse;
    }

    public void updateGuide(List<McDeviceInfo> deviceList) {
        if (UserHelper.isLogin(this)) {
            if (deviceList != null) {
                int len = deviceList.size();
                if (len > 0) {
                    if (len == 1) {
                        McDeviceInfo info = deviceList.get(0);
                        if (info != null) {
                            if (info.getId() != 0) {
                                guideExpImg.setVisibility(View.GONE);
                                guideEXpText.setVisibility(View.GONE);
                            }
                        }

                    }
                }
            }
        }
        guideContainer.setVisibility(View.VISIBLE);
    }

}
