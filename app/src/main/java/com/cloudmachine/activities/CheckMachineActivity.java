package com.cloudmachine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cloudmachine.R;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.OwnDeviceAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.base.bean.PageBean;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.Constants;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CheckMachineActivity extends BaseAutoLayoutActivity implements XRecyclerView.LoadingListener, BaseRecyclerAdapter.OnItemClickListener {

    private OwnDeviceAdapter ownDeviceAdapter;
    private XRecyclerView mRecyclerView;
    PageBean mPage;
    List<McDeviceInfo> mAllPageItems =new ArrayList<>();
    int pageNum=1;
    boolean isRefresh,isLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_machine);
        initView();
    }

    @Override
    public void initPresenter() {

    }


    private void initView() {
        mRecyclerView= (XRecyclerView) findViewById(R.id.select_mc_xrlv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        obtainOwnDeviceList(pageNum);
    }
    public void obtainOwnDeviceList(int page){
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getOwnDeviceList(page).compose(RxHelper.<List<McDeviceInfo>>handleBaseResult()).subscribe(new RxSubscriber<BaseRespose<List<McDeviceInfo>>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<List<McDeviceInfo>> br) {
                resetLoadStatus();
                mPage= br.getPage();
                List<McDeviceInfo> items=br.getResult();
                if (mPage!=null){
                    if (mPage.first){
                        mAllPageItems.clear();
                    }
                    if (items!=null&&items.size()>0){
                        mAllPageItems.addAll(items);
                        if (ownDeviceAdapter==null){
                            long lastSelDeviceId= getIntent().getLongExtra(Constants.DEVICE_ID,-1);
                            ownDeviceAdapter = new OwnDeviceAdapter(mContext, mAllPageItems);
                            ownDeviceAdapter.setLastSelectedId(lastSelDeviceId);
                            ownDeviceAdapter.setOnItemClickListener(CheckMachineActivity.this);
                            mRecyclerView.setAdapter(ownDeviceAdapter);
                        }else{
                            ownDeviceAdapter.updateItems(mAllPageItems);
                        }
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                resetLoadStatus();
            }
        }));

    }

    private void resetLoadStatus() {
        if (isRefresh){
            isRefresh=false;
            mRecyclerView.refreshComplete();
        }
        if (isLoadMore){
            isLoadMore=false;
            mRecyclerView.loadMoreComplete();
        }
    }



    @Override
    public void onRefresh() {
        isRefresh=true;
        pageNum=1;
        obtainOwnDeviceList(pageNum);
    }

    @Override
    public void onLoadMore() {
        if (mPage!=null&&!mPage.last){
            isLoadMore=true;
            pageNum++;
            obtainOwnDeviceList(pageNum);
        }else{
            isLoadMore=false;
            mRecyclerView.setNoMore(true);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.putExtra("selInfo", mAllPageItems.get(position));
        setResult(Constants.CLICK_POSITION, intent);
        finish();
    }

}
