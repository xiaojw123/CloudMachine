package com.cloudmachine.ui.homepage.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.recycleadapter.HomePageAdapter;
import com.cloudmachine.struc.HomeBannerBean;
import com.cloudmachine.struc.LatestDailyEntity;
import com.cloudmachine.ui.homepage.contract.HomePageContract;
import com.cloudmachine.ui.homepage.model.HomePageModel;
import com.cloudmachine.ui.homepage.presenter.HomePagePresenter;
import com.cloudmachine.itemtype.HomeTypeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/17 下午10:29
 * 修改人：shixionglu
 * 修改时间：2017/3/17 下午10:29
 * 修改备注：
 */

public class HomePageFragment extends BaseFragment<HomePagePresenter, HomePageModel> implements HomePageContract.View {


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView       mRecyclerView;
    private HomePageAdapter    homePageAdapter;
    private List<HomeTypeItem> homePageList;

    @Override
    protected void initView() {
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

        mPresenter.getLatestDaily();
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
        homePageList.clear();
        LatestDailyEntity latestDailyEntity = (LatestDailyEntity)t;
        homePageList.add(new HomeBannerBean(latestDailyEntity.getTop_stories()));
        homePageAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
