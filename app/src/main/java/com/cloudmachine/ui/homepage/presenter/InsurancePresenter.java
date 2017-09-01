package com.cloudmachine.ui.homepage.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.HomeBannerBean;
import com.cloudmachine.ui.homepage.contract.InsuranceContract;
import com.cloudmachine.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by xiaojw on 2017/4/28.
 */
public class InsurancePresenter  extends InsuranceContract.Presenter{


    @Override
    public void getSharedInfo() {
        mRxManage.add(mModel.getSharedInfo().subscribe(new RxSubscriber<ArrayList<HomeBannerBean>>(mContext,false) {
            @Override
            protected void _onNext(ArrayList<HomeBannerBean> homeBannerBeen) {
                mView.returnSharedInfo(homeBannerBeen);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.error(message,true);
            }
        }));
    }
}
