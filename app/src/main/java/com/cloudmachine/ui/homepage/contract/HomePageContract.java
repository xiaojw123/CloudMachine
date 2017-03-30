package com.cloudmachine.ui.homepage.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.recyclerbean.HomeIssueDetailBean;
import com.cloudmachine.recyclerbean.HomeNewsBean;

import java.util.ArrayList;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：首页主页面契约类
 * 创建人：shixionglu
 * 创建时间：2017/3/17 下午10:34
 * 修改人：shixionglu
 * 修改时间：2017/3/17 下午10:34
 * 修改备注：
 */

public interface HomePageContract {

    interface Model extends BaseModel {

        Observable<ArrayList<HomeBannerBean>> getHomeBannerInfo();

        Observable<ArrayList<HomeNewsBean>> getHomeMidAdvertisement();

        Observable<HomeIssueDetailBean> getHotQuestion();
    }

    interface View extends BaseView {

        void returnHomeBannerInfo(ArrayList<HomeBannerBean> homeBannerBeen);

        void returnHomeMidAdvertisement(ArrayList<HomeNewsBean> homeNewsBeen);

        void returnHotQuestion(HomeIssueDetailBean homeIssueDetailBean);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {


        public abstract void getHomeBannerInfo();

        public abstract void getHomeMidAdvertisement();

        public abstract void getHotQuestion();
    }
}
