package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.ActivitesAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.HomeBannerBean;
import com.cloudmachine.ui.home.contract.ActivitesContract;
import com.cloudmachine.ui.home.model.ActvitiesModel;
import com.cloudmachine.ui.home.presenter.ActivitiesPresenter;
import com.cloudmachine.utils.UMengKey;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class ActivitesActivity extends BaseAutoLayoutActivity<ActivitiesPresenter, ActvitiesModel> implements ActivitesContract.View {
    RecyclerView mRecyclerView;
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
        mRecyclerView = (RecyclerView) findViewById(R.id.activities_item_rlv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (mActivitesAdapter == null) {
            mActivitesAdapter = new ActivitesAdapter(this);
        }
        mRecyclerView.setAdapter(mActivitesAdapter);
        mPresenter.getHomeBannerInfo();
    }

    @Override
    public void returnHomeBannerInfo(ArrayList<HomeBannerBean> homeBannerBeen) {
        mActivitesAdapter.updateItems(homeBannerBeen);
    }

    @Override
    public void loadBannerError() {

    }
}
