package com.cloudmachine.ui.homepage.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.recyclerbean.HomeIssueDetailBean;
import com.cloudmachine.recyclerbean.HomeNewsBean;
import com.cloudmachine.ui.homepage.contract.HomePageContract;
import com.cloudmachine.utils.Constants;
import com.github.mikephil.charting.utils.AppLog;

import java.util.ArrayList;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/18 上午8:00
 * 修改人：shixionglu
 * 修改时间：2017/3/18 上午8:00
 * 修改备注：
 */

public class HomePagePresenter extends HomePageContract.Presenter {

    @Override
    public void getHomeBannerInfo() {
        mRxManage.add(mModel.getHomeBannerInfo().subscribe(new RxSubscriber<ArrayList<HomeBannerBean>>(mContext,false) {
            @Override
            protected void _onNext(ArrayList<HomeBannerBean> homeBannerBeen) {
                Constants.MyLog(homeBannerBeen.toString());
                mView.returnHomeBannerInfo(homeBannerBeen);
            }

            @Override
            protected void _onError(String message) {
                mView.loadBannerError();
            }
        }));
    }

    @Override
    public void getHomeMidAdvertisement() {
        mRxManage.add(mModel.getHomeMidAdvertisement().subscribe(new RxSubscriber<ArrayList<HomeNewsBean>>(mContext,false) {
            @Override
            protected void _onNext(ArrayList<HomeNewsBean> homeNewsBeen) {
                mView.returnHomeMidAdvertisement(homeNewsBeen);
            }

            @Override
            protected void _onError(String message) {
                mView.loadMidAdError();
            }
        }));
    }

    @Override
    public void getHotQuestion() {
        mRxManage.add(mModel.getHotQuestion().subscribe(new RxSubscriber<HomeIssueDetailBean>(mContext,false) {
            @Override
            protected void _onNext(HomeIssueDetailBean homeIssueDetailBean) {
                AppLog.print("onNext__returnHotQuestion____");
                mView.returnHotQuestion(homeIssueDetailBean);
            }

            @Override
            protected void _onError(String message) {
                mView.loadHotQuestionError();

            }
        }));
    }

    @Override
    public void getMessageUntreated() {

    }


}
