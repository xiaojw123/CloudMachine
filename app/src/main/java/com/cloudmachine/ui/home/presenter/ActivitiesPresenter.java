package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.HomeBannerBean;
import com.cloudmachine.ui.home.contract.ActivitesContract;

import java.util.ArrayList;

/**
 * Created by xiaojw on 2017/5/23.
 */

public class ActivitiesPresenter extends ActivitesContract.Presenter {
    @Override
    public void getHomeBannerInfo() {
        mRxManage.add(mModel.getHomeBannerInfo().subscribe(new RxSubscriber<ArrayList<HomeBannerBean>>(mContext,false) {
            @Override
            protected void _onNext(ArrayList<HomeBannerBean> homeBannerBeen) {
                mView.returnHomeBannerInfo(homeBannerBeen);
            }

            @Override
            protected void _onError(String message) {
                mView.loadBannerError();
            }
        }));
    }
}
