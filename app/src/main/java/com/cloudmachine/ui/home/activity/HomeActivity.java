package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
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
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.McDeviceLocation;
import com.cloudmachine.struc.Member;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.cloudmachine.ui.home.model.HomeModel;
import com.cloudmachine.ui.home.presenter.HomePresenter;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.ui.homepage.activity.ViewMessageActivity;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.ui.personal.activity.MyQRCodeActivity;
import com.cloudmachine.ui.personal.activity.PersonalDataActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ShareDialog;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.widget.NotfyImgView;
import com.github.mikephil.charting.utils.AppLog;
import com.navigation.other.Location;
import com.rey.material.widget.FloatingActionButton;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseMapActivity<HomePresenter, HomeModel> implements AMapLocationListener, LocationSource, HomeContract.View, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, AMap.OnMapClickListener, BaseRecyclerAdapter.OnItemClickListener, View.OnClickListener {
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
    LinearLayout homeHeadLyout;
    @BindView(R.id.home_menu_btn)
    FloatingActionButton menuBtn;
    @BindView(R.id.home_title_layout1)
    RelativeLayout titleLayout1;
    @BindView(R.id.item_message_tv)
    TextView itemMessageTv;
    private OnLocationChangedListener mListener = null;//定位监听器
    PopupWindow menuPop;
    List<McDeviceInfo> mDeviceList;
    Location locNow;
    ImageView promotionImg;
    PopupWindow promotionPop;
    boolean isAdShow = true;
    boolean isWindowFocus;
    HomeBannerBean mPromotionBean;
    Marker curMarker, workMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPromotionView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
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
        if (!SwitchHelper.isAdShowed(this)) {
            if (promotionPop == null) {
                View contentView = LayoutInflater.from(this).inflate(R.layout.pop_home_ad, null);
                promotionImg = (ImageView) contentView.findViewById(R.id.pop_ad_img);
                ImageView closeImg = (ImageView) contentView.findViewById(R.id.pop_ad_close_img);
                closeImg.setOnClickListener(this);
                promotionPop = getAnimPop(contentView);
            }
            mPresenter.getPromotionInfo();
        }
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
            mPresenter.getDevices(memberId, Constants.MC_DevicesList_AllType);

        }
    }

    //地图相关监听
    @Override
    public void initAMap() {
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation.getErrorCode() == 0) {
            if (isFirstLoc) {
                if (locNow == null) {
                    locNow = new Location(amapLocation.getLatitude(), amapLocation.getLongitude(), amapLocation.getAddress());
                }
//                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                if (mListener != null) {
                    mListener.onLocationChanged(amapLocation);
                }
                isFirstLoc = false;
            }
            mlocClient.stopLocation();
        }
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            isWindowFocus = true;
            AppLog.print("onWindowFocusChanged____");
            showPromotionPop();
        }
    }

    private void showPromotionPop() {
        if (isAdShow && mPromotionBean != null) {
            isAdShow = false;
            promotionPop.showAtLocation(getWindow().getDecorView(), Gravity.FILL, 0, 0);
            SwitchHelper.setSwitchPromotionAd(this, true);
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @OnClick({R.id.home_menu_btn, R.id.home_head_layout, R.id.item_message, R.id.item_ask, R.id.item_repair_history, R.id.item_card_coupon, R.id.item_qr_code, R.id.item_about, R.id.item_share_app, R.id.home_me_img, R.id.home_box_img, R.id.home_actvite_img, R.id.home_question_ans, R.id.home_flush, R.id.home_repair_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_ad_close_img:
                promotionPop.dismiss();
                break;

            case R.id.home_marker_window_layout:
                gotoDeviceDetail(view);
                break;


            case R.id.home_menu_btn:
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
//                    menuPop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    // 实例化一个ColorDrawable颜色为半透明
//                    menuPop.setAnimationStyle(R.style.PopAnimationStyle);
//                    menuPop.setFocusable(true);
//                    menuPop.setOutsideTouchable(true);
//                    menuPop.setContentView(contentView);
                }
//                menuPop.showAtLocation(getWindow().getDecorView(), Gravity.FILL, 0, 0);
                break;


            case R.id.home_me_img:
                if (UserHelper.isLogin(this)) {
                    drawerLayout.openDrawer(homeMeLayout);
                    Member member = MemeberKeeper.getOauth(this);
                    if (member != null) {
                        Glide.with(this).load(member.getLogo()).error(R.drawable.default_img).into(homeHeadImg);
                        homeNicknameTv.setText(member.getNickName());
                    }
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
            case R.id.home_box_img:
                Constants.toActivity(this, WanaCloudBox.class, null);
                break;
            case R.id.home_actvite_img:
                Constants.toActivity(this, ActivitesActivity.class, null);
                break;
            case R.id.home_question_ans:
                Bundle bundle = new Bundle();
//                bundle.putString("url", "http://h5.test.cloudm.com/n/ask_qlist");
                bundle.putString("url", ApiConstants.H5_HOST + "n/ask_qlist");
                Constants.toActivity(this, QuestionCommunityActivity.class, bundle);
                break;
            case R.id.home_flush:

                break;
            case R.id.home_repair_btn:
                Constants.toActivity(this, MaintenanceSupervisorActivity.class, null);
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
                }
                break;
            case R.id.item_repair_history:
                if (UserHelper.isLogin(this)) {
                    Constants.toActivity(this, RepairRecordNewActivity.class, null);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
            case R.id.item_card_coupon://卡券
                Constants.toActivity(this, ViewCouponActivity.class, null, false);
                break;
            case R.id.item_qr_code://我的二维码
                Bundle codeB = new Bundle();
//                codeB.putString(MEMBER_ID, String.valueOf(memberId));
                Constants.toActivity(this, MyQRCodeActivity.class, codeB);
                break;
            case R.id.item_about://关于与帮助
                Constants.toActivity(this, AboutCloudActivity.class, null);
                break;
            case R.id.item_share_app://分享app
                ShareDialog shareDialog = new ShareDialog(this, SESSIONURL, SESSIONTITLE, SESSIONDESCRIPTION, -1);
                shareDialog.show();
                MobclickAgent.onEvent(mContext, UMengKey.count_share_app);
                break;
            case R.id.home_head_layout:
                Member member = MemeberKeeper.getOauth(this);
                Bundle headB = new Bundle();
                headB.putSerializable("memberInfo", member);
                Constants.toActivity(this, PersonalDataActivity.class, headB);
                break;
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
            itemMessageTv.setActivated(true);
        } else {
            itemMessageTv.setActivated(false);
        }
    }

    @Override
    public void updateDevices(List<McDeviceInfo> deviceList) {
        int size = deviceList.size();
        if (size > 0) {
            menuBtn.setVisibility(View.VISIBLE);
        } else {
            menuBtn.setVisibility(View.GONE);
        }
        mDeviceList = deviceList;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        List<McDeviceInfo> workDeivceList = new ArrayList<>();
        for (McDeviceInfo info : deviceList) {
            Marker marker;
            McDeviceLocation location = info.getLocation();
            double lat = location.getLat();
            double lng = location.getLng();
            LatLng latLng = new LatLng(lat, lng);
            if (info.getWorkStatus() == 1) {
                marker = aMap.addMarker(getMarkerOptions(latLng, R.drawable.icon_machine_work));
                workMarker = marker;
                builder.include(latLng);
                workDeivceList.add(info);
            } else {
                marker = aMap.addMarker(getMarkerOptions(latLng, R.drawable.icon_machine_unwork));
            }
            marker.setObject(info);
        }
        LatLngBounds bounds = builder.build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
        if (workDeivceList.size() == 1) {
            if (workMarker != null) {
                workMarker.showInfoWindow();
            }
        }
    }

    @Override
    public void updatePromotionInfo(HomeBannerBean promotionBean) {
        String picUrl = promotionBean.picAddress;
        if (!TextUtils.isEmpty(picUrl)) {
            mPromotionBean = promotionBean;
            Glide.with(this).load(picUrl).into(promotionImg);
            if (isWindowFocus) {
                showPromotionPop();
            }
        }

    }

    public boolean onMarkerClick(Marker marker) {
        if (!marker.isInfoWindowShown()) {
            marker.showInfoWindow();
        }
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        AppLog.print("returnInfoView____");
        curMarker = marker;
        McDeviceInfo bean = (McDeviceInfo) marker.getObject();
        View view = LayoutInflater.from(this).inflate(R.layout.home_marker_window, null);
        TextView title_tv = (TextView) view.findViewById(R.id.marker_title_tv);
        TextView fuelconsue_tv = (TextView) view.findViewById(R.id.marker_fuelconsume_tv);
        TextView len_tv = (TextView) view.findViewById(R.id.marker_timelen_tv);
        TextView loc_tv = (TextView) view.findViewById(R.id.marker_loc_tv);
        view.setTag(bean);
        McDeviceLocation location = bean.getLocation();
        title_tv.setText(bean.getName());
        loc_tv.setText(location.getPosition());
//        fuelconsue_tv.setText(bean.getFuelCunsume());
//        len_tv.setText(bean.getTimtLen());
//        loc_tv.setText(bean.getLoc());
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return getInfoWindow(marker);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (curMarker != null) {
            curMarker.hideInfoWindow();
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
        bundle.putSerializable(Constants.MC_LOC_NOW, locNow);
        Constants.toActivity(this, DeviceDetailActivity.class, bundle);
    }


}
