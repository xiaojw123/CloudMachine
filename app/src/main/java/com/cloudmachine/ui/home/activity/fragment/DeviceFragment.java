package com.cloudmachine.ui.home.activity.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cloudmachine.R;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.DeviceListAdpater;
import com.cloudmachine.bean.AdBean;
import com.cloudmachine.bean.LarkDeviceDetail;
import com.cloudmachine.bean.LarkDeviceInfo;
import com.cloudmachine.bean.LarkLocBean;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.activity.DeviceDetailActivity;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.home.activity.QuestionCommunityActivity;
import com.cloudmachine.ui.home.contract.DeviceContract;
import com.cloudmachine.ui.home.model.DeviceModel;
import com.cloudmachine.ui.home.presenter.DevicePresenter;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiaojw on 2017/7/19.
 * 高德地图2D转3D  Amap刷新flus-->reload
 */

public class DeviceFragment extends BaseMapFragment<DevicePresenter, DeviceModel> implements Handler.Callback, DeviceContract.View, View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener, XRecyclerView.LoadingListener {
    @BindView(R.id.device_flush_imgBtn)
    ImageButton homeFlushBtn;
    @BindView(R.id.device_phone_imgBtn)
    ImageButton phoneBtn;
    @BindView(R.id.device_menu_tv)
    TextView menuTv;
    @BindView(R.id.device_vp)
    ViewPager deviceVp;
    @BindView(R.id.device_box_act_tv)
    TextView boxActTv;
    @BindView(R.id.device_redpacket_img)
    ImageView redPacketImg;

    List<McDeviceInfo> mDeviceList;
    List<LarkDeviceInfo> mLarkDeviceList;
    PopupWindow menuPop, phonePop;
    TextView devicesNumTv;
    DeviceListAdpater deviceListAdpater;
    List<View> viewList;
    ClearEditTextView popSearchEdt;
    View popLineView;
    XRecyclerView devicesListRlv;
    List<AdBean> mItems;
    Handler mHandler;
    int actIndex, actLen;
    String mBoxTel = Constants.CUSTOMER_PHONE_BOX;
    String mRepairTel = Constants.CUSTOMER_PHONE_REPAIR;
    private int deviceSize;
    long memberId = Constants.INVALID_DEVICE_ID;
    private List<LarkDeviceDetail> mAllDeviceList = new ArrayList<>();
    private int pageNum, mToalSize;
    private boolean isRefresh, isLoadMore, isSearch, isLastPage;

    @Override
    protected void initView() {
        MobclickAgent.onEvent(getActivity(), MobEvent.TIME_HOME_MAP);
        mPresenter.registerRxEvent();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()) {
            MobclickAgent.onEvent(getActivity(), MobEvent.TIME_HOME_MAP);
        }
        loadData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            MobclickAgent.onEvent(getActivity(), MobEvent.TIME_HOME_MAP);
            loadData();
        }
    }

    @Override
    protected void setAMap() {
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMarkerClickListener(this);
    }


    private void loadData() {

        mPresenter.getDeviceMapList(1);
        if (UserHelper.isLogin(getActivity())) {
            memberId = UserHelper.getMemberId(getActivity());
            if (mLarkDeviceList != null && mLarkDeviceList.size() > 1) {
                menuTv.setVisibility(View.VISIBLE);
            } else {
                menuTv.setVisibility(View.GONE);
            }
            mPresenter.getAllDeviceList(1, null);
        } else {
            memberId = Constants.INVALID_DEVICE_ID;
            aMap.clear();
            aMap.reloadMap();
            menuTv.setVisibility(View.GONE);
        }
        if (mItems == null) {
            mHandler = new Handler(this);
            ((HomeActivity) getActivity()).obtainSystemAd(HomeActivity.AD_ROLL);
        }
        mPresenter.getServiceTel();
        mPresenter.getReadPacketConfig();
    }


    private void showPhonePop() {
        if (phonePop == null) {
            View phoneView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_home_phoe, null);
            RelativeLayout alertView = (RelativeLayout) phoneView.findViewById(R.id.pop_alert_view);
            TextView boxTv = (TextView) phoneView.findViewById(R.id.phone_box_tv);
            TextView repairTv = (TextView) phoneView.findViewById(R.id.phone_repair_tv);
            TextView cancelTv = (TextView) phoneView.findViewById(R.id.phone_cancel_tv);
            alertView.setOnClickListener(this);
            View bgView = phoneView.findViewById(R.id.pop_device_bg);
            bgView.setOnClickListener(this);
            cancelTv.setOnClickListener(this);
            boxTv.setOnClickListener(this);
            repairTv.setOnClickListener(this);
            phonePop = CommonUtils.getAnimPop(phoneView);
        }
        phonePop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.FILL, 0, 0);
    }


    private void showMenuPop() {
        if (mAllDeviceList == null) {
            return;
        }
        if (menuPop == null) {
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_home_menu, null);
            View menuPopBg = contentView.findViewById(R.id.pop_device_bg);
            View menuPopCo = contentView.findViewById(R.id.pop_device_contentlayout);
            popLineView = contentView.findViewById(R.id.pop_device_line);
            popSearchEdt = (ClearEditTextView) contentView.findViewById(R.id.pop_device_search_edt);
            popSearchEdt.setOnEditorActionListener(serachActionListener);
            popSearchEdt.addTextChangedListener(searchWather);
            menuPopCo.setOnClickListener(this);
            menuPopBg.setOnClickListener(this);
            devicesNumTv = (TextView) contentView.findViewById(R.id.pop_devie_num_tv);
            devicesListRlv = (XRecyclerView) contentView.findViewById(R.id.pop_device_list_rlv);
            devicesListRlv.setPullRefreshEnabled(true);
            devicesListRlv.setLoadingMoreEnabled(true);
            devicesListRlv.setLoadingListener(this);
            if (mToalSize >= 4) {
                devicesListRlv.getLayoutParams().height = DensityUtil.dip2px(getActivity(), 427);

            }
            devicesListRlv.setLayoutManager(new LinearLayoutManager(getActivity()));
            deviceListAdpater = new DeviceListAdpater(getActivity(), mAllDeviceList);
            deviceListAdpater.setOnItemClickListener(this);
            devicesListRlv.setAdapter(deviceListAdpater);
            menuPop = CommonUtils.getAnimPop(contentView);
        } else {
            deviceListAdpater.updateItems(mAllDeviceList);
            devicesListRlv.smoothScrollToPosition(0);
        }
        devicesNumTv.setText("全部(" + mToalSize + ")");
        if (mToalSize >= 20) {
            popSearchEdt.setVisibility(View.VISIBLE);
            popLineView.setVisibility(View.GONE);
            Editable editable = popSearchEdt.getText();
            if (editable != null && editable.length() > 0) {
                updateSearchList();
            }
        } else {
            popSearchEdt.setVisibility(View.GONE);
            popLineView.setVisibility(View.VISIBLE);
        }
        menuPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.FILL, 0, 0);
    }

    TextWatcher searchWather = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateSearchList();
        }
    };

    private void updateSearchList() {
        isSearch = true;
        devicesListRlv.refresh();
    }


    TextView.OnEditorActionListener serachActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ((InputMethodManager) getActivity().getApplication().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(popSearchEdt.getWindowToken(), 0);
            }
            return false;
        }
    };


    private void flush() {
        aMap.clear();
        aMap.reloadMap();
        mPresenter.getDeviceMapList(1);
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_device;
    }

    LatLngBounds.Builder latLngBuilder;


    @Override
    public void retrunAllDeviceError() {
        resetScrollStatus();
    }

    @Override
    public void returnAllDeviceList(List<LarkDeviceDetail> deviceList, boolean isFirst, boolean isLast, int toalSize) {
        mToalSize = toalSize;
        isLastPage = isLast;
        if (isFirst) {
            pageNum = 1;
            mAllDeviceList.clear();
        }
        mAllDeviceList.addAll(deviceList);
        if (isRefresh || isLoadMore || isSearch) {
            resetScrollStatus();
            if (deviceListAdpater != null) {
                deviceListAdpater.updateItems(mAllDeviceList);
            }
        }
    }

    private void resetScrollStatus() {
        if (isSearch) {
            isSearch = false;
            devicesNumTv.setText("全部(" + mToalSize + ")");
        }
        if (isRefresh) {
            isRefresh = false;
            devicesListRlv.refreshComplete();
        }
        if (isLoadMore) {
            isLoadMore = false;
            devicesListRlv.loadMoreComplete();
        }
    }

    @Override
    public void updateLarkDevices(List<LarkDeviceInfo> deviceList) {
        aMap.clear();
        aMap.reloadMap();
        mLarkDeviceList = deviceList;
        viewList = new ArrayList<>();
        if (mLarkDeviceList != null) {
            deviceSize = mLarkDeviceList.size();
            if (deviceSize > 0) {
                deviceVp.setVisibility(View.VISIBLE);
                latLngBuilder = LatLngBounds.builder();
                UserHelper.setOwner(getActivity(), memberId, false);
                addLarkMarkerView(0);
            } else {
                menuTv.setVisibility(View.GONE);
                deviceVp.setVisibility(View.INVISIBLE);
            }
        } else {
            menuTv.setVisibility(View.GONE);
            deviceVp.setVisibility(View.INVISIBLE);
        }
    }


    //递归遍历DeviceList添加Marker
    public void addLarkMarkerView(final int position) {
        final LarkDeviceInfo info = mLarkDeviceList.get(position);
        checkOwner(info);
        String imgUrl = CommonUtils.getMarkerIconUrl(info.getTypePicUrl(), info.getWorkStatus());
        final LatLng latLng = new LatLng(info.getLat(), info.getLng());
        latLngBuilder.include(latLng);
        Glide.with(this).load(imgUrl)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        ImageView img = new ImageView(getActivity());
                        img.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(getActivity(), 50), DensityUtil.dip2px(getActivity(), 37)));
                        img.setImageDrawable(resource);
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                        MarkerOptions options = new MarkerOptions();
                        options.icon(BitmapDescriptorFactory.fromView(img));
                        options.position(latLng);
                        Marker marker = aMap.addMarker(options);
                        marker.setObject(info);
                        curMarker = marker;
                        viewList.add(getItemView(marker));
                        if (position < deviceSize - 1) {
                            addLarkMarkerView(position + 1);
                        } else {
                            updateViewPager(viewList.size());
                        }
                    }
                });


    }

    private void checkOwner(LarkDeviceInfo info) {
        if (!UserHelper.isOwner(getActivity(), memberId)) {
            if (Constants.isOwner(info.getRoleType())) {
                UserHelper.setOwner(getActivity(), memberId, true);
            }
        }
    }


    public void updateViewPager(int pageLen) {
        if (pageLen > 1) {
            menuTv.setVisibility(View.VISIBLE);
        } else {
            menuTv.setVisibility(View.GONE);
        }
        if (pageLen > 0) {
            if (pageLen > 1) {
                setMapZoomTo(ZOOM_HOME);
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(), 0));
            } else {
                setMapZoomTo(ZOOM_DEFAULT);
            }
            setSelectedMarker(0);
        }
        deviceVp.addOnPageChangeListener(onPageChangeL);
        deviceVp.setAdapter(new DevicePagerAdapter(viewList));
        deviceVp.setCurrentItem(0);
        if (!UserHelper.getGuideTag(getActivity())) {
            UserHelper.insertGuideTag(getActivity(), true);
            ((HomeActivity) getActivity()).updateGuide(mDeviceList);
        }
    }

    public void updateRollAd(List<AdBean> items) {
        mItems = items;
        if (mItems != null && mItems.size() > 0) {
            boxActTv.setVisibility(View.VISIBLE);
            if (mItems != null && mItems.size() > 0) {
                actIndex = 0;
                actLen = mItems.size();
                setCurrentBoxAct();
            }
        } else {
            boxActTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void retrunGetServiceTel(String boxTel, String repairTel) {
        if (!TextUtils.isEmpty(boxTel)) {
            mBoxTel = boxTel;
        }
        if (!TextUtils.isEmpty(repairTel)) {
            mRepairTel = repairTel;
        }

    }

    @Override
    public void retrunPacketConfig(String imgUrl, String jumpUrl, final boolean isNoOpen) {
        redPacketImg.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(getActivity()).load(imgUrl).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    redPacketImg.setImageDrawable(resource);
                    if (isNoOpen) {
                        mPresenter.startRedPacketAnim(redPacketImg);
                    }
                }
            });
        } else {
            redPacketImg.setImageResource(R.drawable.ic_red_packet);
            if (isNoOpen) {
                mPresenter.startRedPacketAnim(redPacketImg);
            }
        }
        redPacketImg.setEnabled(!TextUtils.isEmpty(jumpUrl));
        redPacketImg.setTag(jumpUrl);

    }

    @Override
    public void hidenPacket() {
        redPacketImg.setVisibility(View.GONE);
    }

    @Override
    public void updateDevicesError(String errorMsg) {
        aMap.clear();
        aMap.reloadMap();
        menuTv.setVisibility(View.GONE);
        deviceVp.setVisibility(View.INVISIBLE);
    }

    private void setCurrentBoxAct() {
        AdBean info = mItems.get(actIndex);
        if (info != null) {
            boxActTv.setTag(info);
            boxActTv.setText(info.getAdTitle());
            String adJumpLink = info.getAdJumpLink();
            if (!TextUtils.isEmpty(adJumpLink)) {
                boxActTv.setEnabled(true);
                Drawable rd = getResources().getDrawable(R.drawable.ic_box_arrow_right);
                boxActTv.setCompoundDrawablesWithIntrinsicBounds(null, null, rd, null);
            } else {
                boxActTv.setEnabled(false);
                boxActTv.setCompoundDrawables(null, null, null, null);
            }
            if (actLen > 1) {
                mHandler.sendEmptyMessageDelayed(Constants.HANDLER_CHANGE_BOX_ACT, 10000);
            }
        }
    }


    private View getItemView(Marker marker) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_deviceinfo, null);
        v.setTag(marker);
        v.setOnClickListener(this);
        return v;
    }

    ViewPager.OnPageChangeListener onPageChangeL = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setSelectedMarker(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void setSelectedMarker(int position) {
        View itemView = viewList.get(position);
        Marker itemMarker = (Marker) itemView.getTag();
        itemMarker.showInfoWindow();
        aMap.moveCamera(CameraUpdateFactory.changeLatLng((itemMarker.getPosition())));
        LarkDeviceInfo info = (LarkDeviceInfo) itemMarker.getObject();
        mPresenter.updatePagerItem(info.getDeviceId(), itemView);
    }


    @OnClick({R.id.device_redpacket_img, R.id.device_box_act_tv, R.id.device_phone_imgBtn, R.id.device_menu_tv, R.id.device_flush_imgBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.device_redpacket_img://红包跳转
                String jumpUrl = (String) view.getTag();
                Bundle data = new Bundle();
                data.putString(QuestionCommunityActivity.H5_URL, jumpUrl);
                Constants.toActivity(getActivity(), QuestionCommunityActivity.class, data);
                break;

            case R.id.device_box_act_tv:
                AdBean info = (AdBean) view.getTag();
                if (info != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(QuestionCommunityActivity.H5_URL, info.getAdJumpLink());
                    bundle.putString(QuestionCommunityActivity.SHARE_PIC, info.getAdPicUrl());
                    bundle.putString(QuestionCommunityActivity.SHARE_LINK, info.getAdJumpLink());
                    Constants.toActivity(getActivity(), QuestionCommunityActivity.class, bundle);
                }
                break;
            case R.id.phone_box_tv://客服热线-云盒子
                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_HOME_CALL_CLICK);
                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_HOME_CALL_YUNBOX_CLICK);
                CommonUtils.callPhone(getActivity(), mBoxTel);
                break;
            case R.id.phone_repair_tv://客服热线-报修
                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_HOME_CALL_CLICK);
                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_HOME_CALL_REPAIR_CLICK);
                CommonUtils.callPhone(getActivity(), mRepairTel);
                break;
            case R.id.device_phone_imgBtn:
                showPhonePop();
                break;
            case R.id.phone_cancel_tv:
            case R.id.pop_device_bg:
                if (menuPop != null && menuPop.isShowing()) {
                    menuPop.dismiss();
                }
                if (phonePop != null && phonePop.isShowing()) {
                    phonePop.dismiss();
                }
                break;

            case R.id.device_info_cotainer:
                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_CLICK_MACHINE_DETAIL_FROM_UNDER_INFOVIEW);
                Marker marker = (Marker) view.getTag();
                gotoDeviceDetail((LarkDeviceInfo) marker.getObject());
                break;
            case R.id.device_flush_imgBtn:
                MobclickAgent.onEvent(getActivity(), UMengKey.count_machine_refresh);
                flush();
                break;

            case R.id.device_menu_tv:
                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_HOME_ALL_LIST_CLICK);
                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_MENU_OPEN);
                showMenuPop();
                break;
        }


    }

    @Override
    public void onItemClick(View view, int position) {
        MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_CLICK_MACHINE_DETAIL_FROM_LIST);
        LarkDeviceDetail detail = (LarkDeviceDetail) view.getTag();
        LarkDeviceInfo info = new LarkDeviceInfo();
        info.setDeviceId(detail.getDeviceId());
        info.setDeviceName(detail.getDeviceName());
        info.setTypeId(detail.getTypeId());
        info.setTypePicUrl(detail.getTypePicUrl());
        info.setCategory(detail.getCategory());
        info.setRoleType(detail.getRoleType());
        info.setSnId(detail.getSnId());
        info.setWorkStatus(detail.getWorkStatus());
        LarkLocBean loc = detail.getLocation();
        if (loc != null) {
            info.setLat(loc.getLat());
            info.setLng(loc.getLng());
        }
        gotoDeviceDetail(info);
    }

    @Override
    public View getMarkerInfoView(Marker marker) {
        final LarkDeviceInfo bean = (LarkDeviceInfo) marker.getObject();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = DensityUtil.dip2px(getActivity(), 7);
        TextView title_tv = new TextView(getActivity());
        title_tv.setGravity(Gravity.CENTER);
        title_tv.setBackground(getResources().getDrawable(R.drawable.bg_marker_win));
        title_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        int left = DensityUtil.dip2px(getActivity(), 12);
        title_tv.setPadding(left, 0, left, DensityUtil.dip2px(getActivity(), 7));
        title_tv.setTextColor(getResources().getColor(R.color.black));
        title_tv.setLayoutParams(params);
        title_tv.setMaxEms(12);
        title_tv.setEllipsize(TextUtils.TruncateAt.END);
        title_tv.setSingleLine();
        title_tv.setText(bean.getDeviceName());
        title_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_CLICK_MACHINE_DETAIL_FROM_ANNOTATION);
                gotoDeviceDetail(bean);
            }
        });
        int index = mLarkDeviceList.indexOf(bean);
        if (deviceVp.getCurrentItem() != index) {
            deviceVp.setCurrentItem(index);
        }
        return title_tv;
    }


    private void gotoDeviceDetail(LarkDeviceInfo info) {
        MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_CLICK_MACHINE_DETAIL);
        if (menuPop != null && menuPop.isShowing()) {
            menuPop.dismiss();
        }
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.DEVICE_ID, info.getDeviceId());
        bundle.putBoolean(Constants.IS_OWNER, Constants.isOwner(info.getRoleType()));
        Constants.toActivity(getActivity(), DeviceDetailActivity.class, bundle);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == Constants.HANDLER_CHANGE_BOX_ACT) {
            ++actIndex;
            actIndex = actLen > actIndex ? actIndex : 0;
            setCurrentBoxAct();
        }
        return false;
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageNum = 1;
        mPresenter.getAllDeviceList(pageNum, getSearchKey());

    }

    public String getSearchKey() {
        String searchKey = popSearchEdt.getText().toString();
        if (searchKey.length() == 0) {
            searchKey = null;
        }
        return searchKey;
    }

    @Override
    public void onLoadMore() {
        if (!isLastPage) {
            isLoadMore = true;
            pageNum++;
            mPresenter.getAllDeviceList(pageNum, getSearchKey());
        } else {
            isLoadMore = false;
            devicesListRlv.setNoMore(true);
        }
    }


    private class DevicePagerAdapter extends PagerAdapter {
        List<View> mViewList;

        DevicePagerAdapter(List<View> viewList) {
            mViewList = viewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itmeView = mViewList.get(position);
            container.addView(itmeView);
            return itmeView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mHandler != null) {
            mHandler.removeMessages(Constants.HANDLER_CHANGE_BOX_ACT);
        }
        super.onDestroy();
    }
}

