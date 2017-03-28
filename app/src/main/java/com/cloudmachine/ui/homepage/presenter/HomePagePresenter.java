package com.cloudmachine.ui.homepage.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.struc.LatestDailyEntity;
import com.cloudmachine.ui.homepage.contract.HomePageContract;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;

import rx.Subscriber;

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
    public void getLatestDaily() {
        mRxManage.add(mModel.getLatestDaily().subscribe(new Subscriber<LatestDailyEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LatestDailyEntity latestDailyEntity) {
                Constants.MyLog("执行到这里");
                mView.refreshHomeList(latestDailyEntity);
            }
        }));
    }

    @Override
    public void getHomeBannerInfo() {
        mRxManage.add(mModel.getHomeBannerInfo().subscribe(new RxSubscriber<ArrayList<HomeBannerBean>>(mContext,false) {
            @Override
            protected void _onNext(ArrayList<HomeBannerBean> homeBannerBeen) {
                Constants.MyLog("轮播信息"+homeBannerBeen.toString());
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
