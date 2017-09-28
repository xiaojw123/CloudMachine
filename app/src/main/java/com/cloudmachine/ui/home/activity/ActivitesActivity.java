package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudmachine.R;
import com.cloudmachine.adapter.ActivitesAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.HomeBannerBean;
import com.cloudmachine.ui.home.contract.ActivitesContract;
import com.cloudmachine.ui.home.model.ActvitiesModel;
import com.cloudmachine.ui.home.presenter.ActivitiesPresenter;
import com.cloudmachine.utils.UMengKey;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class ActivitesActivity extends BaseAutoLayoutActivity<ActivitiesPresenter, ActvitiesModel> implements ActivitesContract.View, XRecyclerView.LoadingListener {
    XRecyclerView mRecyclerView;
    ActivitesAdapter mActivitesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activites);
        initView();
        MobclickAgent.onEvent(this, UMengKey.time_ad_page);
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    private void initView() {
        mRecyclerView = (XRecyclerView) findViewById(R.id.activities_item_rlv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        if (mActivitesAdapter == null) {
            mActivitesAdapter = new ActivitesAdapter(this);
        }
        mRecyclerView.setAdapter(mActivitesAdapter);
        mPresenter.getHomeBannerInfo();
    }

    @Override
    public void returnHomeBannerInfo(ArrayList<HomeBannerBean> homeBannerBeen) {
        mActivitesAdapter.updateItems(homeBannerBeen);
        mRecyclerView.refreshComplete();
    }

    @Override
    public void loadBannerError() {
        mRecyclerView.refreshComplete();
    }

    @Override
    public void onRefresh() {
        mPresenter.getHomeBannerInfo();
    }

    @Override
    public void onLoadMore() {

    }

}
