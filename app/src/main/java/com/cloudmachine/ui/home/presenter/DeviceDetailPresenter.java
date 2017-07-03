package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.struc.McDeviceBasicsInfo;
import com.cloudmachine.ui.home.contract.DeviceDetailContract;

/**
 * Created by xiaojw on 2017/5/25.
 */

public class DeviceDetailPresenter extends DeviceDetailContract.Presenter{

    @Override
    public void getDeviceInfo(String deviceId, long memberId) {
        mRxManage.add(mModel.reqDeviceInfo(deviceId,memberId).subscribe(new RxSubscriber<McDeviceBasicsInfo>(mContext,false) {
            @Override
            protected void _onNext(McDeviceBasicsInfo info) {
                mView.retrunDeviceInfo(info);
            }

            @Override
            protected void _onError(String message) {

            }
        }));

//        mModel.reqDeviceInfo(deviceId,memberId).subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                 .subscribe(new Subscriber<BaseRespose<McDeviceBasicsInfo>>() {
//                                                     @Override
//                                                     public void onCompleted() {
//
//                                                     }
//
//                                                     @Override
//                                                     public void onError(Throwable e) {
//
//                                                     }
//
//                                                     @Override
//                                                     public void onNext(BaseRespose<McDeviceBasicsInfo> mcDeviceBasicsInfoBaseRespose) {
//                                                         mView.retrunDeviceInfo(mcDeviceBasicsInfoBaseRespose.getResult());
//                                                     }
//                                                 });

    }

    @Override
    public void getDeviceInfo(String deviceId) {
        mRxManage.add(mModel.reqDeviceInfo(deviceId).subscribe(new RxSubscriber<McDeviceBasicsInfo>(mContext,false) {
            @Override
            protected void _onNext(McDeviceBasicsInfo info) {
                mView.retrunDeviceInfo(info);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
