package com.cloudmachine.ui.home.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cloudmachine.R;
import com.cloudmachine.activities.AboutCloudActivity;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.AdBean;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.MenuBean;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.home.activity.fragment.DeviceFragment;
import com.cloudmachine.ui.home.activity.fragment.MaintenanceFragment;
import com.cloudmachine.ui.home.activity.fragment.WebFragment;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.cloudmachine.ui.home.model.HomeModel;
import com.cloudmachine.ui.home.presenter.HomePresenter;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.ui.personal.activity.PersonalDataActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.widget.NotfyImgView;
import com.cloudmachine.zxing.activity.CaptureActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

public class HomeActivity extends BaseAutoLayoutActivity<HomePresenter, HomeModel> implements HomeContract.View, View.OnClickListener {
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static final String RXEVENT_UPDATE_REMIND = "rxevent_update_remind";
    public static final String KEY_H5_AUTORITY = "key_h5_autority";
    private static final String AUTORITY_YUNBOX = "yunbox";
    private static final String AUTORITY_MYORDER = "myOrder";
    private static final String AUTORITY_HTTP = "http";
    public static boolean isForeground = false;
    private static final String ACT_SP_NAME = "activities_sp";
    private static final String KEY_ACT_SIZE = "key_act_size";
    public static final int PEM_REQCODE_CAMERA = 0x114;
    public static final int REQ_CODE_SCAN_QRCODE = 0x222;
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
    List<AdBean> mItems;
    Fragment deviceFragment, maintenaceFragment, h5Fragment;
    int leftMargin, lastSelIndex;
    String lastToken;
    boolean isFirst = true;
    SparseArray<WebFragment> webFmtArray = new SparseArray<>();
    WebFragment selWebFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mPresenter.initUmeng();
        mPresenter.registerMsgReceiver();
        mPresenter.initH5Config();
        mPresenter.registerNewVersionRemid();
        obtainSystemAd(AD_START);
        checkLocPermission();
        AsyncTask
    }

    private void checkLocPermission() {
        RxPermissions.getInstance(mContext).request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean grant) {
                int code = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
                if (code == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.initLocation();
                } else {
                    CommonUtils.showPermissionDialog(mContext, Constants.PermissionType.LOCATION_STORAGE);
                }
            }
        });
    }

    @Override
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
                    homeMenuCotainer.addView(mPresenter.getMenuView(bean, onMenuClickListener, leftMargin));
                }
            }
            showFragmentNew((TextView) homeMenuCotainer.getChildAt(0));
        } else {
            homeMenuHsv.setVisibility(View.GONE);
            homeMenuDefault.setVisibility(View.VISIBLE);
            showFragment(deviceTv);
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
                ((WebFragment) h5Fragment).loadWebUrl();
                ft.show(h5Fragment);
            }
        }
        ft.commit();
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
                hidenWebFragment(ft, null);
                break;
            case 2://维修
                if (deviceFragment != null && deviceFragment.isVisible()) {
                    ft.hide(deviceFragment);
                }
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
                int id = menuBean.getId();
                String menuLink = menuBean.getMenuLink();
                WebFragment fmt = webFmtArray.get(id);
                hidenWebFragment(ft, fmt);
                if (fmt == null) {
                    fmt = new WebFragment(menuLink);
                    ft.add(R.id.home_fragment_cotainer, fmt);
                    webFmtArray.put(id, fmt);
                } else {
                    fmt.loadWebUrl();
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


    //初始化消息、机器列表
    private void loadData() {
        Member member = MemeberKeeper.getOauth(this);
        if (member != null) {
            String token = UserHelper.getToken(this);
            Glide.with(mContext).load(member.getLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .error(R.drawable.ic_default_head)
                    .into(homeHeadImg);
            homeNicknameTv.setText(member.getNickName());
            if (isFirst) {
                isFirst = false;
                mPresenter.initHomeMenu(false);
            } else {
                if (!TextUtils.equals(lastToken, token)) {
                    mPresenter.initHomeMenu(true);
                }
            }
            lastToken = token;
            mPresenter.updateUnReadMessage();
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
                mPresenter.initHomeMenu(false);
            } else {
                if (lastToken != null) {
                    mPresenter.initHomeMenu(true);
                }
            }
            lastToken = null;
        }
        obtainSystemAd(AD_ACTIVITIES);
        mPresenter.getVersionInfo();

    }

    @Override
    public void reloadUrl() {
        if (h5Fragment != null && h5Fragment.isVisible()) {
            ((WebFragment) h5Fragment).loadWebUrl();
        }
        for (int i = 0; i < webFmtArray.size(); i++) {
            WebFragment childFmt = webFmtArray.valueAt(i);
            if (childFmt.isVisible()) {
                childFmt.loadWebUrl();
            }
        }
    }

    @Override
    public void updateVersionRemind(boolean hasNewVersion) {
        if (hasNewVersion) {
            meAlert.setVisibility(View.VISIBLE);
            aboutNimg.setNotifyPointVisible(true);
        } else {
            meAlert.setVisibility(View.VISIBLE);
            aboutNimg.setNotifyPointVisible(true);
        }

    }


    public void updateDeviceMessage() {
        if (UserHelper.isLogin(this)) {
            mPresenter.updateUnReadMessage();
            mRxManager.post(Constants.UPDATE_DEVICE_LIST, null);
        }
    }


    @OnClick({R.id.home_title_clock, R.id.home_guide_sure_btn, R.id.home_san_img, R.id.item_my_order, R.id.home_title_device, R.id.home_title_maintenance, R.id.home_head_layout, R.id.item_message, R.id.item_repair_history, R.id.item_purse, R.id.item_about, R.id.home_me_img, R.id.home_actvite_img})
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
                if (UserHelper.isLogin(this)) {
                    Constants.toActivity(this, PurseActivity.class, null);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
            case R.id.item_about://关于与帮助
                Constants.toActivity(this, AboutCloudActivity.class, null);
                break;
            case R.id.home_head_layout:
                if (UserHelper.isLogin(this)) {
                    Constants.toActivity(this, PersonalDataActivity.class, null);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
        }
    }

    public void gotoScanCode() {
        if (hasPermission(PEM_REQCODE_CAMERA, Manifest.permission.CAMERA)) {
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

    //消息小红点
    @Override
    public void updateMessageCount(int msgCount, int orderCount) {
        if (msgCount > 0 || orderCount > 0) {
            meAlert.setVisibility(View.VISIBLE);
            if (msgCount > 0) {
                itmeMessageNimg.setNotifyPointVisible(true);
            } else {
                itmeMessageNimg.setNotifyPointVisible(false);
            }
            if (orderCount > 0) {
                itemOrderNimg.setNotifyPointVisible(true);
            } else {
                itemOrderNimg.setNotifyPointVisible(false);
            }
        } else {
            itmeMessageNimg.setNotifyPointVisible(false);
            itemOrderNimg.setNotifyPointVisible(false);
            if (aboutNimg.isNotifyVisbile()) {
                meAlert.setVisibility(View.VISIBLE);
            } else {
                meAlert.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void updateAdWindow(List<AdBean> items) {
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
    protected void updateAdRoll(List<AdBean> items) {
        ((DeviceFragment) deviceFragment).updateRollAd(items);
    }


    @Override
    protected void updateAdActivities(List<AdBean> items) {
        if (items != null) {
            SharedPreferences sp = getSharedPreferences(ACT_SP_NAME, MODE_PRIVATE);
            int actSize = sp.getInt(KEY_ACT_SIZE, -1);
            int itemLen = items.size();
            if (actSize != -1) {
                if (itemLen > actSize) {
                    //红点
                    homeActviteImg.setNotifyPointVisible(true);
                } else {
                    homeActviteImg.setNotifyPointVisible(false);
                }
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(KEY_ACT_SIZE, itemLen);
            editor.apply();
        }
    }

    @Override
    public void updateH5View() {
        String authory = getIntent().getStringExtra(KEY_H5_AUTORITY);
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
        AdBean item = mItems.get(pos);
        if (item != null) {
            Glide.with(this).load(item.getAdPicUrl()).into(new GlideDrawableImageViewTarget(promotionImg) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    if (!promotionPop.isShowing()) {
                        promotionPop.showAtLocation(getWindow().getDecorView(), Gravity.FILL, 0, 0);
                    }
                }
            });
            final String jumpLink = item.getAdJumpLink();
            promotionImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(jumpLink)) {
                        MobclickAgent.onEvent(HomeActivity.this, MobEvent.COUNT_HOME_ALERT_AD_CLICK);
                        dismissPop();
                        Bundle bundle = new Bundle();
                        bundle.putString(QuestionCommunityActivity.H5_URL, jumpLink);
                        Constants.toActivity(HomeActivity.this, QuestionCommunityActivity.class, bundle);
                    }
                }
            });
        } else {
            promotionImg.setOnClickListener(this);
        }

    }


    @Override
    protected void onDestroy() {
        mPresenter.unRegisterMsgReceiver();
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // 双击退出页面
        return mPresenter.exit(keyCode, event);
    }


    public void jumpH5Page(String h5Url) {
        Bundle bundle = new Bundle();
        bundle.putString(QuestionCommunityActivity.H5_URL, h5Url);
        Constants.toActivity(this, QuestionCommunityActivity.class, bundle);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CLEAR_WEBCACHE://清理H5缓存
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    CommonUtils.showPermissionDialog(this, Constants.PermissionType.STORAGE);
                } else {
                    clearWebCahe();
                }
                break;
            case PEM_REQCODE_CAMERA:
                if (isGrandPermission(resultCode, Constants.PermissionType.CAMERA)) {
                    scanQRCode();
                }
                break;
            case REQ_CODE_SCAN_QRCODE:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    String resultStr = bundle.getString("qr_scan_result");
                    String qrUrl = mPresenter.getQrUrl(resultStr);
                    Bundle qBundle = new Bundle();
                    qBundle.putBoolean(QuestionCommunityActivity.QR_CODE, true);
                    qBundle.putString(QuestionCommunityActivity.H5_URL, qrUrl);
                    Constants.toActivity(this, QuestionCommunityActivity.class, qBundle);
                }
                break;
            default:
                if (resultCode == RES_UPDATE_TIKCET) {
                    if (selWebFragment != null) {
                        selWebFragment.loadWebUrl();
                    }
                }
                break;

        }


    }

    public void scanQRCode() {
        if (mPresenter.isCameraCanUse()) {
            Constants.toActivityForR(this, CaptureActivity.class, null, REQ_CODE_SCAN_QRCODE);
        } else {
            Toast.makeText(this, "请打开此应用的摄像头权限！", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateGuide(List<McDeviceInfo> deviceList) {
        if (UserHelper.isLogin(this)) {
            if (deviceList != null) {
                int len = deviceList.size();
                if (len > 0) {
                    if (len == 1) {
                        McDeviceInfo info = deviceList.get(0);
                        if (info != null) {
                            if (info.getDeviceId() != 0) {
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
