package com.cloudmachine.ui.homepage.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cloudmachine.R;
import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.base.baserx.RxBus;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.recycleadapter.HomePageAdapter;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.recyclerbean.HomeBannerTransfer;
import com.cloudmachine.recyclerbean.HomeDiliverBean;
import com.cloudmachine.recyclerbean.HomeHotIssueBean;
import com.cloudmachine.recyclerbean.HomeIssueDetailBean;
import com.cloudmachine.recyclerbean.HomeLoadMoreBean;
import com.cloudmachine.recyclerbean.HomeLocalBean;
import com.cloudmachine.recyclerbean.HomeNewsBean;
import com.cloudmachine.recyclerbean.HomeNewsTransfer;
import com.cloudmachine.recyclerbean.HomePageBean;
import com.cloudmachine.recyclerbean.HomePageType;
import com.cloudmachine.struc.ScoreInfo;
import com.cloudmachine.ui.homepage.activity.MessageActivity;
import com.cloudmachine.ui.homepage.contract.HomePageContract;
import com.cloudmachine.ui.homepage.model.HomePageModel;
import com.cloudmachine.ui.homepage.presenter.HomePagePresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;

import java.util.ArrayList;

import rx.Subscriber;
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

public class HomePageFragment extends BaseFragment<HomePagePresenter, HomePageModel> implements HomePageContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout        mSwipeRefreshLayout;
    private RecyclerView              mRecyclerView;
    private HomePageAdapter           homePageAdapter;
    private ArrayList<HomePageType>   homePageList;
    private HomePageBean              homePageBean;
    private ImageView                 ivMessage;
    private ArrayList<HomeBannerBean> homeBannerList;
    private ArrayList<HomeNewsBean>   homeNewsList;
    private boolean                   getBannerInfo;
    private boolean                   getMidNewsInfo;
    private boolean                   getHotIssueInfo;
    private boolean                   isRefresh;
    private boolean                   refreshHotIssueOnly;//热门问题换一换
    private boolean isfirstLoadHotIssue = true;//是否第一次加载热门问题
    private HomeIssueDetailBean mHomeIssueDetailBean;
    private int                 signBetweenTime;


    @Override
    protected void initView() {

        initFindViews();
        initListeners();
        initDatas();
        //设置布局的排列方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(homePageAdapter);
        getData();
        mPresenter.getHomeBannerInfo();
        mPresenter.getHomeMidAdvertisement();
        mPresenter.getHotQuestion();

        mRxManager.on(Constants.GET_HOTISSUE, new Action1<Object>() {
            @Override
            public void call(Object o) {
                Constants.MyLog("热门问题换一换");
                refreshHotIssueOnly();
            }
        });

        mRxManager.on(Constants.REFRESH_SIGN_STATE, new Action1<Object>() {
            @Override
            public void call(Object o) {
                //仅刷新签到状态
                Constants.MyLog("接受到刷新请求");
                refreshSignStateOnly();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MemeberKeeper.getOauth(getActivity()) != null) {
            refreshSignState();
        }
    }


    private void refreshSignStateOnly() {

       /* if (homeBannerList != null && homeBannerList.size() > 0) {
            homePageList.set(1, new HomeLocalBean());
        } else {
            homePageList.set(0, new HomeLocalBean());
        }*/
        homePageAdapter.notifyDataSetChanged();
    }

    //热门问题换一换
    private void refreshHotIssueOnly() {
        refreshHotIssueOnly = true;
        mPresenter.getHotQuestion();
    }


    private void initDatas() {

        homePageList = new ArrayList<>();
        homeBannerList = new ArrayList<>();
        homeNewsList = new ArrayList<>();
        homePageAdapter = new HomePageAdapter(getActivity(), homePageList);
    }

    private void initListeners() {
        ivMessage.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
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

    }

    private void initFindViews() {
        ivMessage = (ImageView) viewParent.findViewById(R.id.iv_message);
        mSwipeRefreshLayout = (SwipeRefreshLayout) viewParent.findViewById(R.id.swipe_layout);
        mRecyclerView = (RecyclerView) viewParent.findViewById(R.id.recyclerView);
    }

    private void getData() {
        homePageList.clear();
        //本地信息类（商城，车险，问答，报修，签到）
        homePageList.add(new HomeLocalBean());
        //分割线
        homePageList.add(new HomeDiliverBean());
        //分割线
        homePageList.add(new HomeDiliverBean());
        //热门问题标题头（换一换）
        homePageList.add(new HomeHotIssueBean());
        //热门问题内容
        //homePageList.add(new HomeIssueDetailBean());
        //分割线
        homePageList.add(new HomeDiliverBean());
        //查看更多信息页面
        homePageList.add(new HomeLoadMoreBean());
        //刷新recyclerview
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


    //拿到轮播信息成功
    @Override
    public void returnHomeBannerInfo(ArrayList<HomeBannerBean> homeBannerBeen) {
        homeBannerList.clear();
        homeBannerList.addAll(homeBannerBeen);
        getBannerInfo = true;
        homePageList.add(0, new HomeBannerTransfer(homeBannerList));
        finishRefresh();
        homePageAdapter.notifyDataSetChanged();
    }

    //拿到中间广告位信息成功
    @Override
    public void returnHomeMidAdvertisement(ArrayList<HomeNewsBean> homeNewsBeen) {
        homeNewsList.clear();
        homeNewsList.addAll(homeNewsBeen);
        getMidNewsInfo = true;
        if (homeNewsList != null && homeNewsList.size() > 0) {
            if (homeBannerList != null && homeBannerList.size() > 0) {
                homePageList.add(3, new HomeNewsTransfer(homeNewsList));
            } else {
                homePageList.add(2, new HomeNewsTransfer(homeNewsList));
            }
        }
        finishRefresh();
        homePageAdapter.notifyDataSetChanged();

    }

    //获取热门问题成功过
    @Override
    public void returnHotQuestion(HomeIssueDetailBean homeIssueDetailBean) {
        //Constants.MyLog(homeIssueDetailBean.toString());
        mHomeIssueDetailBean = homeIssueDetailBean;
        getHotIssueInfo = true;

        if (isfirstLoadHotIssue) {
            //热门问题加载插入位置
            if (homeBannerList.size() > 0 && homeNewsList.size() > 0) {
                homePageList.add(6, mHomeIssueDetailBean);
            } else if (homeBannerList.size() <= 0 && homeNewsList.size() <= 0) {
                homePageList.add(4, mHomeIssueDetailBean);
            } else {
                homePageList.add(5, mHomeIssueDetailBean);
            }
            isfirstLoadHotIssue = false;
        }

        finishRefresh();

        //是否只刷新热门问题
        if (refreshHotIssueOnly) {
            if (homeBannerList.size() > 0 && homeNewsList.size() > 0) {
                homePageList.set(6, mHomeIssueDetailBean);
            } else if (homeBannerList.size() <= 0 && homeNewsList.size() <= 0) {
                homePageList.set(4, mHomeIssueDetailBean);
            } else {
                homePageList.set(5, mHomeIssueDetailBean);
            }
            refreshHotIssueOnly = false;
        }

        homePageAdapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_message:
                Constants.toActivity(getActivity(), MessageActivity.class, null, false);
                break;
        }
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    private void refreshData() {
        Constants.MyLog("刷新方法调用了");
        getBannerInfo = false;
        getMidNewsInfo = false;
        getHotIssueInfo = false;
        isRefresh = true;
        mPresenter.getHomeBannerInfo();
        mPresenter.getHomeMidAdvertisement();
        mPresenter.getHotQuestion();
    }

    //查看是否为刷新
    public void finishRefresh() {

        if (getMidNewsInfo && getBannerInfo && getHotIssueInfo && isRefresh && !refreshHotIssueOnly) {
            Constants.MyLog("进入刷新方法");
            mRecyclerView.scrollToPosition(0);
            mSwipeRefreshLayout.setRefreshing(false);
            getData();
            homePageList.add(0, new HomeBannerTransfer(homeBannerList));
            homePageList.add(3, new HomeNewsTransfer(homeNewsList));
            homePageList.add(6, mHomeIssueDetailBean);
        }
    }


    private void refreshSignState() {

        Api.getDefault(HostType.CLOUDM_HOST)
                .getUserScoreInfo(MemeberKeeper.getOauth(getActivity()).getId())
                .compose(RxHelper.<ScoreInfo>handleResult())
                .subscribe(new Subscriber<ScoreInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ScoreInfo scoreInfo) {
                        if (null != scoreInfo
                                && !TextUtils.isEmpty(scoreInfo.getServerTime())
                                && null != MemeberKeeper.getOauth(getActivity())) {
                            //上一次签到的时间
                            String oldTime = MySharedPreferences
                                    .getSharedPString(MySharedPreferences.key_score_update_time
                                            + String.valueOf(MemeberKeeper.getOauth(
                                            getActivity()).getId()));
                            //签到时间间隔
                            signBetweenTime = Constants.getDateDays(
                                    Constants.changeDateFormat(
                                            scoreInfo.getServerTime(),
                                            Constants.DateFormat2,
                                            Constants.DateFormat1),
                                    Constants.changeDateFormat(
                                            oldTime,
                                            Constants.DateFormat2,
                                            Constants.DateFormat1),
                                    Constants.DateFormat1
                            );
                            if (oldTime == null) {
                                signBetweenTime = 1;
                            }
                        } else {
                            signBetweenTime = 1;
                        }
                        Constants.MyLog("拿到的签到时间间隔" + signBetweenTime);
                        RxBus.getInstance().post(Constants.SIGN_OR_NOTSIGN,signBetweenTime);
                    }
                });
    }
}
