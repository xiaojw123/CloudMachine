package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.DepositAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.DepositItem;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

public class DepositActivity extends BaseAutoLayoutActivity implements XRecyclerView.LoadingListener {

    XRecyclerView depositListXrl;
    TextView emptTv;
    DepositAdapter mPurseDetailAdapter;
    boolean isRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        initRecyclerView();
    }

    private void initRecyclerView() {
        emptTv = (TextView) findViewById(R.id.deposit_empt_tv);
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
                ToastUtils.showToast(DepositActivity.this, message);
                depositListXrl.setVisibility(View.GONE);
                emptTv.setVisibility(View.VISIBLE);
            }
        }));
    }

    @Override
    public void initPresenter() {

    }

    public void updateRecyclerView(List<DepositItem> itemList) {
        if (itemList != null && itemList.size() > 0) {
            depositListXrl.setVisibility(View.VISIBLE);
            emptTv.setVisibility(View.GONE);
            if (mPurseDetailAdapter == null) {
                mPurseDetailAdapter = new DepositAdapter(this, itemList);
                depositListXrl.setAdapter(mPurseDetailAdapter);
            } else {
                mPurseDetailAdapter.updateItems(itemList);
            }
        } else {
            depositListXrl.setVisibility(View.GONE);
            emptTv.setVisibility(View.VISIBLE);
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
