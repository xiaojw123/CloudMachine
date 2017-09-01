package com.cloudmachine.ui.home.model;

import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.McDeviceBasicsInfo;
import com.cloudmachine.ui.home.contract.DeviceDetailContract;

import rx.Observable;

/**
 * Created by xiaojw on 2017/5/25.
 */

public class DeviceDetailModel implements DeviceDetailContract.Model {
    @Override
    public Observable<McDeviceBasicsInfo> reqDeviceInfo(String deviceId, long memberId) {
            return Api.getDefault(HostType.CLOUDM_HOST).getDeviceInfo(deviceId, memberId).compose(RxHelper.<McDeviceBasicsInfo>handleResult());

    }

    @Override
    public Observable<McDeviceBasicsInfo> reqDeviceInfo(String deviceId) {

        return Api.getDefault(HostType.CLOUDM_HOST).getDeviceInfo(deviceId).compose(RxHelper.<McDeviceBasicsInfo>handleResult());
    }


}
