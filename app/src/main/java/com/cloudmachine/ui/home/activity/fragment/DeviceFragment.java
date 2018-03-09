package com.cloudmachine.ui.home.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.cloudmachine.R;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.DeviceListAdpater;
import com.cloudmachine.bean.ArticleInfo;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.McDeviceLocation;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.activity.DeviceDetailActivity;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.home.contract.DeviceContract;
import com.cloudmachine.ui.home.model.DeviceModel;
import com.cloudmachine.ui.home.presenter.DevicePresenter;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.locatecity.PingYinUtil;
import com.cloudmachine.utils.widgets.ClearEditTextView;
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

public class DeviceFragment extends BaseMapFragment<DevicePresenter, DeviceModel> implements Handler.Callback, DeviceContract.View, View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener {
    private static final long DEFUALT_UNLOGIN_ID = -1;
//    @BindView(R.id.device_ques_ans_tv)
//    TextView homeQuestionAnsTv;
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

    List<McDeviceInfo> mDeviceList;
    PopupWindow menuPop, phonePop;
    TextView devicesNumTv;
    DeviceListAdpater deviceListAdpater;
    long lasMemberId;
    int pageLen;
    List<View> viewList;
    ClearEditTextView popSearchEdt;
    View popLineView;
    RecyclerView devicesListRlv;
    List<ArticleInfo> infoList;
    Handler mHandler;
    int actIndex, actLen;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            MobclickAgent.onEvent(getActivity(), MobEvent.TIME_HOME_MAP);
            loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()){
            MobclickAgent.onEvent(getActivity(), MobEvent.TIME_HOME_MAP);
        }
        loadData();
    }

    @Override
    protected void setAMap() {
        aMap.setInfoWindowAdapter(this);
//        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
    }

    private void loadData() {
        if (UserHelper.isLogin(getActivity())) {
            long memberId = UserHelper.getMemberId(getActivity());
            // TODO: 2017/8/14 for v3.2.0 Test
//            if (memberId==lasMemberId){
//                return;
//            }
            if (mDeviceList != null && mDeviceList.size() > 1) {
                menuTv.setVisibility(View.VISIBLE);
            } else {
                menuTv.setVisibility(View.GONE);
            }
            mPresenter.getDevices(memberId, Constants.MC_DevicesList_AllType);
            lasMemberId = memberId;
        } else {
            if (lasMemberId == DEFUALT_UNLOGIN_ID) {
                return;
            }
            aMap.clear();
            aMap.reloadMap();
            menuTv.setVisibility(View.GONE);
            mPresenter.getDevices(Constants.MC_DevicesList_AllType);
            lasMemberId = DEFUALT_UNLOGIN_ID;
        }
        if (infoList == null) {
            mHandler = new Handler(this);
            mPresenter.getArticleList();
        }
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
                if (UserHelper.isLogin(getActivity())) {
                    mPresenter.getDevices(UserHelper.getMemberId(getActivity()), Constants.MC_DevicesList_AllType);
                } else {
                    mPresenter.getDevices(Constants.MC_DevicesList_AllType);
                }
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
        if (mDeviceList == null) {
            return;
        }
        int len = mDeviceList.size();
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
            devicesListRlv = (RecyclerView) contentView.findViewById(R.id.pop_device_list_rlv);
            if (len >= 4) {
                devicesListRlv.getLayoutParams().height = DensityUtil.dip2px(getActivity(), 427);

            }
//            devicesListRlv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            devicesListRlv.setLayoutManager(new LinearLayoutManager(getActivity()));
            deviceListAdpater = new DeviceListAdpater(getActivity(), mDeviceList);
            deviceListAdpater.setOnItemClickListener(this);
            devicesListRlv.setAdapter(deviceListAdpater);
            menuPop = CommonUtils.getAnimPop(contentView);
        } else {
            deviceListAdpater.updateItems(mDeviceList);
            devicesListRlv.smoothScrollToPosition(0);
        }
        devicesNumTv.setText("全部(" + len + ")");
        if (len >= 20) {
            popSearchEdt.setVisibility(View.VISIBLE);
            popLineView.setVisibility(View.GONE);
            Editable editable=popSearchEdt.getText();
            if (editable!=null&&editable.length()>0){
                updateSearchList(editable);
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
            updateSearchList(s);
        }
    };

    private void updateSearchList(Editable s) {
        if (s.length() > 0) {
            String searchKey = s.toString();
            List<McDeviceInfo> searchList = new ArrayList<>();
            for (McDeviceInfo info : mDeviceList) {
                String deviceName = info.getName();
                if (deviceName != null) {
                    String deviceNamePy = PingYinUtil.getPingYin(deviceName);
                    if (deviceName.contains(searchKey) || deviceNamePy.contains(searchKey)) {
                        searchList.add(info);
                    }
                }
            }
            deviceListAdpater.updateItems(searchList);
        } else {
            deviceListAdpater.updateItems(mDeviceList);
        }
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
        if (UserHelper.isLogin(getActivity())) {
            mPresenter.getDevices(UserHelper.getMemberId(getActivity()), Constants.MC_DevicesList_AllType);
        } else {
            mPresenter.getDevices(Constants.MC_DevicesList_AllType);
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
    public void updateDevices(List<McDeviceInfo> deviceList) {
        aMap.clear();
        aMap.reloadMap();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        mDeviceList = new ArrayList<>(deviceList);
        viewList = new ArrayList<>();
        List<McDeviceInfo> myDevices = new ArrayList<>();
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (McDeviceInfo info : deviceList) {
            if (info.getType() == 1) {
                myDevices.add(info);
            }
            Marker marker;
            McDeviceLocation location = info.getLocation();
            double lat = location.getLat();
            double lng = location.getLng();
            if (lat == 0 || lng == 0) {
                mDeviceList.remove(info);
                continue;
            }
            LatLng latLng = new LatLng(lat, lng);
            builder.include(latLng);
            b.include(latLng);
            if (info.getWorkStatus() == 1) {
                marker = aMap.addMarker(getMarkerOptions(getActivity(), latLng, R.drawable.icon_machine_work));
            } else {
                marker = aMap.addMarker(getMarkerOptions(getActivity(), latLng, R.drawable.icon_machine_unwork));
            }
            marker.setObject(info);
            curMarker = marker;
            viewList.add(getItemView(info, marker));
        }
        UserHelper.setMyDevices(myDevices);
        int size = mDeviceList.size();
        if (size > 1) {
            menuTv.setVisibility(View.VISIBLE);
        } else {
            menuTv.setVisibility(View.GONE);
        }
        pageLen = viewList.size();
        if (pageLen > 0) {
            if (pageLen > 1) {
                setMapZoomTo(ZOOM_HOME);
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(b.build(), 0));
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


    private View getItemView(McDeviceInfo info, Marker marker) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_deviceinfo, null);
        TextView locTv = (TextView) v.findViewById(R.id.device_info_loc);
        TextView statusTv = (TextView) v.findViewById(R.id.device_info_status);
        TextView oilTv = (TextView) v.findViewById(R.id.device_info_oil);
        TextView lenTv = (TextView) v.findViewById(R.id.device_info_timelen);
        McDeviceLocation location = info.getLocation();
        if (info.getWorkStatus() == 1) {
            statusTv.setVisibility(View.VISIBLE);
        } else {
            statusTv.setVisibility(View.GONE);
        }
        oilTv.setText(CommonUtils.formatOilValue(info.getOilLave()));
        lenTv.setText(CommonUtils.formatTimeLen(info.getWorkTime()));
        locTv.setText(location.getPosition());
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
//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itemMarker.getPosition(),);
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
                CommonUtils.callPhone(getActivity(), Constants.CUSTOMER_PHONE_BOX);
                break;
            case R.id.phone_repair_tv://客服热线-报修
                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_HOME_CALL_CLICK);
                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_HOME_CALL_REPAIR_CLICK);
                CommonUtils.callPhone(getActivity(), Constants.CUSTOMER_PHONE_REPAIR);
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
                gotoDeviceDetail((McDeviceInfo) marker.getObject());
                break;
//            case R.id.device_ques_ans_tv://问答
//                MobclickAgent.onEvent(getActivity(), MobEvent.TIME_H5_COMMUNITY_PAGE);
//                MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_HOME_ASK_CLICK);
//                Bundle bundle = new Bundle();
//                bundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppCommunity);
//                Constants.toActivity(getActivity(), QuestionCommunityActivity.class, bundle);
//                break;
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
        final McDeviceInfo bean = (McDeviceInfo) marker.getObject();
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
        int index = mDeviceList.indexOf(bean);
        if (deviceVp.getCurrentItem() != index) {
            deviceVp.setCurrentItem(index);
        }
        return title_tv;
    }


    private void gotoDeviceDetail(McDeviceInfo info) {
        if (menuPop != null && menuPop.isShowing()) {
            menuPop.dismiss();
        }
        MobclickAgent.onEvent(getActivity(), MobEvent.COUNT_CLICK_MACHINE_DETAIL);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.MC_DEVICEINFO, info);
//        bundle.putSerializable(Constants.MC_LOC_NOW, locNow);
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


    private class DevicePagerAdapter extends PagerAdapter {
        List<View> mViewList;

        public DevicePagerAdapter(List<View> viewList) {
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

