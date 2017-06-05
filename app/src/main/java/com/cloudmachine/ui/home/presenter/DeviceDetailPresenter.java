package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.struc.McDeviceBasicsInfo;
import com.cloudmachine.ui.home.contract.DeviceDetailContract;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiaojw on 2017/5/25.
 */

public class DeviceDetailPresenter extends DeviceDetailContract.Presenter{

    @Override
    public void getDeviceInfo(String deviceId, long memberId) {
        mModel.reqDeviceInfo(deviceId,memberId).subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                 .subscribe(new Subscriber<BaseRespose<McDeviceBasicsInfo>>() {
                                                     @Override
                                                     public void onCompleted() {

                                                     }

                                                     @Override
                                                     public void onError(Throwable e) {

                                                     }

                                                     @Override
                                                     public void onNext(BaseRespose<McDeviceBasicsInfo> mcDeviceBasicsInfoBaseRespose) {
                                                         mView.retrunDeviceInfo(mcDeviceBasicsInfoBaseRespose.getResult());
                                                     }
                                                 });

    }
}
