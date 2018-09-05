package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.PayCodeAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.base.bean.PageBean;
import com.cloudmachine.bean.PayCodeItem;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.widget.CommonTitleView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 云盒支付码
 */
public class PayCodeActivity extends BaseAutoLayoutActivity implements XRecyclerView.LoadingListener {
    private static final String PAY_CODE_HISTORY = "pay_code_history";
    CommonTitleView mTitleView;
    XRecyclerView mRecyclerView;
    TextView emptyTv;
    long memberId;
    String pageType;
    PayCodeAdapter mAdapter;
    List<PayCodeItem> mToalItems = new ArrayList<>();
    boolean isRefresh, isLoadMore;
    int pageNum, mToalPageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_code);
        initView();

    }


    private void initView() {
        memberId = UserHelper.getMemberId(this);
        mTitleView = (CommonTitleView) findViewById(R.id.pay_code_ctv);
        emptyTv = (TextView) findViewById(R.id.pay_code_empty);
        pageType = getIntent().getStringExtra(Constants.PAGET_TYPE);
        if (PAY_CODE_HISTORY.equals(pageType)) {
            mTitleView.setTitleName("历史");
            emptyTv.setText("暂无历史记录");
        } else {
            mTitleView.setTitleName("云盒支付码");
            mTitleView.setRightText("历史", rightClickListener);
            emptyTv.setText("您目前暂无云盒支付码");
        }
        mRecyclerView = (XRecyclerView) findViewById(R.id.pay_code_rlv);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pageNum = 1;
        loadData();
    }


    private void loadData() {
        if (TextUtils.equals(PAY_CODE_HISTORY, pageType)) {
            mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).getCodeHistoryList(memberId, pageNum, 20).compose(RxHelper.<List<PayCodeItem>>handleBaseResult()).subscribe(new RxSubscriber<BaseRespose<List<PayCodeItem>>>(this) {
                @Override
                protected void _onNext(BaseRespose<List<PayCodeItem>> br) {
                    updateItemAdapter(br);

                }

                @Override
                protected void _onError(String message) {
                    retrunErrorMessage(message);
                }
            }));

        } else {
            mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).getBoxCodeList(memberId, pageNum, 20).compose(RxHelper.<List<PayCodeItem>>handleBaseResult()).subscribe(new RxSubscriber<BaseRespose<List<PayCodeItem>>>(this) {
                @Override
                protected void _onNext(BaseRespose<List<PayCodeItem>> br) {
                    updateItemAdapter(br);
                }

                @Override
                protected void _onError(String message) {
                    retrunErrorMessage(message);
                }
            }));
        }
    }

    private void updateItemAdapter(BaseRespose<List<PayCodeItem>> br) {
        if (br != null) {
            List<PayCodeItem> items = br.getResult();
            PageBean page = br.getPage();
            mToalPageSize = page.totalPages;
            if (page.totalElements > 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyTv.setVisibility(View.GONE);
            } else {
                mRecyclerView.setVisibility(View.GONE);
                emptyTv.setVisibility(View.VISIBLE);
            }
            if (items != null && items.size() > 0) {
                if (isRefresh) {
                    isRefresh = false;
                    mToalItems.clear();
                    mRecyclerView.refreshComplete();
                }
                if (isLoadMore) {
                    isLoadMore = false;
                    mRecyclerView.loadMoreComplete();
                }
                if (!mToalItems.containsAll(items)) {
                    mToalItems.addAll(items);
                }
                if (mAdapter == null) {
                    mAdapter = new PayCodeAdapter(mContext, mToalItems);
                    mAdapter.setHistoryQuery(TextUtils.equals(PAY_CODE_HISTORY, pageType));
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.updateItems(mToalItems);
                }
            }
        }
    }

    private void retrunErrorMessage(String message) {
        if (isRefresh) {
            isRefresh = false;
            mRecyclerView.refreshComplete();
        }
        if (isLoadMore) {
            isLoadMore = false;
            pageNum -= 1;
            if (pageNum < 1) {
                pageNum = 1;
            }
            mRecyclerView.loadMoreComplete();
        }
        ToastUtils.showToast(mContext, message);
    }

    private View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle historyBundle = new Bundle();
            historyBundle.putString(Constants.PAGET_TYPE, PAY_CODE_HISTORY);
            Constants.toActivity(PayCodeActivity.this, PayCodeActivity.class, historyBundle);
        }
    };


    @Override
    public void initPresenter() {

    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageNum = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        if (pageNum < mToalPageSize) {
            isLoadMore = true;
            pageNum++;
            loadData();
        } else {
            mRecyclerView.setNoMore(true);
        }


    }
}
