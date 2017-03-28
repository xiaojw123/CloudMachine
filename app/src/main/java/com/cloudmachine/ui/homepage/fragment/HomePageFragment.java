package com.cloudmachine.ui.homepage.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.recycleadapter.HomePageAdapter;
import com.cloudmachine.recyclerbean.HomeDiliverBean;
import com.cloudmachine.recyclerbean.HomeHotIssueBean;
import com.cloudmachine.recyclerbean.HomeIssueDetailBean;
import com.cloudmachine.recyclerbean.HomeLoadMoreBean;
import com.cloudmachine.recyclerbean.HomeLocalBean;
import com.cloudmachine.recyclerbean.HomeNewsBean;
import com.cloudmachine.recyclerbean.HomePageBean;
import com.cloudmachine.recyclerbean.HomePageType;
import com.cloudmachine.ui.homepage.activity.MessageActivity;
import com.cloudmachine.ui.homepage.contract.HomePageContract;
import com.cloudmachine.ui.homepage.model.HomePageModel;
import com.cloudmachine.ui.homepage.presenter.HomePagePresenter;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/17 下午10:29
 * 修改人：shixionglu
 * 修改时间：2017/3/17 下午10:29
 * 修改备注：
 */

public class HomePageFragment extends BaseFragment<HomePagePresenter, HomePageModel> implements HomePageContract.View, View.OnClickListener {


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView       mRecyclerView;
    private HomePageAdapter    homePageAdapter;
    private ArrayList<HomePageType> homePageList;
    private HomePageBean homePageBean;
    private ImageView ivMessage;


    @Override
    protected void initView() {
        ivMessage = (ImageView) viewParent.findViewById(R.id.iv_message);
        ivMessage.setOnClickListener(this);
        mRxManager.on("localrefresh", new Action1<String>() {
            @Override
            public void call(String s) {
                Constants.MyLog("拿到刷新事件");
            }
        });
        homePageList = new ArrayList<>();
        homePageAdapter = new HomePageAdapter(getActivity(), homePageList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) viewParent.findViewById(R.id.swipe_layout);
        mRecyclerView = (RecyclerView) viewParent.findViewById(R.id.recyclerView);
        //设置布局的排列方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(homePageAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        getData();
    }

    private void getData() {
        homePageList.clear();
        //mPresenter.getLatestDaily();
        mPresenter.getHomeBannerInfo();
        homePageList.add(new HomeLocalBean());
        homePageList.add(new HomeDiliverBean());
        homePageList.add(new HomeNewsBean());
        homePageList.add(new HomeDiliverBean());
        homePageList.add(new HomeHotIssueBean());
        homePageList.add(new HomeIssueDetailBean());
        homePageList.add(new HomeDiliverBean());
        homePageList.add(new HomeLoadMoreBean());

        homePageAdapter.notifyDataSetChanged();

    }


    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_homepage;
    }

    @Override
    public <T> void refreshHomeList(T t) {


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_message:
                Constants.MyLog("页面跳转");
                Constants.toActivity(getActivity(), MessageActivity.class,null,false);
                break;
        }
    }
}
