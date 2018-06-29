package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.IncomeListAdpater;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.base.bean.PageBean;
import com.cloudmachine.bean.SalaryHistoryItem;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.widgets.wheelview.OnWheelScrollListener;
import com.cloudmachine.utils.widgets.wheelview.WheelView;
import com.cloudmachine.utils.widgets.wheelview.adapter.NumericWheelAdapter;
import com.cloudmachine.utils.widgets.wheelview.adapter.WheelViewAdapter;
import com.cloudmachine.widget.CommonTitleView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IncomeSpendActivity extends BaseAutoLayoutActivity implements XRecyclerView.LoadingListener, View.OnClickListener {
    public static final String IS_MEMBER = "is_member";
    public static final String TYPE = "type";
    public static final int TYPE_SPEND = 1;
    public static final int TYPE_INCOME = 2;
    @BindView(R.id.inspend_in_xrlv)
    XRecyclerView mIncomeRlv;
    @BindView(R.id.inspend_out_xrlv)
    XRecyclerView mSpendRlv;
    @BindView(R.id.inspend_income_tv)
    TextView incomeTv;
    @BindView(R.id.inspend_spend_tv)
    TextView spendTv;
    @BindView(R.id.inspend_title_cotainer)
    LinearLayout titleContainer;
    @BindView(R.id.inspend_calendar_cotainer)
    FrameLayout calendarCotainer;
    @BindView(R.id.inspned_wv_year)
    WheelView yearWv;
    @BindView(R.id.inspned_wv_month)
    WheelView monthWv;
    @BindView(R.id.inspned_wv_cancel)
    TextView cancelTv;
    @BindView(R.id.inspned_wv_sure)
    TextView surreTv;
    @BindView(R.id.inspend_empty_tv)
    TextView emptTv;
    @BindView(R.id.inspend_title_tv)
    CommonTitleView titleView;
    int type;
    IncomeListAdpater mIncomAdpater;
    IncomeListAdpater mSpendAdapter;
    int pageNum = 1;
    int toalPages;
    int pageSize = 20;
    List<SalaryHistoryItem> mIncomeItems = new ArrayList<>();
    List<SalaryHistoryItem> mSpendItems = new ArrayList<>();
    NumericWheelAdapter yAdpater;
    NumericWheelAdapter mAdapter2;
    NumericWheelAdapter mAdapter1;
    int lastYearPos;
    int curMonth;
    String year;
    String month;
    boolean isRefresh;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_spend);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleView.setRightImg(R.drawable.ic_salary_calendar, this);
        initWheelView();
        boolean isMmber = getIntent().getBooleanExtra(IS_MEMBER, false);
        type = getIntent().getIntExtra(TYPE, TYPE_INCOME);
        if (isMmber) {
            titleView.setTitleName("工资历史");
            titleContainer.setVisibility(View.GONE);
            mSpendRlv.setVisibility(View.GONE);
        } else {
            titleContainer.setVisibility(View.VISIBLE);
            if (type == TYPE_INCOME) {
                incomeTv.setSelected(true);
                spendTv.setSelected(false);
            } else {
                incomeTv.setSelected(false);
                spendTv.setSelected(true);
            }
            mSpendRlv.setPullRefreshEnabled(true);
            mSpendRlv.setLoadingMoreEnabled(true);
            mSpendRlv.setLoadingListener(this);
            mSpendRlv.setLayoutManager(new LinearLayoutManager(this));
        }
        mIncomeRlv.setPullRefreshEnabled(true);
        mIncomeRlv.setLoadingMoreEnabled(true);
        mIncomeRlv.setLoadingListener(this);
        mIncomeRlv.setLayoutManager(new LinearLayoutManager(this));
        getSalaryHistory(type, 1, pageSize);

    }

    private void initWheelView() {
        int minYear = Integer.parseInt(CommonUtils.getPastYear(9));
        int maxYear = Integer.parseInt(CommonUtils.getPastYear(0));
        yAdpater = new NumericWheelAdapter(this, minYear, maxYear, "%02d");
        yAdpater.setLabel("年");
        yearWv.setDrawShadows(false);
        yearWv.setViewAdapter(yAdpater);
        lastYearPos = yAdpater.getItemsCount() - 1;
        yearWv.setCyclic(false);
        yearWv.setCurrentItem(lastYearPos);
        yearWv.setVisibleItems(7);
        yearWv.setWheelBackground(R.drawable.wheel_bg);
        yearWv.addScrollingListener(yearScrollListener);
        mAdapter2 = new NumericWheelAdapter(mContext, 1, 12, "%02d");
        mAdapter2.setLabel("月");
        curMonth = Integer.parseInt(CommonUtils.getPastMonth(0));
        mAdapter1 = new NumericWheelAdapter(mContext, 1, curMonth, "%02d");
        mAdapter1.setLabel("月");
        monthWv.setDrawShadows(false);
        monthWv.setViewAdapter(mAdapter1);
        monthWv.setCyclic(false);
        monthWv.setCurrentItem(mAdapter1.getItemsCount() - 1);
        monthWv.setVisibleItems(7);
        monthWv.setWheelBackground(R.drawable.wheel_bg);
    }

    private OnWheelScrollListener yearScrollListener = new OnWheelScrollListener() {


        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            WheelViewAdapter adapter = monthWv.getViewAdapter();
            if (wheel.getCurrentItem() == yAdpater.getItemsCount() - 1) {
                if (adapter != mAdapter1) {
                    monthWv.setViewAdapter(mAdapter1);
                }

            } else {
                if (adapter != mAdapter2) {
                    monthWv.setViewAdapter(mAdapter2);
                }
            }
            monthWv.setCurrentItem(0);

        }

    };

    @Override
    public void initPresenter() {


    }


    @OnClick({R.id.inspned_wv_cancel, R.id.inspned_wv_sure, R.id.inspend_income_tv, R.id.inspend_spend_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_titleview_right_img:
                if (calendarCotainer.getVisibility() != View.VISIBLE) {
                    calendarCotainer.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.inspned_wv_cancel:
                calendarCotainer.setVisibility(View.GONE);
                break;
            case R.id.inspned_wv_sure:
                calendarCotainer.setVisibility(View.GONE);
                if (yAdpater == null) {
                    return;
                }
                year = yAdpater.getItemText(yearWv.getCurrentItem()).toString();
                month = String.valueOf(monthWv.getCurrentItem() + 1);
                if (type == TYPE_INCOME) {
                    mIncomeRlv.refresh();
                } else {
                    mSpendRlv.refresh();
                }
                break;
            case R.id.inspend_income_tv:
                if (incomeTv.isSelected()) {
                    return;
                }
                type = TYPE_INCOME;
                incomeTv.setSelected(true);
                spendTv.setSelected(false);
                mIncomeRlv.setVisibility(View.VISIBLE);
                mSpendRlv.setVisibility(View.GONE);
                year = null;
                month = null;
                mIncomeRlv.refresh();
                break;
            case R.id.inspend_spend_tv:
                if (spendTv.isSelected()) {
                    return;
                }
                type = TYPE_SPEND;
                spendTv.setSelected(true);
                incomeTv.setSelected(false);
                mIncomeRlv.setVisibility(View.GONE);
                mSpendRlv.setVisibility(View.VISIBLE);
                year = null;
                month = null;
                mSpendRlv.refresh();
                break;

        }
    }

    public void getSalaryHistory(int type, int page, int size) {
        getSalaryHistory(type, null, null, page, size);
    }


    public void getSalaryHistory(final int type, String year, String month, final int pageNum, int size) {
        this.pageNum = pageNum;
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).getSalaryHistoryRecords(UserHelper.getMemberId(mContext), type, year, month, pageNum, size).compose(RxHelper.<List<SalaryHistoryItem>>handleBaseResult()).subscribe(new RxSubscriber<BaseRespose<List<SalaryHistoryItem>>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<List<SalaryHistoryItem>> br) {
                if (br != null) {
                    PageBean page = br.getPage();
                    if (page != null && page.totalElements > 0) {
                        toalPages = page.totalPages;
                        List<SalaryHistoryItem> items = br.getResult();
                        if (type == TYPE_SPEND) {
                            if (pageNum == 1) {
                                mSpendItems.clear();
                            }
                            mSpendItems.addAll(items);
                            emptTv.setVisibility(View.GONE);
                            mIncomeRlv.setVisibility(View.GONE);
                            mSpendRlv.setVisibility(View.VISIBLE);
                            mSpendRlv.loadMoreComplete();
                            updateSpendAdapter(mSpendItems);
                        } else if (type == TYPE_INCOME) {
                            if (pageNum == 1) {
                                mIncomeItems.clear();
                            }
                            mIncomeItems.addAll(items);
                            emptTv.setVisibility(View.GONE);
                            mSpendRlv.setVisibility(View.GONE);
                            mIncomeRlv.setVisibility(View.VISIBLE);
                            mIncomeRlv.loadMoreComplete();
                            updateIncomeAdapter(mIncomeItems);
                        }
                    } else {
                        emptTv.setVisibility(View.VISIBLE);
                        mIncomeRlv.setVisibility(View.GONE);
                        mSpendRlv.setVisibility(View.GONE);
                    }
                }
                refreshComplete(type);
            }

            @Override
            protected void _onError(String message) {
                refreshComplete(type);
            }
        }));

    }

    private void refreshComplete(int type) {
        if (isRefresh) {
            isRefresh=false;
            if (type==TYPE_INCOME){
                mIncomeRlv.refreshComplete();
            }else{
                mSpendRlv.refreshComplete();
            }
        }
    }


    private void updateIncomeAdapter(List<SalaryHistoryItem> items) {
        if (mIncomAdpater == null) {
            mIncomAdpater = new IncomeListAdpater(mContext, items);
            mIncomAdpater.setType(TYPE_INCOME);
            mIncomeRlv.setAdapter(mIncomAdpater);
            final StickyRecyclerHeadersDecoration headerDecor = new StickyRecyclerHeadersDecoration(mIncomAdpater);
            mIncomeRlv.addItemDecoration(headerDecor);
            mIncomAdpater.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    headerDecor.invalidateHeaders();
                }
            });
        } else {
            mIncomAdpater.updateItems(items);
        }
        if (isRefresh){
            mIncomeRlv.smoothScrollToPosition(0);
        }
    }

    private void updateSpendAdapter(List<SalaryHistoryItem> items) {
        if (mSpendAdapter == null) {
            mSpendAdapter = new IncomeListAdpater(mContext, items);
            mSpendAdapter.setType(TYPE_SPEND);
            mSpendRlv.setAdapter(mSpendAdapter);
            final StickyRecyclerHeadersDecoration headerDecor = new StickyRecyclerHeadersDecoration(mSpendAdapter);
            mSpendRlv.addItemDecoration(headerDecor);
            mSpendAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    headerDecor.invalidateHeaders();
                }
            });
        } else {
            mSpendAdapter.updateItems(items);
        }
        if (isRefresh){
            mSpendRlv.smoothScrollToPosition(0);
        }
    }


    @Override
    public void onRefresh() {
        isRefresh = true;
        getSalaryHistory(type, year, month, 1, pageSize);
    }

    @Override
    public void onLoadMore() {
        if (pageNum < toalPages) {
            pageNum++;
            getSalaryHistory(type, year, month, pageNum, pageSize);
        } else {
            if (type==TYPE_INCOME){
                mIncomeRlv.setNoMore(true);
            }else{
                mSpendRlv.setNoMore(true);

            }
        }

    }

}
