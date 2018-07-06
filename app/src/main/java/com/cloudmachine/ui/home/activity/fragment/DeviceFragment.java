package com.cloudmachine.ui.home.activity.fragment;

import android.content.Context;
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
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.base.bean.PageBean;
import com.cloudmachine.bean.ArticleInfo;
import com.cloudmachine.bean.DeviceItem;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.activity.DeviceDetailActivity;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.home.contract.DeviceContract;
import com.cloudmachine.ui.home.contract.ExtrContract;
import com.cloudmachine.ui.home.model.DeviceModel;
import com.cloudmachine.ui.home.presenter.DevicePresenter;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.locatecity.PingYinUtil;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by xiaojw on 2017/7/19.
 * 高德地图2D转3D  Amap刷新flus-->reload
 */

public class DeviceFragment extends BaseMapFragment<DevicePresenter, DeviceModel> implements Handler.Callback, DeviceContract.View, View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener, XRecyclerView.LoadingListener {
    private static final long DEFUALT_UNLOGIN_ID = -1;
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

    PopupWindow menuPop, phonePop;
    TextView devicesNumTv;
    DeviceListAdpater deviceListAdpater;
    long lasMemberId;
    List<View> viewList;
    List<DeviceItem> toalPageItems = new ArrayList<>();
    ClearEditTextView popSearchEdt;
    View popLineView;
    XRecyclerView devicesListRlv;
    List<ArticleInfo> infoList;
    Handler mHandler;
    int actIndex, actLen;
    String mBoxTel = Constants.CUSTOMER_PHONE_BOX;
    String mRepairTel = Constants.CUSTOMER_PHONE_REPAIR;
    private int deviceSize;
    long memberId;
    int pageNum, mToalPages, mToalSize;
    boolean isRefresh, isLoadMore;
    List<McDeviceInfo> mAllDeviceInfos = new ArrayList<>();
    LatLngBounds.Builder latLngBuilder;
    String searchText;
    boolean isAllInit;
    List<McDeviceInfo> myDevices=new ArrayList<>();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            MobclickAgent.onEvent(getActivity(), MobEvent.TIME_HOME_MAP);
            loadData();
        }
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
    protected void setAMap() {
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMarkerClickListener(this);
    }

    private void loadData() {
        if (UserHelper.isLogin(getActivity())) {
            memberId = UserHelper.getMemberId(getActivity());
            isAllInit = true;
            searchText = null;
            UserHelper.setOwner(getActivity(), memberId, false);
            mPresenter.getDeivceItems(String.valueOf(memberId), 1);
            mPresenter.getAllDeviceList(memberId, 1, searchText);
            lasMemberId = memberId;
        } else {
            isAllInit = false;
            searchText = null;
            if (lasMemberId == DEFUALT_UNLOGIN_ID) {
                return;
            }
            aMap.clear();
            aMap.reloadMap();
            menuTv.setVisibility(View.GONE);
            mPresenter.getDeivceItems(null, 1);
            lasMemberId = DEFUALT_UNLOGIN_ID;
        }
        if (infoList == null) {
            mHandler = new Handler(this);
            mPresenter.getArticleList();
        }
        mPresenter.getServiceTel();
    }

    @Override
    protected void initView() {
        MobclickAgent.onEvent(getActivity(), MobEvent.TIME_HOME_MAP);
        registerRxEvent();
    }

    private void registerRxEvent() {
        mRxManager.on(Constants.UPDATE_DEVICE_LIST, new Action1<String>() {
            @Override
            public void call(String o) {
                String userId = UserHelper.isLogin(getActivity()) ? String.valueOf(UserHelper.getMemberId(getActivity())) : null;
                mPresenter.getDeivceItems(userId, 1);
            }
        });
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
        if (mAllDeviceInfos == null) {
            return;
        }
        if (menuPop == null) {
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_home_menu, null);
            View menuPopBg = contentView.findViewById(R.id.pop_device_bg);
            View menuPopCo = contentView.findViewById(R.id.pop_device_contentlayout);
            popLineView = contentView.findViewById(R.id.pop_device_line);
            popSearchEdt = (ClearEditTextView) contentView.findViewById(R.id.pop_device_search_edt);
            popSearchEdt.setOnEditorActionListener(serachActionListener);
            menuPopCo.setOnClickListener(this);
            menuPopBg.setOnClickListener(this);
            devicesNumTv = (TextView) contentView.findViewById(R.id.pop_devie_num_tv);
            devicesListRlv = (XRecyclerView) contentView.findViewById(R.id.pop_device_list_rlv);
            if (mToalSize >= 4) {
                devicesListRlv.getLayoutParams().height = DensityUtil.dip2px(getActivity(), 427);

            }
            devicesListRlv.setLayoutManager(new LinearLayoutManager(getActivity()));
            devicesListRlv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            deviceListAdpater = new DeviceListAdpater(getActivity(), mAllDeviceInfos);
            deviceListAdpater.setOnItemClickListener(this);
            devicesListRlv.setAdapter(deviceListAdpater);
            devicesListRlv.setPullRefreshEnabled(true);
            devicesListRlv.setLoadingMoreEnabled(true);
            devicesListRlv.setLoadingListener(this);
            menuPop = CommonUtils.getAnimPop(contentView);
        } else {
            deviceListAdpater.updateItems(mAllDeviceInfos);
            devicesListRlv.smoothScrollToPosition(0);
        }
        devicesNumTv.setText("全部(" + mToalSize + ")");
        if (mToalSize >= 20) {
            popSearchEdt.setVisibility(View.VISIBLE);
            popLineView.setVisibility(View.GONE);
            Editable editable = popSearchEdt.getText();
            if (editable != null && editable.length() > 0) {
                updateSearchList(editable);
            }
        } else {
            popSearchEdt.setVisibility(View.GONE);
            popLineView.setVisibility(View.VISIBLE);
        }
        menuPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.FILL, 0, 0);
    }


    private void updateSearchList(Editable s) {
        if (s.length() > 0) {
            String searchKey = s.toString();
            if (TextUtils.isEmpty(searchKey)) {
                return;
            }
            searchText = searchKey;
            mPresenter.getAllDeviceList(memberId, 1, searchText);
        }
    }

    TextView.OnEditorActionListener serachActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ((InputMethodManager) getActivity().getApplication().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(popSearchEdt.getWindowToken(), 0);
                String key = popSearchEdt.getText().toString();
                if (TextUtils.isEmpty(key)) {
                    ToastUtils.showToast(getActivity(), "机器名称不能为空");
                    return false;
                }
                searchText = key;
                mPresenter.getAllDeviceList(memberId, 1, searchText);
            }
            return false;
        }
    };


    private void flush() {
        aMap.clear();
        aMap.reloadMap();
        if (UserHelper.isLogin(getActivity())) {
            mPresenter.getDeivceItems(String.valueOf(UserHelper.getMemberId(getActivity())), 1);
        } else {
            mPresenter.getDeivceItems(null, 1);
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_device;
    }


    @Override
    public void updateDeviceItems(List<DeviceItem> items, int page) {
        aMap.clear();
        aMap.reloadMap();
        viewList = new ArrayList<>();
        latLngBuilder = null;
        latLngBuilder = LatLngBounds.builder();
        toalPageItems = items;
        deviceSize = toalPageItems.size();
        if (deviceSize > 0) {
            deviceVp.setVisibility(View.VISIBLE);
            addNewMarkerView(0);
        } else {
            deviceVp.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateDeviceItemError() {
        aMap.clear();
        aMap.reloadMap();
        deviceVp.setVisibility(View.INVISIBLE);
    }


    @Override
    public void updatePager(McDeviceInfo info) {


    }

    @Override
    public void updateAllDeviceList(BaseRespose<List<McDeviceInfo>> respose, int pageNum) {
        this.pageNum = pageNum;
        if (respose.success()) {
            if (pageNum == 1) {
                myDevices.clear();
                mAllDeviceInfos.clear();
                PageBean bean = respose.getPage();
                if (bean != null) {
                    mToalPages = bean.totalPages;
                    mToalSize = bean.totalElements;
                    if (isAllInit) {
                        isAllInit = false;
                        if (mToalSize > 0) {
                            menuTv.setVisibility(View.VISIBLE);
                        } else {
                            menuTv.setVisibility(View.GONE);
                        }
                    }
                }
            }
            List<McDeviceInfo> items = respose.getResult();
            if (items != null && items.size() > 0) {
                for (McDeviceInfo item:items){
                    if (item.getType()==1){
                        myDevices.add(item);
                    }
                }
                UserHelper.setMyDevices(myDevices);
                mAllDeviceInfos.addAll(items);
            }
            if (deviceListAdpater != null) {
                devicesNumTv.setText("全部(" + mToalSize + ")");
                deviceListAdpater.updateItems(mAllDeviceInfos);
            }
        }
        updateRecyclerView();
    }


    @Override
    public void updateAllDeviceError(String message) {
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        if (isRefresh) {
            devicesListRlv.refreshComplete();
        }
        if (isLoadMore) {
            devicesListRlv.loadMoreComplete();
        }
    }


    //递归遍历DeviceList添加Marker
    public void addNewMarkerView(final int position) {
        final DeviceItem item = toalPageItems.get(position);
        initOwerTag(item.getId(), item.getType());
        String imgUrl = CommonUtils.getMarkerIconUrl(item.getTypePicUrl(), item.getWorkStatus());
        final LatLng latLng = new LatLng(item.getLat(), item.getLng());
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
                        marker.setObject(item);
                        curMarker = marker;
                        viewList.add(getItemView(marker));
                        if (position < deviceSize - 1) {
                            addNewMarkerView(position + 1);
                        } else {
                            updateViewPager(viewList.size());
                        }
                    }
                });
    }

    private void initOwerTag(long deviceId, int deviceType) {
        if (!UserHelper.isOwner(getActivity(), memberId)) {
            boolean isOwner = !Constants.isNoEditInMcMember(deviceId, deviceType);
            if (isOwner) {
                UserHelper.setOwner(getActivity(), memberId, true);
            }
        }


    }


    public void updateViewPager(int pageLen) {
        if (pageLen > 1) {
            setMapZoomTo(ZOOM_HOME);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(), 0));
        } else {
            setMapZoomTo(ZOOM_DEFAULT);
        }
        DevicePagerAdapter adapter = new DevicePagerAdapter(viewList);
        deviceVp.addOnPageChangeListener(onPageChangeL);
        deviceVp.setAdapter(adapter);
        deviceVp.setCurrentItem(0);
        setSelectedMarker(0);
        if (!UserHelper.getGuideTag(getActivity())) {
            UserHelper.insertGuideTag(getActivity(), true);
            ((HomeActivity) getActivity()).updateGuide(toalPageItems);
        }
    }

    @Override
    public void updateDevices(List<McDeviceInfo> deviceList) {

    }

    @Override
    public void updateArticles(List<ArticleInfo> articleInfoList) {
        infoList = articleInfoList;
        if (infoList != null && infoList.size() > 0) {
            boxActTv.setVisibility(View.VISIBLE);
            if (infoList != null && infoList.size() > 0) {
                actIndex = 0;
                actLen = infoList.size();
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


    private void setCurrentBoxAct() {
        ArticleInfo info = infoList.get(actIndex);
        if (info != null) {
            boxActTv.setTag(info);
            boxActTv.setText(info.getArtTitle());
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
            AppLog.print("onPageSelected_____" + position);
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
        if (!itemView.isSelected()) {
            itemView.setSelected(true);
            DeviceItem bean = (DeviceItem) itemMarker.getObject();
            mPresenter.getDeviceNowData(String.valueOf(bean.getId()), itemView);
        }
    }


    @OnClick({R.id.device_box_act_tv, R.id.device_phone_imgBtn, R.id.device_menu_tv, R.id.device_flush_imgBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.device_box_act_tv:
                ArticleInfo info = (ArticleInfo) view.getTag();
                if (info != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(QuestionCommunityActivity.H5_URL, info.getPicUrl());
                    bundle.putString(QuestionCommunityActivity.SHARE_PIC, info.getShareAddress());
                    bundle.putString(QuestionCommunityActivity.SHARE_LINK, info.getArtLink());
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
                gotoDeviceDetail((DeviceItem) marker.getObject());
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
        gotoDeviceDetail((McDeviceInfo) view.getTag());
    }

    @Override
    public View getMarkerInfoView(Marker marker) {
        final DeviceItem bean = (DeviceItem) marker.getObject();
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
        title_tv.setText(bean.getName());
        title_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_CLICK_MACHINE_DETAIL_FROM_ANNOTATION);
                gotoDeviceDetail(bean);
            }
        });
        int index = toalPageItems.indexOf(bean);
        if (deviceVp.getCurrentItem() != index) {
            deviceVp.setCurrentItem(index);
        }
        return title_tv;
    }


    private void gotoDeviceDetail(DeviceItem item) {
        if (menuPop != null && menuPop.isShowing()) {
            menuPop.dismiss();
        }
        MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_CLICK_MACHINE_DETAIL);
        McDeviceInfo info = new McDeviceInfo();
        info.setId(item.getId());
        info.setSnId(item.getSnId());
        info.setType(item.getType());
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.MC_DEVICEINFO, info);
        Constants.toActivity(getActivity(), DeviceDetailActivity.class, bundle);
    }

    private void gotoDeviceDetail(McDeviceInfo info) {
        if (menuPop != null && menuPop.isShowing()) {
            menuPop.dismiss();
        }
        MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_CLICK_MACHINE_DETAIL);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.MC_DEVICEINFO, info);
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
        pageNum = 1;
        isRefresh = true;
        mPresenter.getAllDeviceList(memberId, pageNum, searchText);
    }

    @Override
    public void onLoadMore() {
        isLoadMore = true;
        if (pageNum < mToalPages) {
            pageNum++;
            mPresenter.getAllDeviceList(memberId, pageNum, searchText);
        } else {
            devicesListRlv.setNoMore(false);
        }

    }


    private class DevicePagerAdapter extends PagerAdapter {
        List<View> mViewList;

        private DevicePagerAdapter(List<View> viewList) {
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
    public void onDestroy() {
        if (mHandler != null) {
            mHandler.removeMessages(Constants.HANDLER_CHANGE_BOX_ACT);
        }
        super.onDestroy();
    }
}

