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
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.Marker;
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
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.VersionInfo;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.net.task.GetVersionAsync;
import com.cloudmachine.ui.home.activity.fragment.DeviceFragment;
import com.cloudmachine.ui.home.activity.fragment.MaintenanceFragment;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.cloudmachine.ui.home.model.HomeModel;
import com.cloudmachine.ui.home.model.PopItem;
import com.cloudmachine.ui.home.presenter.HomePresenter;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.ui.homepage.activity.ViewMessageActivity;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.ui.personal.activity.MyQRCodeActivity;
import com.cloudmachine.ui.personal.activity.PersonalDataActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.VersionU;
import com.cloudmachine.widget.NotfyImgView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/*
* 更新通知提醒需测试*/
public class HomeActivity extends BaseAutoLayoutActivity<HomePresenter, HomeModel> implements Handler.Callback, HomeContract.View, View.OnClickListener {
    public static final String RXEVENT_UPDATE_REMIND = "rxevent_update_remind";
    public static final String KEY_H5_AUTORITY = "key_h5_autority";
    private static final String AUTORITY_YUNBOX = "yunbox";
    private static final String AUTORITY_MYORDER = "myOrder";
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private static final String ACT_SP_NAME = "activities_sp";
    private static final String KEY_ACT_SIZE = "key_act_size";
    public static final int PEM_REQCODE_WRITESD = 0x113;
    private String downLoadLink;
    @BindView(R.id.home_me_img)
    ImageView homeMeImg;
    @BindView(R.id.home_box_img)
    ImageView homeBoxImg;
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
    @BindView(R.id.item_ask)
    FrameLayout itemAsk;
    @BindView(R.id.item_repair_history)
    FrameLayout itemRepairHistory;
    @BindView(R.id.item_card_coupon)
    FrameLayout itemCardCoupon;
    @BindView(R.id.item_qr_code)
    FrameLayout itemQrCode;
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
    @BindView(R.id.home_me_img_alert)
    View meAlert;
    @BindView(R.id.item_my_order)
    FrameLayout itemMyOrder;
    @BindView(R.id.item_about_niv)
    NotfyImgView aboutNimg;


    ImageView promotionImg;
    PopupWindow promotionPop;
    List<PopItem> mItems;
    Marker curMarker;
    private Handler mHandler;
    private MessageReceiver mMessageReceiver;

    Fragment deviceFragment, maintenaceFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        MobclickAgent.enableEncrypt(true); // 友盟统计
        MobclickAgent.onEvent(this, MobEvent.TIME_HOME);
        mHandler = new Handler(this);
        registerMessageReceiver();
        showFragment(deviceTv);
        mPresenter.getH5ConfigInfo();
        initUpdateConfig();
        new GetVersionAsync(mContext, mHandler).execute();
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
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (titleView == deviceTv) {
            deviceTv.setTextColor(Color.BLACK);
            maintenanceTv.setTextColor(Color.GRAY);
            if (deviceFragment == null) {
                deviceFragment = new DeviceFragment();
                if (maintenaceFragment != null) {
                    ft.hide(maintenaceFragment);
                }
                ft.add(R.id.home_fragment_cotainer, deviceFragment);
            } else {
                if (deviceFragment.isVisible()) {
                    return;
                }
                ft.hide(maintenaceFragment);
                ft.show(deviceFragment);
            }
        }
        if (titleView == maintenanceTv) {
            deviceTv.setTextColor(Color.GRAY);
            maintenanceTv.setTextColor(Color.BLACK);
            if (maintenaceFragment == null) {
                maintenaceFragment = new MaintenanceFragment();
                if (deviceFragment != null) {
                    ft.hide(deviceFragment);
                }
                ft.add(R.id.home_fragment_cotainer, maintenaceFragment);
            } else {
                if (maintenaceFragment.isVisible()) {
                    return;
                }
                ft.hide(deviceFragment);
                ft.show(maintenaceFragment);
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
        }
        mPresenter.getHomeBannerInfo();
//        long time = VerisonCheckSP.getTime(this);
//        if (time != 0
//                && System.currentTimeMillis() - time < 1000 * 60 * 60 * 24) {
//
//        } else {

//        }

    }

//    private void testPromption() {
//        List<PopItem> items = new ArrayList<>();
//        PopItem item1 = new PopItem();
//        item1.setActPictureLink("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502961692435&di=75956a99c9e00870c3039659eba01471&imgtype=0&src=http%3A%2F%2Fi2.qhimg.com%2Ft018d01274dd54968d4.jpg");
//        item1.setJumpLink("http://www.geekpark.net/");
//        item1.setPopupType(2);
//        items.add(item1);
//        PopItem item2 = new PopItem();
//        item2.setActPictureLink("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502961769796&di=0331a6a42253271286e42d70bb7e61e6&imgtype=0&src=http%3A%2F%2Fimg.qqzhi.com%2Fupload%2Fimg_4_2347346394D2346761690_23.jpg");
//        item2.setJumpLink("http://www.jianshu.com/");
//        item2.setPopupType(2);
//        items.add(item2);
//        PopItem item3 = new PopItem();
//        item3.setActPictureLink("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502961823875&di=f326ebb245e1e119726ca75da4042eff&imgtype=0&src=http%3A%2F%2Fimg.mp.sohu.com%2Fupload%2F20170720%2F184865028df4431da335cd01c9173db0_th.png");
//        item3.setJumpLink("https://www.huxiu.com/");
//        item3.setPopupType(2);
//        items.add(item3);
//        updatePromotionInfo(items);
//    }


    @OnClick({R.id.item_my_order, R.id.home_title_device, R.id.home_title_maintenance, R.id.home_head_layout, R.id.item_message, R.id.item_ask, R.id.item_repair_history, R.id.item_card_coupon, R.id.item_qr_code, R.id.item_about, R.id.home_me_img, R.id.home_box_img, R.id.home_actvite_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_my_order:
                MobclickAgent.onEvent(this,MobEvent.TIME_H5_MY_ORDER_PAGE);
                Bundle bundle = new Bundle();
                bundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppOrderList);
                Constants.toActivity(this, QuestionCommunityActivity.class, bundle);
                break;

            case R.id.home_title_device:
            case R.id.home_title_maintenance:
                showFragment(view);
                break;
            case R.id.home_marker_window_layout:
                if (curMarker != null) {
                    curMarker.hideInfoWindow();
                }
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
            case R.id.home_box_img:
                Bundle boxExtras = new Bundle();
                boxExtras.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppBoxDetail);
                Constants.toActivity(this, QuestionCommunityActivity.class, null);
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
            case R.id.item_ask://我的提问
                if (UserHelper.isLogin(this)) {
                    MobclickAgent.onEvent(this,MobEvent.TIME_H5_MY_ASK_PAGE);
                    Bundle askBundle = new Bundle();
                    askBundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppMyQuestion);
                    Constants.toActivity(this, QuestionCommunityActivity.class, askBundle, false);
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
            case R.id.item_card_coupon://卡券
                if (UserHelper.isLogin(this)) {
//                    Constants.toActivity(this, ViewCouponActivity.class, null, false);
                    //TODO: 2017/9/29 优惠券改版-code138
                    Constants.toActivity(this, ViewCouponActivityNew.class, null, false);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
            case R.id.item_qr_code://我的二维码
                if (UserHelper.isLogin(this)) {
                    Bundle codeB = new Bundle();
//                codeB.putString(MEMBER_ID, String.valueOf(memberId));
                    Constants.toActivity(this, MyQRCodeActivity.class, codeB);
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
    public void updateMessageCount(int count) {
        if (count > 0) {
            itmeMessageNimg.setNotifyPointVisible(true);
            meAlert.setVisibility(View.VISIBLE);
        } else {
            itmeMessageNimg.setNotifyPointVisible(false);
            if (!aboutNimg.isNotifyVisbile()) {
                meAlert.setVisibility(View.GONE);
            }
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
        String authory = getAuthory();
        if (AUTORITY_YUNBOX.equals(authory)) {
            jumpH5Page(ApiConstants.AppBoxDetail);
        } else if (AUTORITY_MYORDER.equals(authory)) {
            if (UserHelper.isLogin(this)) {
                jumpH5Page(ApiConstants.AppOrderList);
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
                                MobclickAgent.onEvent(HomeActivity.this,MobEvent.COUNT_HOME_ALERT_AD_CLICK);
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
                        Constants.updateVersion(this, mHandler,
                                vInfo.getMustUpdate(), vInfo.getMessage(), vInfo.getLink());
                    }

                }
                break;
            case Constants.HANDLER_VERSIONDOWNLOAD:
                downLoadLink = (String) msg.obj;
                PermissionsChecker checker = new PermissionsChecker(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checker.lacksPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    PermissionsActivity.startActivityForResult(this, PEM_REQCODE_WRITESD,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                MobclickAgent.onKillProcess(this);
                System.exit(0);
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
        if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            ToastUtils.showToast(this, "更新失败！！");
            CommonUtils.showPermissionDialog(this);
        } else {
            Constants.versionDownload(HomeActivity.this, downLoadLink);
        }

    }
}
