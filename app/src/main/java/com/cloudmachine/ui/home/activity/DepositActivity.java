package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudmachine.R;
import com.cloudmachine.adapter.DepositAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.DepositItem;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DepositActivity extends BaseAutoLayoutActivity implements XRecyclerView.LoadingListener {

    XRecyclerView depositListXrl;
    DepositAdapter mPurseDetailAdapter;
    boolean isRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        initRecyclerView();
        List<DepositItem> itemList = new ArrayList<>();
        itemList.add(new DepositItem());
        itemList.add(new DepositItem());
        itemList.add(new DepositItem());
        itemList.add(new DepositItem());
        updateRecyclerView(itemList);
    }

    private void initRecyclerView() {
        depositListXrl = (XRecyclerView) findViewById(R.id.deposit_list_xrl);
        depositListXrl.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_line_horztial));
        depositListXrl.addItemDecoration(decoration);
        depositListXrl.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        depositListXrl.setPullRefreshEnabled(true);
        depositListXrl.setLoadingMoreEnabled(false);
        depositListXrl.setLoadingListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataRxTask();
    }

    private void getDataRxTask() {
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).getDepositList(UserHelper.getMemberId(this)).compose(RxSchedulers.<BaseRespose<List<DepositItem>>>io_main()).subscribe(new RxSubscriber<BaseRespose<List<DepositItem>>>(this) {
            @Override
            protected void _onNext(BaseRespose<List<DepositItem>> listBaseRespose) {
                if (isRefresh) {
                    isRefresh = false;
                    depositListXrl.refreshComplete();
                }
                updateRecyclerView(listBaseRespose.getResult());
            }

            @Override
            protected void _onError(String message) {
                AppLog.print("error message__" + message);
                ToastUtils.showToast(DepositActivity.this, message);
            }
        }));
    }

    @Override
    public void initPresenter() {

    }

    public void updateRecyclerView(List<DepositItem> itemList) {
        if (mPurseDetailAdapter == null) {
            mPurseDetailAdapter = new DepositAdapter(this, itemList);
            depositListXrl.setAdapter(mPurseDetailAdapter);
        } else {
            mPurseDetailAdapter.updateItems(itemList);
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        getDataRxTask();

    }

    @Override
    public void onLoadMore() {

    }
}
