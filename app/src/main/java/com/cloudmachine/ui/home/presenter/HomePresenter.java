package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.UnReadMessage;
import com.cloudmachine.ui.home.contract.HomeContract;
import com.github.mikephil.charting.utils.AppLog;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiaojw on 2017/5/22.
 */

public class HomePresenter extends HomeContract.Presenter {
    @Override
    public void updateUnReadMessage(long memberId) {
        mModel.getMessageUntreatedCount(memberId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseRespose<UnReadMessage>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseRespose<UnReadMessage> unReadMessageBaseRespose) {
                        if (unReadMessageBaseRespose == null) {
                            return;
                        }
                        if (unReadMessageBaseRespose.result == null) {
                            return;
                        }
                        mView.updateMessageCount(unReadMessageBaseRespose.result.getCount());
                    }
                });

    }

    @Override
    public void getDevices(long memberId, int type) {
        mRxManage.add(mModel.getDevices(memberId, type).subscribe(new RxSubscriber<List<McDeviceInfo>>(mContext, false) {
            @Override
            protected void _onNext(List<McDeviceInfo> mcDeviceInfos) {
                AppLog.print("onNext____"+mcDeviceInfos);
                mView.updateDevices(mcDeviceInfos);
            }

            @Override
            protected void _onError(String message) {
                AppLog.print("_onError____"+message);

            }
        }));

    }

    @Override
    public void getDevices(int type) {
        mRxManage.add(mModel.getDevices(type).subscribe(new RxSubscriber<List<McDeviceInfo>>(mContext, false) {
            @Override
            protected void _onNext(List<McDeviceInfo> mcDeviceInfos) {
                mView.updateDevices(mcDeviceInfos);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }


    @Override
    public void getPromotionInfo() {
        mModel.getPromotionModel().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseRespose<ArrayList<HomeBannerBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseRespose<ArrayList<HomeBannerBean>> arrayListBaseRespose) {
                        ArrayList<HomeBannerBean> beanList = arrayListBaseRespose.result;
                        for (HomeBannerBean bean : beanList) {
                            if (bean.adsMidSort == 1) {
                                mView.updatePromotionInfo(bean);
                                return;
                            }
                        }

                    }
                });


    }


}
