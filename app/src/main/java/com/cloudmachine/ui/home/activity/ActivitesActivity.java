package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudmachine.R;
import com.cloudmachine.adapter.ActivitesAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.AdBean;
import com.cloudmachine.utils.UMengKey;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class ActivitesActivity extends BaseAutoLayoutActivity implements XRecyclerView.LoadingListener {
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
        obtainSystemAd(AD_ACTIVITIES);
    }

    @Override
    protected void updateAdActivities(List items) {
        mActivitesAdapter.updateItems(items);
        mRecyclerView.refreshComplete();
    }

    @Override
    protected void updateAdActivitiesError() {
        mRecyclerView.refreshComplete();
    }


    @Override
    public void onRefresh() {
        obtainSystemAd(AD_ACTIVITIES);
    }

    @Override
    public void onLoadMore() {

    }

}
