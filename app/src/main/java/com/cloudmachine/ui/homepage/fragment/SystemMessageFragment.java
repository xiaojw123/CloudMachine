package com.cloudmachine.ui.homepage.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.listener.RecyclerItemClickListener;
import com.cloudmachine.recycleadapter.SystemMessageAdapter;
import com.cloudmachine.struc.MessageBO;
import com.cloudmachine.ui.homepage.contract.SystemMessageContract;
import com.cloudmachine.ui.homepage.model.SystemMessageModel;
import com.cloudmachine.ui.homepage.presenter.SystemMessagePresenter;
import com.cloudmachine.utils.MemeberKeeper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/12 下午5:27
 * 修改人：shixionglu
 * 修改时间：2017/4/12 下午5:27
 * 修改备注：
 */

public class SystemMessageFragment extends BaseFragment<SystemMessagePresenter, SystemMessageModel> implements
        Handler.Callback, SystemMessageContract.View {


    @BindView(R.id.recyclerView)
    RecyclerView       mRecyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    private ArrayList<MessageBO> dataList;
    private SystemMessageAdapter mSystemMessageAdapter;
    private int pageSize = 20;
    private int pageCurrent = 1;//当前页数

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void initView() {

        dataList = new ArrayList<>();
        if (MemeberKeeper.getOauth(getActivity()) != null) {
            mPresenter.getSystemMessage(MemeberKeeper.getOauth(getActivity()).getId(),1,20);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSystemMessageAdapter = new SystemMessageAdapter(getActivity(), dataList);
        mRecyclerView.setAdapter(mSystemMessageAdapter);
        //条目点击
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        }));

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
                    //上拉加载的数据
                        mPresenter.getMoreSystemMessage(MemeberKeeper.getOauth(getActivity()).getId(),++pageCurrent,20);
                }
            }
        });
    }

    private void getData() {

        if (MemeberKeeper.getOauth(getActivity()) != null) {
            mPresenter.getSystemMessage(MemeberKeeper.getOauth(getActivity()).getId(),1,20);
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_system_message;
    }


    @Override
    public void returnSystemMessage(List<MessageBO> messageBOs) {
        dataList.clear();
        pageCurrent = 1;
        dataList.addAll(messageBOs);
        mSystemMessageAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void returnMoreSystemMessage(List<MessageBO> messageBOs) {
        dataList.addAll(messageBOs);
        mSystemMessageAdapter.notifyDataSetChanged();
    }
}
