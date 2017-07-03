package com.cloudmachine.ui.home.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.activities.AboutCloudActivity;
import com.cloudmachine.activities.ViewCouponActivity;
import com.cloudmachine.activities.WanaCloudBox;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.DeviceListAdpater;
import com.cloudmachine.api.ApiConstants;
import com.cloudmachine.helper.SwitchHelper;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.task.GetVersionAsync;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.McDeviceLocation;
import com.cloudmachine.struc.Member;
import com.cloudmachine.struc.VersionInfo;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.cloudmachine.ui.home.model.HomeModel;
import com.cloudmachine.ui.home.presenter.HomePresenter;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.ui.homepage.activity.ViewMessageActivity;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.ui.personal.activity.MyQRCodeActivity;
import com.cloudmachine.ui.personal.activity.PersonalDataActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.ShareDialog;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.VerisonCheckSP;
import com.cloudmachine.utils.VersionU;
import com.cloudmachine.utils.mpchart.ValueFormatUtil;
import com.cloudmachine.widget.NotfyImgView;
import com.rey.material.widget.FloatingActionButton;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseMapActivity<HomePresenter, HomeModel> implements Handler.Callback, HomeContract.View, AMap.OnMarkerClickListener, AMap.OnMapClickListener, BaseRecyclerAdapter.OnItemClickListener, View.OnClickListener {
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private static final long DEFUALT_UNLOGIN_ID = -1;
    //分享信息
    private static final String SESSIONTITLE = "云机械";
    private static final String SESSIONDESCRIPTION = "我的工程机械设备都在云机械APP，你的设备在哪里，赶紧加入吧！";
    private static final String SESSIONURL = "http://www.cloudm.com/yjx";
    @BindView(R.id.home_me_img)
    ImageView homeMeImg;
    @BindView(R.id.home_box_img)
    ImageView homeBoxImg;
    @BindView(R.id.home_actvite_img)
    NotfyImgView homeActviteImg;

    @BindView(R.id.home_question_ans)
    FloatingActionButton homeQuestionAnsBtm;
    @BindView(R.id.home_flush)
    FloatingActionButton homeFlushBtn;
    @BindView(R.id.home_repair_btn)
    Button homeRepairBtn;
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
    FrameLayout itemAbout;
    @BindView(R.id.item_share_app)
    FrameLayout itemShareApp;
    @BindView(R.id.home_me_layout)
    LinearLayout homeMeLayout;
    @BindView(R.id.home_head_layout)
    FrameLayout homeHeadLyout;
    @BindView(R.id.home_menu_btn)
    FloatingActionButton menuBtn;
    @BindView(R.id.home_title_layout1)
    RelativeLayout titleLayout1;
    @BindView(R.id.item_message_tv)
    TextView itemMessageTv;
    @BindView(R.id.item_message_nimg)
    NotfyImgView itmeMessageNimg;

    PopupWindow menuPop;
    List<McDeviceInfo> mDeviceList;
    ImageView promotionImg;
    PopupWindow promotionPop;
    boolean isAdShow = true;
    boolean isWindowFocus;
    HomeBannerBean mPromotionBean;
    Marker curMarker;
    long lasMemberId;
    private Handler mHandler;
    private MessageReceiver mMessageReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initPromotionView();
        MobclickAgent.enableEncrypt(true); // 友盟统计
        MobclickAgent.onEvent(this, UMengKey.time_home_map);
        mHandler = new Handler(this);
        registerMessageReceiver();
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
    protected void initAMap() {
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
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

    //初始化促销广告位
    private void initPromotionView() {
        if (promotionPop == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.pop_home_ad, null);
            promotionImg = (ImageView) contentView.findViewById(R.id.pop_ad_img);
            ImageView closeImg = (ImageView) contentView.findViewById(R.id.pop_ad_close_img);
            closeImg.setOnClickListener(this);
            promotionPop = getAnimPop(contentView);
        }
        mPresenter.getPromotionInfo();
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_home;
    }


    //初始化消息、机器列表
    private void loadData() {
        Member member = MemeberKeeper.getOauth(this);
        if (member != null) {
            long memberId = member.getId();
            mPresenter.updateUnReadMessage(memberId);
            if (lasMemberId == memberId) {
                return;
            }
            aMap.clear();
            aMap.invalidate();
            Glide.with(this).load(member.getLogo()).error(R.drawable.default_img).into(homeHeadImg);
            homeNicknameTv.setText(member.getNickName());
            mPresenter.getDevices(memberId, Constants.MC_DevicesList_AllType);
            lasMemberId = memberId;
        } else {
            if (lasMemberId == DEFUALT_UNLOGIN_ID) {
                return;
            }
            aMap.clear();
            aMap.invalidate();
            mPresenter.getDevices(Constants.MC_DevicesList_AllType);
            homeHeadImg.setImageResource(R.drawable.default_img);
            homeNicknameTv.setText("登录");
            lasMemberId = DEFUALT_UNLOGIN_ID;
        }
        long time = VerisonCheckSP.getTime(this);
        if (time != 0
                && System.currentTimeMillis() - time < 1000 * 60 * 60 * 24) {

        } else {
            new GetVersionAsync(mContext, mHandler).execute();
        }

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            isWindowFocus = true;
            showPromotionPop();
        }
    }

    private void showPromotionPop() {
        if (isAdShow && mPromotionBean != null) {
            isAdShow = false;
            promotionPop.showAtLocation(getWindow().getDecorView(), Gravity.FILL, 0, 0);
            SwitchHelper.setSwitchPromotionAdTime(this, mPromotionBean.gmtModified);
        }
    }


    @OnClick({R.id.home_menu_btn, R.id.home_head_layout, R.id.item_message, R.id.item_ask, R.id.item_repair_history, R.id.item_card_coupon, R.id.item_qr_code, R.id.item_about, R.id.item_share_app, R.id.home_me_img, R.id.home_box_img, R.id.home_actvite_img, R.id.home_question_ans, R.id.home_flush, R.id.home_repair_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_marker_content:
                gotoDeviceDetail(view);
                break;
            case R.id.home_marker_window_layout:
                if (curMarker != null) {
                    curMarker.hideInfoWindow();
                }
                break;

            case R.id.pop_ad_close_img:
                promotionPop.dismiss();
                break;


            case R.id.home_menu_btn:
                MobclickAgent.onEvent(mContext, UMengKey.count_menu_open);
                showMenuPop();
                break;


            case R.id.home_me_img:
                drawerLayout.openDrawer(homeMeLayout);
                break;
            case R.id.home_box_img:
                Constants.toActivity(this, WanaCloudBox.class, null);
                break;
            case R.id.home_actvite_img:
                Constants.toActivity(this, ActivitesActivity.class, null);
                break;
            case R.id.home_question_ans://问答
                MobclickAgent.onEvent(this, UMengKey.time_ask_page);
                Bundle bundle = new Bundle();
                bundle.putString("url", ApiConstants.H5_HOST + "n/ask_qlist");
                Constants.toActivity(this, QuestionCommunityActivity.class, bundle);
                break;
            case R.id.home_flush:
                MobclickAgent.onEvent(mContext, UMengKey.count_machine_refresh);
                flush();
                break;

            case R.id.home_repair_btn:
                if (UserHelper.isLogin(this)) {
                    Constants.toActivity(this, MaintenanceSupervisorActivity.class, null);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
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
                    Member member = MemeberKeeper.getOauth(this);
                    Long wjdsId = member.getWjdsId();
                    if (wjdsId != null) {
                        Bundle askBundle = new Bundle();
                        askBundle.putString("url", ApiConstants.H5_HOST + "n/ask_myq?myid=" + wjdsId);
                        Constants.toActivity(this, QuestionCommunityActivity.class, askBundle, false);
                    }
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
                    Constants.toActivity(this, ViewCouponActivity.class, null, false);
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
                if (UserHelper.isLogin(this)) {
                    Constants.toActivity(this, AboutCloudActivity.class, null);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
            case R.id.item_share_app://分享app
                if (UserHelper.isLogin(this)) {
                    ShareDialog shareDialog = new ShareDialog(this, SESSIONURL, SESSIONTITLE, SESSIONDESCRIPTION, -1);
                    shareDialog.show();
                    MobclickAgent.onEvent(mContext, UMengKey.count_share_app);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
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

    private void showMenuPop() {
        if (curMarker != null) {
            curMarker.hideInfoWindow();
        }
        if (menuPop == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.pop_home_menu, null);
            TextView devicesNumTv = (TextView) contentView.findViewById(R.id.pop_devie_num_tv);
            RecyclerView devicesListRlv = (RecyclerView) contentView.findViewById(R.id.pop_device_list_rlv);
            devicesListRlv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            ImageView deviceCloseImg = (ImageView) contentView.findViewById(R.id.pop_device_close_img);
            deviceCloseImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuPop.dismiss();
                }
            });
            devicesNumTv.setText("设备(" + mDeviceList.size() + ")");
            devicesListRlv.setLayoutManager(new LinearLayoutManager(this));
            DeviceListAdpater adpater = new DeviceListAdpater(this, mDeviceList);
            adpater.setOnItemClickListener(this);
            devicesListRlv.setAdapter(adpater);
            menuPop = getAnimPop(contentView);
        }
        menuPop.showAtLocation(getWindow().getDecorView(), Gravity.FILL, 0, 0);
    }

    private void flush() {
        aMap.clear();
        aMap.invalidate();
        if (UserHelper.isLogin(this)) {
            mPresenter.getDevices(UserHelper.getMemberId(this), Constants.MC_DevicesList_AllType);
        } else {
            mPresenter.getDevices(Constants.MC_DevicesList_AllType);
        }
    }

    public PopupWindow getAnimPop(View contentView) {
        PopupWindow pop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 实例化一个ColorDrawable颜色为半透明
        pop.setAnimationStyle(R.style.PopAnimationStyle);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(contentView);
        return pop;
    }

    //消息小红点
    @Override
    public void updateMessageCount(int count) {
        if (count > 0) {
            itmeMessageNimg.setNotifyPointVisible(true);
        } else {
            itmeMessageNimg.setNotifyPointVisible(false);
        }
    }

    @Override
    public void updateDevices(List<McDeviceInfo> deviceList) {
        int size = deviceList.size();
        if (size > 1) {
            menuBtn.setVisibility(View.VISIBLE);
        } else {
            menuBtn.setVisibility(View.GONE);
        }
        mDeviceList = deviceList;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (McDeviceInfo info : deviceList) {
            Marker marker;
            McDeviceLocation location = info.getLocation();
            double lat = location.getLat();
            double lng = location.getLng();
            LatLng latLng = new LatLng(lat, lng);
            builder.include(latLng);
            if (info.getWorkStatus() == 1) {
                if (info.getId() == 0) {
                    marker = aMap.addMarker(getMarkerOptions(latLng, R.drawable.icon_machine_experience));
                } else {
                    marker = aMap.addMarker(getMarkerOptions(latLng, R.drawable.icon_machine_work));
                }
            } else {
                marker = aMap.addMarker(getMarkerOptions(latLng, R.drawable.icon_machine_unwork));
            }
            marker.setObject(info);
            curMarker = marker;
        }
        LatLngBounds bounds = builder.build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
        if (deviceList.size() == 1) {
            if (curMarker != null) {
                curMarker.showInfoWindow();
            }
        }
    }

    @Override
    public void updatePromotionInfo(HomeBannerBean promotionBean) {
        String picUrl = promotionBean.picAddress;
        if (!TextUtils.isEmpty(picUrl)) {
            if (SwitchHelper.isAdShowed(this, promotionBean.gmtModified)) {
                return;
            }
            SwitchHelper.setSwitchPromotionAdTime(this, promotionBean.gmtModified);
            mPromotionBean = promotionBean;
            Glide.with(this).load(picUrl).into(promotionImg);
            if (isWindowFocus) {
                showPromotionPop();
            }
        }

    }


    @Override
    public void onItemClick(View view, int position) {
        gotoDeviceDetail(view);
    }

    private void gotoDeviceDetail(View view) {
        if (menuPop != null && menuPop.isShowing()) {
            menuPop.dismiss();
        }
        McDeviceInfo info = (McDeviceInfo) view.getTag();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.MC_DEVICEINFO, info);
//        bundle.putSerializable(Constants.MC_LOC_NOW, locNow);
        Constants.toActivity(this, DeviceDetailActivity.class, bundle);
    }


    @Override
    public View getMarkerInfoView(Marker marker) {
        McDeviceInfo bean = (McDeviceInfo) marker.getObject();
        View view = LayoutInflater.from(this).inflate(R.layout.home_marker_window, null);
        RelativeLayout contentLayout = (RelativeLayout) view.findViewById(R.id.home_marker_content);
        TextView title_tv = (TextView) view.findViewById(R.id.marker_title_tv);
        TextView oillave_tv = (TextView) view.findViewById(R.id.marker_oillave_tv);
        TextView worktime_tv = (TextView) view.findViewById(R.id.marker_timelen_tv);
        TextView loc_tv = (TextView) view.findViewById(R.id.marker_loc_tv);
        contentLayout.setTag(bean);
        McDeviceLocation location = bean.getLocation();
        title_tv.setText(bean.getName());
        oillave_tv.setText(bean.getOilLave() + "%");
        worktime_tv.setText(ValueFormatUtil.getWorkTime(bean.getWorkTime(), "0时"));
        loc_tv.setText(location.getPosition());
        //体验机 禁止进入设备详情页 todo:待调整
        contentLayout.setOnClickListener(this);
        view.setOnClickListener(this);
        return view;
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_GETVERSION_SUCCESS:
                VersionInfo vInfo = (VersionInfo) msg.obj;
                if (null != vInfo) {
                    Constants.MyLog(vInfo.getMustUpdate() + "1111111");
                    if (vInfo.getMustUpdate() == -1) {
                        // Constants.MyToast(vInfo.getMessage());
                    } else if (vInfo.getMustUpdate() == 0) {
                        boolean isUpdate = CommonUtils.checVersion(VersionU.getVersionName(), vInfo.getVersion());
                        if (isUpdate) {
                            Constants.updateVersion(this, mHandler,
                                    vInfo.getMessage(), vInfo.getLink());
                        }

                    }
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
}
