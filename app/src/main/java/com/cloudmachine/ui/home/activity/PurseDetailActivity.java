package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudmachine.R;
import com.cloudmachine.adapter.PurseDetailAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.PurseDetailItem;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PurseDetailActivity extends BaseAutoLayoutActivity implements XRecyclerView.LoadingListener {


    @BindView(R.id.purse_detail_xrlv)
    XRecyclerView purseDetailXrlv;
    PurseDetailAdapter mPurseDetailAdapter;
    boolean isRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse_detail);
        ButterKnife.bind(this);
        initRecyclerView();

    }

    private void initRecyclerView() {
        purseDetailXrlv.setLayoutManager(new LinearLayoutManager(this));
        purseDetailXrlv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        purseDetailXrlv.setPullRefreshEnabled(true);
        purseDetailXrlv.setLoadingMoreEnabled(true);
        purseDetailXrlv.setLoadingListener(this);
    }

    public void updateRecyclerView(List<PurseDetailItem> itemList){
        if (mPurseDetailAdapter==null){
            mPurseDetailAdapter=new PurseDetailAdapter(this,itemList);
        }else{
            mPurseDetailAdapter.updateItems(itemList);
        }
        purseDetailXrlv.setAdapter(mPurseDetailAdapter);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onRefresh() {
        isRefresh=true;

    }

    @Override
    public void onLoadMore() {

    }
}
