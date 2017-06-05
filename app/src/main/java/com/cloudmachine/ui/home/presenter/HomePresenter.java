package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.recyclerbean.HomeBannerBean;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.UnReadMessage;
import com.cloudmachine.ui.home.contract.HomeContract;

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
        mModel.getDevices(memberId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseRespose<List<McDeviceInfo>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseRespose<List<McDeviceInfo>> devices) {
                        if (devices != null) {
                            mView.updateDevices(devices.getResult());
                        }
                    }
                });
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
