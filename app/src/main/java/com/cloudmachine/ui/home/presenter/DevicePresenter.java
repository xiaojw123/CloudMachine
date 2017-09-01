package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.home.contract.DeviceContract;

import java.util.List;

/**
 * Created by xiaojw on 2017/7/19.
 */

public class DevicePresenter extends DeviceContract.Prensenter {

    @Override
    public void getDevices(final long memberId, int type) {
        mRxManage.add(mModel.getDevices(memberId, type).subscribe(new RxSubscriber<List<McDeviceInfo>>(mContext, false) {
            @Override
            protected void _onNext(List<McDeviceInfo> mcDeviceInfos) {
                if (mcDeviceInfos != null && mcDeviceInfos.size() > 0) {
                    mView.updateDevices(mcDeviceInfos);
                }
                ((HomeActivity) mContext).requestPromotion(memberId);
            }

            @Override
            protected void _onError(String message) {
                ((HomeActivity) mContext).requestPromotion(memberId);
            }
        }));

    }

    @Override
    public void getDevices(int type) {
        mRxManage.add(mModel.getDevices(type).subscribe(new RxSubscriber<List<McDeviceInfo>>(mContext, false) {
            @Override
            protected void _onNext(List<McDeviceInfo> mcDeviceInfos) {
                if (mcDeviceInfos != null && mcDeviceInfos.size() > 0) {
                    mView.updateDevices(mcDeviceInfos);
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }


}
