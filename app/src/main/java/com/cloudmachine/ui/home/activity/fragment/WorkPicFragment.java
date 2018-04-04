package com.cloudmachine.ui.home.activity.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.WorkPicListAdapter;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.PickItemBean;
import com.cloudmachine.bean.ScreenInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.activity.WorkVideoActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.wheelview.OnWheelScrollListener;
import com.cloudmachine.utils.widgets.wheelview.WheelView;
import com.cloudmachine.utils.widgets.wheelview.adapter.ArrayWheelAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiaojw on 2018/3/9.
 */

public class WorkPicFragment extends BaseFragment implements View.OnClickListener, XRecyclerView.LoadingListener {
    private String prefix;
    private static final int PAGE_SIZE = 20;
//    @BindView(R.id.pic_ctv)
//    CommonTitleView picCtv;
    @BindView(R.id.pic_date_sel_flt)
    FrameLayout picDateSelFlt;
    @BindView(R.id.pic_list_xrlv)
    XRecyclerView picListXrlv;
    @BindView(R.id.pic_empty_tv)
    TextView emptyTv;
    @BindView(R.id.pic_llst_wlv)
    WheelView picListWlv;
    @BindView(R.id.pic_date_layout)
    LinearLayout dateLayout;
    @BindView(R.id.wheelview_cancel)
    TextView dateSelCancel;
    @BindView(R.id.wheelview_determine)
    TextView dateSelDetermine;
    @BindView(R.id.date_tv)
    TextView dateTv;
    private int pageNum = 1;
    private int width = 1000;
    WorkPicListAdapter mAdapter;
    String[] itemArray = new String[90];
    private int currentIndex;

    private int totalPages;
    private List<PickItemBean> mTotaltems = new ArrayList<>();
    String imei;
    @Override
    protected void initView() {
//        picCtv.setRightClickListener(rightClickListener);
        for (int i = 0; i < 90; i++) {
            itemArray[i] = CommonUtils.getPastDate(i);
        }
        ArrayWheelAdapter dayAdapter = new ArrayWheelAdapter(getActivity(), itemArray);
        picListWlv.setViewAdapter(dayAdapter);
        picListWlv.setCyclic(false);//是否可循环滑动
        picListWlv.setCurrentItem(0);
        picListWlv.setVisibleItems(7);
        picListWlv.addScrollingListener(scrollListener);
        picListXrlv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        picListXrlv.setLayoutManager(new LinearLayoutManager(getActivity()));
        picListXrlv.setPullRefreshEnabled(true);
        picListXrlv.setLoadingMoreEnabled(true);
        picListXrlv.setLoadingListener(this);
        obtainImei();
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_work_pic;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEvent(getActivity(), MobEvent.TIME_MACHINE_WORK_IMAGES);

    }

    private void obtainImei() {
        String sn = getActivity().getIntent().getStringExtra(Constants.SN_ID);
        width = ScreenInfo.screen_width - 2 * DensityUtil.dip2px(getActivity(), 10);
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_YJX).getImei(UserHelper.getMemberId(getActivity()), sn).compose(RxSchedulers.<JsonObject>io_main()).subscribe(new RxSubscriber<JsonObject>(getActivity()) {
            @Override
            protected void _onNext(JsonObject respJobj) {
                int code = respJobj.get("code").getAsInt();
                if (code == 800) {
                    JsonObject resultJobj = respJobj.getAsJsonObject("result");
                    if (resultJobj != null) {
                        JsonElement imeJel = resultJobj.get("imei");
                        if (imeJel != null) {
                            imei = imeJel.getAsString();
                            prefix = imei;
                            updatePicItems();
                        }
                    }
                } else {
                    JsonElement messageJEL = respJobj.get("message");
                    if (messageJEL != null) {
                        String message = messageJEL.getAsString();
                        ToastUtils.showToast(getActivity(), message);
                    }
                }
            }


            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(getActivity(), message);
            }
        }));
    }


    private void updatePicItems() {

        mRxManager.add((Api.getDefault(HostType.HOST_BH_COFIG).getQnDtuCameraImages(prefix, pageNum, PAGE_SIZE, width)
                .compose(RxSchedulers.<BaseRespose<List<PickItemBean>>>io_main()).subscribe(new RxSubscriber<BaseRespose<List<PickItemBean>>>(getActivity(), false) {
                    @Override
                    protected void _onNext(BaseRespose<List<PickItemBean>> respose) {
//                jsonObject.getAsJsonArray(
                        List<PickItemBean> resultBean = respose.getResult();
                        if (resultBean == null || (pageNum == 1 && resultBean.size() == 0)) {
                            picListXrlv.setVisibility(View.GONE);
                            emptyTv.setVisibility(View.VISIBLE);
                            return;
                        }
                        picListXrlv.setVisibility(View.VISIBLE);
                        emptyTv.setVisibility(View.GONE);
                        int code = respose.getCode();
                        if (code == 800) {
                            totalPages = respose.getPage().totalPages;
                            if (isRefresh) {
                                mTotaltems.clear();
                                isRefresh = false;
                                picListXrlv.refreshComplete();
                                picListXrlv.smoothScrollToPosition(0);
                            }
                            if (isLoadMore) {
                                isLoadMore = false;
                                picListXrlv.loadMoreComplete();
                            }
                            mTotaltems.addAll(respose.getResult());
                            if (mAdapter == null) {
                                mAdapter = new WorkPicListAdapter(getActivity(), mTotaltems);
                                picListXrlv.setAdapter(mAdapter);
                            } else {
                                mAdapter.updateItems(mTotaltems);
                            }

                        } else {
                            ToastUtils.showToast(getActivity(), respose.getMessage());
                        }


                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(getActivity(), message);
                    }
                })));
    }


    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.pic_date_sel_flt, R.id.wheelview_cancel, R.id.wheelview_determine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic_date_sel_flt:
                dateLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.wheelview_cancel:
                dateLayout.setVisibility(View.GONE);
                break;
            case R.id.wheelview_determine:
                dateLayout.setVisibility(View.GONE);
                String text = itemArray[currentIndex];
                if (text.equals(dateTv.getText().toString())) {
                    return;
                }
                isRefresh = true;
                pageNum = 1;
                prefix = imei + "/" + text;
                dateTv.setText(text);
                updatePicItems();
                break;
        }
    }


//    private View.OnClickListener rightClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (picCtv.getVisibility() == View.VISIBLE) {
////                picListXrlv.smoothScrollToPosition(0);
//                picListXrlv.refresh();
//            } else {
//                onRefresh();
//            }
//        }
//    };




    private OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            AppLog.print("item index___" + wheel.getCurrentItem());
            currentIndex = wheel.getCurrentItem();
        }


    };

    public void flushData(){
        if (picListXrlv.getVisibility()==View.VISIBLE){
            picListXrlv.refresh();
        }else{
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        isRefresh = true;
        updatePicItems();
    }

    @Override
    public void onLoadMore() {
        if (pageNum < totalPages) {
            ++pageNum;
            isLoadMore = true;
            updatePicItems();
        } else {
            picListXrlv.setNoMore(true);
        }


    }

    private boolean isRefresh, isLoadMore;
}
