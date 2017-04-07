package com.cloudmachine.ui.homepage.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.base.bean.PageBean;
import com.cloudmachine.recycleadapter.MasterDailyAdapter;
import com.cloudmachine.recyclerbean.MasterDailyBean;
import com.cloudmachine.recyclerbean.MasterDailyType;
import com.cloudmachine.ui.homepage.contract.MasterDailyContract;
import com.cloudmachine.ui.homepage.model.MasterDailyModel;
import com.cloudmachine.ui.homepage.presenter.MasterDailyPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：CloudMachine
 * 类描述：大师日报列表
 * 创建人：shixionglu
 * 创建时间：2017/3/30 下午5:08
 * 修改人：shixionglu
 * 修改时间：2017/3/30 下午5:08
 * 修改备注：
 */

public class MasterDailyActivity extends BaseAutoLayoutActivity<MasterDailyPresenter, MasterDailyModel> implements MasterDailyContract.View {

    @BindView(R.id.title_layout)
    TitleView          mTitleLayout;
    @BindView(R.id.recycler_view)
    RecyclerView       mRecyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    private ArrayList<MasterDailyType> masterList;
    private MasterDailyAdapter mMasterDailyAdapter;
    private PageBean mPage;
    private List<MasterDailyBean> mResult;
    private int pageSize = 20;
    private int pageCurrent = 1;//当前页数
    private int mMaxPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_daily);
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
    }

    private void initListener() {

        //下拉刷新监听
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                View lastchildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                int lastChildBottomY = lastchildView.getBottom();
                int recyclerBottomY = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastchildView);
                if (lastChildBottomY == recyclerBottomY && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    //下拉刷新的数据
                    if (pageCurrent < mMaxPage) {
                        mPresenter.loadMoreDailyInfo(++pageCurrent,pageSize);
                    }

                }
            }
        });
    }

    private void getData() {

        //调用请求数据的事件
        mPresenter.getMasterDailyInfo(1, pageSize);
    }

    private void initData() {
        masterList = new ArrayList<>();
        mMasterDailyAdapter = new MasterDailyAdapter(this, masterList);
        getData();
    }

    private void initView() {

        mTitleLayout.setTitle("大师日报");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mMasterDailyAdapter);

    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }


    @Override
    public void returnMasterDailyInfo(BaseRespose<List<MasterDailyBean>> masterDailyBeanBaseResposeList) {

        masterList.clear();
        pageCurrent = 1;
        mPage = masterDailyBeanBaseResposeList.page;
        calculatePageCount();
        mResult = masterDailyBeanBaseResposeList.result;
        masterList.addAll(mResult);
        mMasterDailyAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
        mSwipeLayout.setRefreshing(false);
    }

    private void calculatePageCount() {

        mMaxPage = (mPage.totalElements / pageSize) + 1;
    }

    @Override
    public void returnLoadMoreDailyInfo(BaseRespose<List<MasterDailyBean>> masterDailyBeanBaseResposeList) {
        PageBean page = masterDailyBeanBaseResposeList.page;
        List<MasterDailyBean> result = masterDailyBeanBaseResposeList.result;
        masterList.addAll(result);
        mMasterDailyAdapter.notifyDataSetChanged();
    }


}
